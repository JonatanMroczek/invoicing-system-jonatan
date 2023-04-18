package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements Database {

    private final HashMap<Integer, Invoice> invoices = new HashMap<>();
    private int index = 1;

    @Override
    public int save(Invoice invoice) {
        invoice.setIndex(index);
        invoices.put(index, invoice);
        return index++;
    }

    @Override
    public Invoice getById(int id) {
        if (!invoices.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exist");
        }
        return invoices.get(id);
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(invoices.values());

    }

    @Override
    public void update(int id, Invoice updatedInvoice) {
        if (!invoices.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exist.");
        }
        updatedInvoice.setIndex(id);
        invoices.put(id, updatedInvoice);

    }

    @Override
    public void delete(int id) {
        invoices.remove(id);

    }


}
