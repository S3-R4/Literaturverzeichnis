package model.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Objects;
import java.util.UUID;
import java.util.NoSuchElementException;

import model.Literatur;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import model.Literatur;

public class data_manager {

	private final Path quellenFile = Path.of("data", "quellenData.json");

	private final ObjectMapper mapper = JsonMapper.builder()
			.changeDefaultVisibility(visibility -> visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
			.build();

	// -------------------------------------------------------------------------------------
	// adds the entry to the json file
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
	// This function checks if the file already exists and creates it if not
	// returns the data as ObjectNode
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
}
