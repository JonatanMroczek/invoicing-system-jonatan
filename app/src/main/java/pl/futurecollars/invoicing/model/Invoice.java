package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;


@Data
public class Invoice {
    private int id;
    private LocalDate date;
    private Company issuingCompany;
    private Company buyingCompany;
    private List<InvoiceEntry> invoiceEntries;


    public Invoice(LocalDate date, Company buyer, Company seller, List<InvoiceEntry> invoiceEntries) {
        this.date = date;
        this.issuingCompany = buyer;
        this.buyingCompany = seller;
        this.invoiceEntries = invoiceEntries;
    }
}
