package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.utils.JsonService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice

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
        given:
        def invoiceAsJson = invoiceAsJson(1)

        expect:
        def firstId = addInvoiceAndReturnId(invoiceAsJson)
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 1
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 2
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 3
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 4
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 5
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 3
        def expectedInvoices = addUniqueInvoices(numberOfInvoices)

        when:
        def invoices = getAllInvoices()

        then:
        invoices.size() == numberOfInvoices
        invoices == expectedInvoices
    }

    def "correct invoice is returned when getting by id"() {
        given:
        def expectedInvoices = addUniqueInvoices(6)
        def verifiedInvoice = expectedInvoices.get(4)

        when:
        def invoice = getInvoiceById(verifiedInvoice.getId())

        then:
        invoice == verifiedInvoice


    }

    def "invoice can be modified"() {
        given:
        def invoiceId = addInvoiceAndReturnId(invoiceAsJson(57))
        def updatedInvoice = invoice(4)
        updatedInvoice.id = invoiceId

        when:
        mockMvc.perform(put("$ENDPOINT/$invoiceId")
                .content(jsonService.toJson(updatedInvoice))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())


        then:
        getInvoiceById(invoiceId) == updatedInvoice


    }

    def "invoice can be deleted"() {
        given:
        addUniqueInvoices(30)

        when:
        getAllInvoices().each { invoice -> deleteInvoice(invoice.getId()) }

        then:
        getAllInvoices().size() == 0
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


}