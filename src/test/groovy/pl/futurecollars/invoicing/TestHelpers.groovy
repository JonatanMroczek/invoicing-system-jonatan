package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        Company.builder()
                .name("Sklep komputerowy Enter$id")
                .taxIdentificationNumber("$id")
                .address("ul. Jesionowa 23/$id 80-234 Gdańsk")
                .build()
    }

    static invoiceEntry(int id) {
        InvoiceEntry.builder()
                .description("Pamięć masowa $id")
                .quantity(1)
                .price(BigDecimal.valueOf(id * 10))
                .vatValue(BigDecimal.valueOf(id * 10 * 0.08))
                .vatRate(Vat.VAT_8)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .seller(company(id))
                .buyer(company(id + 10))
                .invoiceEntries((1..id).collect({ invoiceEntry(it) }))
                .build()
    }
}
