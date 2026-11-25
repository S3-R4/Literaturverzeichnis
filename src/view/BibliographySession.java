package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Literatur;
import model.data.DataManager;
import model.data.LastBibliography;

final class BibliographySession {
    private BibliographySession() {}

    static void openExisting(JFrame parent, Path file) throws IOException {
        DataManager manager = new DataManager();
        if (Files.exists(DataManager.workingPathFor(file))) {
            String[] options = { "Wiederherstellen", "Änderungen verwerfen", "Abbrechen" };
            int choice = JOptionPane.showOptionDialog(parent,
                    "Für " + file.getFileName() + " wurde eine Arbeitskopie gefunden.\n"
                    + "Das Programm wurde möglicherweise unerwartet beendet.\n"
                    + "Möchten Sie die Änderungen wiederherstellen?",
                    "Arbeitskopie gefunden", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 0) {
                manager.recoverJson(file);
            } else if (choice == 1) {
                manager.discardRecoveryAndImport(file);
            } else {
                return;
            }
        } else {
            manager.importJson(file);
        }
        showEditor(parent, manager, file);
    }

    static void showEditor(JFrame parent, DataManager manager, Path file) throws IOException {
        var sources = manager.readAllSources();
        String savedTheme = manager.readTheme();
        GUI_Literaturverwaltung editor = new GUI_Literaturverwaltung(manager, savedTheme);
        editor.addTableRows(sources.toArray(Literatur[]::new));
        parent.dispose();
        try {
            LastBibliography.remember(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(editor, "Der Pfad zum Weiterarbeiten konnte nicht gespeichert werden.\n"
                    + e.getMessage(), "Weiterarbeiten", JOptionPane.WARNING_MESSAGE);
        }
    }
}
