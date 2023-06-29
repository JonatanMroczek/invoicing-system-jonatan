package pl.futurecollars.invoicing.controller.tax

import pl.futurecollars.invoicing.controller.AbstractControllerTest
import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import spock.lang.Unroll

import static pl.futurecollars.invoicing.TestHelpers.company

@Unroll
class TaxCalculatorControllerIntegrationTest extends AbstractControllerTest {

    def "zeros are returned when no invoice is added to the system"() {
        when:
        def taxCalculatorResponse = calculateTax(company(0))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 0


    }

    def "zeros are returned when no invoice is added to the system"() {

        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax(company(-12))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax(company(5))

        then:
        taxCalculatorResponse.income == 150
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 150
        taxCalculatorResponse.collectedVat == 12
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 12

        when:
        taxCalculatorResponse = calculateTax(company(4))

        then:
        taxCalculatorResponse.income == 100
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 100
        taxCalculatorResponse.collectedVat == 8
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 8

        when:
        taxCalculatorResponse = calculateTax(company(8))

        then:
        taxCalculatorResponse.income == 360
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 360
        taxCalculatorResponse.collectedVat == 28.8
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 28.8

        when:
        taxCalculatorResponse = calculateTax(company(15))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 150
        taxCalculatorResponse.incomeMinusCosts == -150
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 12
        taxCalculatorResponse.vatToReturn == -12

    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addUniqueInvoices(15) // sellers: 1-15, buyers: 10-25, 10-15 overlapping

        when:
        def taxCalculatorResponse = calculateTax(company(12))

        then:
        taxCalculatorResponse.income == 780
        taxCalculatorResponse.costs == 30
        taxCalculatorResponse.incomeMinusCosts == 750
        taxCalculatorResponse.collectedVat == 62.4
        taxCalculatorResponse.paidVat == 2.4
        taxCalculatorResponse.vatToReturn == 60
    }


    def "tax is calculated correctly when car is used for personal purposes"() {
        given:
        def invoice = Invoice.builder()
                .seller(company(1))
                .buyer(company(2))
                .invoiceEntries(List.of(
                        InvoiceEntry.builder()
                                .vatValue(BigDecimal.valueOf(23.45))
                                .netPrice(BigDecimal.valueOf(100))
                                .expenseRelatedToCar(
                                        Car.builder()
                                                .personalUse(true)
                                                .build()
                                )
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoice)

        when:
        def taxCalculatorResponse = calculateTax(invoice.getSeller())

        then: "no proportion - it applies only when you are the buyer"
        taxCalculatorResponse.income == 100
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 100
        taxCalculatorResponse.collectedVat == 23.45
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 23.45

        when:
        taxCalculatorResponse = calculateTax(invoice.getBuyer())

        then: "proportion applied - it applies when you are the buyer"
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 111.73
        taxCalculatorResponse.incomeMinusCosts == -111.73
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 11.72
        taxCalculatorResponse.vatToReturn == -11.72
    }

    def "All calculations are executed correctly"() {
        given:
        def ourCompany = Company.builder()
                .taxIdentificationNumber("1234")
                .pensionInsurance(514.57)
                .healthInsurance(319.94)
                .build()

        def invoiceWithIncome = Invoice.builder()
                .seller(ourCompany)
                .buyer(company(2))
                .invoiceEntries(List.of(
                        InvoiceEntry.builder()
                                .netPrice(76011.62)
                                .build()
                ))
                .build()

        def invoiceWithCosts = Invoice.builder()
                .seller(company(4))
                .buyer(ourCompany)
                .invoiceEntries(List.of(
                        InvoiceEntry.builder()
                                .netPrice(11329.47)
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoiceWithIncome)
        addInvoiceAndReturnId(invoiceWithCosts)

        when:
        def taxCalculatorResponse = calculateTax(ourCompany)

        then:
        with(taxCalculatorResponse) {
            income == 76011.62
            costs == 11329.47
            incomeMinusCosts == 64682.15
            pensionInsurance == 514.57
            incomeMinusCostsMinusPensionInsurance == 64167.58
            incomeMinusCostsMinusPensionInsuranceRounded == 64168
            incomeTax == 12191.92
            healthInsurancePaid == 319.94
            healthInsuranceToSubtract == 275.50
            incomeTaxMinusHealthInsurance == 11916.42
            finalIncomeTax == 11916

            collectedVat == 0
            paidVat == 0
            vatToReturn == 0
        }
    }

    def "All calculations are executed correctly"() {
        given:
        def ourCompany = Company.builder()
                .taxIdentificationNumber("1234")
                .pensionInsurance(BigDecimal.valueOf(514.57))
                .healthInsurance(319.94)
                .build()

        def invoiceWithIncome = Invoice.builder()
                .seller(ourCompany)
                .buyer(company(2))
                .invoiceEntries(List.of(
                        InvoiceEntry.builder()
                                .netPrice(76011.62)
                                .build()
                ))
                .build()

        def invoiceWithCosts = Invoice.builder()
                .seller(company(4))
                .buyer(ourCompany)
                .invoiceEntries(List.of(
                        InvoiceEntry.builder()
                                .netPrice(11329.47)
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoiceWithIncome)
        addInvoiceAndReturnId(invoiceWithCosts)

        when:
        def taxCalculatorResponse = calculateTax(ourCompany)

        then:
        with(taxCalculatorResponse) {
            income == 76011.62
            costs == 11329.47
            incomeMinusCosts == 64682.15
            pensionInsurance == 514.57
            incomeMinusCostsMinusPensionInsurance == 64167.58
            incomeMinusCostsMinusPensionInsuranceRounded == 64168
            incomeTax == 12191.92
            healthInsurancePaid == 319.94
            healthInsuranceToSubtract == 275.50
            incomeTaxMinusHealthInsurance == 11916.42
            finalIncomeTax == 11916

            collectedVat == 0
            paidVat == 0
            vatToReturn == 0
        }
    }
}
