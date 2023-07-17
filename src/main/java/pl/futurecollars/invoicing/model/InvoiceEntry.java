package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceEntry {

    @ApiModelProperty(value = "Product/service description", required = true, example = "Xbox one")
    private String description;

    @ApiModelProperty(value = "Number of items", required = true, example = "85")
    private int quantity;

    @ApiModelProperty(value = "Product/service net price", required = true, example = "250.01")
    private BigDecimal netPrice;

    @Builder.Default
    @ApiModelProperty(value = "Tax value of product/service", required = true, example = "50.01")
    private BigDecimal vatValue = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax rate", required = true, example = "VAT_23")
    private Vat vatRate;

    @ApiModelProperty(value = "This expense is related to car, empty if expense is not related to car")
    private Car expenseRelatedToCar;

}
