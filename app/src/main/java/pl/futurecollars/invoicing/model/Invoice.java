package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;


@Data
public class Invoice {
    private int index;
    private LocalDate date;
    private Company issuingCompany;
    private Company buyingCompany;
    private List<InvoiceEntry> invoiceEntries;


    public Invoice(LocalDate date, Company issuingCompany, Company buyingCompany, List<InvoiceEntry> invoiceEntries) {
        this.date = date;
        this.issuingCompany = issuingCompany;
        this.buyingCompany = buyingCompany;
        this.invoiceEntries = invoiceEntries;
    }
}
