package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        Company.builder()
                .taxIdentificationNumber("$id")
                .address("ul. Jesionowa 23/$id 80-234 Gdańsk")
                .name("Sklep komputerowy Enter$id")
                .pensionInsurance(BigDecimal.TEN * BigDecimal.valueOf(id))
                .healthInsurance(BigDecimal.valueOf(100) * BigDecimal.valueOf(id))
                .build()
    }

    static invoiceEntry(int id) {
        InvoiceEntry.builder()
                .description("Pamięć masowa $id")
                .quantity(1)
                .netPrice(BigDecimal.valueOf(id * 10))
                .vatValue(BigDecimal.valueOf(id * 10 * 0.08))
                .vatRate(Vat.VAT_8)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .number("2012/$id")
                .buyer(company(id + 10))
                .seller(company(id))
                .invoiceEntries((1..id).collect({ invoiceEntry(it) }))
                .build()
    }
}
