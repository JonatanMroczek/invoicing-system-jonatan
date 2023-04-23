package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements Database {

    private final HashMap<Integer, Invoice> invoices = new HashMap<>();
    private int index = 1;

    @Override
    public int save(Invoice invoice) {
        invoice.setId(index);
        invoices.put(index, invoice);
        return index++;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return Optional.ofNullable(invoices.get(id));
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
        updatedInvoice.setId(id);
        invoices.put(id, updatedInvoice);

    }

    @Override
    public void delete(int id) {
        invoices.remove(id);

    }

}
