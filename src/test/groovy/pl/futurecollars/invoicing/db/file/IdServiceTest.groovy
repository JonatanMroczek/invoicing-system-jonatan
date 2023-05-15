package pl.futurecollars.invoicing.db.file


import pl.futurecollars.invoicing.utils.FilesService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification {
    private Path nextIdDbPath = File.createTempFile("nextId", ".txt").toPath()

    def "next id starts from 1 if file was empty "() {
        given:
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ["1"] == Files.readAllLines(nextIdDbPath)

        and:
        1 == idService.getNextIdAndIncrement()
        ["2"] == Files.readAllLines(nextIdDbPath)

        and:
        2 == idService.getNextIdAndIncrement()
        ["3"] == Files.readAllLines(nextIdDbPath)
    }

    def "next id starts from last number if file was not empty"() {

        given:
        Files.writeString(nextIdDbPath, "15")
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ["15"] == Files.readAllLines(nextIdDbPath)

        and:
        15 == idService.getNextIdAndIncrement()
        ["16"] == Files.readAllLines(nextIdDbPath)

        and:
        16 == idService.getNextIdAndIncrement()
        ["17"] == Files.readAllLines(nextIdDbPath)
    }


}


