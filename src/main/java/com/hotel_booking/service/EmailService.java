package com.hotel_booking.service;

import com.hotel_booking.model.Booking;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InvoicePdfService invoicePdfService;

    @Value("${stayease.mail.from}")
    private String fromEmail;

    @Value("${server.port:8082}")
    private String serverPort;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // ── Booking Confirmation (with PDF invoice attached) ──────────────
    @Async("emailTaskExecutor")
    public void sendBookingConfirmation(Booking booking) {
        try {
            byte[] invoicePdf = invoicePdfService.generateInvoice(booking);

            MimeMessage message = mailSender.createMimeMessage();
            // multipart = true enables both HTML body + attachment
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("Booking Confirmed – StayEase #" + booking.getId());
            helper.setText(buildBookingEmail(booking), true);
            helper.addAttachment(
                "StayEase-Invoice-" + booking.getId() + ".pdf",
                new org.springframework.core.io.ByteArrayResource(invoicePdf),
                "application/pdf"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("[EmailService] Confirmation email failed for booking #"
                + booking.getId() + " → " + e.getMessage());
        }
    }

    // ── Booking Cancellation ──────────────────────────────────────────
    @Async("emailTaskExecutor")
    public void sendCancellationNotice(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("Booking Cancelled – StayEase #" + booking.getId());
            helper.setText(buildCancellationEmail(booking), true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("[EmailService] Cancellation email failed for booking #"
                + booking.getId() + " → " + e.getMessage());
        }
    }

    // ── Email Templates ───────────────────────────────────────────────

    private String buildBookingEmail(Booking booking) {
        String baseUrl = "http://localhost:" + serverPort;
        return "<!DOCTYPE html><html><body style='margin:0;padding:0;font-family:Segoe UI,sans-serif;background:#f0f2f5;'>"
            + "<div style='max-width:600px;margin:40px auto;background:white;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1);'>"

            // Header
            + "<div style='background:linear-gradient(135deg,#667eea,#764ba2);padding:36px 40px;text-align:center;'>"
            + "<h1 style='color:white;margin:0;font-size:28px;letter-spacing:1px;'>StayEase</h1>"
            + "<p style='color:rgba(255,255,255,0.85);margin:6px 0 0;font-size:14px;'>Hotel Reservation System</p>"
            + "</div>"

            // Status badge + greeting
            + "<div style='text-align:center;padding:28px 40px 0;'>"
            + "<span style='background:#e8f5e9;color:#2e7d32;padding:8px 22px;border-radius:20px;font-weight:600;font-size:14px;'>✓ Booking Confirmed</span>"
            + "<h2 style='color:#2d2d2d;margin:18px 0 4px;font-size:22px;'>Your booking is confirmed!</h2>"
            + "<p style='color:#888;margin:0;font-size:14px;'>Hi <strong style='color:#333;'>"
            + booking.getUser().getName()
            + "</strong>, here are your booking details. Your invoice is attached to this email.</p>"
            + "</div>"

            // Details card
            + "<div style='margin:24px 40px;border-radius:12px;overflow:hidden;border:1px solid #eee;'>"
            + buildRow("Booking ID",   "#" + booking.getId(),                                         "#f9f9ff", true)
            + buildRow("Hotel",        booking.getRoom().getHotel().getName(),                         "white",   false)
            + buildRow("Location",     booking.getRoom().getHotel().getLocation(),                     "#f9f9ff", false)
            + buildRow("Room Type",    booking.getRoom().getRoomType(),                                "white",   false)
            + buildRow("Check-In",     booking.getCheckIn().format(FMT),                              "#f9f9ff", false)
            + buildRow("Check-Out",    booking.getCheckOut().format(FMT),                             "white",   false)
            + buildRow("Total Amount", "&#8377; " + String.format("%.2f", booking.getTotalPrice()),   "#f9f9ff", false)
            + buildRow("Status",       "<span style='color:#2e7d32;font-weight:600;'>CONFIRMED</span>", "white", false)
            + "</div>"

            // PDF note
            + "<div style='margin:0 40px 20px;padding:14px 18px;background:#f0f7ff;border-left:4px solid #667eea;border-radius:6px;'>"
            + "<p style='margin:0;font-size:13px;color:#555;'>📎 <strong>Invoice attached</strong> — Your PDF invoice is attached to this email for your records.</p>"
            + "</div>"

            // CTA
            + "<div style='text-align:center;padding:10px 40px 36px;'>"
            + "<a href='" + baseUrl + "/bookingHistory' style='background:linear-gradient(135deg,#667eea,#764ba2);color:white;padding:13px 32px;border-radius:8px;text-decoration:none;font-weight:600;font-size:15px;display:inline-block;'>View My Bookings</a>"
            + "</div>"

            + buildFooter()
            + "</div></body></html>";
    }

    private String buildCancellationEmail(Booking booking) {
        String baseUrl = "http://localhost:" + serverPort;
        return "<!DOCTYPE html><html><body style='margin:0;padding:0;font-family:Segoe UI,sans-serif;background:#f0f2f5;'>"
            + "<div style='max-width:600px;margin:40px auto;background:white;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1);'>"

            // Header
            + "<div style='background:linear-gradient(135deg,#667eea,#764ba2);padding:36px 40px;text-align:center;'>"
            + "<h1 style='color:white;margin:0;font-size:28px;letter-spacing:1px;'>StayEase</h1>"
            + "<p style='color:rgba(255,255,255,0.85);margin:6px 0 0;font-size:14px;'>Hotel Reservation System</p>"
            + "</div>"

            // Status badge + greeting
            + "<div style='text-align:center;padding:28px 40px 0;'>"
            + "<span style='background:#fdecea;color:#c62828;padding:8px 22px;border-radius:20px;font-weight:600;font-size:14px;'>✕ Booking Cancelled</span>"
            + "<h2 style='color:#2d2d2d;margin:18px 0 4px;font-size:22px;'>Your booking has been cancelled</h2>"
            + "<p style='color:#888;margin:0;font-size:14px;'>Hi <strong style='color:#333;'>"
            + booking.getUser().getName()
            + "</strong>, your booking has been successfully cancelled.</p>"
            + "</div>"

            // Details card
            + "<div style='margin:24px 40px;border-radius:12px;overflow:hidden;border:1px solid #eee;'>"
            + buildRow("Booking ID",   "#" + booking.getId(),                                         "#fff8f8", true)
            + buildRow("Hotel",        booking.getRoom().getHotel().getName(),                         "white",   false)
            + buildRow("Room Type",    booking.getRoom().getRoomType(),                                "#fff8f8", false)
            + buildRow("Check-In",     booking.getCheckIn().format(FMT),                              "white",   false)
            + buildRow("Check-Out",    booking.getCheckOut().format(FMT),                             "#fff8f8", false)
            + buildRow("Amount",       "&#8377; " + String.format("%.2f", booking.getTotalPrice()),   "white",   false)
            + buildRow("Status",       "<span style='color:#c62828;font-weight:600;'>CANCELLED</span>", "#fff8f8", false)
            + "</div>"

            // CTA
            + "<div style='text-align:center;padding:10px 40px 36px;'>"
            + "<a href='" + baseUrl + "/hotelsPage' style='background:linear-gradient(135deg,#667eea,#764ba2);color:white;padding:13px 32px;border-radius:8px;text-decoration:none;font-weight:600;font-size:15px;display:inline-block;'>Browse Hotels</a>"
            + "</div>"

            + buildFooter()
            + "</div></body></html>";
    }

    private String buildRow(String label, String value, String bg, boolean first) {
        String border = first ? "" : "border-top:1px solid #f0f0f0;";
        return "<div style='display:flex;justify-content:space-between;padding:13px 20px;background:" + bg + ";" + border + "'>"
             + "<span style='color:#888;font-size:14px;'>" + label + "</span>"
             + "<span style='color:#333;font-size:14px;font-weight:500;'>" + value + "</span>"
             + "</div>";
    }

    private String buildFooter() {
        return "<div style='background:#f9f9ff;padding:20px 40px;text-align:center;border-top:1px solid #eee;'>"
             + "<p style='color:#aaa;font-size:12px;margin:0;'>© 2025 StayEase – Hotel Reservation System</p>"
             + "<p style='color:#aaa;font-size:12px;margin:6px 0 0;'>This is an automated email. Please do not reply.</p>"
             + "</div>";
    }
}
