package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import model.Buchbeleg;
import model.Website;
import model.data.data_manager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Literatur;
import model.Sortierer;
import model.Sortierer.Kriterium;
import model.Sortierer.Richtung;
import model.Sortierer.SortierPrioritaet;

public class GUI_Literaturverwaltung extends JFrame {
    private static final String[] COLUMN_NAMES = {
        "Nr.", "Titel", "Quellentyp", "Autor", "Jahr"
    };
    private static final Kriterium[] SORT_CRITERIA = {
        Kriterium.TITEL, Kriterium.QUELLENTYP,
        Kriterium.AUTOR, Kriterium.JAHR
    };

    private final data_manager manager;
    private DefaultTableModel tableModel;
    private JTable sourceTable;
    private JComboBox<String> sourceTypeComboBox;
    private final LinkedHashMap<Integer, Richtung> sortDirectionsByColumn = new LinkedHashMap<>();
    private int nextSourceNumber = 1;
    private boolean updatingSourceTypes;
    private DetailsPanel detailsPanel;
    private JButton createReferenceButton;
    private final LinkedHashMap<Integer, Literatur> sourcesByNumber = new LinkedHashMap<>();

    public GUI_Literaturverwaltung() {
        this(new data_manager());
    }

    public GUI_Literaturverwaltung(data_manager manager) {
        this.manager = Objects.requireNonNull(manager, "manager");
        setTitle("Literaturverwaltung - Literaturverzeichnis");
        setSize(600, 600);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        JSplitPane contentSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createListPanel(),
                createDetailsPanel());
        contentSplitPane.setResizeWeight(1);
        mainPanel.add(contentSplitPane, BorderLayout.CENTER);

