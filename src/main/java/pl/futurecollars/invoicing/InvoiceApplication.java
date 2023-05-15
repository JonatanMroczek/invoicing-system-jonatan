package pl.futurecollars.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(InvoiceApplication.class, args);
    }


    /*        FilesService filesService = new FilesService();
        JsonService jsonService = new JsonService();
        IdService idService = new IdService(Path.of(Config.ID_FILE_LOCATION),filesService);

        Database db = new FileBasedDatabase(Path.of(Config.DATABASE_LOCATION),idService,filesService,jsonService);
        InvoiceService invoiceService = new InvoiceService(db);
        Company example = new Company("Sklep Komputerowy Enter", "552-168-66-00", "ul. Jesionowa 23/4 80-234 Gdańsk");
        InvoiceEntry invoiceEntry2 = new InvoiceEntry("Pamięć masowa", BigDecimal.valueOf(23), BigDecimal.valueOf(330), Vat.VAT_0);
        List<InvoiceEntry> entry = new ArrayList<>();
        entry.add(invoiceEntry2);

        Invoice invoice = new Invoice(LocalDate.now(), example, example, entry);
        Invoice invoice1 = new Invoice(LocalDate.now(), example, example, entry);

        db.save(invoice);
        db.save(invoice1);

        System.out.println(db.getAll());
       // db.delete(1);
        System.out.println(db.getAll());*/
}
