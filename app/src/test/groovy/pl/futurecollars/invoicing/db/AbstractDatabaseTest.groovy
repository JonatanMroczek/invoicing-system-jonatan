package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

abstract class AbstractDatabaseTest extends Specification {

    List<Invoice> invoices = (1..12).collect { invoice(it) }
    Database db = getDatabaseInstance()


    abstract Database getDatabaseInstance()


    def "should save an invoice returning id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect { db.save(it) }

        then:
        ids == 1..invoices.size()
        ids.forEach { assert db.getById(it).isPresent() }
        ids.forEach { assert db.getById(it).get().getId() == it }
        ids.forEach { assert db.getById(it).get() == invoices.get(it - 1) }


    }

    def "getById returns empty optional when there is no invoice with given id"() {
        expect:
        !db.getById(1).isPresent()
    }

    def "getAll returns empty collection when there are no invoices"() {
        expect:
        db.getAll().isEmpty()

    }

    def "getAll returns all saved invoices, deleted invoice is not returned"() {
        given:
        invoices.forEach { db.save(it) }

        expect:
        db.getAll().size() == invoices.size()
        db.getAll().forEach { assert it == invoices.get(it.getId() - 1) }

        when:
        db.delete(1)

        then:
        db.getAll().size() == invoices.size() - 1
        db.getAll().forEach { assert it == invoices.get(it.getId() - 1) }
        db.getAll().forEach({ assert it.getId() != 1 })

    }

    def "can delete all invoices"() {
        given:
        invoices.forEach { db.save(it) }

        when:
        db.getAll().forEach { db.delete(it.getId()) }

        then:
        db.getAll().isEmpty()
    }

    def "deleting non existing invoice is not causing any error"() {

        expect:
        db.delete(100)
    }

    def "it's possible to update an invoice"() {
        given:
        invoices.forEach { db.save(it) }

        when:
        db.update(1, invoices.get(4))

        then:
        db.getById(1).get() == invoices.get(4)
    }

    def "updating non existing invoice is throwing and exception"() {
        when:
        db.update(100, invoices.get(1))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Id 100 does not exist."
    }
}
