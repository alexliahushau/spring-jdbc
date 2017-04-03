package com.epam.spring.jdbc.web.utils;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.epam.spring.jdbc.domain.Event;
import com.epam.spring.jdbc.domain.Ticket;
import com.epam.spring.jdbc.domain.User;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TicketsPdf extends AbstractPdfView {
 
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter, HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        final User user = (User) model.get("user");
        final Event event = (Event) model.get("event");
        @SuppressWarnings("unchecked")
        final Set<Ticket> tickets = (Set<Ticket>) model.get("tickets");  
        
        Paragraph header = null;

        if (user != null) {
            header = new Paragraph(new Chunk("Booked tickets by " + user.getFirstName(), FontFactory.getFont(FontFactory.HELVETICA, 30)));
        } else if (event != null && CollectionUtils.isNotEmpty(tickets)) {
            header = new Paragraph(new Chunk("Booked tickets, " + event.getName() + ", air date: " + tickets.stream().findAny().get().getDateTime(), FontFactory.getFont(FontFactory.HELVETICA, 30)));
        } else {
            header = new Paragraph(new Chunk("Booked tickets", FontFactory.getFont(FontFactory.HELVETICA, 30)));
        }

        document.add(header);
        document.add(Chunk.NEWLINE);

        final PdfPTable table = new PdfPTable(7);

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBackgroundColor(Color.lightGray);
 
        table.addCell("id");
        table.addCell("Event");
        table.addCell("Auditorium");
        table.addCell("Air date");
        table.addCell("Seat");
        table.addCell("Rate");
        table.addCell("Purchased");
 
        for (Ticket ticket : tickets) {
        	table.addCell(ticket.getId().toString());
            table.addCell(ticket.getEvent().getName());
            table.addCell(ticket.getEvent().getAuditoriums().get(ticket.getDateTime()).getName());
            table.addCell(ticket.getDateTime().toString());
            table.addCell(ticket.getSeat().toString());
            table.addCell(ticket.getEvent().getRating().toString());
            table.addCell("" + ticket.isPurchased());
            table.completeRow();
        }

        document.add(table);

    }
}
