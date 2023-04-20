package pl.futurecollars.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;

public class App {

    public static void main(String[] args) {

        Database db = new InMemoryDatabase();
        InvoiceService invoiceService = new InvoiceService(db);
        Company example = new Company("Sklep Komputerowy Enter", "552-168-66-00", "ul. Jesionowa 23/4 80-234 Gdańsk");
        InvoiceEntry invoiceEntry2 = new InvoiceEntry("Pamięć masowa", BigDecimal.valueOf(23), BigDecimal.valueOf(330), Vat.VAT_0);
        List<InvoiceEntry> entry = new ArrayList<>();
        entry.add(invoiceEntry2);

        Invoice invoice = new Invoice(LocalDate.now(), example, example, entry);
        Invoice invoice1 = new Invoice(LocalDate.of(2022, 5, 18), example, example, entry);
        invoiceService.save(invoice);
        invoiceService.save(invoice1);
        System.out.println(invoiceService.getById(1));
        System.out.println(invoiceService.getById(5));
        System.out.println(invoiceService.getAll());
        invoiceService.delete(1);
        System.out.println(invoiceService.getAll());
        System.out.println(invoiceService.save(invoice1));
    }

}
