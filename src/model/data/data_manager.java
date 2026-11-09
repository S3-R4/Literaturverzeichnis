package model.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

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

	public String read_data() {
		Object Jsonobject = mapper.readValue(quellenFile, Object.class);
		String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Jsonobject);
		return prettyJson;
	}

	// This function checks if the file already exists and if it doesnt, creates a
	// new one
	private ObjectNode loadData() throws IOException {
		if (Files.notExists(quellenFile)) {
			return mapper.createObjectNode();
		}
		JsonNode data = mapper.readTree(quellenFile.toFile());
		if (data instanceof ObjectNode root) {
			return root;
		}
		throw new IOException("the JSON file has to contain a root object" + quellenFile);
	}

}
