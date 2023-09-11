package pl.futurecollars.invoicing.controller.company

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.controller.AbstractControllerTest
import pl.futurecollars.invoicing.utils.JsonService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.helpers.TestHelpers.company

class CompanyControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService


    def setup() {
        getAllInvoices().each { company -> deleteCompany(company.id) }
    }

    def "get all invoices returns empty array when no companies added"() {
        expect:
        getAllCompanies() == []

    }

    def "add company returns sequential id"() {

        expect:
        def firstId = addCompanyAndReturnId(company(1))
        addCompanyAndReturnId(company(2)) == firstId + 1
        addCompanyAndReturnId(company(3)) == firstId + 2
        addCompanyAndReturnId(company(4)) == firstId + 3
        addCompanyAndReturnId(company(5)) == firstId + 4
        addCompanyAndReturnId(company(6)) == firstId + 5
    }

    def "all companies are returned when getting all companies"() {
        given:
        def numberOfCompanies = 3
        def expectedCompanies = addUniqueCompanies(numberOfCompanies)

        when:
        def companies = getAllCompanies()

        then:
        companies.size() == numberOfCompanies
        companies == expectedCompanies
    }

    def "correct company is returned when getting by id"() {
        given:
        def expectedCompanies = addUniqueCompanies(6)
        def verifiedCompany = expectedCompanies.get(4)

        when:
        def company = getCompanyById(verifiedCompany.getId())

        then:
        company == verifiedCompany


    }

    def "company can be modified"() {
        given:
        def companyId = addCompanyAndReturnId(company(57))
        def updatedCompany = company(4)
        updatedCompany.id = companyId

        expect:
        mockMvc.perform(
                put("$COMPANY_ENDPOINT/$companyId")
                        .content(jsonService.toJson(updatedCompany))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())


        def companyFromDbAfterUpdate = getCompanyById(companyId).toString()
        def expectedCompany = updatedCompany.toString()
        companyFromDbAfterUpdate == expectedCompany


    }


    def "404 is returned when company id is not found when getting company by id [#id]"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                get("$COMPANY_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 168, 1256]
    }

    def "404 is returned when company id is not found when deleting company [#id]"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                delete("$COMPANY_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "404 is returned when company id is not found when updating company [#id]"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                put("$COMPANY_ENDPOINT/$id")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }


    def "company can be deleted"() {
        given:
        addUniqueCompanies(30)

        when:
        getAllCompanies().each { company -> deleteCompany(company.getId()) }

        then:
        getAllCompanies().size() == 0
    }
}