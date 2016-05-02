import java.util.ArrayList;
import java.util.List;

public abstract class Node {

	public BoardState board;
	public boolean isChanceNode;
	public boolean isMinNode;
	public boolean isMaxNode;
	
	public abstract List<Node> expand();
	
}