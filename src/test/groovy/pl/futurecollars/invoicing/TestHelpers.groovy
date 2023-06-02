package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {
    static company(int id) {
        new Company("Sklep komputerowy Enter$id", "552-168-66-$id", "ul. Jesionowa 23/$id 80-234 Gdańsk")

    }

    static invoiceEntry(int id) {
        new InvoiceEntry("Pamięć masowa $id", id * 2, BigDecimal.valueOf(id * 10), BigDecimal.valueOf(id * 10 * 0.08), Vat.VAT_8)
    }

    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id), company(id + 1), List.of(invoiceEntry(id)))
    }
}
