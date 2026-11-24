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

/**
 * Persists sources in JSON categories and tracks identifiers by object identity.
 */
public class DataManager {

	private Path originalQuellenFile = Path.of("data", "quellenDataStandard.json");
	private Path workingQuellenFile = Path.of(originalQuellenFile.toString() + ".swp");

	private boolean workingFileInitialized;

	private final Map<Literatur, String> idsBySource = new IdentityHashMap<>();

	private final ObjectMapper mapper = JsonMapper.builder()
			.changeDefaultVisibility(visibility -> visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
			.build();
    
	// -------------------------------------------------------------------------------------
	/**
	 * Saves a source under its type with a new UUID and records its object identity.
	 *
	 * @param entry source entry
	 * @return the assigned persistent identifier
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	public String add_data(Literatur entry) throws IOException {
		Objects.requireNonNull(entry, "entry");

		String quellenTyp = entry.getClass().getSimpleName();
		ObjectNode root = loadData();

		String id = UUID.randomUUID().toString();
		ObjectNode newEntry = mapper.valueToTree(entry);
		newEntry.put("id", id);

		root.withArrayProperty(quellenTyp).add(newEntry);

		mapper.writerWithDefaultPrettyPrinter().writeValue(workingQuellenFile.toFile(), root);
		idsBySource.put(entry, id);
		return id;
	}

	// ------------------------------------------------------------------------------------
	// reading
	// this func reads all entries
	/**
	 * Reads the selected JSON data as formatted text.
	 *
	 * @return the formatted JSON data
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	public String read_data() throws IOException {
		return prettyJson(loadData());
	}

	// reads all entries of a given type
	/**
	 * Reads the selected JSON data as formatted text.
	 *
	 * @param datatype source category name
	 * @return the formatted JSON data
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	public String read_data(String datatype) throws IOException {
		return prettyJson(entriesOfType(loadData(), datatype));
	}

	// reads the entry of the given ID
	/**
	 * Finds a stored source by identifier across all categories.
	 *
	 * @param id persistent source identifier
	 * @return the matching entry as formatted JSON
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 * @throws NoSuchElementException if no entry has the requested identifier
	 */
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

	/**
	 * Reads a category array, returning an empty array for a missing category.
	 *
	 * @param root JSON root object
	 * @param type source type name
	 * @return the category entries
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	private ArrayNode entriesOfType(ObjectNode root, String type) throws IOException {
		JsonNode entries = root.get(type);

		if (entries == null)
			return mapper.createArrayNode();

		if (entries instanceof ArrayNode list)
			return list;

		throw new IOException("The cathegory " + type + " must have a list");
	}

	/**
	 * Serializes a JSON value with indentation.
	 *
	 * @param data JSON value to format
	 * @return the formatted JSON text
	 */
	private String prettyJson(JsonNode data) {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}

	// --------------------------------------------------------------------------------------
	// deletations based on id
	/**
	 * Removes the first entry with the identifier and saves the file.
	 *
	 * @param id persistent source identifier
	 * @return whether an entry was found and removed
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
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
						.writeValue(workingQuellenFile.toFile(), root);
				idsBySource.entrySet().removeIf(entry -> id.equals(entry.getValue()));
				return true;
			}
		}

		return false;
	}

	// --------------------------------------------------------------------------------------
	// edit
	/**
	 * Replaces a stored source while retaining its identifier, moving categories if needed.
	 *
	 * @param id persistent source identifier
	 * @param updatedEntry replacement source
	 * @return whether the identifier was found and updated
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
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
				.writeValue(workingQuellenFile.toFile(), root);
		idsBySource.entrySet().removeIf(entry -> id.equals(entry.getValue()));
		idsBySource.put(updatedEntry, id);

		return true;
	}

	// ---------------------------------------------------------------------------------------
	// Loads the JSON data, or returns an empty object if the file is missing or
	// empty.
	/**
	 * Loads the active JSON object, using an empty object for a missing or empty file.
	 *
	 * @return the bibliography root object
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	private ObjectNode loadData() throws IOException {
		ensureWorkingFile();
		if (Files.size(workingQuellenFile) == 0)
			return mapper.createObjectNode();

		JsonNode data = mapper.readTree(workingQuellenFile.toFile());

		if (data instanceof ObjectNode root)
			return root;
		throw new IOException("the JSON file has to contain a root object" + workingQuellenFile);
	}

	/**
	 * Locates the first entry whose identifier matches the target.
	 *
	 * @param entries category entries
	 * @param idTarget identifier to locate
	 * @return the matching index, or -1 when absent
	 */
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
	
	/**
	 * Validates a nonempty JSON object file and makes it the active bibliography.
	 *
	 * @param path file path
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
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
    activateFile(path);
}

	/**
	 * Creates an empty JSON object file without overwriting an existing file and activates it.
	 *
	 * @param path file path
	 * @return true if created; false if the path already exists
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
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

		activateFile(path);
		return true;
	}
	
	//------------------------------------------------------------------------------------------
	
	/**
	 * Copies the active JSON file to the destination, replacing an existing file.
	 *
	 * @param path file path
	 * @throws IOException if the file cannot be read or written, or its structure is invalid
	 */
	public void exportJson(Path path) throws IOException {
		Objects.requireNonNull(path, "path");
		
		ensureWorkingFile();
		
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		
		Files.copy(this.workingQuellenFile, path, StandardCopyOption.REPLACE_EXISTING);
	}
	

/**
 * Reconstructs all stored sources and rebuilds their identity-to-ID mapping.
 *
 * @return the reconstructed source list
 * @throws IOException if the file cannot be read or written, or its structure is invalid
 */
public List<Literatur> readAllSources() throws IOException { //Reads JSON and returns is as a object
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

/**
 * Reconstructs the concrete source type from its stored JSON fields.
 *
 * @param type source type name
 * @param entry source entry
 * @return the reconstructed source
 */
private Literatur convertEntry(String type, ObjectNode entry) { //Reads the JSON for name category and decides which class to create
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
				readInteger(entry.get("auflage")), readText(entry.get("erscheinungsort")), firstPage, lastPage);
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

/**
 * Restores common audiovisual metadata from a JSON entry.
 *
 * @param source bibliographic source
 * @param authors author list
 * @param title source title
 * @param published publication date
 * @param retrieved retrieval date
 * @param entry source entry
 */
private void fillAudiovisual(Audiovisuell source,List<String> authors,Optional<String> title,Optional<LocalDate> published,Optional<LocalDate> retrieved,ObjectNode entry) {
    source.setAutor(authors);
    source.setTitel(title);
    source.setVeroeffentlichungsdatum(published);
    source.setAbrufdatum(retrieved);
    source.setBeitragsArt(readText(entry.get("beitragsArt")));
    source.setMedium(readText(entry.get("medium")));
    source.setUrl(readText(entry.get("url")));
}

/**
 * Reads non-null array values as strings, ignoring non-array input.
 *
 * @param node JSON value to read
 * @return the collected strings
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
 * Reads an author by index with an empty fallback beyond the list.
 *
 * @param authors author list
 * @param index zero-based index
 * @return the author or an empty string
 */
private String authorAt(List<String> authors, int index) {
    return index < authors.size() ? authors.get(index) : "";
}

/**
 * Converts a JSON value to text, treating missing and null values as empty.
 *
 * @param node JSON value to read
 * @return the text value
 */
private String readString(JsonNode node) {
return node == null || node.isNull() ? "" : node.asText();
}

/**
 * Trims a JSON text value and treats empty text as absent.
 *
 * @param node JSON value to read
 * @return the optional text
 */
private Optional<String> readText(JsonNode node) {
String value = readString(node).trim();
return value.isEmpty() ? Optional.empty() : Optional.of(value);
}

/**
 * Reads an integer or a legacy numeric string, allowing missing, null or blank values.
 *
 * @param node JSON value to read
 * @return the optional integer
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
 * Parses a JSON date as ISO local-date text, allowing missing or blank values.
 *
 * @param node JSON value to read
 * @return the optional date
 */
private Optional<LocalDate> readDate(JsonNode node) {
    if (node == null || node.isNull() || node.asText().isBlank()) {
    return Optional.empty();
    }

    return Optional.of(LocalDate.parse(node.asText()));
}

/**
 * Reads a JSON boolean with false as the fallback.
 *
 * @param node JSON value to read
 * @return the boolean value
 */
private boolean readBoolean(JsonNode node) {
    return node != null && !node.isNull() && node.asBoolean(false);
}
/**
 * Looks up the identifier recorded for this exact source object.
 *
 * @param source bibliographic source
 * @return the identifier, or null if the object is not tracked
 */
public String getId(Literatur source) {
    return idsBySource.get(source);
}

/** Creates the working copy before the default bibliography is first used. */
private void ensureWorkingFile() throws IOException {
    if (workingFileInitialized) {
        return;
    }
    Path parent = workingQuellenFile.getParent();
    if (parent != null) {
        Files.createDirectories(parent);
    }
    if (Files.exists(originalQuellenFile)) {
        Files.copy(originalQuellenFile, workingQuellenFile, StandardCopyOption.REPLACE_EXISTING);
    } else {
        Files.writeString(workingQuellenFile, "{}");
    }
    workingFileInitialized = true;
}

/** Activates a bibliography only after its working copy is ready. */
private void activateFile(Path path) throws IOException {
    Path workingPath = Path.of(path.toString() + ".swp");
    Files.copy(path, workingPath, StandardCopyOption.REPLACE_EXISTING);
    originalQuellenFile = path;
    workingQuellenFile = workingPath;
    workingFileInitialized = true;
    idsBySource.clear();
}

/** Discards the working copy without changing the original bibliography. */
public void deleteWorkingFile() throws IOException {
    Files.deleteIfExists(workingQuellenFile);
    workingFileInitialized = false;
    idsBySource.clear();
}

/** Commits the working copy to the original bibliography. */
public void save() throws IOException{
    ensureWorkingFile();
	Path temp = Path.of(workingQuellenFile.toString() + ".tmp");
	Files.copy(workingQuellenFile, temp, StandardCopyOption.REPLACE_EXISTING);
	try {
        Files.move(temp, originalQuellenFile, StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING);
    } catch (AtomicMoveNotSupportedException e) {
        Files.move(temp, originalQuellenFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
}
