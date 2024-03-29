package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice

class InvoiceServiceIntegrationTests extends Specification {
    private InvoiceService service
    private List<Invoice> invoices

    def setup() {
        Database db = new InMemoryDatabase()
        service = new InvoiceService(db)

        invoices = (1..12).collect { invoice(it) }
    }

    def "should save an invoice returning id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect { service.save(it) }

        then:
        ids == (1L..invoices.size()).collect()
        ids.forEach { assert service.getById(it).isPresent() }
        ids.forEach { assert service.getById(it).get().getId() == it }
        ids.forEach { assert service.getById(it).get() == invoices.get((int) it - 1) }


    }

    def "getById returns empty optional when there is no invoice with given id"() {
        expect:
        !service.getById(1).isPresent()
    }

    def "getAll returns empty collection when there are no invoices"() {
        expect:
        service.getAll().isEmpty()

    }

    def "getAll returns all saved invoices, deleted invoice is not returned"() {
        given:
        invoices.forEach { service.save(it) }

        expect:
        service.getAll().size() == invoices.size()
        service.getAll().forEach { assert it == invoices.get((int) it.getId() - 1) }

        when:
        service.delete(1)

        then:
        service.getAll().size() == invoices.size() - 1
        service.getAll().forEach { assert it == invoices.get((int) it.getId() - 1) }
        service.getAll().forEach({ assert it.getId() != 1 })

    }

    def "can delete all invoices"() {
        given:
        invoices.forEach { service.save(it) }

        when:
        service.getAll().forEach { service.delete(it.getId()) }

        then:
        service.getAll().isEmpty()
    }

    def "deleting non existing invoice is returning empty Optional"() {

        expect:
        service.delete(100) == Optional.empty()
    }

    def "it's possible to update an invoice"() {
        given:
        invoices.forEach { service.save(it) }

        when:
        service.update(1, invoices.get(4))

        then:
        service.getById(1).get() == invoices.get(4)
    }

    def "updating non existing invoice is returning empty optional"() {
        expect:
        service.update(100, invoices.get(1)) == Optional.empty()


    }
}
