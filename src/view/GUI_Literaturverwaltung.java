package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import model.data.DataManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.FuzzySearch;
import model.Literatur;
import model.Sortierer;
import model.Sortierer.Kriterium;
import model.Sortierer.Richtung;
import model.Sortierer.SortierPrioritaet;

/**
 * Displays the source table with search, sorting, filtering, and a details
 * editor.
 */
public class GUI_Literaturverwaltung extends JFrame {
	private static final String[] COLUMN_NAMES = { "Nr.", "Titel", "Quellentyp", "Autor", "Jahr" };
	private static final Kriterium[] SORT_CRITERIA = {
			Kriterium.TITEL, Kriterium.QUELLENTYP,
			Kriterium.AUTOR, Kriterium.JAHR
	};

	private final FuzzySearch fuzzySearch = new FuzzySearch();
	private JTextField searchField;
	private JButton searchButton;
	private javax.swing.Timer searchTimer;
	private final DataManager manager;
	private DefaultTableModel tableModel;
	private JTable sourceTable;
	private JComboBox<String> sourceTypeComboBox;
	private final LinkedHashMap<Integer, Richtung> sortDirectionsByColumn = new LinkedHashMap<>();
	private int nextSourceNumber = 1;
	private boolean updatingSourceTypes;
	private DetailsPanel detailsPanel;
	private JButton createReferenceButton;
	private final LinkedHashMap<Integer, Literatur> sourcesByNumber = new LinkedHashMap<>();
	private String currentTheme = "Hell";

	/**
	 * Creates the GUI_Literaturverwaltung view and its controls.
	 */
	public GUI_Literaturverwaltung() {
		this(new DataManager());
	}

