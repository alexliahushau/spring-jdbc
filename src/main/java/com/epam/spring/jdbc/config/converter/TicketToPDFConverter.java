package com.epam.spring.jdbc.config.converter;

import java.awt.Color;
import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.epam.spring.jdbc.domain.Ticket;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TicketToPDFConverter extends AbstractHttpMessageConverter<Ticket> {

	public TicketToPDFConverter(){
        super(new MediaType("application", "pdf"));
    }
	
	@Override
	protected boolean supports(Class<?> clazz) {
		if(clazz.getSimpleName().equalsIgnoreCase("Ticket")){
            return true;
        }
		return false;
	}

	@Override
	protected Ticket readInternal(Class<? extends Ticket> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void writeInternal(Ticket ticket, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		final Document document = new Document();
		final PdfWriter writer;
		
		try {
			writer = PdfWriter.getInstance(document, outputMessage.getBody());
			writer.setInitialLeading(16);
			document.open();
			
			final Paragraph header = new Paragraph(new Chunk("Ticket id: " + ticket.getId(), FontFactory.getFont(FontFactory.HELVETICA, 30)));
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
	 
        	table.addCell(ticket.getId().toString());
            table.addCell(ticket.getEvent().getName());
            table.addCell(ticket.getEvent().getAuditoriums().get(ticket.getDateTime()).getName());
            table.addCell(ticket.getDateTime().toString());
            table.addCell(ticket.getSeat().toString());
            table.addCell(ticket.getEvent().getRating().toString());
            table.addCell("" + ticket.isPurchased());
            table.completeRow();
	        
            document.add(header);
            document.add(Chunk.NEWLINE);
            document.add(table);
	        
	        document.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
