import java.util.ArrayList;
import java.util.List;

public class ChanceNode extends Node{
	MinNode minNode; 
	MaxNode maxNode;  
	int dieRoll; 
	public boolean isChanceNode; 
	public boolean isMaxNode; 
	public boolean isMinNode; 
	
	public ChanceNode(MaxNode maxNode, int dieRoll) {
		this.minNode=null;
		this.maxNode=maxNode;
		this.dieRoll=dieRoll;
		this.isChanceNode=true; 
		this.isMaxNode=false; 
		this.isMinNode=false; 
	}
	
	public ChanceNode(MinNode minNode, int dieRoll) {
		this.minNode=minNode;
		this.maxNode=null;
		this.dieRoll=dieRoll;
		this.isChanceNode=true; 
		this.isMaxNode=false; 
		this.isMinNode=false; 
	}

	public List<Node> expand(){
		int numPlayer;
		Board board; 
		
		//means that it is number 1's turn
		if(minNode==null){
			numPlayer=1;
			board=maxNode.board;
		}
		else{
			numPlayer=2;
			board=minNode.board;
		}
		
		ArrayList<Integer> list = board.checkMovingPositions(numPlayer);
		ArrayList<Node> listOfPossibleBoards = new ArrayList<Node>(); 
		for(int i=0; i<list.size();i++)
		{
			int move = list.get(i);
				if(board.checkIfPieceCanMove(numPlayer, move+dieRoll))
				{
					Node node; 
					//here the board needs to be copied
					board.boardA[move].pop();
					board.boardA[move+dieRoll].push(numPlayer);
					
					if(minNode==null){
						node= new MinNode(board); 
					}
					else{
						node= new MaxNode(board);
					}

					listOfPossibleBoards.add(node);
				}
		}
		return listOfPossibleBoards;
	}
	

}