	/**
	 * Creates the GUI_Literaturverwaltung view and its controls.
	 *
	 * @param manager data manager for the active bibliography
	 */
	public GUI_Literaturverwaltung(DataManager manager) {
		this.manager = Objects.requireNonNull(manager, "manager");
		setTitle("Literaturverwaltung - Literaturverzeichnis");
		setSize(1200, 750);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setJMenuBar(createMenuBar());

		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		JSplitPane contentSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				createListPanel(),
				createDetailsPanel());
		contentSplitPane.setResizeWeight(1);
		mainPanel.add(contentSplitPane, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			/**
			 * Checks unsaved edits before stopping the search timer and closing the window.
			 *
			 * @param event event that triggered the callback
			 */
			@Override
			public void windowClosing(WindowEvent event) {
				if (detailsPanel.confirmCloseIfEditing()) {
					searchTimer.stop();
					dispose();
				}
			}

		});

		setVisible(true);
		SwingUtilities.invokeLater(() -> contentSplitPane.setDividerLocation(0.65));
	}

	/**
	 * Builds the export and settings menus.
	 *
	 * @return the configured menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// create datei menu with menu item and action
		JMenu dateiMenu = new JMenu("Datei");
		JMenuItem export = new JMenuItem("Export");
		export.addActionListener(e -> {
			GUI_Verzeichnis_Exportieren dialog = new GUI_Verzeichnis_Exportieren(this, manager);
			dialog.setVisible(true);
		});
		dateiMenu.add(export);

		menuBar.add(dateiMenu);

		// create settings menu with menu item and action
		JMenu settingsMenu = new JMenu("Einstellungen");
		JMenuItem theme = new JMenuItem("Theme");
		theme.addActionListener(e -> {
			GUI_Einstellungen dialog = new GUI_Einstellungen(this, this::changeTheme, currentTheme);
			dialog.setVisible(true);
		});
		settingsMenu.add(theme);

		menuBar.add(settingsMenu);
		return menuBar;
	}

	/**
	 * Builds the source list, search controls, and creation button.
	 *
	 * @return the source list panel
	 */
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

	/**
	 * Builds a read-only table with selection and header-sorting listeners.
	 *
	 * @return the configured source table
	 */
	private JTable createSourceTable() {
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
			@Override
			/**
			 * Disables direct editing of table cells.
			 *
			 * @param row    zero-based table row
			 * @param column zero-based table column
			 * @return false for every cell
			 */
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(tableModel);
		int[] columnWidths = { 45, 250, 110, 160, 60 };
		for (int column = 0; column < columnWidths.length; column++) {
			table.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
		}

		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));

		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			/**
			 * Updates sorting after a single left click on a sortable table header.
			 *
			 * @param event event that triggered the callback
			 */
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

	/**
	 * Deletes the selected source after asking the user for confirmation.
	 * The source is removed from the JSON file using its unique ID
	 * and from the GUI table. If canceled, no changes are made.
	 *
	 * @param source the source to be deleted
	 */
	private void deleteSelectedSource(Literatur source) {
		if (source == null) {
			return;
		}
		int confirmation = JOptionPane.showConfirmDialog(this,
				"Möchten Sie diese Quelle wirklich löschen?\n\n" + source.getTitel().orElse("Ohne Titel"),
				"Quelle löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (confirmation != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			String id = manager.getId(source);
			if (id == null || !manager.delete_data_by_id(id)) {
				JOptionPane.showMessageDialog(this, "Die Quelle konnte nicht gefunden werden.", "Fehler beim Löschen",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			sourcesByNumber.values().removeIf(entry -> entry == source);
			updateSourceTypes();
			refreshTable();
			detailsPanel.showSource(null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Die Quelle konnte nicht gelöscht werden:\n" + e.getMessage(),
					"Fehler beim Löschen", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Builds search and type filters with a 150 ms search debounce.
	 *
	 * @return the search controls
	 */
	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel(new BorderLayout());
		JPanel searchControlsPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchTimer = new javax.swing.Timer(150, event -> {
			if (searchField.isEnabled()) {
				refreshTable();
			}
		});
		searchTimer.setRepeats(false);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			/**
			 * Restarts the search debounce timer after text insertion.
			 *
			 * @param event event that triggered the callback
			 */
			public void insertUpdate(DocumentEvent event) {
				searchTimer.restart();
			}

			@Override
			/**
			 * Restarts the search debounce timer after text removal.
			 *
			 * @param event event that triggered the callback
			 */
			public void removeUpdate(DocumentEvent event) {
				searchTimer.restart();
			}

			@Override
			/**
			 * Restarts the search debounce timer after document attribute changes.
			 *
			 * @param event event that triggered the callback
			 */
			public void changedUpdate(DocumentEvent event) {
				searchTimer.restart();
			}
		});
		searchField.getAccessibleContext().setAccessibleName("Quellen durchsuchen");
		searchControlsPanel.add(searchField, BorderLayout.CENTER);
		searchButton = new JButton("Suchen");
		java.awt.event.ActionListener searchAction = event -> {
			searchTimer.stop();
			if (searchField.isEnabled()) {
				refreshTable();
			}
		};
		searchButton.addActionListener(searchAction);
		searchField.addActionListener(searchAction);
		searchButton.setIcon(new ImageIcon("src/view/assets/magnifier.png"));
		searchControlsPanel.add(searchButton, BorderLayout.EAST);
		searchPanel.add(searchControlsPanel, BorderLayout.CENTER);

		sourceTypeComboBox = new JComboBox<>(new String[] { "Alle Quellentypen" });
		sourceTypeComboBox.getAccessibleContext().setAccessibleName("Quellentyp");
		sourceTypeComboBox.addActionListener(event -> {
			if (!updatingSourceTypes)
				refreshTable();
		});
		searchPanel.add(sourceTypeComboBox, BorderLayout.EAST);
		return searchPanel;
	}

	/**
	 * Connects the details editor to table updates and editing-state controls.
	 *
	 * @return the details panel
	 */
	private JPanel createDetailsPanel() {
		detailsPanel = new DetailsPanel(
				source -> updateTableRow(sourceTable.getSelectedRow(), source),
				this::addCreatedSource,
				editing -> {
					searchField.setEnabled(!editing);
					searchButton.setEnabled(!editing);
					if (editing) {
						searchTimer.stop();
					} else {
						searchTimer.restart();
					}
					createReferenceButton.setEnabled(!editing);
					sourceTypeComboBox.setEnabled(!editing);
					sourceTable.setEnabled(!editing);
					sourceTable.getTableHeader().setEnabled(!editing);
				},
				this::deleteSelectedSource);
		return detailsPanel;
	}

	/**
	 * Adds and selects a new source in the table, then attempts to persist it.
	 *
	 * @param source bibliographic source
	 */
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

		// save the new Literatur
		try {
			manager.add_data(source);
		} catch (Exception e) {
			new JOptionPane("" + e);
		}
	}

	/**
	 * Displays the selected table source or clears details when nothing is
	 * selected.
	 */
	private void showSelectedSource() {
		int selectedRow = sourceTable.getSelectedRow();
		Literatur source = selectedRow < 0 ? null
				: sourcesByNumber.get((Integer) tableModel.getValueAt(selectedRow, 0));
		detailsPanel.showSource(source);
	}

	/**
	 * Toggles a column direction, optionally clearing other sort priorities.
	 *
	 * @param column         zero-based table column
	 * @param onlyThisColumn whether to clear other sort priorities
	 */
	private void updateSortPriority(int column, boolean onlyThisColumn) {
		Richtung direction = sortDirectionsByColumn.get(column) == Richtung.AUFSTEIGEND
				? Richtung.ABSTEIGEND
				: Richtung.AUFSTEIGEND;
		if (onlyThisColumn) {
			sortDirectionsByColumn.clear();
		}
		sortDirectionsByColumn.put(column, direction);
	}

	/**
	 * Sorts visible rows while preserving stable source numbers and selection.
	 */
	private void sortTableRows() {
		if (sortDirectionsByColumn.isEmpty()) {
			return;
		}
		List<SortierPrioritaet> priorities = new ArrayList<>();
		for (var entry : sortDirectionsByColumn.entrySet()) {
			// Numbers are unique, so criteria after Nr. cannot change the order.
			if (entry.getKey() == 0)
				break;
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
					? Integer.compare(a, b)
					: Integer.compare(b, a));
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

	/**
	 * Displays sort direction and priority in the table headers.
	 */
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

	/**
	 * Builds the number, title, type, author, and year cells for a source.
	 *
	 * @param source bibliographic source
	 * @param number stable source number
	 * @return the table row values
	 */
	private Object[] createTableRow(Literatur source, int number) {
		Objects.requireNonNull(source, "source");
		List<String> authors = source.getAutor();
		String authorText = String.join(", ", authors.stream()
				.filter(author -> author != null && !author.isBlank()).toList());
		Integer publicationYear = source.getVeroeffentlichungsdatum()
				.map(LocalDate::getYear).orElse(null);
		return new Object[] {
				number, source.getTitel().orElse(""), source.getClass().getSimpleName(), authorText,
				publicationYear
		};
	}

	/**
	 * Rebuilds visible rows from the search and type filters, then restores sorting
	 * and selection.
	 */
	private void refreshTable() {
		int selectedRow = sourceTable.getSelectedRow();
		Integer selectedNumber = selectedRow < 0 ? null
				: (Integer) tableModel.getValueAt(selectedRow, 0);
		String filter = (String) sourceTypeComboBox.getSelectedItem();
		String search = searchField.getText();
		List<Object[]> visibleRows = new ArrayList<>();
		for (var entry : sourcesByNumber.entrySet()) {
			Literatur source = entry.getValue();
			if ((filter == null || filter.equals("Alle Quellentypen")
					|| filter.equals(source.getClass().getSimpleName()))
					&& fuzzySearch.suitsSearch(source, search)) {
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

	/**
	 * Refreshes available type filters while preserving a still-valid selection.
	 */
	private void updateSourceTypes() {
		String selectedType = (String) sourceTypeComboBox.getSelectedItem();
		List<String> types = new ArrayList<>();
		for (Literatur source : sourcesByNumber.values()) {
			String type = source.getClass().getSimpleName();
			if (!types.contains(type))
				types.add(type);
		}
		updatingSourceTypes = true;
		try {
			sourceTypeComboBox.removeAllItems();
			sourceTypeComboBox.addItem("Alle Quellentypen");
			for (String type : types)
				sourceTypeComboBox.addItem(type);
			if (selectedType != null && types.contains(selectedType)) {
				sourceTypeComboBox.setSelectedItem(selectedType);
			}
		} finally {
			updatingSourceTypes = false;
		}
	}

	/**
	 * Adds a source to the in-memory table model and refreshes filters.
	 *
	 * @param source bibliographic source
	 */
	public void addTableRow(Literatur source) {
		Objects.requireNonNull(source, "source");
		sourcesByNumber.put(nextSourceNumber, source);
		nextSourceNumber++;
		updateSourceTypes();
		refreshTable();
	}

	/**
	 * Adds sources to the in-memory table model and refreshes filters once.
	 *
	 * @param sources sources to display
	 */
	public void addTableRows(Literatur[] sources) {
		Objects.requireNonNull(sources, "sources");
		for (Literatur source : sources)
			Objects.requireNonNull(source, "source");
		for (Literatur source : sources) {
			sourcesByNumber.put(nextSourceNumber, source);
			nextSourceNumber++;
		}
		updateSourceTypes();
		refreshTable();
	}

	/**
	 * Removes a visible row from the in-memory source collection and refreshes the
	 * table.
	 *
	 * @param rowIndex zero-based visible row index
	 */
	public void removeTableRow(int rowIndex) {
		sourcesByNumber.remove((Integer) tableModel.getValueAt(rowIndex, 0));
		updateSourceTypes();
		refreshTable();
	}

	/**
	 * Replaces the source at a visible row, retaining its source number.
	 *
	 * @param rowIndex zero-based visible row index
	 * @param source   bibliographic source
	 */
	public void updateTableRow(int rowIndex, Literatur source) {
		Objects.requireNonNull(source, "source");
		int number = (Integer) tableModel.getValueAt(rowIndex, 0);
		sourcesByNumber.put(number, source);
		updateSourceTypes();
		refreshTable();
		showSelectedSource();
	}

	/**
	 * Clears the in-memory sources and restarts source numbering.
	 */
	public void resetTable() {
		sourcesByNumber.clear();
		nextSourceNumber = 1;
		updateSourceTypes();
		refreshTable();
	}

	/**
	 * Updates the colors of a component and all its child components.
	 */
	private void applyTheme(Component component, Color background,
			Color foreground, Color inputBackground) {

		component.setForeground(foreground);

		if (component instanceof JTextField || component instanceof JTextArea || component instanceof JComboBox<?>) {
			component.setBackground(inputBackground);
		} else {
			component.setBackground(background);
		}
		if (component instanceof JTable table) {
			table.setGridColor(foreground);
			table.getTableHeader().setBackground(background);
			table.getTableHeader().setForeground(foreground);
		}
		if (component instanceof Container container) {
			for (Component child : container.getComponents()) {
				applyTheme(child, background, foreground, inputBackground);
			}
		}
	}

	/**
	 * Applies the selected theme to the existing interface.
	 *
	 * @param theme selected theme ("Hell" or "Dunkel")
	 */
	private void changeTheme(String theme) {
		currentTheme = theme;
		boolean dark = "Dunkel".equals(theme);
		Color background = dark ? new Color(0x2B2B2B) : Color.WHITE;
		Color foreground = dark ? new Color(0xF0F0F0) : Color.BLACK;
		Color inputBackground = dark ? new Color(0x424242) : Color.WHITE;

		applyTheme(getContentPane(), background, foreground, inputBackground);
		applyTheme(getJMenuBar(), background, foreground, inputBackground);

		repaint();
	}
}