        // TEST: remove these examples after reviewing table selection.
        addTableRows(new Literatur[] {
            new Buchbeleg(List.of("Kafka, Franz"), Optional.of(LocalDate.of(1915, 1, 1)),
                    Optional.of("Die Verwandlung"), Optional.of("Kurt Wolff Verlag"),
                    Optional.of(1), Optional.of("Leipzig"), Optional.of(1), Optional.of(72)),
            new Buchbeleg(List.of("Goethe, Johann Wolfgang von"), Optional.of(LocalDate.of(1808, 1, 1)),
                    Optional.of("Faust"), Optional.of("Cotta"), Optional.of(1),
                    Optional.of("Tübingen"), Optional.of(1), Optional.of(200)),
            new Website(List.of("Beispielautor"), "Beispielwebsite", Optional.of("Digitale Medien"),
                    "https://example.org", LocalDate.of(2026, 11, 17),
                    LocalDate.of(2024, 6, 1), "Artikel")
        });
        setVisible(true);
        SwingUtilities.invokeLater(() -> contentSplitPane.setDividerLocation(0.65));
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("Datei"));
        menuBar.add(new JMenu("Einstellungen"));
        return menuBar;
    }

    private JPanel createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(createSearchPanel(), BorderLayout.NORTH);
        sourceTable = createSourceTable();
        listPanel.add(new JScrollPane(sourceTable), BorderLayout.CENTER);
        createReferenceButton = new JButton("+ Neuen Verweis erstellen +");
        createReferenceButton.addActionListener(event -> detailsPanel.beginCreation());
        listPanel.add(createReferenceButton, BorderLayout.SOUTH);
        return listPanel;
    }

    private JTable createSourceTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        int[] columnWidths = {45, 250, 110, 160, 60};
        for (int column = 0; column < columnWidths.length; column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
        }

        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
        
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int column = table.columnAtPoint(event.getPoint());
                if (table.isEnabled() && SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1
                        && column >= 0 && table.getTableHeader().getResizingColumn() == null) {
                    updateSortPriority(column, event.isControlDown());
                    sortTableRows();
                    updateSortIndicators();
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                showSelectedSource();
            }
        });
        return table;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel searchControlsPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.getAccessibleContext().setAccessibleName("Quellen durchsuchen");
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        JButton searchButton = new JButton("Suchen");
        searchButton.setIcon(new ImageIcon("src/view/assets/magnifier.png"));
        searchControlsPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchControlsPanel, BorderLayout.CENTER);

        sourceTypeComboBox = new JComboBox<>(new String[] {"Alle Quellentypen"});
        sourceTypeComboBox.getAccessibleContext().setAccessibleName("Quellentyp");
        sourceTypeComboBox.addActionListener(event -> {
            if (!updatingSourceTypes) refreshTable();
        });
        searchPanel.add(sourceTypeComboBox, BorderLayout.EAST);
        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        detailsPanel = new DetailsPanel(
                source -> updateTableRow(sourceTable.getSelectedRow(), source),
                this::addCreatedSource,
                editing -> {
                    createReferenceButton.setEnabled(!editing);
                    sourceTypeComboBox.setEnabled(!editing);
                    sourceTable.setEnabled(!editing);
                    sourceTable.getTableHeader().setEnabled(!editing);
                });
        return detailsPanel;
    }

    private void addCreatedSource(Literatur source) {
        int number = nextSourceNumber;
        addTableRow(source);
        String filter = (String) sourceTypeComboBox.getSelectedItem();
        String type = source.getClass().getSimpleName();
        if (!"Alle Quellentypen".equals(filter) && !type.equals(filter)) {
            sourceTypeComboBox.setSelectedItem(type);
        }
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (tableModel.getValueAt(row, 0).equals(number)) {
                sourceTable.setRowSelectionInterval(row, row);
                break;
            }
        }
    }

    private void showSelectedSource() {
        int selectedRow = sourceTable.getSelectedRow();
        Literatur source = selectedRow < 0 ? null
                : sourcesByNumber.get((Integer) tableModel.getValueAt(selectedRow, 0));
        detailsPanel.showSource(source);
    }

    private void updateSortPriority(int column, boolean onlyThisColumn) {
        Richtung direction = sortDirectionsByColumn.get(column) == Richtung.AUFSTEIGEND
                ? Richtung.ABSTEIGEND : Richtung.AUFSTEIGEND;
        if (onlyThisColumn) {
            sortDirectionsByColumn.clear();
        }
        sortDirectionsByColumn.put(column, direction);
    }

    private void sortTableRows() {
        if (sortDirectionsByColumn.isEmpty()) {
            return;
        }
        List<SortierPrioritaet> priorities = new ArrayList<>();
        for (var entry : sortDirectionsByColumn.entrySet()) {
            // Numbers are unique, so criteria after Nr. cannot change the order.
            if (entry.getKey() == 0) break;
            priorities.add(new SortierPrioritaet(SORT_CRITERIA[entry.getKey() - 1], entry.getValue()));
        }
        int selectedRow = sourceTable.getSelectedRow();
        Integer selectedNumber = selectedRow < 0 ? null
                : (Integer) tableModel.getValueAt(selectedRow, 0);
        List<Integer> rowNumbers = new ArrayList<>();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            rowNumbers.add((Integer) tableModel.getValueAt(row, 0));
        }
        Richtung numberDirection = sortDirectionsByColumn.get(0);
        if (numberDirection != null) {
            rowNumbers.sort((a, b) -> numberDirection == Richtung.AUFSTEIGEND
                    ? Integer.compare(a, b) : Integer.compare(b, a));
        }
        List<Literatur> sources = new ArrayList<>();
        IdentityHashMap<Literatur, ArrayDeque<Integer>> numbersBySource = new IdentityHashMap<>();
        for (int number : rowNumbers) {
            Literatur source = sourcesByNumber.get(number);
            sources.add(source);
            numbersBySource.computeIfAbsent(source, key -> new ArrayDeque<>()).addLast(number);
        }
        List<Literatur> sortedSources = new Sortierer().sortiere(sources, priorities);
        tableModel.setRowCount(0);
        for (Literatur source : sortedSources) {
            int number = numbersBySource.get(source).removeFirst();
            tableModel.addRow(createTableRow(source, number));
        }
        if (selectedNumber != null) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (selectedNumber.equals(tableModel.getValueAt(row, 0))) {
                    sourceTable.setRowSelectionInterval(row, row);
                    break;
                }
            }
        }
    }

    private void updateSortIndicators() {
        for (int column = 0; column < tableModel.getColumnCount(); column++) {
            sourceTable.getColumnModel().getColumn(column).setHeaderValue(tableModel.getColumnName(column));
        }
        int priority = 1;
        for (var entry : sortDirectionsByColumn.entrySet()) {
            int column = entry.getKey();
            String triangle = entry.getValue() == Richtung.AUFSTEIGEND ? " ▲" : " ▼";
            String label = tableModel.getColumnName(column) + triangle + priority;
            sourceTable.getColumnModel().getColumn(column).setHeaderValue(label);
            priority++;
        }
        sourceTable.getTableHeader().repaint();
    }

    private Object[] createTableRow(Literatur source, int number) {
        Objects.requireNonNull(source, "source");
        List<String> authors = source.getAutor().orElse(List.of());
        String authorText = String.join(", ", authors.stream()
                .filter(author -> author != null && !author.isBlank()).toList());
        Integer publicationYear = source.getVeroeffentlichungsdatum()
                .map(LocalDate::getYear).orElse(null);
        return new Object[] {
            number, source.getTitel().orElse(""), source.getClass().getSimpleName(), authorText,
            publicationYear
        };
    }

    private void refreshTable() {
        int selectedRow = sourceTable.getSelectedRow();
        Integer selectedNumber = selectedRow < 0 ? null
                : (Integer) tableModel.getValueAt(selectedRow, 0);
        String filter = (String) sourceTypeComboBox.getSelectedItem();
        List<Object[]> visibleRows = new ArrayList<>();
        for (var entry : sourcesByNumber.entrySet()) {
            Literatur source = entry.getValue();
            if (filter == null || filter.equals("Alle Quellentypen")
                    || filter.equals(source.getClass().getSimpleName())) {
                visibleRows.add(createTableRow(source, entry.getKey()));
            }
        }
        tableModel.setRowCount(0);
        for (Object[] row : visibleRows) {
            tableModel.addRow(row);
        }
        sortTableRows();
        if (selectedNumber != null) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (selectedNumber.equals(tableModel.getValueAt(row, 0))) {
                    sourceTable.setRowSelectionInterval(row, row);
                    break;
                }
            }
        }
    }

    private void updateSourceTypes() {
        String selectedType = (String) sourceTypeComboBox.getSelectedItem();
        List<String> types = new ArrayList<>();
        for (Literatur source : sourcesByNumber.values()) {
            String type = source.getClass().getSimpleName();
            if (!types.contains(type)) types.add(type);
        }
        updatingSourceTypes = true;
        try {
            sourceTypeComboBox.removeAllItems();
            sourceTypeComboBox.addItem("Alle Quellentypen");
            for (String type : types) sourceTypeComboBox.addItem(type);
            if (selectedType != null && types.contains(selectedType)) {
                sourceTypeComboBox.setSelectedItem(selectedType);
            }
        } finally {
            updatingSourceTypes = false;
        }
    }

    public void addTableRow(Literatur source) {
        Objects.requireNonNull(source, "source");
        sourcesByNumber.put(nextSourceNumber, source);
        nextSourceNumber++;
        updateSourceTypes();
        refreshTable();
    }

    public void addTableRows(Literatur[] sources) {
        Objects.requireNonNull(sources, "sources");
        for (Literatur source : sources) Objects.requireNonNull(source, "source");
        for (Literatur source : sources) {
            sourcesByNumber.put(nextSourceNumber, source);
            nextSourceNumber++;
        }
        updateSourceTypes();
        refreshTable();
    }

    public void removeTableRow(int rowIndex) {
        sourcesByNumber.remove((Integer) tableModel.getValueAt(rowIndex, 0));
        updateSourceTypes();
        refreshTable();
    }

    public void updateTableRow(int rowIndex, Literatur source) {
        Objects.requireNonNull(source, "source");
        int number = (Integer) tableModel.getValueAt(rowIndex, 0);
        sourcesByNumber.put(number, source);
        updateSourceTypes();
        refreshTable();
        showSelectedSource();
    }

    public void resetTable() {
        sourcesByNumber.clear();
        nextSourceNumber = 1;
        updateSourceTypes();
        refreshTable();
    }
}
