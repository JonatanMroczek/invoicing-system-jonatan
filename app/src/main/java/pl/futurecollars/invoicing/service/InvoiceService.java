package pl.futurecollars.invoicing.service;

import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

    private final Database db;
    private int id;

    public InvoiceService(Database db) {
        this.db = db;
    }

    public int save(Invoice invoice) {
        return db.save(invoice);

    }

    public Invoice getById(int id) {
        return db.getById(id);
    }

    public List<Invoice> getAll() {
        return db.getAll();

    }

    public void update(int id, Invoice updatedInvoice) {
        db.update(id, updatedInvoice);
    }

    public void delete(int id) {
        db.delete(id);
    }
}


