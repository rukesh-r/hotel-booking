package com.hotel_booking.service;

import com.hotel_booking.model.Booking;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePdfService {

    private static final Color BRAND_COLOR = new Color(102, 126, 234);
    private static final Color DARK        = new Color(33, 33, 33);
    private static final Color LIGHT_GRAY  = new Color(245, 245, 250);
    private static final Color MID_GRAY    = new Color(150, 150, 160);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public byte[] generateInvoice(Booking booking) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        doc.open();

        // ── Header band ──────────────────────────────────────────────
        PdfContentByte cb = writer.getDirectContent();
        cb.setColorFill(BRAND_COLOR);
        cb.rectangle(50, 770, 495, 55);
        cb.fill();

        Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.WHITE);
        Font tagFont   = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(220, 220, 255));

        Paragraph brand = new Paragraph("StayEase", brandFont);
        brand.setAlignment(Element.ALIGN_LEFT);
        brand.setSpacingBefore(14f);
        doc.add(brand);

        Paragraph tag = new Paragraph("Hotel Reservation System  |  www.stayease.com", tagFont);
        tag.setAlignment(Element.ALIGN_LEFT);
        doc.add(tag);

        doc.add(Chunk.NEWLINE);

        // ── Invoice title row ─────────────────────────────────────────
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, DARK);
        Font subFont   = FontFactory.getFont(FontFactory.HELVETICA, 10, MID_GRAY);

        PdfPTable titleRow = new PdfPTable(2);
        titleRow.setWidthPercentage(100);
        titleRow.setWidths(new float[]{1f, 1f});

        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.addElement(new Phrase("INVOICE", titleFont));
        leftCell.addElement(new Phrase("Booking #" + booking.getId(), subFont));
        titleRow.addCell(leftCell);

        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, MID_GRAY);
        String issueDate = booking.getBookingDate() != null
                ? booking.getBookingDate().format(FMT)
                : LocalDate.now().format(FMT);
        rightCell.addElement(new Phrase("Issue Date: " + issueDate, dateFont));

        String statusText = booking.getStatus() != null ? booking.getStatus().name() : "PENDING";
        Color statusColor = "CONFIRMED".equals(statusText) ? new Color(40, 167, 69)
                          : "CANCELLED".equals(statusText) ? new Color(220, 53, 69)
                          : new Color(255, 153, 0);
        rightCell.addElement(new Phrase("Status:  " + statusText,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, statusColor)));
        titleRow.addCell(rightCell);

        doc.add(titleRow);
        doc.add(Chunk.NEWLINE);

        // ── Divider ───────────────────────────────────────────────────
        drawLine(cb, 50, 660, 545, 660);

        // ── Billed To / Hotel Info ────────────────────────────────────
        Font sectionLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, MID_GRAY);
        Font infoFont     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, DARK);
        Font infoSub      = FontFactory.getFont(FontFactory.HELVETICA, 10, DARK);

        PdfPTable infoRow = new PdfPTable(2);
        infoRow.setWidthPercentage(100);
        infoRow.setSpacingBefore(10f);

        PdfPCell billCell = new PdfPCell();
        billCell.setBorder(Rectangle.NO_BORDER);
        billCell.addElement(new Phrase("BILLED TO", sectionLabel));
        billCell.addElement(new Phrase(booking.getUser().getName(), infoFont));
        billCell.addElement(new Phrase(booking.getUser().getEmail(), infoSub));
        infoRow.addCell(billCell);

        PdfPCell hotelCell = new PdfPCell();
        hotelCell.setBorder(Rectangle.NO_BORDER);
        hotelCell.addElement(new Phrase("HOTEL", sectionLabel));
        hotelCell.addElement(new Phrase(booking.getRoom().getHotel().getName(), infoFont));
        hotelCell.addElement(new Phrase(booking.getRoom().getHotel().getLocation(), infoSub));
        infoRow.addCell(hotelCell);

        doc.add(infoRow);
        doc.add(Chunk.NEWLINE);

        // ── Booking Details Table ─────────────────────────────────────
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{1f, 1.5f});

        addTableHeader(table, "DETAIL", "VALUE");
        addTableRow(table, "Booking ID",    "#" + booking.getId(),                                          false);
        addTableRow(table, "Guest Name",    booking.getUser().getName(),                                    true);
        addTableRow(table, "Hotel",         booking.getRoom().getHotel().getName(),                         false);
        addTableRow(table, "Room Type",     booking.getRoom().getRoomType(),                                true);
        addTableRow(table, "Check-In",      booking.getCheckIn().format(FMT),                              false);
        addTableRow(table, "Check-Out",     booking.getCheckOut().format(FMT),                             true);
        addTableRow(table, "Duration",      calcNights(booking.getCheckIn(), booking.getCheckOut()) + " Night(s)", false);
        addTableRow(table, "Room Rate",     "Rs. " + String.format("%.2f", booking.getRoom().getPrice()) + " / night", true);
        addTableRow(table, "Booking Status", statusText,                                                    false);
        addTableRow(table, "Booking Date",  issueDate,                                                      true);

        doc.add(table);
        doc.add(Chunk.NEWLINE);

        // ── Total Price Box ───────────────────────────────────────────
        PdfPTable totalTable = new PdfPTable(1);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell totalCell = new PdfPCell();
        totalCell.setBackgroundColor(BRAND_COLOR);
        totalCell.setPadding(12f);
        totalCell.setBorder(Rectangle.NO_BORDER);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font totalLabel = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.WHITE);
        Font totalAmt   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE);
        totalCell.addElement(new Phrase("TOTAL AMOUNT", totalLabel));
        totalCell.addElement(new Phrase("Rs. " + String.format("%.2f", booking.getTotalPrice()), totalAmt));
        totalTable.addCell(totalCell);

        doc.add(totalTable);

        // ── Footer ────────────────────────────────────────────────────
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        drawLine(cb, 50, 120, 545, 120);

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, MID_GRAY);
        Paragraph footer = new Paragraph(
            "Thank you for choosing StayEase! For support, contact support@stayease.com\n" +
            "This is a computer-generated invoice and does not require a signature.", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(8f);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String col1, String col2) {
        Font hFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        for (String h : new String[]{col1, col2}) {
            PdfPCell cell = new PdfPCell(new Phrase(h, hFont));
            cell.setBackgroundColor(BRAND_COLOR);
            cell.setPadding(8f);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, String label, String value, boolean shaded) {
        Color bg = shaded ? LIGHT_GRAY : Color.WHITE;
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, DARK);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, DARK);

        PdfPCell c1 = new PdfPCell(new Phrase(label, labelFont));
        c1.setBackgroundColor(bg); c1.setPadding(8f); c1.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2 = new PdfPCell(new Phrase(value, valueFont));
        c2.setBackgroundColor(bg); c2.setPadding(8f); c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        table.addCell(c2);
    }

    private void drawLine(PdfContentByte cb, float x1, float y1, float x2, float y2) {
        cb.setColorStroke(new Color(220, 220, 230));
        cb.setLineWidth(0.8f);
        cb.moveTo(x1, y1); cb.lineTo(x2, y2); cb.stroke();
    }

    private long calcNights(LocalDate checkIn, LocalDate checkOut) {
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
