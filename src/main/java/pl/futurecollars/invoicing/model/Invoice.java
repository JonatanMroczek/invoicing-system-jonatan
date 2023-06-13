package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

    @ApiModelProperty(value = "Invoice id (generated by application)", required = true, example = "1")
    private int id;

    @ApiModelProperty(value = "Date of invoice creation", required = true, example = "2021-05-23")
    private LocalDate date;

    @ApiModelProperty(value = "Company who bought the product/service", required = true)
    private Company seller;

    @ApiModelProperty(value = "Company who is selling the product/service", required = true)
    private Company buyer;

    @ApiModelProperty(value = "List of products/services", required = true)
    private List<InvoiceEntry> invoiceEntries;

    public Invoice(LocalDate date, Company buyer, Company seller, List<InvoiceEntry> invoiceEntries) {
        this.date = date;
        this.seller = buyer;
        this.buyer = seller;
        this.invoiceEntries = invoiceEntries;
    }
}
