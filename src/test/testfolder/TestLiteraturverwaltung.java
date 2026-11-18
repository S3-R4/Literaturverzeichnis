package testfolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import model.Buchbeleg;
import model.Literatur;
import model.Website;
import org.junit.jupiter.api.Test;
import view.GUI_Literaturverwaltung;

public class TestLiteraturverwaltung {
    @Test
    public void literaturRowsCategoriesAndSorting() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Swing window test requires a display");
        SwingUtilities.invokeAndWait(() -> {
            GUI_Literaturverwaltung window = new GUI_Literaturverwaltung() {
                @Override
                public void setVisible(boolean visible) { }
            };
            try {
                JTable table = findComponent(window.getContentPane(), JTable.class);
                JComboBox<?> categories = findComponent(window.getContentPane(), JComboBox.class);
                assertNotNull(table);
                assertNotNull(categories);
                window.resetTable();
                table.setSize(800, 200);
                table.doLayout();
                table.getTableHeader().setSize(800, 24);
                assertEquals(0, table.getRowCount());
                assertEquals(List.of("Alle Quellentypen"), items(categories));

                Literatur older = website("Zebra", "Mueller", 900);
                Literatur newer = website("Apfel", "mueller", 2000);
                Literatur book = new Buchbeleg(List.of("Schmidt"),
                        Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Buch"),
                        Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty());
                window.addTableRows(new Literatur[] {older, newer, book});
                assertEquals(3, table.getRowCount());
                assertEquals("Website", table.getValueAt(0, 2));
                assertEquals("Buchbeleg", table.getValueAt(2, 2));
                assertEquals(List.of("Alle Quellentypen", "Website", "Buchbeleg"), items(categories));
                categories.setSelectedItem("Website");
                assertEquals(2, table.getRowCount());

                assertThrows(NullPointerException.class, () -> window.updateTableRow(0, null));
                assertEquals("Zebra", table.getValueAt(0, 1));
                assertThrows(NullPointerException.class,
                        () -> window.addTableRows(new Literatur[] {older, null}));
                assertEquals(2, table.getRowCount());

                table.setRowSelectionInterval(0, 0);
                click(table, 3, false);
                click(table, 4, false);
                click(table, 4, false);
                assertEquals(2, table.getValueAt(0, 0));
                assertEquals(1, table.getValueAt(table.getSelectedRow(), 0));
                assertEquals("Autor ▲1", table.getColumnModel().getColumn(3).getHeaderValue());
                assertEquals("Jahr ▼2", table.getColumnModel().getColumn(4).getHeaderValue());
                click(table, 0, true);
                assertEquals(1, table.getValueAt(0, 0));
                assertEquals("Autor", table.getColumnModel().getColumn(3).getHeaderValue());
                click(table, 0, false);
                assertEquals(2, table.getValueAt(0, 0));
                int selectedRow = table.getSelectedRow();
                window.updateTableRow(selectedRow, website("Updated", "A", 2025));
                assertEquals(1, table.getValueAt(table.getSelectedRow(), 0));
                window.addTableRow(website("Added", "A", 2024));
                assertEquals(4, table.getValueAt(0, 0));
                categories.setSelectedItem("Website");
                assertEquals("Website", categories.getSelectedItem());
                window.removeTableRow(0);
                assertEquals(2, table.getRowCount());
                categories.setSelectedItem("Alle Quellentypen");
                assertEquals(3, table.getRowCount());
                assertEquals("Buchbeleg", table.getValueAt(0, 2));
                categories.setSelectedItem("Website");
                window.addTableRow(website("Another", "A", 2024));
                assertEquals(5, table.getValueAt(0, 0));

                window.resetTable();
                assertEquals(0, table.getRowCount());
                assertEquals(List.of("Alle Quellentypen"), items(categories));
                window.addTableRow(book);
                assertEquals(1, table.getValueAt(0, 0));
                assertEquals(List.of("Alle Quellentypen", "Buchbeleg"), items(categories));
                window.addTableRow(older);
                categories.setSelectedItem("Website");
                window.removeTableRow(0);
                assertEquals(List.of("Alle Quellentypen", "Buchbeleg"), items(categories));
                assertEquals("Alle Quellentypen", categories.getSelectedItem());

                window.resetTable();
                categories.setSelectedItem(null);
                window.addTableRow(website(null, null, null));
                assertEquals("", table.getValueAt(0, 1));
                assertEquals("", table.getValueAt(0, 3));
                assertNull(table.getValueAt(0, 4));
                assertEquals("Website", table.getValueAt(0, 2));
                int authorWidth = table.getColumnModel().getColumn(3).getWidth();
                Website manyAuthors = website("Long authors", "LongAuthor".repeat(100), 2020);
                List<String> authors = manyAuthors.getAutor();
                authors.add(null);
                authors.add("");
                authors.add("Second author");
                window.addTableRow(manyAuthors);
                table.doLayout();
                assertEquals("LongAuthor".repeat(100) + ", Second author", table.getValueAt(0, 3));
                assertEquals(authorWidth, table.getColumnModel().getColumn(3).getWidth());
                assertEquals(160, table.getColumnModel().getColumn(3).getPreferredWidth());
                assertTrue(table.getTableHeader().getResizingAllowed());
                assertFalse(table.getTableHeader().getReorderingAllowed());
            } finally {
                window.dispose();
            }
        });
    }

    private Website website(String title, String author, Integer year) {
        return new Website(author == null ? List.of() : List.of(author), "",
                Optional.ofNullable(title), "", null,
                year == null ? null : LocalDate.of(year, 1, 1), "");
    }

    private List<Object> items(JComboBox<?> comboBox) {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            items.add(comboBox.getItemAt(i));
        }
        return items;
    }

    private <T extends Component> T findComponent(Container parent, Class<T> type) {
        for (Component component : parent.getComponents()) {
            if (type.isInstance(component)) {
                return type.cast(component);
            }
            if (component instanceof Container container) {
                T found = findComponent(container, type);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void click(JTable table, int column, boolean control) {
        var header = table.getTableHeader();
        var bounds = header.getHeaderRect(column);
        header.dispatchEvent(new MouseEvent(header, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), control ? InputEvent.CTRL_DOWN_MASK : 0,
                bounds.x + bounds.width / 2, 10, 1, false, MouseEvent.BUTTON1));
    }
}
