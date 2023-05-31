package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
@Repository
public class FileBasedDatabase implements Database {

    private final Path databasePath;
    private final IdService idService;
    private final FilesService filesService;
    private final JsonService jsonService;

    @Override
    public int save(Invoice invoice) {
        try {
            invoice.setId(idService.getNextIdAndIncrement());
            filesService.appendLineToFile(databasePath, jsonService.toJson(invoice));
            return invoice.getId();
        } catch (IOException e) {
            throw new RuntimeException("Database failed to save invoice", e);
        }
    }

    @Override
    public Optional<Invoice> getById(int id) {
        try {
            return filesService.readAllLines(databasePath).stream().filter(line -> containsId(line, id))
                .map(line -> jsonService.toObject(line, Invoice.class)).findFirst();
        } catch (IOException ex) {
            throw new RuntimeException("Database failed to get invoice with id: " + id, ex);
        }
    }

    @Override
    public List<Invoice> getAll() {

        try {
            return Files.readAllLines(databasePath).stream().map(line -> jsonService.toObject(line, Invoice.class)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read invoices from file", e);
        }
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {

        try {

            List<String> allInvoices = filesService.readAllLines(databasePath);

            var invoicesExpectUpdated = allInvoices.stream().filter(line -> !line.contains(String.valueOf(id))).collect(Collectors.toList());

            updatedInvoice.setId(id);
            invoicesExpectUpdated.add(jsonService.toJson(updatedInvoice));

            filesService.writeLinesToFile(databasePath, invoicesExpectUpdated);

            allInvoices.removeAll(invoicesExpectUpdated);
            return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(allInvoices.get(0), Invoice.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to update invoice with id:" + id, e);
        }
    }

    @Override
    public Optional<Invoice> delete(int id) {
        try {
            List<String> allInvoices = filesService.readAllLines(databasePath);
            var invoiceListExpectDeleted =
                filesService.readAllLines(databasePath).stream().filter(line -> !containsId(line, id)).collect(Collectors.toList());
            filesService.writeLinesToFile(databasePath, invoiceListExpectDeleted);

            allInvoices.removeAll(invoiceListExpectDeleted);
            return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(allInvoices.get(0), Invoice.class));
        } catch (IOException e) {
            throw new RuntimeException("Filed to delete invoice with id: " + id, e);
        }

    }

    private boolean containsId(String line, int id) {
        return line.contains("\"id\":" + id + ",");
    }
}
