package model.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.NoSuchElementException;

import model.Literatur;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

public class data_manager {

	private Path quellenFile = Path.of("data", "quellenDataStandard.json");

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
	public void importJson(Path path) {
		Objects.requireNonNull(path, "path");
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
}
