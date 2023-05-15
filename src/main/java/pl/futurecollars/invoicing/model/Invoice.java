package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

    private int id;
    private LocalDate date;
    private Company seller;
    private Company buyer;
    private List<InvoiceEntry> invoiceEntries;

    public Invoice(LocalDate date, Company buyer, Company seller, List<InvoiceEntry> invoiceEntries) {
        this.date = date;
        this.seller = buyer;
        this.buyer = seller;
        this.invoiceEntries = invoiceEntries;
    }
}
