import java.util.ArrayList;
import java.util.List;

public class ExpectiminimaxAgent extends Agent {

	int MAX_DEPTH=6;

	
	@Override
	public int movePiece(int Player) {
		BoardState bs = new BoardState(Board.boardA, Board.outPlayer1, Board.outPlayer2, Board.winPlayer1, Board.winPlayer2, Board.finalPlayer1, Board.finalPlayer2);
		return move(bs, Board.dieRoll());
	} 

	
	public int move(BoardState board, int dieRoll){
		int numPlayer=1;
		ArrayList<Integer> list = board.checkMovingPositions(numPlayer);
		
		Node tempNode;
		BoardState tempBoard; 
		int tempMove; 
		int finalMove=0; 
		Node bestNode=null; 
		
		double maxVal = Double.NEGATIVE_INFINITY;
		double tempVal; 
		
		for(int i=0; i<list.size();i++)
		{
			
			int move = list.get(i);
				if(board.checkIfPieceCanMove(numPlayer, move+dieRoll))
				{
					tempBoard=board.copyBoardState(board);
					tempBoard.movePiece(numPlayer, move, dieRoll);
					tempMove=move; 
			
					tempNode=new MaxNode(tempBoard); 
					tempVal = minimaxValue(tempNode,1);

					
					if(tempVal > maxVal)
					{
						bestNode = tempNode;
						maxVal = tempVal;
						finalMove=tempMove; 
					}
				}
		}
		//System.out.println("final move"+finalMove);
		return finalMove; 
	}
	
	/*
	 * computing minimax value
	 */
	public double minimaxValue(Node n, int depth){
		//System.out.println(n.board.outPlayer1);
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
				v+=minimaxValue(list.get(i), depth+1)/list.size(); 
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
	public double heuristic1(BoardState board, int numPlayer)
	{
		double heuristic=0.0; 
		if(numPlayer==1)
		{
			heuristic+=board.finalPlayer1*50; 
			heuristic+=board.outPlayer2*10; 
		}
		else
		{
			heuristic+=board.finalPlayer2*50;
			heuristic+=board.outPlayer1*10; 
		}
		return heuristic; 
	}
	
	@Override
	public String movePiece(int Player, int position, int dieRoll) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
