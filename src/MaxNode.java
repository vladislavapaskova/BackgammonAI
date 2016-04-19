import java.util.List;
import java.util.ArrayList;

/*
 * creates a max node
 */
public class MaxNode extends Node{
	MaxNode parent;
	Board board;
	public boolean isChanceNode; 
	public boolean isMaxNode; 
	public boolean isMinNode; 
	
	public MaxNode(Board board){
		this.parent=null; 
		this.board=board;
		this.isChanceNode=false; 
		this.isMaxNode=true; 
		this.isMinNode=false; 
	}
	
	public MaxNode(MaxNode parent, Board board){
		this.parent=parent; 
		this.board=board; 
		this.isChanceNode=false; 
		this.isMaxNode=true; 
		this.isMinNode=false;
	}
	
	//expand to a chance node meaning the chance of rolling a 1...6 
	public List<Node> expand(){
		ArrayList<Node> list = new ArrayList<Node>();
		for(int i=1; i<=6; i++){
			ChanceNode node = new ChanceNode(this, i);
			list.add(node);
		}
		return list; 
	}
	
	public Boolean isMaxNode(){
		return true; 
	}
}