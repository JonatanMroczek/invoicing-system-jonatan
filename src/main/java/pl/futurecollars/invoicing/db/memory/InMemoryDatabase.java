package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

public class InMemoryDatabase<T extends WithId> implements Database<T> {

    private final HashMap<Long, T> items = new HashMap<>();
    private long index = 1;

    @Override
    public long save(T item) {
        item.setId(index);
        items.put(index, item);
        return index++;
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(items.values());

    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        updatedItem.setId(id);
        return Optional.ofNullable(items.put(id, updatedItem));

    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(items.remove(id));

    }

}
