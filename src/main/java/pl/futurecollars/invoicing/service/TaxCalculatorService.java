package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@AllArgsConstructor
@Service
public class TaxCalculatorService {

    private final Database database;

    public BigDecimal income(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal cost(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal incomingVat(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal getEarnings(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(cost(taxIdentificationNumber));
    }

    public BigDecimal getVatToReturn(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalculatorResult calculateTaxes(String taxIdentificationNumber) {
        return TaxCalculatorResult.builder()
            .income(income(taxIdentificationNumber))
            .costs(cost(taxIdentificationNumber))
            .earnings(getEarnings(taxIdentificationNumber))
            .incomingVat(incomingVat(taxIdentificationNumber))
            .outgoingVat(outgoingVat(taxIdentificationNumber))
            .vatToReturn(getVatToReturn(taxIdentificationNumber))
            .build();
    }

    public Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
        return invoice -> taxIdentificationNumber.equals(invoice.getBuyer().getTaxIdentificationNumber());

    }

    public Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
        return invoice -> taxIdentificationNumber.equals(invoice.getSeller().getTaxIdentificationNumber());
    }
}
