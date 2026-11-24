package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.function.Consumer;
import java.util.*;
import java.util.List;
import java.time.LocalDate;
import model.*;
import model.Audiovisuell.*;
import javax.swing.*;

import controller.Controller;
import java.util.regex.Pattern;


/**
 * Displays source details and manages creation and editing through callbacks.
 */
final class DetailsPanel extends JPanel {
	private final JPanel attributesPanel = new JPanel();
	private final JScrollPane attributesScrollPane = new JScrollPane(attributesPanel);
	private final JTextArea citationPreview = new JTextArea(3, 20);
	private final JLabel sourceTitle = new JLabel();
	private final JLabel sourceType = new JLabel();
	private final JComboBox<String> citationType = new JComboBox<>(new String[] { "Kurzbeleg", "Langbeleg" });
	private final JButton copyCitationButton = new JButton("Kopieren");

	private final Consumer<Literatur> saveChanges;
	private final Consumer<Literatur> addSource;
	private final JPanel sourceTypePanel = new JPanel(new BorderLayout());
	private final JComboBox<String> creationTypeComboBox = new JComboBox<>(new String[] {
			"Quellentyp wählen…", "Buchbeleg", "Website", "BildQuelle", "Zeitungsartikel",
			"FIlmsVideos", "Youtube", "PodcastGesamtwerk", "Podcastfolgen",
			"SocialMediaBeitraege", "Sammelbandbeleg"
	});
	private final LinkedHashMap<String, String> creationDraft = new LinkedHashMap<>();
	private boolean creating;
	private final Consumer<Boolean> editingChanged;
	private final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
	private final JLabel inputError = new JLabel();
	private final JButton deleteButton = new JButton("Löschen");
	private final JButton editButton = new JButton("Bearbeiten");
	private final LinkedHashMap<String, JTextField> editFields = new LinkedHashMap<>();
	private Map<String, String> shownAttributes = Map.of();
	private Literatur currentSource;
	private boolean editing;
	private Controller controller = new Controller();
	private JLabel activeToast;
	private javax.swing.Timer activeToastTimer;

