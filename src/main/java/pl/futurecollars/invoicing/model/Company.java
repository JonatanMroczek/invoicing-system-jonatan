package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder

public class Company {

    @ApiModelProperty(value = "Company name", required = true, example = "Sklep Komputerowy Enter")
    private String name;

    @ApiModelProperty(value = "Tax identification number", required = true, example = "552-168-66-00")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "ul. Jesionowa 8b, 80-254 Gdańsk")
    private String address;

    public Company(String name, String taxIdentificationNumber, String address) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
