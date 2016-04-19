import java.util.ArrayList;
import java.util.List;

public class MinNode extends Node{
	MinNode parent;
	
	public MinNode(BoardState board){
		this.parent=null; 
		this.board=board; 
		this.isChanceNode=false; 
		this.isMaxNode=false; 
		this.isMinNode=true;
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
}