	/**
	 * Creates the DetailsPanel view and its controls.
	 *
	 * @param saveChanges    callback receiving an edited source
	 * @param addSource      callback receiving a new source
	 * @param editingChanged callback notified when editing mode changes
	 */
	DetailsPanel(Consumer<Literatur> saveChanges, Consumer<Literatur> addSource,
			Consumer<Boolean> editingChanged, Consumer<Literatur> deleteSource) {
		super(new BorderLayout(0, 16));
		this.saveChanges = saveChanges;
		this.addSource = addSource;
		this.editingChanged = editingChanged;
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		setBackground(new Color(0xE6E6E6));
		setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
		setMinimumSize(new Dimension(250, 0));

		JPanel headingPanel = new JPanel(new BorderLayout(0, 7));
		headingPanel.setOpaque(false);
		JLabel heading = new JLabel("QUELLENDETAILS");
		heading.setFont(getFont().deriveFont(Font.BOLD, 13f));
		headingPanel.add(heading, BorderLayout.NORTH);
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0xA8A8A8));
		separator.setBackground(getBackground());
		headingPanel.add(separator, BorderLayout.SOUTH);
		JPanel sourceHeading = new JPanel(new GridLayout(0, 1, 0, 7));
		sourceHeading.setOpaque(false);
		sourceTitle.setFont(getFont().deriveFont(Font.BOLD, 13f));
		sourceType.setFont(getFont());
		sourceHeading.add(sourceTitle);
		sourceTypePanel.setOpaque(false);
		sourceTypePanel.add(sourceType, BorderLayout.CENTER);
		sourceHeading.add(sourceTypePanel);
		creationTypeComboBox.getAccessibleContext().setAccessibleName("Quellentyp für neuen Verweis");
		creationTypeComboBox.addActionListener(event -> {
			if (creating)
				showCreationFields();
		});
		JPanel header = new JPanel(new BorderLayout(0, 14));
		header.setOpaque(false);
		header.add(headingPanel, BorderLayout.NORTH);
		header.add(sourceHeading, BorderLayout.CENTER);
		add(header, BorderLayout.NORTH);

		attributesPanel.setLayout(new BoxLayout(attributesPanel, BoxLayout.Y_AXIS));
		attributesPanel.setBackground(getBackground());
		attributesScrollPane.setBorder(BorderFactory.createEmptyBorder());
		attributesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
		attributesScrollPane.getViewport().setBackground(getBackground());
		add(attributesScrollPane, BorderLayout.CENTER);

		citationPreview.setFont(getFont());
		citationPreview.setEditable(false);
		citationPreview.setLineWrap(true);
		citationPreview.setWrapStyleWord(true);
		citationPreview.setMargin(new Insets(8, 8, 8, 8));
		citationPreview.setBackground(new Color(0xF7F8FA));
		citationPreview.getAccessibleContext().setAccessibleName("Belegvorschau");
		JScrollPane previewScrollPane = new JScrollPane(citationPreview);
		previewScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xBFC1C4)));
		JPanel citationPanel = new JPanel(new BorderLayout(0, 7));
		citationPanel.setOpaque(false);
		citationType.getAccessibleContext().setAccessibleName("Belegart");
		citationType.addActionListener(event -> updateCitationPreview());
		actionPanel.setOpaque(false);
		inputError.setForeground(new Color(0xA00000));
		inputError.setFont(getFont().deriveFont(11f));
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(event -> deleteSource.accept(currentSource));
		editButton.setEnabled(false);
		editButton.addActionListener(event -> beginEditing());
		JPanel controls = new JPanel(new BorderLayout(0, 5));
		controls.setOpaque(false);
		controls.add(actionPanel, BorderLayout.NORTH);
		controls.add(inputError, BorderLayout.CENTER);
		JPanel citationHeader = new JPanel(new BorderLayout(8, 0));
		copyCitationButton.addActionListener(event -> {
			controller.copyToClipboard(citationPreview.getText());
			showNotificationToast(copyCitationButton, "Wurde in Zwischenablage kopiert!");
		});

		citationHeader.setOpaque(false);
		citationHeader.add(citationType, BorderLayout.WEST);
		citationHeader.add(copyCitationButton, BorderLayout.EAST);
		controls.add(citationHeader, BorderLayout.SOUTH);
		citationPanel.add(controls, BorderLayout.NORTH);
		updateActions();
		citationPanel.add(previewScrollPane, BorderLayout.CENTER);
		add(citationPanel, BorderLayout.SOUTH);
		showDetails("", "", Map.of());
	}

	/**
	 * Displays a short citation unless editing is active; null clears the details.
	 *
	 * @param source bibliographic source
	 */
	void showSource(Literatur source) {
		if (editing) {
			return;
		}
		currentSource = source;
		deleteButton.setEnabled(source != null);
		editButton.setEnabled(source instanceof Buchbeleg || source instanceof Website
				|| source instanceof BildQuelle || source instanceof Zeitungsartikel || source instanceof Audiovisuell);
		if (source == null) {
			showDetails("", "", Map.of());
			return;
		}
		LinkedHashMap<String, String> attributes = readAttributes(source);
		showDetails(source.getTitel().orElse(""), source.getClass().getSimpleName(), attributes);
	}

	private void updateCitationPreview() {
		boolean available = currentSource != null && !creating;
		citationType.setEnabled(available);
		copyCitationButton.setEnabled(false);
		String text = "";
		if (available) {
			try {
				text = citationType.getSelectedIndex() == 0
						? currentSource.generiereKurzbeleg()
						: currentSource.generiereLangbeleg();
				copyCitationButton.setEnabled(text != null && !text.isBlank());
			} catch (RuntimeException error) {
				text = citationType.getSelectedItem() + " konnte nicht erstellt werden: " + error.getMessage();
			}
		}
		citationPreview.setText(text);
		citationPreview.setCaretPosition(0);
	}

	/**
	 * Collects display labels and values supported for the source type.
	 *
	 * @param source bibliographic source
	 * @return the ordered display attributes
	 */
	private LinkedHashMap<String, String> readAttributes(Literatur source) {
		LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
		String authors = String.join(", ", source.getAutor().stream()
				.filter(author -> author != null && !author.isBlank()).toList());
		attributes.put("Autor / Herausgeber", authors);
		attributes.put("Titel", source.getTitel().orElse(""));
		attributes.put("Veröffentlichungsdatum", formatDate(source.getVeroeffentlichungsdatum()));
		attributes.put("Abrufdatum", formatDate(source.getAbrufdatum()));
		if (source instanceof Buchbeleg book) {
			attributes.put("Verlag", book.getBuchverlag());
			attributes.put("Auflage", book.getAuflage());
			attributes.put("Erscheinungsort", book.getErscheinungsort());
			attributes.put("Seitenangabe", book.getSeitenangabe());
		} else if (source instanceof Website website) {
			attributes.put("Websitename", website.getWebsitename());
			attributes.put("URL", website.getUrl());
			attributes.put("Textart", website.getTextart());
		} else if (source instanceof BildQuelle image) {
			attributes.put("Medienart", image.getMedienart());
			attributes.put("Herausgeber", image.getHerausgeber());
			attributes.put("Anbieter", image.getAnbieter().orElse(""));
		} else if (source instanceof Zeitungsartikel article) {
			attributes.put("Zeitung", article.getZeitung());
			attributes.put("Seitenzahl", article.getSeitenzahl());
			attributes.put("URL", article.getUrl());
		} else if (source instanceof Audiovisuell media) {
			attributes.put("Beitragsart", media.getBeitragsArt().orElse(""));
			attributes.put("Medium", media.getMedium().orElse(""));
			attributes.put("URL", media.getUrl().orElse(""));
			if (media instanceof FIlmsVideos film) {
				attributes.put("Produktionsfirma", film.getProduktionsfirma().orElse(""));
			} else if (media instanceof PodcastGesamtwerk podcast) {
				attributes.put("Herausgebende Organisation", podcast.getHerausgebendeOrganisation().orElse(""));
			} else if (media instanceof Podcastfolgen episode) {
				attributes.put("Herausgebende Organisation", episode.getHerausgebendeOrganisation().orElse(""));
				attributes.put("Folgennummer", episode.getPodcastFolge().map(String::valueOf).orElse(""));
				attributes.put("Folgentitel", episode.getPodcastFolgenName().orElse(""));
			} else if (media instanceof SocialMediaBeitraege post) {
				attributes.put("Text", post.getText().orElse(""));
				attributes.put("Beschreibung", post.getBeschreibung().orElse(""));
			}
		}
		return attributes;
	}

	/**
	 * Formats an optional date as ISO local-date text.
	 *
	 * @param date date
	 * @return the date text, or an empty string
	 */
	private String formatDate(Optional<LocalDate> date) {
		return date.map(LocalDate::toString).orElse("");
	}

	/**
	 * Rebuilds the attribute display and citation preview from the supplied values.
	 *
	 * @param title      source title
	 * @param type       source type name
	 * @param attributes ordered attribute labels and values
	 */
	void showDetails(String title, String type, Map<String, String> attributes) {
		shownAttributes = attributes;
		sourceTitle.setText(title);
		sourceType.setText(type);
		sourceTypePanel.removeAll();
		sourceTypePanel.add(creating ? creationTypeComboBox : sourceType, BorderLayout.CENTER);
		sourceTypePanel.revalidate();
		sourceTypePanel.repaint();
		attributesPanel.removeAll();
		editFields.clear();
		if (attributes.isEmpty()) {
			JLabel prompt = new JLabel(creating
					? "Bitte einen Quellentyp auswählen."
					: "Bitte einen Eintrag auswählen.");
			prompt.setFont(getFont());
			attributesPanel.add(prompt);
		}
		for (Map.Entry<String, String> attribute : attributes.entrySet()) {
			addAttribute(attribute.getKey(), attribute.getValue());
		}
		attributesPanel.add(Box.createVerticalGlue());
		updateCitationPreview();
		attributesPanel.revalidate();
		attributesPanel.repaint();
		attributesScrollPane.getViewport().setViewPosition(new Point(0, 0));
	}

	/**
	 * Adds an attribute row as a label or editable field for the current mode.
	 *
	 * @param name  name
	 * @param value attribute value
	 */
	private void addAttribute(String name, String value) {
		JPanel fieldPanel = new JPanel(new BorderLayout(0, 4));
		fieldPanel.setOpaque(false);
		fieldPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xCCCCCC)),
				BorderFactory.createEmptyBorder(0, 0, 7, 0)));
		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(getFont().deriveFont(11f));
		nameLabel.setForeground(Color.DARK_GRAY);
		String text = value == null || value.isBlank() ? "—" : value;
		String escapedText = text.replace("&", "&amp;")
				.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
		JLabel valueLabel = new JLabel("<html>" + escapedText + "</html>");
		valueLabel.setFont(getFont());
		valueLabel.setToolTipText(text);
		fieldPanel.add(nameLabel, BorderLayout.NORTH);
		if (editing) {
			JTextField input = new JTextField(value == null ? "" : value);
			input.setFont(getFont());
			input.getAccessibleContext().setAccessibleName(name);
			if (name.equals("Autor / Herausgeber")) {
				input.setToolTipText("Mehrere Autoren mit Semikolon trennen.");
			} else if (name.equals("Veröffentlichungsdatum") || name.equals("Abrufdatum")) {
				input.setToolTipText("JJJJ-MM-TT; leer lassen, wenn unbekannt.");
			}
			editFields.put(name, input);
			fieldPanel.add(input, BorderLayout.CENTER);
		} else {
			if ("URL".equals(name) && value != null && !value.isBlank()) {
				valueLabel.setForeground(Color.BLUE);
				valueLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				valueLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent event) {
						if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1) {
							openUrl(value);
						}
					}
				});
			}
			fieldPanel.add(valueLabel, BorderLayout.CENTER);
		}
		fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldPanel.getPreferredSize().height));
		attributesPanel.add(fieldPanel);
		attributesPanel.add(Box.createVerticalStrut(12));
	}

	private void openUrl(String url) {
		try {
			URI uri = new URI(url.trim());
			String scheme = uri.getScheme();
			if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) || uri.getHost() == null) {
				throw new IllegalArgumentException("Ungültige Webadresse.");
			}
			if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				throw new UnsupportedOperationException("Browser kann nicht geöffnet werden.");
			}
			Desktop.getDesktop().browse(uri);
		} catch (Exception error) {
			JOptionPane.showMessageDialog(this,
					"Die Webadresse konnte nicht geöffnet werden.\nBitte überprüfen Sie die Adresse.\n\n"
							+ error.getMessage(),
					"Fehler beim Öffnen", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Starts a new source draft unless an edit is already active.
	 */
	void beginCreation() {
		if (editing)
			return;
		creating = true;
		creationDraft.clear();
		setEditing(true);
		creationTypeComboBox.setSelectedIndex(0);
		showCreationFields();
	}

	/**
	 * Preserves draft values and rebuilds fields for the selected source type.
	 */
	private void showCreationFields() {
		for (Map.Entry<String, JTextField> entry : editFields.entrySet()) {
			creationDraft.put(entry.getKey(), entry.getValue().getText());
		}
		inputError.setText("");
		LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
		if (creationTypeComboBox.getSelectedIndex() > 0) {
			if ("Sammelbandbeleg".equals(creationTypeComboBox.getSelectedItem())) {
				// shortcut: enable this form once Sammelbandbeleg extends Literatur on this
				// branch.
				inputError.setText("Sammelbandbeleg ist hier noch kein Literatur-Typ.");
			} else {
				attributes = readAttributes(createBlankSource((String) creationTypeComboBox.getSelectedItem()));
				attributes.replaceAll((name, value) -> creationDraft.getOrDefault(name, ""));
			}
		}
		showDetails("Neuen Verweis erstellen", "", attributes);
		updateActions();
	}

	/**
	 * Creates an empty source for a supported source type.
	 *
	 * @param type source type name
	 * @return the new source
	 */
	private Literatur createBlankSource(String type) {
		return switch (type) {
			case "Buchbeleg" -> new Buchbeleg(List.of(), Optional.empty(), Optional.empty(),
					Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
			case "Website" -> new Website(List.of(), "", Optional.empty(), "", null, null, "");
			case "BildQuelle" -> new BildQuelle();
			case "Zeitungsartikel" -> new Zeitungsartikel(null, "", "", "", "", List.of());
			case "FIlmsVideos" -> new FIlmsVideos();
			case "Youtube" -> new Youtube();
			case "PodcastGesamtwerk" -> new PodcastGesamtwerk();
			case "Podcastfolgen" -> new Podcastfolgen();
			case "SocialMediaBeitraege" -> new SocialMediaBeitraege();
			default -> throw new IllegalArgumentException("Unbekannter Quellentyp: " + type);
		};
	}

	/**
	 * Loads editable values for the current source and enables editing mode.
	 */
	private void beginEditing() {
		LinkedHashMap<String, String> values = new LinkedHashMap<>(shownAttributes);
		List<String> authors = currentSource.getAutor();
		values.put("Autor / Herausgeber", String.join("; ", authors.stream()
				.filter(author -> author != null && !author.isBlank()).toList()));
		if (currentSource instanceof Buchbeleg) {
			// These strings are the book getters' placeholders for missing values.
			if (values.get("Verlag").equals("o.V."))
				values.put("Verlag", "");
			if (values.get("Erscheinungsort").equals("o.O."))
				values.put("Erscheinungsort", "");
		}
		setEditing(true);
		showDetails(sourceTitle.getText(), sourceType.getText(), values);
	}

	/**
	 * Discards the draft and restores the current source display.
	 */
	private void cancelEditing() {
		creating = false;
		creationDraft.clear();
		setEditing(false);
		showSource(currentSource);
	}

	/**
	 * Validates form values and invokes the creation or update callback.
	 */
	private void acceptEditing() {
		Literatur replacement;
		try {
			replacement = readFormSource();
		} catch (IllegalArgumentException | java.time.DateTimeException error) {
			inputError.setText(error.getMessage() == null ? "Auflage und Seitenangabe prüfen." : error.getMessage());
			inputError.setToolTipText(inputError.getText());
			return;
		}
		boolean newSource = creating;
		creating = false;
		creationDraft.clear();
		setEditing(false);
		if (newSource) {
			addSource.accept(replacement);
		} else {
			saveChanges.accept(replacement);
		}
		showSource(replacement);
	}

	/**
	 * Builds a replacement source from the current form values.
	 *
	 * @return the source represented by the form
	 */
	private Literatur readFormSource() {
		List<String> authors = new ArrayList<>();
		for (String author : inputText("Autor / Herausgeber").split(";")) {
			if (!author.isBlank())
				authors.add(author.trim());
		}
		Optional<LocalDate> published = readDate("Veröffentlichungsdatum");
		Optional<LocalDate> retrieved = readDate("Abrufdatum");
		String type = creating ? (String) creationTypeComboBox.getSelectedItem()
				: currentSource.getClass().getSimpleName();
		if (type.equals(Website.class.getSimpleName())) {
			return new Website(authors, inputText("Websitename"), readText("Titel"),
					inputText("URL"), retrieved.orElse(null), published.orElse(null), inputText("Textart"));
		}
		if (!type.equals("Buchbeleg")) {
			Literatur source = createBlankSource(type);
			if (source instanceof BildQuelle image) {
				if (authors.size() > 4) {
					throw new IllegalArgumentException("Bildquellen unterstützen hier höchstens vier Urheber.");
				}
				image.setUrheber1(authors.size() > 0 ? authors.get(0) : "");
				image.setUrheber2(authors.size() > 1 ? authors.get(1) : "");
				image.setUrheber3(authors.size() > 2 ? authors.get(2) : "");
				image.setUrheber4(authors.size() > 3 ? authors.get(3) : "");
				image.setMedienart(readText("Medienart"));
				image.setHerausgeber(readText("Herausgeber"));
				image.setAnbieter(readText("Anbieter"));
			} else {
				source.setAutor(authors);
			}
			source.setTitel(readText("Titel"));
			source.setVeroeffentlichungsdatum(published);
			source.setAbrufdatum(retrieved);
			if (source instanceof Zeitungsartikel article) {
				article.setZeitung(inputText("Zeitung"));
				article.setSeitenzahl(inputText("Seitenzahl"));
				article.setUrl(inputText("URL"));
			} else if (source instanceof Audiovisuell media) {
				media.setBeitragsArt(readText("Beitragsart"));
				media.setMedium(readText("Medium"));
				media.setUrl(readText("URL"));
				if (media instanceof FIlmsVideos film) {
					film.setProduktionsfirma(readText("Produktionsfirma"));
				} else if (media instanceof PodcastGesamtwerk podcast) {
					podcast.setHerausgebendeOrganisation(readText("Herausgebende Organisation"));
				} else if (media instanceof Podcastfolgen episode) {
					episode.setHerausgebendeOrganisation(readText("Herausgebende Organisation"));
					episode.setPodcastFolge(readNumber(inputText("Folgennummer")));
					episode.setPodcastFolgenName(readText("Folgentitel"));
				} else if (media instanceof SocialMediaBeitraege post) {
					post.setText(readText("Text"));
					post.setBeschreibung(readText("Beschreibung"));
				}
			}
			return source;
		}
		String[] pages = inputText("Seitenangabe").split("-", -1);
		if (pages.length > 2 || (pages.length == 2 && (pages[0].isBlank() || pages[1].isBlank()))) {
			throw new IllegalArgumentException("Seitenangabe: z. B. 12 oder 12-20.");
		}
		Buchbeleg book = new Buchbeleg(authors, published, readText("Titel"), readText("Verlag"),
				readNumber(inputText("Auflage")), readText("Erscheinungsort"), readNumber(pages[0]),
				pages.length == 2 ? readNumber(pages[1]) : Optional.empty());
		book.setAbrufdatum(retrieved);
		return book;
	}

	/**
	 * Reads and trims the field with the given attribute label.
	 *
	 * @param name name
	 * @return the trimmed input
	 */
	private String inputText(String name) {
		return editFields.get(name).getText().trim();
	}

	/**
	 * Reads an optional trimmed text value from the named form field.
	 *
	 * @param name name
	 * @return the optional field value
	 */
	private Optional<String> readText(String name) {
		String text = inputText(name);
		return text.isEmpty() ? Optional.empty() : Optional.of(text);
	}

	/**
	 * Reads an optional ISO local date from the named form field.
	 *
	 * @param name name
	 * @return the optional field value
	 */
	private Optional<LocalDate> readDate(String name) {
		String text = inputText(name);
        text = normalizeDate(text);
		return text.isEmpty() ? Optional.empty() : Optional.of(LocalDate.parse(text));
	}

    private static final Pattern DAY_FIRST_DATE =
        Pattern.compile("(\\d{1,2})([./])(\\d{1,2})\\2(\\d{4})");

    private static String normalizeDate(String text) {
        var match = DAY_FIRST_DATE.matcher(text);
        if (!match.matches()) {
            return text;
        }

        int day = Integer.parseInt(match.group(1));
        int month = Integer.parseInt(match.group(3));
        return String.format(Locale.ROOT, "%s-%02d-%02d", match.group(4), month, day);
    }

	/**
	 * Parses optional integer input and reports invalid numeric text.
	 *
	 * @param text text
	 * @return the optional integer
	 */
	private Optional<Integer> readNumber(String text) {
		if (text.isBlank())
			return Optional.empty();
		try {
			return Optional.of(Integer.parseInt(text.trim()));
		} catch (NumberFormatException error) {
			throw new IllegalArgumentException("Auflage und Seitenangaben müssen ganze Zahlen sein.");
		}
	}

	/**
	 * Updates editing mode, notifies the owner, and refreshes available actions.
	 *
	 * @param editing whether editing mode is active
	 */
	private void setEditing(boolean editing) {
		this.editing = editing;
		inputError.setText("");
		editingChanged.accept(editing);
		updateActions();
	}

	/**
	 * Rebuilds the buttons for viewing, editing, or creating a source.
	 */
	private void updateActions() {
		actionPanel.removeAll();
		if (editing) {
			JButton cancel = new JButton("Abbrechen");
			cancel.addActionListener(event -> discardEditing());
			JButton accept = new JButton("Übernehmen");
			accept.addActionListener(event -> acceptEditing());
			accept.setEnabled(!creating || (creationTypeComboBox.getSelectedIndex() > 0
					&& !"Sammelbandbeleg".equals(creationTypeComboBox.getSelectedItem())));
			actionPanel.add(cancel);
			actionPanel.add(accept);
		} else {
			actionPanel.add(deleteButton);
			actionPanel.add(editButton);
		}
		actionPanel.revalidate();
		actionPanel.repaint();
	}

	/**
	 * Checks for a selected creation type or edits differing from displayed values.
	 *
	 * @return whether the current draft has unsaved changes
	 */
	private boolean hasUnsavedChanges() {
		if (!editing) {
			return false;
		}
		if (creating) {
			return creationTypeComboBox.getSelectedIndex() > 0;
		}
		for (Map.Entry<String, JTextField> entry : editFields.entrySet()) {
			String original = shownAttributes.getOrDefault(entry.getKey(), "");
			if (!Objects.equals(entry.getValue().getText(), original)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Asks before discarding changed values and restores viewing mode if accepted.
	 */
	public void discardEditing() {
		if (hasUnsavedChanges()) {
			int choice = JOptionPane.showConfirmDialog(this, "Es gibt ungespeicherte Änderungen. Wirklich verwerfen?",
					"Ungespeicherte Änderungen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice != JOptionPane.YES_OPTION) {
				return;
			}
			setEditing(false);
			showSource(currentSource);
		}
		cancelEditing();
	}

	/**
	 * Offers to apply or discard unsaved changes before closing.
	 *
	 * @return whether closing may proceed
	 */
	boolean confirmCloseIfEditing() {
		if (!hasUnsavedChanges()) {
			return true;
		}
		Object[] options = { "Übernehmen", "Verwerfen", "Weiter bearbeiten" };
		int choice = JOptionPane.showOptionDialog(this, "Sie haben Änderungen, die noch nicht übernommen wurden.",
				"Ungespeicherte Änderungen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
				options[2]);
		if (choice == 0) {
			acceptEditing();
			return !editing;
		}
		if (choice == 1) {
			cancelEditing();
			return true;
		}
		return false;
	}

	/**
	 * Displays a temporary toast notification above the specified component.
	 *
	 * @param anchor  the component above which the toast should be positioned
	 * @param message the notification text to display
	 */
	public void showNotificationToast(JComponent anchor, String message) {
		JRootPane rootPane = SwingUtilities.getRootPane(anchor);
		if (rootPane == null)
			return;
		JLayeredPane layeredPane = rootPane.getLayeredPane();

		// if there's an old toast -> stop timer + delete label
		if (activeToastTimer != null) {
			activeToastTimer.stop();
		}
		if (activeToast != null) {
			layeredPane.remove(activeToast);
			layeredPane.repaint();
		}

		// extracts standard colors from active swing theme
		Color tempBg = UIManager.getColor("ToolTip.background");
		Color tempFg = UIManager.getColor("ToolTip.foreground");
		final Color baseBg = (tempBg != null) ? tempBg : new Color(0xF7F8FA);
		final Color baseFg = (tempFg != null) ? tempFg : Color.DARK_GRAY;

		// Creates new toast (ohne Outline, nur Innenabstand)
		JLabel toast = new JLabel(message);
		toast.setFont(anchor.getFont().deriveFont(11f));
		toast.setOpaque(true);
		toast.setBackground(baseBg);
		toast.setForeground(baseFg);
		toast.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		toast.setSize(toast.getPreferredSize());

		Point pos = SwingUtilities.convertPoint(anchor.getParent(), anchor.getLocation(), layeredPane);
		int x = Math.min(
				pos.x + (anchor.getWidth() - toast.getWidth()) / 2,
				layeredPane.getWidth() - toast.getWidth() - 10);
		int y = pos.y - toast.getHeight() - 6;
		toast.setLocation(Math.max(10, x), Math.max(10, y));

		layeredPane.add(toast, JLayeredPane.POPUP_LAYER);
		activeToast = toast;

		// Fade-Timer (16 ms = ~60 fps, 32 steps)
		javax.swing.Timer fadeTimer = new javax.swing.Timer(16, null);
		fadeTimer.setInitialDelay(800);
		fadeTimer.addActionListener(e -> {
			int alpha = toast.getBackground().getAlpha() - 8;
			if (alpha <= 0) {
				fadeTimer.stop();
				layeredPane.remove(toast);
				layeredPane.repaint();
				if (activeToast == toast) {
					activeToast = null;
				}
			} else {
				toast.setBackground(new Color(baseBg.getRed(), baseBg.getGreen(), baseBg.getBlue(), alpha));
				toast.setForeground(new Color(baseFg.getRed(), baseFg.getGreen(), baseFg.getBlue(), alpha));
				layeredPane.repaint();
			}
		});
		activeToastTimer = fadeTimer;
		fadeTimer.start();
	}

}
