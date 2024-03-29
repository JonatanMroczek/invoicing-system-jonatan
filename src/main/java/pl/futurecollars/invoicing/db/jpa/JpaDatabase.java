package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class JpaDatabase<T extends WithId> implements Database<T> {

    private final CrudRepository<T, Long> repository;

    @Override
    public long save(T item) {
        return repository.save(item).getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return Streamable.of(repository.findAll()).toList();

    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        Optional<T> itemOptional = getById(id);

        if (itemOptional.isPresent()) {
            updatedItem.setId(id);
            repository.save(updatedItem);
        }

        return itemOptional;
    }

    @Override
    public Optional<T> delete(long id) {
        Optional<T> item = getById(id);

        item.ifPresent(repository::delete);

        return item;
    }
}
