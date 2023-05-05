package pl.futurecollars.invoicing.db


import pl.futurecollars.invoicing.db.memory.InMemoryDatabase

class InMemoryDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
    }
}