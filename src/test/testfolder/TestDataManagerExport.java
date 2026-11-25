package test.testfolder;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import model.Buchbeleg;
import model.data.DataManager;
import tools.jackson.databind.json.JsonMapper;

class TestDataManagerExport {
    @TempDir Path directory;

    @Test
    void exportsBookWithNumericEditionAndPagesAndImportsItAgain() throws IOException {
        DataManager manager = new DataManager();
        assertTrue(manager.createJson(directory.resolve("books.json")));
        manager.add_data(new Buchbeleg(List.of("Anna Müller"), Optional.of(LocalDate.of(2020, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.of(3), Optional.of("Berlin"),
                Optional.of(12), Optional.of(15)));
        Path exported = directory.resolve("export.json");
        manager.exportJson(exported, "Dunkel");

        var book = JsonMapper.builder().build().readTree(exported.toFile()).path("Buchbeleg").path(0);
        assertTrue(book.path("auflage").isIntegralNumber());
        assertTrue(book.path("seitenangabe").path("anfang").isIntegralNumber());
        assertTrue(book.path("seitenangabe").path("ende").isIntegralNumber());

        DataManager reimported = new DataManager();
        reimported.importJson(exported);
        Buchbeleg restored = (Buchbeleg) reimported.readAllSources().getFirst();
        assertEquals("3", restored.getAuflage());
        assertEquals("12-15", restored.getSeitenangabe());
    }

    @Test
    void importsLegacyBookFieldsAsTextOrSinglePageNumber() throws IOException {
        Path source = directory.resolve("legacy.json");
        Files.writeString(source, """
                {"Buchbeleg":[
                  {"id":"range","auflage":"3","seitenangabe":"12-15"},
                  {"id":"page","auflage":2,"seitenangabe":234},
                  {"id":"empty","auflage":"","seitenangabe":""}
                ]}
                """);
        DataManager manager = new DataManager();
        manager.importJson(source);

        var books = manager.readAllSources();
        assertEquals("3", ((Buchbeleg) books.get(0)).getAuflage());
        assertEquals("12-15", ((Buchbeleg) books.get(0)).getSeitenangabe());
        assertEquals("234", ((Buchbeleg) books.get(1)).getSeitenangabe());
        assertEquals("", ((Buchbeleg) books.get(2)).getAuflage());
        assertEquals("", ((Buchbeleg) books.get(2)).getSeitenangabe());
    }

    @Test
    void exportsImportedFileToNewPathAndKeepsOriginalActive() throws IOException {
        Path source = directory.resolve("original.json");
        String original = "{\"Buchbeleg\":[],\"theme\":\"Dunkel\"}";
        Files.writeString(source, original);
        DataManager manager = new DataManager();
        manager.importJson(source);
        Path target = directory.resolve("new-folder/copy.json");
        manager.exportJson(target,"Dunkel");
        var exportedJson = JsonMapper.builder().build().readTree(target.toFile());
        assertEquals("Dunkel", exportedJson.path("theme").asText());
        assertTrue(exportedJson.path("Buchbeleg").isArray());
        Files.writeString(target, "{}");
        Path secondCopy = directory.resolve("second.json");
        manager.exportJson(secondCopy,"Dunkel");
        var secondExport = JsonMapper.builder().build().readTree(secondCopy.toFile());
        assertEquals("Dunkel", secondExport.path("theme").asText());
        assertEquals(original, Files.readString(source));
    }

    @Test
    void exportsNewlyCreatedFileAndReplacesExistingTarget() throws IOException {
        DataManager manager = new DataManager();
        assertTrue(manager.createJson(directory.resolve("new.json")));
        Path target = directory.resolve("existing.json");
        Files.writeString(target, "old contents");
        manager.exportJson(target,"Dunkel");
        var exportedJson = JsonMapper.builder().build().readTree(target.toFile());
        assertEquals("Dunkel", exportedJson.path("theme").asText());
    }

    @Test
    void reportsInvalidDestinationWithoutChangingSource() throws IOException {
        Path source = directory.resolve("source.json");
        DataManager manager = new DataManager();
        manager.createJson(source);
        Path blocker = directory.resolve("file-not-directory");
        Files.writeString(blocker, "blocker");
        assertThrows(IOException.class, () -> manager.exportJson(blocker.resolve("copy.json"),"Dunkel"));
        assertEquals("{}", Files.readString(source));
    }
}
