package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.JsonService;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    private static final String DATABASE_LOCATION = "db";
    private static final String ID_FILE_NAME = "id.txt";
    private static final String INVOICES_FILE_NAME = "invoices.txt";

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public Database fileBasedDatabase(
        IdService idService,
        FilesService filesService,
        JsonService jsonService,
        @Value("${invoicing-system.database.directory}") String databaseDirectory,
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile
    ) throws IOException {
        log.info("Creating in-file database");
        Path filePath = Files.createTempFile(databaseDirectory, invoicesFile);
        return new FileBasedDatabase(filePath, idService, filesService, jsonService);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public IdService idService(
        FilesService filesService,
        @Value("${invoicing-system.database.directory}") String databaseDirectory,
        @Value("${invoicing-system.database.id.file}") String idFile) throws IOException {
        Path idFilePath = Files.createTempFile(databaseDirectory, idFile);
        return new IdService(idFilePath, filesService);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    public Database inMemoryDatabase() {
        log.info("Creating in-memory database");
        return new InMemoryDatabase();
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
    public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
        log.info("Creating SQL database");
        return new SqlDatabase(jdbcTemplate);
    }

}
