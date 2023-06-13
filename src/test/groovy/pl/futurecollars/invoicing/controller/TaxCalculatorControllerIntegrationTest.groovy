package pl.futurecollars.invoicing.controller


import spock.lang.Unroll

@Unroll
class TaxCalculatorControllerIntegrationTest extends AbstractControllerTest {
    def "zeros are returned when no invoice is added to the system"() {
        when:
        def taxCalculatorResponse = calculateTax("0")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0


    }

    def "zeros are returned when no invoice is added to the system"() {

        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("no match")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("5")

        then:
        taxCalculatorResponse.income == 150
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 150
        taxCalculatorResponse.incomingVat == 12
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 12

        when:
        taxCalculatorResponse = calculateTax("4")

        then:
        taxCalculatorResponse.income == 100
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 100
        taxCalculatorResponse.incomingVat == 8
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 8

        when:
        taxCalculatorResponse = calculateTax("8")

        then:
        taxCalculatorResponse.income == 360
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 360
        taxCalculatorResponse.incomingVat == 28.8
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 28.8

        when:
        taxCalculatorResponse = calculateTax("15")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 150
        taxCalculatorResponse.earnings == -150
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 12
        taxCalculatorResponse.vatToReturn == -12

    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addUniqueInvoices(15) // sellers: 1-15, buyers: 10-25, 10-15 overlapping

        when:
        def taxCalculatorResponse = calculateTax("12")

        then:
        taxCalculatorResponse.income == 780
        taxCalculatorResponse.costs == 30
        taxCalculatorResponse.earnings == 750
        taxCalculatorResponse.incomingVat == 62.4
        taxCalculatorResponse.outgoingVat == 2.4
        taxCalculatorResponse.vatToReturn == 60
    }


}
