package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceEntry {
    private String description;
    private BigDecimal price;
    private BigDecimal vatValue;
    private Enum<Vat> VatRate;

}
