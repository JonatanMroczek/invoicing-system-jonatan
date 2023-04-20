package pl.futurecollars.invoicing.model;

import lombok.Data;

@Data

public class Company {

    private String name;
    private String taxIdentificationNumber;
    private String address;

    public Company(String name, String taxIdentificationNumber, String address) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
