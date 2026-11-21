package model.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import model.*;
import model.Audiovisuell.*;
import tools.jackson.databind.*;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.*;

public class DataManager {

	private Path quellenFile = Path.of("data", "quellenDataStandard.json");
	private final Map<Literatur, String> idsBySource = new IdentityHashMap<>();

	private final ObjectMapper mapper = JsonMapper.builder()
			.changeDefaultVisibility(visibility -> visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
			.build();
    
	// -------------------------------------------------------------------------------------
	/**
	 * adds the entry to the json file
	 */
	public String add_data(Literatur entry) throws IOException {
		Objects.requireNonNull(entry, "entry");

		String quellenTyp = entry.getClass().getSimpleName();
		ObjectNode root = loadData();

		String id = UUID.randomUUID().toString();
		ObjectNode newEntry = mapper.valueToTree(entry);
		newEntry.put("id", id);

		root.withArrayProperty(quellenTyp).add(newEntry);

		Files.createDirectories(quellenFile.getParent());
		mapper.writerWithDefaultPrettyPrinter().writeValue(quellenFile.toFile(), root);
		idsBySource.put(entry, id);
		return id;
	}

	// ------------------------------------------------------------------------------------
	// reading
	// this func reads all entries
	public String read_data() throws IOException {
		return prettyJson(loadData());
	}

	// reads all entries of a given type
	public String read_data(String datatype) throws IOException {
		return prettyJson(entriesOfType(loadData(), datatype));
	}

	// reads the entry of the given ID
	public String read_data_by_id(String id) throws IOException {
		Objects.requireNonNull(id, "id");

		ObjectNode root = loadData();
		JsonNode idTarget = mapper.valueToTree(id);

		for (JsonNode category : root.values()) {
			if (!(category instanceof ArrayNode entries))
				continue;
			int index = findIndex(entries, idTarget);
			if (index >= 0)
				return prettyJson(entries.get(index));
		}
		throw new NoSuchElementException(
				"No entries with ID" + id);
	}

	private ArrayNode entriesOfType(ObjectNode root, String type) throws IOException {
		JsonNode entries = root.get(type);

		if (entries == null)
			return mapper.createArrayNode();

		if (entries instanceof ArrayNode list)
			return list;

		throw new IOException("The cathegory " + type + " must have a list");
	}

	private String prettyJson(JsonNode data) {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}

	// --------------------------------------------------------------------------------------
	// deletations based on id
	public boolean delete_data_by_id(String id) throws IOException {
		Objects.requireNonNull(id, "id");

		ObjectNode root = loadData();
		JsonNode idTarget = mapper.valueToTree(id);

		for (JsonNode category : root.values()) {
			if (!(category instanceof ArrayNode entries)) {
				continue;
			}

			int index = findIndex(entries, idTarget);
			if (index >= 0) {
				entries.remove(index);
				mapper.writerWithDefaultPrettyPrinter()
						.writeValue(quellenFile.toFile(), root);
				return true;
			}
		}

		return false;
	}

	// --------------------------------------------------------------------------------------
	// edit
	public boolean edit_entry(String id, Literatur updatedEntry) throws IOException {
		Objects.requireNonNull(id, "id");
		Objects.requireNonNull(updatedEntry, "updatedEntry");

		ObjectNode root = loadData();
		JsonNode idTarget = mapper.valueToTree(id);

		ArrayNode originalEntries = null;
		int originalIndex = -1;

		// looks for the entry
		for (JsonNode category : root.values()) {
			if (!(category instanceof ArrayNode entries)) {
				continue;
			}

			int index = findIndex(entries, idTarget);
			if (index >= 0) {
				originalEntries = entries;
				originalIndex = index;
				break;
			}
		}

		if (originalEntries == null) {
			return false;
		}

		// Converts the new data into the entry, but preserves the id
		ObjectNode replacement = mapper.valueToTree(updatedEntry);
		replacement.put("id", id);

		String type = updatedEntry.getClass().getSimpleName();
		ArrayNode targetEntries = root.withArrayProperty(type);

		if (originalEntries == targetEntries) {
			originalEntries.set(originalIndex, replacement);
		} else {
			originalEntries.remove(originalIndex);
			targetEntries.add(replacement);
		}

		mapper.writerWithDefaultPrettyPrinter()
				.writeValue(quellenFile.toFile(), root);

		return true;
	}

	// ---------------------------------------------------------------------------------------
	// Loads the JSON data, or returns an empty object if the file is missing or
	// empty.
	private ObjectNode loadData() throws IOException {
		if (Files.notExists(quellenFile) || Files.size(quellenFile) == 0)
			return mapper.createObjectNode();

		JsonNode data = mapper.readTree(quellenFile.toFile());

		if (data instanceof ObjectNode root)
			return root;
		throw new IOException("the JSON file has to contain a root object" + quellenFile);
	}

	private int findIndex(ArrayNode entries, JsonNode idTarget) {
		for (int i = 0; i < entries.size(); i++) {
			if (idTarget.equals(entries.get(i).path("id"))) {
				return i;
			}
		}

		return -1;
	}

	// ---------------------------------------------------------------------------------------
	// import
	
	public void importJson(Path path) throws IOException {
    Objects.requireNonNull(path, "path");
    if (!Files.isRegularFile(path) || !Files.isReadable(path)) {throw new IOException("Die ausgewählte Datei kann nicht gelesen werden.");
    }
    if (Files.size(path) == 0) {throw new IOException("Die JSON-Datei ist leer.");
    }
    JsonNode data = mapper.readTree(path.toFile());
    if (!(data instanceof ObjectNode)) {throw new IOException("Die JSON-Datei muss ein JSON-Objekt enthalten.");
    }

    // Only switch files after validation succeeds.
    this.quellenFile = path;
}

	public boolean createJson(Path path) throws IOException {
		Objects.requireNonNull(path, "path");

		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}

		try {
			Files.writeString(
					path,
					"{}",
					java.nio.file.StandardOpenOption.CREATE_NEW);
		} catch (java.nio.file.FileAlreadyExistsException e) {
			return false;
		}

		this.quellenFile = path;
		return true;
	}
	
