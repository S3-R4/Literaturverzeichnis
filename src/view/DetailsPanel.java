package view;

import java.awt.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import model.Literatur;
import model.Buchbeleg;
import model.Website;
import javax.swing.*;

final class DetailsPanel extends JPanel {
    private final JPanel attributesPanel = new JPanel();
    private final JScrollPane attributesScrollPane = new JScrollPane(attributesPanel);
    private final JTextArea citationPreview = new JTextArea(3, 20);
    private final JLabel sourceTitle = new JLabel();
    private final JLabel sourceType = new JLabel();

    DetailsPanel() {
        super(new BorderLayout(0, 16));
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
        sourceHeading.add(sourceType);
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
        citationPanel.add(caption, BorderLayout.NORTH);
        citationPanel.add(previewScrollPane, BorderLayout.CENTER);
        add(citationPanel, BorderLayout.SOUTH);
        showDetails("", "", Map.of(), "");
    }

    void showSource(Literatur source) {
        if (source == null) {
            showDetails("", "", Map.of(), "");
            return;
        }
        LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
        String authors = source.getAutor() == null ? "" : String.join(", ", source.getAutor().stream()
                .filter(author -> author != null && !author.isBlank()).toList());
        attributes.put("Autor / Herausgeber", authors);
        attributes.put("Titel", source.getTitel());
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
        }
        String shortCitation;
        try {
            shortCitation = source.generiereKurzbeleg();
        } catch (RuntimeException error) {
            shortCitation = "Kurzbeleg konnte nicht erstellt werden: " + error.getMessage();
        }
        showDetails(source.getTitel(), source.getClass().getSimpleName(), attributes, shortCitation);
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    void showDetails(String title, String type, Map<String, String> attributes, String shortCitation) {
        sourceTitle.setText(title);
        sourceType.setText(type);
        attributesPanel.removeAll();
        if (attributes.isEmpty()) {
            JLabel prompt = new JLabel("Bitte einen Eintrag auswählen.");
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
        fieldPanel.add(valueLabel, BorderLayout.CENTER);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldPanel.getPreferredSize().height));
        attributesPanel.add(fieldPanel);
        attributesPanel.add(Box.createVerticalStrut(12));
    }
}
