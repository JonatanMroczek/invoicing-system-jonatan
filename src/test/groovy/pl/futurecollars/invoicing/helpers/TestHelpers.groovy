package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(long id) {
        Company.builder()
                .taxIdentificationNumber("$id")
                .address("ul. Jesionowa 23/$id 80-234 Gdańsk")
                .name("Sklep komputerowy Enter$id")
                .pensionInsurance(BigDecimal.TEN * BigDecimal.valueOf(id).setScale(2))
                .healthInsurance(BigDecimal.valueOf(100) * BigDecimal.valueOf(id).setScale(2))
                .build()
    }

    static invoiceEntry(long id) {
        InvoiceEntry.builder()
                .description("Pamięć masowa $id")
                .quantity(BigDecimal.valueOf(1).setScale(2))
                .netPrice(BigDecimal.valueOf(id * 10).setScale(2))
                .vatValue(BigDecimal.valueOf(id * 10 * 0.08).setScale(2))
                .vatRate(Vat.VAT_8)
                .build()
    }

    static invoice(long id) {
        Invoice.builder()
                .date(LocalDate.now())
                .number("2012/$id")
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ invoiceEntry(it) }))
                .build()
    }
}
