import view.GUI;
import view.GUI_Literaturverwaltung;
import view.GUI_Neues_Verzeichnis;
import view.GUI_Verzeichnis_Importieren;

import javax.swing.SwingUtilities;

public class Literaturverzeichnis {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
