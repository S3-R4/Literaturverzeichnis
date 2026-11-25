package model.data;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Stores the last opened original bibliography across application launches. */
public final class LastBibliography {
    private static final Path RECORD = Path.of(System.getProperty("user.home"),
            ".literaturverzeichnis", "letzte-datei.txt");

    private LastBibliography() {}

    public static void remember(Path file) throws IOException {
        Files.createDirectories(RECORD.getParent());
        Path temp = Files.createTempFile(RECORD.getParent(), "letzte-datei-", ".tmp");
        try {
            Files.writeString(temp, file.toAbsolutePath().toString());
            try {
                Files.move(temp, RECORD, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, RECORD, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    public static Path read() throws IOException {
        String stored;
        try {
            stored = Files.readString(RECORD);
        } catch (NoSuchFileException e) {
            throw new IOException("Noch keine Datei hinterlegt.", e);
        } catch (IOException e) {
            throw new IOException("Letzter Dateipfad nicht lesbar.", e);
        }
        try {
            Path path = Path.of(stored);
            if (!path.isAbsolute()) {
                throw new IOException("Gespeicherter Dateipfad ungültig.");
            }
            return path;
        } catch (InvalidPathException e) {
            throw new IOException("Gespeicherter Dateipfad ungültig.", e);
        }
    }
}
