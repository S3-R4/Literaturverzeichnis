package model;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Optional;

public class Validierung {
    public Validierung() {

    }

    public static Optional<String> pruefeJahr(Optional<String> jahr) {
        if (jahr == null || jahr.isEmpty() || jahr.get().trim().isBlank()) {
            return Optional.of(Fehlermeldungen.getJahrFehler());
        }

        try {
            int jahrzahl = Integer.parseInt(jahr.get().trim());

            if (jahrzahl < 1000 || jahrzahl > 2027) {
                return Optional.of(Fehlermeldungen.getJahrFehler());
            }

        } catch (NumberFormatException e) {
            return Optional.of(Fehlermeldungen.getJahrFehler());
        }

        return Optional.empty();
    }

    public static Optional<String> pruefeDatum(Optional<String> datum) {
        if (datum == null || datum.isEmpty() || datum.get().isBlank()) {
            return Optional.of(Fehlermeldungen.getDatumFehler());
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(datum.get().trim(), formatter);
            return Optional.empty();

        } catch (DateTimeParseException e) {
            return Optional.of(Fehlermeldungen.getDatumFehler());
        }
    }

    public static Optional<String> pruefeText(Optional<String> text) {
        if (text == null || text.isEmpty() || text.get().isBlank()) {
            return Optional.of(Fehlermeldungen.getTextFehler());
        }

        return Optional.empty();
    }

    public static Optional<String> pruefeURL(Optional<String> url) {
        if (url == null || url.isEmpty() || url.get().isBlank()) {
            return Optional.of(Fehlermeldungen.getURLFehler());
        }

        try {
            URI uri = new URI(url.get().trim());

            if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                    || uri.getHost() == null) {

                return Optional.of(Fehlermeldungen.getURLFehler());
            }

        } catch (URISyntaxException e) {
            return Optional.of(Fehlermeldungen.getURLFehler());
        }

        return Optional.empty();
    }
}
