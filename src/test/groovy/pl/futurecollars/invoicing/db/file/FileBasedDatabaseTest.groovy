package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FilesService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files

class FileBasedDatabaseTest extends AbstractDatabaseTest {

    def dbPath

    @Override
    Database getDatabaseInstance() {
        FilesService filesService = new FilesService()
        def idPath = File.createTempFile("ids", ".txt").toPath()
        dbPath = File.createTempFile("invoices", ".txt").toPath()
        IdService idService = new IdService(idPath, filesService)
        return new FileBasedDatabase(dbPath, idService, filesService, new JsonService(), Invoice)
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()


        when:
        db.save(TestHelpers.invoice(4))

        then:

        1 == Files.readAllLines(dbPath).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(dbPath).size()
    }
}
