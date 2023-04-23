package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

class InvoiceServiceUnitTest extends Specification {
    private InvoiceService invoiceService
    private Database database


    def setup() {
        database = Mock()
        invoiceService = new InvoiceService(database)

    }


    def "Save"() {
        given:
        def invoice = TestHelpers.invoice(1)

        when:
        invoiceService.save(invoice)

        then:
        1 * database.save(invoice)


    }

    def "GetById"() {
        given:
        def invoiceId = 20

        when:
        invoiceService.delete(invoiceId)

        then:
        1 * database.delete(invoiceId)
    }

    def "GetAll"() {
        when:
        invoiceService.getAll()

        then:
        1 * database.getAll()

    }

    def "Update"() {
        given:
        def invoice = TestHelpers.invoice(5)

        when:
        invoiceService.update(invoice.getId(), invoice)

        then:
        1 * database.update(invoice.getId(), invoice)

    }

    def "Delete"() {
        given:
        def invoiceId = 200

        when:
        invoiceService.delete(invoiceId)

        then:
        database.delete(invoiceId)
    }
}
