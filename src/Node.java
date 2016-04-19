import java.util.ArrayList;
import java.util.List;

public abstract class Node {

	public Board board;
	public boolean isChanceNode;
	public boolean isMinNode;
	public boolean isMaxNode;
	
	public List<Node> expand() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/*
	Node parent;
	Board board;
	int totaldepth; 
	int dieRoll; 

	public Node(int dieRoll) {
		this.parent = null;
		this.board = new Board();
		this.dieRoll=dieRoll; 
	}
	
	public Node(Node parent, Board board, int dieRoll) {
		this.parent=parent;
		this.board=board;
		this.dieRoll=dieRoll;
	}
	
	public Node(Node parent, Board board) {
		this.parent=parent;
		this.board=board;
	}
	
	public String toString() {
		return board.printBoard();
	}

	public List<Node> expand(int numPlayer) {
		ArrayList<Integer> list = board.checkMovingPositions(numPlayer);
		ArrayList<Node> listOfPossibleBoards = new ArrayList<Node>(); 
		for(int i=0; i<list.size();i++)
		{
			int move = list.get(i);
				if(board.checkIfPieceCanMove(numPlayer, move+dieRoll))
				{
					//here the board needs to be copied
					board.boardA[move].pop();
					board.boardA[move+dieRoll].push(numPlayer);
					Node newnode=new Node(this, board, this.dieRoll);
					listOfPossibleBoards.add(newnode);
				}
		}
		return listOfPossibleBoards;
	}
	
	public int depth(Node n) 
	{
		int count = 0;
		while (n.parent != null) {
			n = n.parent;
			count++;
		}
		return count;
	}
	*/
}