package model.data;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 * Manages the original bibliography and its editable JSON working copy.
 */
final class BibliographyFiles {

	/**
	 * Jackson mapper used for reading and writing the bibliography.
	 */
	private final ObjectMapper mapper;
	/**
	 * Path to the saved version of the active bibliography.
	 */
	private Path original = Path.of("data", "quellenDataStandard.json");
	/**
	 * Path to the editable sibling file for the active bibliography.
	 */
	private Path workingCopy = workingPathFor(original);
	/**
	 * Whether the working copy has been created or selected for this document.
	 */
	private boolean workingCopyInitialized;

	/**
	 * Creates the file handler for the default bibliography.
	 *
	 * @param mapper Jackson mapper used to read and write file contents
	 */
	BibliographyFiles(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	// -------------------------------------------------------------------------------------
	// Open, create and recover

	/**
	 * Derives the path to a bibliography's working copy.
	 *
	 * @param path path to the original bibliography
	 * @return the sibling path with {@code .swp} appended to the file name
	 */
	static Path workingPathFor(Path path) {
		return path.resolveSibling(path.getFileName() + ".swp");
	}

	/**
	 * Validates and opens an existing bibliography with a fresh working copy.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the original is unreadable or has an invalid root, or the working
	 *     copy cannot be created
	 */
	void open(Path path) throws IOException {
		validateJson(path);
		activateFile(path);
	}

	/**
	 * Opens the document using an existing, valid working copy.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the working copy is missing, unreadable, empty or has an invalid root
	 */
	void recover(Path path) throws IOException {
		Path workingPath = workingPathFor(path);
		validateJson(workingPath);
		selectFiles(path, workingPath);
	}

	/**
	 * Replaces the working copy with the saved bibliography and opens it.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the original cannot be validated or the working copy cannot be
	 *     replaced
	 */
	void discardAndOpen(Path path) throws IOException {
		validateJson(path);
		Path workingPath = workingPathFor(path);
		Files.copy(path, workingPath, StandardCopyOption.REPLACE_EXISTING);
		selectFiles(path, workingPath);
	}

	/**
	 * Creates and opens an empty bibliography.
	 *
	 * @param path path to the new bibliography
	 * @return {@code true} if created and opened, or {@code false} if the original already exists
	 * @throws IOException if the original or its working copy cannot be created
	 */
	boolean create(Path path) throws IOException {
		Objects.requireNonNull(path, "path");
		createParentDirectories(path);
		try {
			Files.writeString(path, "{}", StandardOpenOption.CREATE_NEW);
		} catch (FileAlreadyExistsException e) {
			return false;
		}
		activateFile(path);
		return true;
	}

	/**
	 * Validates that the file is readable and contains a nonempty document with a JSON object as its
	 * root.
	 *
	 * @param path file to validate
	 * @throws IOException if the file is unreadable, empty or its root is not a JSON object
	 * @throws tools.jackson.core.JacksonException if the contents cannot be parsed as JSON
	 */
	private void validateJson(Path path) throws IOException {
		Objects.requireNonNull(path, "path");
		if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
			throw new IOException("Die ausgewählte Datei kann nicht gelesen werden.");
		}
		if (Files.size(path) == 0) {
			throw new IOException("Die JSON-Datei ist leer.");
		}
		if (!(mapper.readTree(path.toFile()) instanceof ObjectNode)) {
			throw new IOException("Die JSON-Datei muss ein JSON-Objekt enthalten.");
		}
	}

	/**
	 * Creates a working copy before switching to the given document.
	 *
	 * @param path path to the original bibliography
	 * @throws IOException if the original cannot be copied to the working path
	 */
	private void activateFile(Path path) throws IOException {
		Path workingPath = workingPathFor(path);
		Files.copy(path, workingPath);
		selectFiles(path, workingPath);
	}

	/**
	 * Marks the original and its ready working copy as the active document.
	 *
	 * @param path path to the original bibliography
	 * @param workingPath path to the prepared working copy
	 */
	private void selectFiles(Path path, Path workingPath) {
		original = path;
		workingCopy = workingPath;
		workingCopyInitialized = true;
	}

	/**
	 * Recovers or creates the working copy when the document is first used.
	 *
	 * @throws IOException if the working copy cannot be created or recovered
	 */
	private void ensureWorkingCopy() throws IOException {
		if (workingCopyInitialized) {
			return;
		}
		createParentDirectories(workingCopy);
		if (Files.exists(workingCopy)) {
			recover(original);
			return;
		}
		if (Files.exists(original)) {
			Files.copy(original, workingCopy);
		} else {
			Files.writeString(workingCopy, "{}", StandardOpenOption.CREATE_NEW);
		}
		workingCopyInitialized = true;
	}

	// -------------------------------------------------------------------------------------
	// Read, write and persist

	/**
	 * Parses the working copy, initializing it on first use.
	 *
	 * @return the JSON root object, or an empty object if the working file is empty
	 * @throws IOException if the working copy cannot be prepared or read, or its root is not a
	 *     JSON object
	 * @throws tools.jackson.core.JacksonException if the contents cannot be parsed as JSON
	 */
	ObjectNode read() throws IOException {
		ensureWorkingCopy();
		if (Files.size(workingCopy) == 0) {
			return mapper.createObjectNode();
		}
		JsonNode data = mapper.readTree(workingCopy.toFile());
		if (data instanceof ObjectNode root) {
			return root;
		}
		throw new IOException("the JSON file has to contain a root object" + workingCopy);
	}

	/**
	 * Writes the updated root to the working copy after it has been opened or read.
	 *
	 * @param root complete bibliography to write as JSON
	 * @throws tools.jackson.core.JacksonException if the root cannot be serialized or written
	 */
	void write(ObjectNode root) {
		mapper.writerWithDefaultPrettyPrinter().writeValue(workingCopy.toFile(), root);
	}

	/**
	 * Copies the current working copy to an export destination.
	 *
	 * @param path destination file, which will be replaced if it exists
	 * @throws IOException if the working copy cannot be prepared or copied to the destination
	 */
	void exportTo(Path path) throws IOException {
		Objects.requireNonNull(path, "path");
		ensureWorkingCopy();
		createParentDirectories(path);
		Files.copy(workingCopy, path, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Saves through a temporary sibling file, using an atomic move when supported.
	 *
	 * @throws IOException if the working copy cannot be prepared or copied, or the original cannot
	 *     be replaced
	 */
	void save() throws IOException {
		ensureWorkingCopy();
		Path temp = Path.of(workingCopy.toString() + ".tmp");
		Files.copy(workingCopy, temp, StandardCopyOption.REPLACE_EXISTING);
		try {
			Files.move(temp, original, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		} catch (AtomicMoveNotSupportedException e) {
			Files.move(temp, original, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Removes the working copy so it can be initialized again when needed.
	 *
	 * @throws IOException if the working copy cannot be deleted
	 */
	void deleteWorkingCopy() throws IOException {
		Files.deleteIfExists(workingCopy);
		workingCopyInitialized = false;
	}

	/**
	 * Creates the parent directory when the path has one.
	 *
	 * @param path file path whose parent directory should be created
	 * @throws IOException if the parent directory cannot be created
	 */
	private void createParentDirectories(Path path) throws IOException {
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
	}
}
