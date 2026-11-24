package model.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Audiovisuell.Audiovisuell;
import model.Audiovisuell.FIlmsVideos;
import model.Audiovisuell.PodcastGesamtwerk;
import model.Audiovisuell.Podcastfolgen;
import model.Audiovisuell.SocialMediaBeitraege;
import model.Audiovisuell.Youtube;
import model.BildQuelle;
import model.Buchbeleg;
import model.Literatur;
import model.Website;
import model.Zeitungsartikel;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Converts bibliographic sources to and from the JSON format used by the bibliography.
 */
final class SourceJsonMapper {

	/**
	 * Jackson mapper used to serialize the fields of source objects.
	 */
	private final ObjectMapper mapper;

	/**
	 * Creates a converter for bibliographic entries.
	 *
	 * @param mapper Jackson mapper used to serialize source fields
	 */
	SourceJsonMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	// -------------------------------------------------------------------------------------
	// Entry conversion

	/**
	 * Serializes a source and includes its persistent ID.
	 *
	 * @param source bibliographic source to serialize
	 * @param id persistent ID to write into the entry
	 * @return the serialized source as a JSON object
	 */
	ObjectNode toJson(Literatur source, String id) {
		ObjectNode entry = mapper.valueToTree(source);
		entry.put("id", id);
		return entry;
	}

	/**
	 * Reads the persistent ID stored in a JSON entry.
	 *
	 * @param entry JSON object representing a bibliographic source
	 * @return the ID as text, or an empty string if it is missing or null
	 */
	String readId(ObjectNode entry) {
		return readString(entry.get("id"));
	}

	/**
	 * Reconstructs the concrete source type from its stored fields.
	 *
	 * @param type category name that identifies the source type
	 * @param entry stored JSON object containing the source fields
	 * @return a new source instance populated from the entry
	 * @throws IllegalArgumentException if the type is unsupported or a numeric field or page range
	 *     is invalid
	 * @throws java.time.format.DateTimeParseException if a date is present but invalid
	 */
	Literatur fromJson(String type, ObjectNode entry) {
		List<String> authors = readStringList(entry.get("autor"));
		Optional<LocalDate> published = readDate(entry.get("veroeffentlichungsdatum"));
		Optional<LocalDate> retrieved = readDate(entry.get("abrufdatum"));
		Optional<String> title = readText(entry.get("titel"));

		return switch (type) {
			case "Buchbeleg" -> {
				JsonNode pages = entry.get("seitenangabe");
				Optional<Integer> firstPage = readInteger(pages == null ? null : pages.get("anfang"));
				Optional<Integer> lastPage = readInteger(pages == null ? null : pages.get("ende"));
				if (pages != null && pages.isString() && pages.asText().contains("-")) {
					String[] range = pages.asText().split("-", 2);
					firstPage = Optional.of(Integer.parseInt(range[0].trim()));
					lastPage = Optional.of(Integer.parseInt(range[1].trim()));
				} else if (pages != null && !pages.isObject()) {
					firstPage = readInteger(pages);
				}
				yield new Buchbeleg(authors, published, title, readText(entry.get("buchverlag")),
						readInteger(entry.get("auflage")), readText(entry.get("erscheinungsort")),
						firstPage, lastPage);
			}
			case "Website" -> new Website(authors, readString(entry.get("websitename")), title,
					readString(entry.get("url")), retrieved.orElse(null), published.orElse(null),
					readString(entry.get("textart")));
			case "Zeitungsartikel" -> new Zeitungsartikel(published.orElse(null), title.orElse(""),
					readString(entry.get("zeitung")), readString(entry.get("seitenzahl")),
					readString(entry.get("url")), authors);
			case "BildQuelle" -> {
				BildQuelle image = new BildQuelle();
				image.setUrheber1(authorAt(authors, 0));
				image.setUrheber2(authorAt(authors, 1));
				image.setUrheber3(authorAt(authors, 2));
				image.setUrheber4(authorAt(authors, 3));
				image.setMehrUrheber(readBoolean(entry.get("MehrUrheber")));
				image.setTitel(title);
				image.setMedienart(readText(entry.get("Medienart")));
				image.setVeroeffentlichungsdatum(published);
				image.setAbrufdatum(retrieved);
				image.setHerausgeber(readText(entry.get("Herausgeber")));
				image.setAnbieter(readText(entry.get("Anbieter")));
				yield image;
			}
			case "FIlmsVideos" -> {
				FIlmsVideos film = new FIlmsVideos();
				fillAudiovisual(film, authors, title, published, retrieved, entry);
				film.setProduktionsfirma(readText(entry.get("produktionsfirma")));
				yield film;
			}
			case "Youtube" -> {
				Youtube youtube = new Youtube();
				fillAudiovisual(youtube, authors, title, published, retrieved, entry);
				yield youtube;
			}
			case "PodcastGesamtwerk" -> {
				PodcastGesamtwerk podcast = new PodcastGesamtwerk();
				fillAudiovisual(podcast, authors, title, published, retrieved, entry);
				podcast.setHerausgebendeOrganisation(readText(entry.get("herausgebendeOrganisation")));
				yield podcast;
			}
			case "Podcastfolgen" -> {
				Podcastfolgen episode = new Podcastfolgen();
				fillAudiovisual(episode, authors, title, published, retrieved, entry);
				episode.setHerausgebendeOrganisation(readText(entry.get("herausgebendeOrganisation")));
				episode.setPodcastFolge(readInteger(entry.get("podcastFolge")));
				episode.setPodcastFolgenName(readText(entry.get("podcastFolgenName")));
				yield episode;
			}
			case "SocialMediaBeitraege" -> {
				SocialMediaBeitraege post = new SocialMediaBeitraege();
				fillAudiovisual(post, authors, title, published, retrieved, entry);
				post.setText(readText(entry.get("text")));
				post.setBeschreibung(readText(entry.get("beschreibung")));
				yield post;
			}
			default -> throw new IllegalArgumentException("Unbekannter Quellentyp: " + type);
		};
	}

