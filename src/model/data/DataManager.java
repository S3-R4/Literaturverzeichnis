package model.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import model.Literatur;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Manages entries in the active bibliography and tracks their persistent IDs
 * by Java object identity.
 */
public class DataManager {

	/**
	 * Jackson mapper configured to serialize the fields of source objects.
	 */
	private final ObjectMapper mapper = JsonMapper.builder()
			.changeDefaultVisibility(visibility -> visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
			.build();
	/**
	 * File operations for the active bibliography.
	 */
	private final BibliographyFiles files = new BibliographyFiles(mapper);
	/**
	 * Converter between source objects and stored JSON entries.
	 */
	private final SourceJsonMapper sourceJson = new SourceJsonMapper(mapper);
	/**
	 * Persistent IDs indexed by the identity of each loaded source instance.
	 */
	private final Map<Literatur, String> idsBySource = new IdentityHashMap<>();

	// -------------------------------------------------------------------------------------
	// Entry changes

	/**
	 * Adds a source to the working copy under its type and assigns it a new ID.
	 *
	 * @param entry source to add
	 * @return the assigned persistent ID
	 * @throws IOException if the working copy cannot be read or written
	 */
	public String add_data(Literatur entry) throws IOException {
		Objects.requireNonNull(entry, "entry");
		ObjectNode root = files.read();

		String id = UUID.randomUUID().toString();
		ObjectNode newEntry = sourceJson.toJson(entry, id);
		root.withArrayProperty(entry.getClass().getSimpleName()).add(newEntry);

		files.write(root);
		idsBySource.put(entry, id);
		return id;
	}

	/**
	 * Removes the first source with the given ID from the working copy.
	 *
	 * @param id persistent source ID
	 * @return whether a source was found and removed
	 * @throws IOException if the working copy cannot be read or written
	 */
	public boolean delete_data_by_id(String id) throws IOException {
		Objects.requireNonNull(id, "id");
		ObjectNode root = files.read();
		JsonNode idTarget = mapper.valueToTree(id);

		for (JsonNode category : root.values()) {
			if (!(category instanceof ArrayNode entries)) {
				continue;
			}
			int index = findIndex(entries, idTarget);
			if (index >= 0) {
				entries.remove(index);
				files.write(root);
				idsBySource.entrySet().removeIf(entry -> id.equals(entry.getValue()));
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces a source in the working copy, preserving its ID. If its type changes,
	 * the entry moves to the end of its new category.
	 *
	 * @param id persistent source ID
	 * @param updatedEntry replacement source
	 * @return whether a source was found and replaced
	 * @throws IOException if the working copy cannot be read or written
	 */
	public boolean edit_entry(String id, Literatur updatedEntry) throws IOException {
		Objects.requireNonNull(id, "id");
		Objects.requireNonNull(updatedEntry, "updatedEntry");
		ObjectNode root = files.read();
		JsonNode idTarget = mapper.valueToTree(id);

		for (JsonNode category : root.values()) {
			if (!(category instanceof ArrayNode entries)) {
				continue;
			}
			int index = findIndex(entries, idTarget);
			if (index < 0) {
				continue;
			}

			ObjectNode replacement = sourceJson.toJson(updatedEntry, id);
			ArrayNode targetEntries = root.withArrayProperty(updatedEntry.getClass().getSimpleName());
			if (entries == targetEntries) {
				entries.set(index, replacement);
			} else {
				entries.remove(index);
				targetEntries.add(replacement);
			}

			files.write(root);
			idsBySource.entrySet().removeIf(entry -> id.equals(entry.getValue()));
			idsBySource.put(updatedEntry, id);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	// Entry reads and ID lookup

	/**
	 * Reads the entire working copy as formatted JSON.
	 *
	 * @return the formatted bibliography
	 * @throws IOException if the working copy cannot be read or has an invalid root
	 */
	public String read_data() throws IOException {
		return prettyJson(files.read());
	}

	/**
	 * Reads the entries of one source type as formatted JSON.
	 *
	 * @param datatype source category name
	 * @return the formatted category array, or an empty array if the category is absent
	 * @throws IOException if the working copy cannot be read or its structure is invalid
	 */
	public String read_data(String datatype) throws IOException {
		JsonNode entries = files.read().get(datatype);
		if (entries == null) {
			return prettyJson(mapper.createArrayNode());
		}
		if (entries instanceof ArrayNode) {
			return prettyJson(entries);
		}
		throw new IOException("The cathegory " + datatype + " must have a list");
	}

	/**
	 * Finds the first source with the given ID across all categories.
	 *
	 * @param id persistent source ID
	 * @return the matching entry as formatted JSON
	 * @throws IOException if the working copy cannot be read or has an invalid root
	 * @throws NoSuchElementException if no source has the requested ID
	 */
	public String read_data_by_id(String id) throws IOException {
		Objects.requireNonNull(id, "id");
		ObjectNode root = files.read();
		JsonNode idTarget = mapper.valueToTree(id);

		for (JsonNode category : root.values()) {
			if (category instanceof ArrayNode entries) {
				int index = findIndex(entries, idTarget);
				if (index >= 0) {
					return prettyJson(entries.get(index));
				}
			}
		}
		throw new NoSuchElementException("No entries with ID" + id);
	}

	/**
	 * Reconstructs the stored sources and registers their IDs for the new instances.
	 *
	 * @return the reconstructed sources in document order
	 * @throws IOException if the working copy cannot be read or a source is invalid
	 */
	public List<Literatur> readAllSources() throws IOException {
		ObjectNode root = files.read();
		List<Literatur> sources = new ArrayList<>();
		idsBySource.clear();

		for (var category : root.properties()) {
			String type = category.getKey();
			if (!(category.getValue() instanceof ArrayNode entries)) {
				throw new IOException("Die Kategorie " + type + " muss eine Liste enthalten.");
			}

			for (JsonNode entry : entries) {
				if (!(entry instanceof ObjectNode object)) {
					throw new IOException("Ein Eintrag in " + type + " ist ungültig.");
				}
				try {
					Literatur source = sourceJson.fromJson(type, object);
					String id = sourceJson.readId(object);
					if (id.isBlank()) {
						throw new IOException("Ein Eintrag in " + type + " hat keine ID.");
					}
					idsBySource.put(source, id);
					sources.add(source);
				} catch (RuntimeException e) {
					throw new IOException(
							"Fehler beim Lesen eines Eintrags vom Typ " + type + ": " + e.getMessage(), e);
				}
			}
		}
		return sources;
	}

	/**
	 * Looks up the persistent ID registered for the given source instance.
	 *
	 * @param source source instance
	 * @return its ID, or {@code null} if the instance is not registered
	 */
	public String getId(Literatur source) {
		return idsBySource.get(source);
	}

	/**
	 * Finds the first entry with the requested ID.
	 *
	 * @param entries entries in the category to search
	 * @param idTarget ID represented as a JSON value
	 * @return the matching index, or {@code -1} if the ID is absent
	 */
	private int findIndex(ArrayNode entries, JsonNode idTarget) {
		for (int i = 0; i < entries.size(); i++) {
			if (idTarget.equals(entries.get(i).path("id"))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Formats a JSON value with indentation.
	 *
	 * @param data JSON value to format
	 * @return the formatted JSON text
	 * @throws tools.jackson.core.JacksonException if the value cannot be serialized
	 */
	private String prettyJson(JsonNode data) {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}

	// -------------------------------------------------------------------------------------
	// Document lifecycle

	/**
	 * Returns the sibling path used as the working copy for a bibliography.
	 *
	 * @param path path to the original bibliography
	 * @return the working copy path, with {@code .swp} appended to the file name
	 */
	public static Path workingPathFor(Path path) {
		return BibliographyFiles.workingPathFor(path);
	}

	/**
	 * Opens an existing bibliography and creates its working copy.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the original is invalid or the working copy cannot be created
	 */
	public void importJson(Path path) throws IOException {
		files.open(path);
		idsBySource.clear();
	}

	/**
	 * Opens a bibliography using its existing working copy.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the working copy is missing, unreadable or invalid
	 */
	public void recoverJson(Path path) throws IOException {
		files.recover(path);
		idsBySource.clear();
	}

	/**
	 * Replaces an existing working copy with the saved bibliography and opens it.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the original is invalid or the working copy cannot be replaced
	 */
	public void discardRecoveryAndImport(Path path) throws IOException {
		files.discardAndOpen(path);
		idsBySource.clear();
	}

	/**
	 * Creates an empty bibliography and opens it for editing.
	 *
	 * @param path path to the new bibliography
	 * @return whether the bibliography was created, or {@code false} if the path already exists
	 * @throws IOException if the original or working copy cannot be created
	 */
	public boolean createJson(Path path) throws IOException {
		if (!files.create(path)) {
			return false;
		}
		idsBySource.clear();
		return true;
	}

	/**
	 * Exports the working copy to the given destination, replacing an existing file.
	 *
	 * @param path destination path
	 * @throws IOException if the working copy cannot be read or copied to the destination
	 */
	public void exportJson(Path path) throws IOException {
		files.exportTo(path);
	}

	/**
	 * Saves the working copy to the original bibliography through a temporary file.
	 *
	 * @throws IOException if the working copy cannot be read or the original cannot be replaced
	 */
	public void save() throws IOException {
		files.save();
	}

	/**
	 * Discards the active working copy and forgets its source instances.
	 *
	 * @throws IOException if the working copy cannot be deleted
	 */
	public void deleteWorkingFile() throws IOException {
		files.deleteWorkingCopy();
		idsBySource.clear();
	}
}
