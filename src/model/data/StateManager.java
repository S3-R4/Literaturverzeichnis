package model.data;

import java.util.ArrayDeque;
import java.util.Deque;
import tools.jackson.databind.node.ObjectNode;

public class StateManager {
	private final Deque<ObjectNode> undoStack = new ArrayDeque<>();
	private final Deque<ObjectNode> redoStack = new ArrayDeque<>();

	void recordChange(ObjectNode before){
		undoStack.push(before);
		redoStack.clear();
	}
}
