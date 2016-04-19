import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Minimax {
	
	ExpectiminimaxAgent agent;  
	int MAX_DEPTH=4; 

	
	public Board move(Board board, int dieRoll){
		int numPlayer=1;
		ArrayList<Integer> list = board.checkMovingPositions(numPlayer);
		
		MaxNode tempNode; 
		MaxNode bestNode=null; 
		
		double maxVal = Double.NEGATIVE_INFINITY;
		double tempVal; 
		
		for(int i=0; i<list.size();i++)
		{
			int move = list.get(i);
				if(board.checkIfPieceCanMove(numPlayer, move+dieRoll))
				{
					//here the board needs to be copied
					board.boardA[move].pop();
					board.boardA[move+dieRoll].push(numPlayer);
				}
				tempNode=new MaxNode(board); 
				tempVal = minimaxValue(tempNode,1);
				
				if(tempVal > maxVal)
				{
					bestNode = tempNode;
					maxVal = tempVal;
				}
		}
		
		return bestNode.board; 
	}
	
	/*
	 * computing minimax value
	 */
	public double minimaxValue(Node n, int depth){
		if(depth == MAX_DEPTH)
		{
			return heuristic1(n.board, 1);
		}

		if(n.isChanceNode==true) // chance node
		{
			double v = 0.0;
			List<Node> list = new ArrayList<Node>(); 
			list = n.expand(); 
			
			for(int i=0; i<list.size(); i++){
				v+=minimaxValue(list.get(i), depth)/list.size(); 
			}
			
			return v;
			
		}
		else if(n.isMinNode==true) // min node
		{
			double v = Double.POSITIVE_INFINITY;
			List<Node> list = new ArrayList<Node>(); 
			list = n.expand(); 
			
			for(int i=0; i<list.size(); i++){
				v=Math.min(v, minimaxValue(list.get(i), depth+1));
			}

			return v;
		}
		else
		{
			double v = Double.NEGATIVE_INFINITY;
			List<Node> list = new ArrayList<Node>(); 
			list = n.expand(); 
			
			for(int i=0; i<list.size(); i++){
				v=Math.max(v, minimaxValue(list.get(i), depth+1));
			}

			return v;
		}
	}
	
	
	/*
	 * This strategy tries to take out players pieces
	 */
	public double heuristic1(Board b, int numPlayer)
	{
		double heuristic=0; 
		if(numPlayer==1)
		{
			heuristic=b.outPlayer2*10; 
		}
		else
		{
			heuristic=b.outPlayer1*10; 
		}
		return heuristic; 
	}
	
}
