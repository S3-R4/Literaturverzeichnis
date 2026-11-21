package test.testfolder;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import model.data.DataManager;

class TestDataManagerExport {
    @TempDir Path directory;

    @Test
    void exportsImportedFileToNewPathAndKeepsOriginalActive() throws IOException {
        Path source = directory.resolve("original.json");
        String original = "{\"Buchbeleg\":[]}";
        Files.writeString(source, original);
        DataManager manager = new DataManager();
        manager.importJson(source);
        Path target = directory.resolve("new-folder/copy.json");
        manager.exportJson(target);
        assertEquals(original, Files.readString(target));
        Files.writeString(target, "{}");
        Path secondCopy = directory.resolve("second.json");
        manager.exportJson(secondCopy);
        assertEquals(original, Files.readString(secondCopy));
        assertEquals(original, Files.readString(source));
    }

    @Test
    void exportsNewlyCreatedFileAndReplacesExistingTarget() throws IOException {
        DataManager manager = new DataManager();
        assertTrue(manager.createJson(directory.resolve("new.json")));
        Path target = directory.resolve("existing.json");
        Files.writeString(target, "old contents");
        manager.exportJson(target);
        assertEquals("{}", Files.readString(target));
    }

    @Test
    void reportsInvalidDestinationWithoutChangingSource() throws IOException {
        Path source = directory.resolve("source.json");
        DataManager manager = new DataManager();
        manager.createJson(source);
        Path blocker = directory.resolve("file-not-directory");
        Files.writeString(blocker, "blocker");
        assertThrows(IOException.class, () -> manager.exportJson(blocker.resolve("copy.json")));
        assertEquals("{}", Files.readString(source));
    }
}
