package com.hotel_booking.controller;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.service.InvoicePdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InvoiceController {

    @Autowired
    private InvoicePdfService invoicePdfService;

    @Autowired
    private BookingRepository bookingRepository;

    /** Path-variable endpoint: GET /invoice/{bookingId} */
    @GetMapping("/invoice/{bookingId}")
    public void downloadInvoiceByPath(@PathVariable int bookingId,
                                      HttpSession session,
                                      HttpServletResponse response) throws Exception {
        serveInvoice(bookingId, session, response);
    }

    /** Query-param endpoint: GET /invoice/download?bookingId= */
    @GetMapping("/invoice/download")
    public void downloadInvoiceByParam(@RequestParam int bookingId,
                                       HttpSession session,
                                       HttpServletResponse response) throws Exception {
        serveInvoice(bookingId, session, response);
    }

    private void serveInvoice(int bookingId, HttpSession session,
                               HttpServletResponse response) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/loginPage");
            return;
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found.");
            return;
        }

        if (booking.getUser().getId() != user.getId() && !"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        byte[] pdf = invoicePdfService.generateInvoice(booking);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"StayEase-Invoice-" + bookingId + ".pdf\"");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}
