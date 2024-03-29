package pl.futurecollars.invoicing.controller.Invoice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.controller.AbstractControllerTest
import pl.futurecollars.invoicing.utils.JsonService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice
import static pl.futurecollars.invoicing.helpers.TestHelpers.resetIds

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService

    private static final String ENDPOINT = "/invoices"

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "get all invoices returns empty array when no invoices added"() {
        expect:
        getAllInvoices() == []

    }

    def "add invoice returns sequential id"() {

        expect:
        def firstId = addInvoiceAndReturnId(invoice(1))
        addInvoiceAndReturnId(invoice(2)) == firstId + 1
        addInvoiceAndReturnId(invoice(3)) == firstId + 2
        addInvoiceAndReturnId(invoice(4)) == firstId + 3
        addInvoiceAndReturnId(invoice(5)) == firstId + 4
        addInvoiceAndReturnId(invoice(6)) == firstId + 5
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 3
        def expectedInvoices = addUniqueInvoices(numberOfInvoices)

        when:
        def invoices = getAllInvoices()

        then:
        invoices.size() == numberOfInvoices
        invoices.forEach { resetIds(it) }
        expectedInvoices.forEach { resetIds(it) }
        invoices == expectedInvoices
    }

    def "correct invoice is returned when getting by id"() {
        given:
        def expectedInvoices = addUniqueInvoices(6)
        def verifiedInvoice = expectedInvoices.get(4)

        when:
        def invoice = getInvoiceById(verifiedInvoice.getId())

        then:
        resetIds(invoice) == resetIds(verifiedInvoice)


    }

    def "invoice can be modified"() {
        given:
        def invoiceId = addInvoiceAndReturnId(invoice(57))
        def updatedInvoice = invoice(4)
        updatedInvoice.id = invoiceId

        when:
        mockMvc.perform(put("$ENDPOINT/$invoiceId")
                .content(jsonService.toJson(updatedInvoice))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())


        then:
        def invoiceFromDbAfterUpdate = resetIds(getInvoiceById(invoiceId))
        def expectedInvoice = resetIds(updatedInvoice)
        invoiceFromDbAfterUpdate.toString() == expectedInvoice.toString()


    }

    def "404 is returned when invoice id is not found when getting invoice by id [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                get("$ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 168, 1256]
    }

    def "404 is returned when invoice id is not found when deleting invoice [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                delete("$ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "404 is returned when invoice id is not found when updating invoice [#id]"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                put("$ENDPOINT/$id")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }


    def "invoice can be deleted"() {
        given:
        addUniqueInvoices(30)

        when:
        getAllInvoices().each { invoice -> deleteInvoice(invoice.getId()) }

        then:
        getAllInvoices().size() == 0
    }
}