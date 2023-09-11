package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class MongoBasedDatabase<T extends WithId> implements Database<T> {

    private final MongoCollection<T> items;

    private MongoIdProvider mongoIdProvider;

    private static Document idFilter(long id) {
        return new Document("_id", id);
    }

    @Override
    public long save(T item) {
        item.setId(mongoIdProvider.getNextAndIncrement());
        items.insertOne(item);
        return item.getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(items.find(idFilter(id)).first());
    }

    @Override
    public List<T> getAll() {
        return Streamable.of(items.find()).toList();
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        updatedItem.setId(id);
        return Optional.ofNullable(items.findOneAndReplace(idFilter(id), updatedItem));
    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(items.findOneAndDelete(idFilter(id)));
    }
}