	//------------------------------------------------------------------------------------------
	
	/**
	 * save the current Json as a copy on a new FilePath
	 * @param path neuer Speicherort
	 */
	public void exportJson(Path path) throws IOException { //TODO: funktioniert irgenwie noch nicht
		Objects.requireNonNull(path, "path");
		
		if (this.quellenFile == null) {
			throw new IllegalStateException("No JSON file loaded");
		}
		
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		
		Files.copy(this.quellenFile, path, StandardCopyOption.REPLACE_EXISTING);
	}
	
public List<Literatur> readAllSources() throws IOException { //Reads JSON and returns it as a object
    ObjectNode root = loadData();
    List<Literatur> sources = new ArrayList<>();
	idsBySource.clear();
    for (var category : root.properties()) {
        String type = category.getKey();
        JsonNode entries = category.getValue();

        if (!(entries instanceof ArrayNode array)) {
            throw new IOException("Die Kategorie " + type + " muss eine Liste enthalten.");
        }

        for (JsonNode entry : array) {
            if (!(entry instanceof ObjectNode object)) {
                throw new IOException("Ein Eintrag in " + type + " ist ungültig.");
            }

            try {
                Literatur source = convertEntry(type, object);
				String id = readString(object.get("id"));

                if (id.isBlank()) {
                    throw new IOException("Ein Eintrag in " + type + " hat keine ID.");
                }
				idsBySource.put(source, id);
				sources.add(source);
            } catch (RuntimeException e) {
                throw new IOException(
                    "Fehler beim Lesen eines Eintrags vom Typ " + type + ": " + e.getMessage(), e
                );
            }
        }
    }

    return sources;
}
    private Literatur convertEntry(String type, ObjectNode entry) { //Reads the JSON for name category and decides which class to create
        List<String> authors = readStringList(entry.get("autor"));
        Optional<LocalDate> published = readDate(entry.get("veroeffentlichungsdatum"));
        Optional<LocalDate> retrieved = readDate(entry.get("abrufdatum"));
        Optional<String> title = readText(entry.get("titel"));
        
        return switch (type) {
            case "Buchbeleg" -> {
                    JsonNode pages = entry.get("seitenangabe");
                    yield new Buchbeleg(authors,published,title,readText(entry.get("buchverlag")),readInteger(entry.get("auflage")),readText(entry.get("erscheinungsort")),readInteger(pages == null ? null : pages.get("anfang")),readInteger(pages == null ? null : pages.get("ende")));
                }

                case "Sammelbandbeleg" -> {
                    JsonNode pages = entry.get("seitenangabe");
                    yield new Sammelbandbeleg(authors,published,title,readStringList(entry.get("herausgeber")),readText(entry.get("sammelbandtitel")),readInteger(entry.get("auflage")),readText(entry.get("verlag")),readText(entry.get("erscheinungsort")),readInteger(pages == null ? null : pages.get("anfang")),readInteger(pages == null ? null : pages.get("ende")));
                }

                case "Website" -> new Website(authors,readString(entry.get("websitename")),title,readString(entry.get("url")),retrieved.orElse(null),published.orElse(null),readString(entry.get("textart")));

                case "Zeitungsartikel" -> new Zeitungsartikel(published.orElse(null),title.orElse(""),readString(entry.get("zeitung")),readString(entry.get("seitenzahl")),readString(entry.get("url")),authors);

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
                podcast.setHerausgebendeOrganisation(
                    readText(entry.get("herausgebendeOrganisation"))
                );
                yield podcast;
                }

                case "Podcastfolgen" -> {
                Podcastfolgen episode = new Podcastfolgen();
                fillAudiovisual(episode, authors, title, published, retrieved, entry);
                episode.setHerausgebendeOrganisation(readText(entry.get("herausgebendeOrganisation"))
                );
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

private void fillAudiovisual(Audiovisuell source,List<String> authors,Optional<String> title,Optional<LocalDate> published,Optional<LocalDate> retrieved,ObjectNode entry) {
    source.setAutor(authors);
    source.setTitel(title);
    source.setVeroeffentlichungsdatum(published);
    source.setAbrufdatum(retrieved);
    source.setBeitragsArt(readText(entry.get("beitragsArt")));
    source.setMedium(readText(entry.get("medium")));
    source.setUrl(readText(entry.get("url")));
}

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

private String authorAt(List<String> authors, int index) {
    return index < authors.size() ? authors.get(index) : "";
}

private String readString(JsonNode node) {
return node == null || node.isNull() ? "" : node.asText();
}

private Optional<String> readText(JsonNode node) {
String value = readString(node).trim();
return value.isEmpty() ? Optional.empty() : Optional.of(value);
}

private Optional<Integer> readInteger(JsonNode node) {
    if (node == null || node.isNull()) {
    return Optional.empty();
    }

    if (!node.isNumber()) {
        throw new IllegalArgumentException("Eine Zahl wurde erwartet.");
    }

    return Optional.of(node.intValue());
}

private Optional<LocalDate> readDate(JsonNode node) {
    if (node == null || node.isNull() || node.asText().isBlank()) {
    return Optional.empty();
    }

    return Optional.of(LocalDate.parse(node.asText()));
}

private boolean readBoolean(JsonNode node) {
    return node != null && !node.isNull() && node.asBoolean(false);
}
public String getId(Literatur source) {
    return idsBySource.get(source);
}

}