	/**
	 * Restores metadata shared by the audiovisual source types.
	 *
	 * @param source audiovisual source to populate
	 * @param authors authors of the source
	 * @param title title, if present
	 * @param published publication date, if present
	 * @param retrieved retrieval date, if present
	 * @param entry JSON object containing the remaining audiovisual fields
	 */
	private void fillAudiovisual(Audiovisuell source, List<String> authors, Optional<String> title,
			Optional<LocalDate> published, Optional<LocalDate> retrieved, ObjectNode entry) {
		source.setAutor(authors);
		source.setTitel(title);
		source.setVeroeffentlichungsdatum(published);
		source.setAbrufdatum(retrieved);
		source.setBeitragsArt(readText(entry.get("beitragsArt")));
		source.setMedium(readText(entry.get("medium")));
		source.setUrl(readText(entry.get("url")));
	}

	// -------------------------------------------------------------------------------------
	// Field readers

	/**
	 * Reads the non-null elements of a JSON array as strings.
	 *
	 * @param node JSON value to read
	 * @return the collected strings, or an empty list for a non-array value
	 */
	private List<String> readStringList(JsonNode node) {
		List<String> values = new ArrayList<>();
		if (node instanceof ArrayNode array) {
			for (JsonNode value : array) {
				if (!value.isNull()) {
					values.add(value.asText());
				}
			}
		}
		return values;
	}

	/**
	 * Looks up an author by position.
	 *
	 * @param authors list of authors
	 * @param index zero-based author position
	 * @return the author at that position, or an empty string if the position is past the end of
	 *     the list
	 * @throws IndexOutOfBoundsException if the index is negative
	 */
	private String authorAt(List<String> authors, int index) {
		return index < authors.size() ? authors.get(index) : "";
	}

	/**
	 * Reads a JSON value as text.
	 *
	 * @param node JSON value to read, which may be {@code null}
	 * @return the text representation, or an empty string if the value is missing or null
	 */
	private String readString(JsonNode node) {
		return node == null || node.isNull() ? "" : node.asText();
	}

	/**
	 * Trims the text of a JSON field.
	 *
	 * @param node JSON field value, which may be {@code null}
	 * @return the trimmed text, or an empty optional if there is no text after trimming
	 */
	private Optional<String> readText(JsonNode node) {
		String value = readString(node).trim();
		return value.isEmpty() ? Optional.empty() : Optional.of(value);
	}

	/**
	 * Reads a JSON integer or a legacy numeric string.
	 *
	 * @param node JSON value to read, which may be {@code null}
	 * @return the integer, or an empty optional if the value is missing, null or blank
	 * @throws IllegalArgumentException if the value cannot be represented as a Java {@code int}
	 */
	private Optional<Integer> readInteger(JsonNode node) {
		if (node == null || node.isNull()) {
			return Optional.empty();
		}
		if (node.isString()) {
			return readText(node).map(Integer::parseInt);
		}
		if (!node.isIntegralNumber() || !node.canConvertToInt()) {
			throw new IllegalArgumentException("Eine ganze Zahl wurde erwartet.");
		}
		return Optional.of(node.intValue());
	}

	/**
	 * Reads a calendar date in ISO local-date format.
	 *
	 * @param node JSON date value, which may be {@code null}
	 * @return the parsed date, or an empty optional if the value is missing, null or blank
	 * @throws java.time.format.DateTimeParseException if the date text is invalid
	 */
	private Optional<LocalDate> readDate(JsonNode node) {
		if (node == null || node.isNull() || node.asText().isBlank()) {
			return Optional.empty();
		}
		return Optional.of(LocalDate.parse(node.asText()));
	}

	/**
	 * Reads a JSON value as a boolean.
	 *
	 * @param node JSON value to read, which may be {@code null}
	 * @return the boolean value, or {@code false} if the value is missing, null or cannot be
	 *     converted
	 */
	private boolean readBoolean(JsonNode node) {
		return node != null && !node.isNull() && node.asBoolean(false);
	}
}
