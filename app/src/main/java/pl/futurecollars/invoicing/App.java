package pl.futurecollars.invoicing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.futurecollars.invoicing.config.Config;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.file.IdService;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;
import pl.futurecollars.invoicing.util.FilesService;
import pl.futurecollars.invoicing.util.JsonService;

public class App {

    public static void main(String[] args) throws IOException {

        FilesService filesService = new FilesService();
        JsonService jsonService = new JsonService();
        IdService idService = new IdService(Path.of(Config.ID_FILE_LOCATION),filesService);

        Database db = new FileBasedDatabase(filesService, Path.of(Config.DATABASE_LOCATION),jsonService,idService);
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
        db.delete(1);
        System.out.println(db.getAll());

//        objectMapper.writeValue(new File("invoices.yml"),invoice);
//
//        Invoice invoiceFromFile = objectMapper.readValue(new File("invoices.yml"), Invoice.class);
//        System.out.println(invoiceFromFile);
//        invoiceService.save(invoice1);
//        System.out.println(invoiceService.getById(1));
//        System.out.println(invoiceService.getById(5));
//        System.out.println(invoiceService.getAll());
//        invoiceService.delete(id);
//        System.out.println(invoiceService.getAll());
//        System.out.println(invoiceService.save(invoice1));
    }

}
