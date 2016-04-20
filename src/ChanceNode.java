import java.util.ArrayList;
import java.util.List;

public class ChanceNode extends Node{
	MinNode minNode; 
	MaxNode maxNode;  
	int dieRoll; 
	
	public ChanceNode(MaxNode maxNode, int dieRoll) {
		this.minNode=null;
		this.maxNode=maxNode;
		this.dieRoll=dieRoll;
		this.isChanceNode=true; 
		this.isMaxNode=false; 
		this.isMinNode=false;
		this.board=maxNode.board;
	}
	
	public ChanceNode(MinNode minNode, int dieRoll) {
		this.minNode=minNode;
		this.maxNode=null;
		this.dieRoll=dieRoll;
		this.isChanceNode=true; 
		this.isMaxNode=false; 
		this.isMinNode=false;
		this.board=minNode.board;
	}

	public List<Node> expand(){
		int numPlayer;
		BoardState temp; 
		
		//means that it is number 1's turn
		if(minNode==null){
			numPlayer=1;
		}
		else{
			numPlayer=2;
		}
		
		ArrayList<Integer> list = board.checkMovingPositions(numPlayer);
		ArrayList<Node> listOfPossibleBoards = new ArrayList<Node>(); 
		for(int i=0; i<list.size();i++)
		{
			int move = list.get(i);
				if(board.checkIfPieceCanMove(numPlayer, move+dieRoll))
				{
					Node node; 
					System.out.println(move);
					temp=board.copyBoardState(board);
					temp.movePiece(numPlayer, move, dieRoll);
					
					if(minNode==null){
						node= new MinNode(temp); 
					}
					else{
						node= new MaxNode(temp);
					}

					listOfPossibleBoards.add(node);
				}
		}
		return listOfPossibleBoards;
	}
	

}
