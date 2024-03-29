package pl.futurecollars.invoicing.db.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
@Configuration
public class InMemoryDatabaseConfiguration {

    @Bean
    public Database<Invoice> inMemoryInvoiceDatabase() {
        log.info("Creating in-memory invoice database");
        return new InMemoryDatabase<>();
    }

    @Bean
    public Database<Company> inMemoryCompanyDatabase() {
        log.info("Creating in-memory company database");
        return new InMemoryDatabase<>();
    }
}
