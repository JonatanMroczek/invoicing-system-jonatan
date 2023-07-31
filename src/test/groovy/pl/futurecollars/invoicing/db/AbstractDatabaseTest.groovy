package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice
abstract class AbstractDatabaseTest extends Specification {

    List<Invoice> invoices = (1..12).collect { invoice(it) }

    abstract Database getDatabaseInstance()

    Database database

    def setup() {
        database = getDatabaseInstance()
        database.reset()

        assert database.getAll().isEmpty()
    }

    def "should save an invoice returning id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect { database.save(it) }

        then:
        ids == (1L..invoices.size()).collect()
        ids.forEach { assert database.getById(it).isPresent() }
        ids.forEach { assert database.getById(it).get().getId() == it }
        ids.forEach {

            def expectedInvoice = resetIds(invoices.get((int) it - 1))
            def invoiceFromDb = resetIds(database.getById(it).get())
            assert invoiceFromDb.toString() == expectedInvoice.toString()}


    }

    def "getById returns empty optional when there is no invoice with given id"() {
        expect:
        !database.getById(1).isPresent()
    }

    def "getAll returns empty collection when there are no invoices"() {
        expect:
        database.getAll().isEmpty()

    }

    def "getAll returns all saved invoices, deleted invoice is not returned"() {
        given:
        invoices.forEach { database.save(it) }

        expect:
        database.getAll().size() == invoices.size()
        database.getAll().forEach { assert it == invoices.get((int)it.getId() - 1) }

        when:
        database.delete(1)

        then:
        database.getAll().size() == invoices.size() - 1
        database.getAll().forEach { assert it == invoices.get((int)it.getId() - 1) }
        database.getAll().forEach({ assert it.getId() != 1 })

    }

    def "can delete all invoices"() {
        given:
        invoices.forEach { database.save(it) }

        when:
        database.getAll().forEach { database.delete(it.getId()) }

        then:
        database.getAll().isEmpty()
    }

    def "deleting non existing invoice is not causing any error"() {

        expect:
        database.delete(100) == Optional.empty()
    }

    def "it's possible to update an invoice"() {
        given:
        invoices.forEach { database.save(it) }

        when:
        database.update(1, invoices.get(4))

        then:
        database.getById(1).get() == invoices.get(4)
    }

    def "updating non existing invoice is returning empty optional"() {
        expect:
        database.update(100, invoices.get(1)) == Optional.empty()


    }
    // resetting is necessary because database query returns ids while we don't know ids in original invoice
    private static Invoice resetIds(Invoice invoice) {
        invoice.getBuyer().id = null
        invoice.getSeller().id = null
        invoice.invoiceEntries.forEach {
            it.id = null
       }
        invoice
    }
}