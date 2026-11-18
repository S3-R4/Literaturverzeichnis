package view;

import java.awt.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import model.Literatur;
import model.Buchbeleg;
import model.Website;
import model.BildQuelle;
import model.Zeitungsartikel;
import model.Audiovisuell.*;
import javax.swing.*;

final class DetailsPanel extends JPanel {
    private final JPanel attributesPanel = new JPanel();
    private final JScrollPane attributesScrollPane = new JScrollPane(attributesPanel);
    private final JTextArea citationPreview = new JTextArea(3, 20);
    private final JLabel sourceTitle = new JLabel();
    private final JLabel sourceType = new JLabel();

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
    private final JButton editButton = new JButton("Bearbeiten");
    private final LinkedHashMap<String, JTextField> editFields = new LinkedHashMap<>();
    private Map<String, String> shownAttributes = Map.of();
    private Literatur currentSource;
    private boolean editing;

    DetailsPanel(Consumer<Literatur> saveChanges, Consumer<Literatur> addSource,
            Consumer<Boolean> editingChanged) {
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
            if (creating) showCreationFields();
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
        citationPreview.getAccessibleContext().setAccessibleName("Kurzbeleg-Vorschau");
        JScrollPane previewScrollPane = new JScrollPane(citationPreview);
        previewScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xBFC1C4)));
        JPanel citationPanel = new JPanel(new BorderLayout(0, 7));
        citationPanel.setOpaque(false);
        JLabel caption = new JLabel("Kurzbeleg");
        caption.setFont(getFont().deriveFont(11f));
        caption.setForeground(Color.DARK_GRAY);
        actionPanel.setOpaque(false);
        inputError.setForeground(new Color(0xA00000));
        inputError.setFont(getFont().deriveFont(11f));
        editButton.setEnabled(false);
        editButton.addActionListener(event -> beginEditing());
        JPanel controls = new JPanel(new BorderLayout(0, 5));
        controls.setOpaque(false);
        controls.add(actionPanel, BorderLayout.NORTH);
        controls.add(inputError, BorderLayout.CENTER);
        controls.add(caption, BorderLayout.SOUTH);
        citationPanel.add(controls, BorderLayout.NORTH);
        updateActions();
        citationPanel.add(previewScrollPane, BorderLayout.CENTER);
        add(citationPanel, BorderLayout.SOUTH);
        showDetails("", "", Map.of(), "");
    }

    void showSource(Literatur source) {
        if (editing) {
            return;
        }
        currentSource = source;
        editButton.setEnabled(source instanceof Buchbeleg || source instanceof Website);
        if (source == null) {
            showDetails("", "", Map.of(), "");
            return;
        }
        LinkedHashMap<String, String> attributes = readAttributes(source);
        String shortCitation;
        try {
            shortCitation = source.generiereKurzbeleg();
        } catch (RuntimeException error) {
            shortCitation = "Kurzbeleg konnte nicht erstellt werden: " + error.getMessage();
        }
        showDetails(source.getTitel().orElse(""), source.getClass().getSimpleName(), attributes, shortCitation);
    }

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

    private String formatDate(Optional<LocalDate> date) {
        return date.map(LocalDate::toString).orElse("");
    }

    void showDetails(String title, String type, Map<String, String> attributes, String shortCitation) {
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
                    ? "Bitte einen Quellentyp auswählen." : "Bitte einen Eintrag auswählen.");
            prompt.setFont(getFont());
            attributesPanel.add(prompt);
        }
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            addAttribute(attribute.getKey(), attribute.getValue());
        }
        attributesPanel.add(Box.createVerticalGlue());
        citationPreview.setText(shortCitation == null ? "" : shortCitation);
        citationPreview.setCaretPosition(0);
        attributesPanel.revalidate();
        attributesPanel.repaint();
        attributesScrollPane.getViewport().setViewPosition(new Point(0, 0));
    }

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
            fieldPanel.add(valueLabel, BorderLayout.CENTER);
        }
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldPanel.getPreferredSize().height));
        attributesPanel.add(fieldPanel);
        attributesPanel.add(Box.createVerticalStrut(12));
    }

    void beginCreation() {
        if (editing) return;
        creating = true;
        creationDraft.clear();
        setEditing(true);
        creationTypeComboBox.setSelectedIndex(0);
        showCreationFields();
    }

    private void showCreationFields() {
        for (Map.Entry<String, JTextField> entry : editFields.entrySet()) {
            creationDraft.put(entry.getKey(), entry.getValue().getText());
        }
        inputError.setText("");
        LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
        if (creationTypeComboBox.getSelectedIndex() > 0) {
            if ("Sammelbandbeleg".equals(creationTypeComboBox.getSelectedItem())) {
                // shortcut: enable this form once Sammelbandbeleg extends Literatur on this branch.
                inputError.setText("Sammelbandbeleg ist hier noch kein Literatur-Typ.");
            } else {
                attributes = readAttributes(createBlankSource((String) creationTypeComboBox.getSelectedItem()));
                attributes.replaceAll((name, value) -> creationDraft.getOrDefault(name, ""));
            }
        }
        showDetails("Neuen Verweis erstellen", "", attributes, "");
        updateActions();
    }

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

    private void beginEditing() {
        LinkedHashMap<String, String> values = new LinkedHashMap<>(shownAttributes);
        List<String> authors = currentSource.getAutor();
        values.put("Autor / Herausgeber", String.join("; ", authors.stream()
                .filter(author -> author != null && !author.isBlank()).toList()));
        if (currentSource instanceof Buchbeleg) {
            // These strings are the book getters' placeholders for missing values.
            if (values.get("Verlag").equals("o.V.")) values.put("Verlag", "");
            if (values.get("Erscheinungsort").equals("o.O.")) values.put("Erscheinungsort", "");
        }
        setEditing(true);
        showDetails(sourceTitle.getText(), sourceType.getText(), values, citationPreview.getText());
    }

    private void cancelEditing() {
        creating = false;
        creationDraft.clear();
        setEditing(false);
        showSource(currentSource);
    }

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

    private Literatur readFormSource() {
        List<String> authors = new ArrayList<>();
        for (String author : inputText("Autor / Herausgeber").split(";")) {
            if (!author.isBlank()) authors.add(author.trim());
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

    private String inputText(String name) {
        return editFields.get(name).getText().trim();
    }

    private Optional<String> readText(String name) {
        String text = inputText(name);
        return text.isEmpty() ? Optional.empty() : Optional.of(text);
    }

    private Optional<LocalDate> readDate(String name) {
        String text = inputText(name);
        return text.isEmpty() ? Optional.empty() : Optional.of(LocalDate.parse(text));
    }

    private Optional<Integer> readNumber(String text) {
        if (text.isBlank()) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(text.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Auflage und Seitenangaben müssen ganze Zahlen sein.");
        }
    }

    private void setEditing(boolean editing) {
        this.editing = editing;
        inputError.setText("");
        editingChanged.accept(editing);
        updateActions();
    }

    private void updateActions() {
        actionPanel.removeAll();
        if (editing) {
            JButton cancel = new JButton("Abbrechen");
            cancel.addActionListener(event -> cancelEditing());
            JButton accept = new JButton("Übernehmen");
            accept.addActionListener(event -> acceptEditing());
            accept.setEnabled(!creating || (creationTypeComboBox.getSelectedIndex() > 0
                    && !"Sammelbandbeleg".equals(creationTypeComboBox.getSelectedItem())));
            actionPanel.add(cancel);
            actionPanel.add(accept);
        } else {
            actionPanel.add(editButton);
        }
        actionPanel.revalidate();
        actionPanel.repaint();
    }
}
