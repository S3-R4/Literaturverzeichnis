package test.testfolder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import model.BildQuelle;
import model.data.DataManager;
import tools.jackson.databind.json.JsonMapper;

class TestDataManagerWorkingFile {
    @TempDir Path directory;

    @Test
    void reopensInterruptedSessionOnlyAfterRecoveryDecision() throws IOException {
        Path original = directory.resolve("interrupted.json");
        DataManager first = new DataManager();
        first.createJson(original);
        String id = first.add_data(new BildQuelle());

        DataManager reopened = new DataManager();
        assertThrows(java.nio.file.FileAlreadyExistsException.class, () -> reopened.importJson(original));
        reopened.recoverJson(original);
        assertEquals(id, reopened.getId(reopened.readAllSources().get(0)));
        assertEquals("{}", Files.readString(original));
        reopened.save();

        reopened.delete_data_by_id(id);
        DataManager discarded = new DataManager();
        discarded.discardRecoveryAndImport(original);
        assertEquals(id, discarded.getId(discarded.readAllSources().get(0)));
    }

    @Test
    void editedSourceKeepsItsIdForFurtherEditsAndDeletion() throws IOException {
        DataManager manager = new DataManager();
        Path original = directory.resolve("edit-delete.json");
        manager.createJson(original);
        BildQuelle first = new BildQuelle();
        String id = manager.add_data(first);
        BildQuelle edited = new BildQuelle();
        edited.setTitel(java.util.Optional.of("Edited title"));
        assertTrue(manager.edit_entry(id, edited));
        assertNull(manager.getId(first));
        assertEquals(id, manager.getId(edited));
        assertTrue(manager.read_data_by_id(id).contains("Edited title"));
        BildQuelle editedAgain = new BildQuelle();
        editedAgain.setTitel(java.util.Optional.of("Second edit"));
        assertTrue(manager.edit_entry(manager.getId(edited), editedAgain));
        assertNull(manager.getId(edited));
        assertTrue(manager.read_data_by_id(id).contains("Second edit"));
        assertTrue(manager.delete_data_by_id(manager.getId(editedAgain)));
        assertNull(manager.getId(editedAgain));
        assertTrue(manager.readAllSources().isEmpty());
        assertEquals("{}", Files.readString(original));
    }

    @Test
    void discardingRemovesWorkingCopyAndPreservesOriginal() throws IOException {
        DataManager manager = new DataManager();
        Path original = directory.resolve("discard.json");
        manager.createJson(original);
        BildQuelle source = new BildQuelle();
        manager.add_data(source);
        Path working = Path.of(original + ".swp");
        assertTrue(Files.exists(working));
        manager.deleteWorkingFile();
        assertFalse(Files.exists(working));
        assertEquals("{}", Files.readString(original));
        assertNull(manager.getId(source));
        manager.deleteWorkingFile();
        assertTrue(manager.readAllSources().isEmpty());
    }

    @Test
    void cleanupAfterSavingPreservesSavedChanges() throws IOException {
        DataManager manager = new DataManager();
        Path original = directory.resolve("saved.json");
        manager.createJson(original);
        manager.add_data(new BildQuelle());
        manager.save();
        String saved = Files.readString(original);
        manager.deleteWorkingFile();
        assertFalse(Files.exists(Path.of(original + ".swp")));
        assertEquals(saved, Files.readString(original));
        assertEquals(1, manager.readAllSources().size());
    }

    @Test
    void changesStayInWorkingCopyUntilSaved() throws IOException {
        Path original = directory.resolve("sources.json");
        Files.writeString(original, "{}");
        DataManager manager = new DataManager();
        manager.importJson(original);

        String id = manager.add_data(new BildQuelle());
        assertEquals("{}", Files.readString(original));
        assertTrue(manager.read_data_by_id(id).contains(id));
        assertEquals(1, manager.readAllSources().size());
        assertTrue(manager.edit_entry(id, new BildQuelle()));
        assertEquals("{}", Files.readString(original));

        Path exported = directory.resolve("export.json");
        manager.exportJson(exported, "Dunkel");
        var exportedJson = JsonMapper.builder().build().readTree(exported.toFile());
        assertEquals("Dunkel", exportedJson.path("theme").asText());
        assertTrue(exportedJson.path("BildQuelle").isArray());
        manager.save();
        assertEquals(Files.readString(Path.of(original + ".swp")), Files.readString(original));
        assertFalse(Files.exists(Path.of(original + ".swp.tmp")));

        assertTrue(manager.delete_data_by_id(id));
        assertTrue(manager.readAllSources().isEmpty());
        assertTrue(JsonMapper.builder().build().readTree(original.toFile()).path("BildQuelle").isArray());
        manager.save();
        assertEquals(Files.readString(Path.of(original + ".swp")), Files.readString(original));
    }

    @Test
    void switchingFilesUsesTheNewWorkingCopy() throws IOException {
        DataManager manager = new DataManager();
        Path first = directory.resolve("first.json");
        assertTrue(manager.createJson(first));
        String id = manager.add_data(new BildQuelle());
        Path second = directory.resolve("second.json");
        Files.writeString(second, "{}");
        manager.importJson(second);
        assertTrue(manager.readAllSources().isEmpty());
        assertFalse(manager.delete_data_by_id(id));
        manager.add_data(new BildQuelle());
        manager.save();
        assertEquals("{}", Files.readString(first));
        assertNotEquals("{}", Files.readString(second));
    }
}
