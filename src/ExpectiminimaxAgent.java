import java.util.ArrayList;
import java.util.List;

public class ExpectiminimaxAgent extends Agent {

	int MAX_DEPTH=6;

	
	@Override
	public int movePiece(int numPlayer) {
		int finalSix;
		int winPiece;
		int playingPos;
		int outPieces;
		int diceRoll;
		if (numPlayer == 1) {
			winPiece = Board.winPlayer1;
			finalSix = Board.finalPlayer1;
			outPieces = Board.outPlayer1;
		} else {
			winPiece = Board.winPlayer2;
			finalSix = Board.finalPlayer2;
			outPieces = Board.outPlayer2;
		}
		// set the dice roll
		BoardState bs = new BoardState(Board.boardA, Board.outPlayer1, Board.outPlayer2, Board.winPlayer1,
				Board.winPlayer2, Board.finalPlayer1, Board.finalPlayer2);
		diceRoll = Board.dieRoll();
		playingPos = move(bs, diceRoll);
		if (outPieces == 0) {
			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll))) {
						if (Board.itterations < 15) {
							Board.itterations++;
						} else {
							Board.itterations = 0;
							return -1;
						}
					}
					// take the piece from where it was
					Board.boardA[playingPos].pop();
					// check if the piece is on top of the other player's piece
					if (!Board.boardA[playingPos + diceRoll].isEmpty()
							&& Board.boardA[playingPos + diceRoll].peek() == 2) {
						if (playingPos + diceRoll < 6) {
							Board.finalPlayer2--;
						}
						Board.boardA[playingPos + diceRoll].pop();
						Board.outPlayer2++;
					}
					Board.boardA[playingPos + diceRoll].push(1);
					// check if it is final six
					if (playingPos < 18 && (playingPos + diceRoll) > 17) {
						Board.finalPlayer1++;
					}
				} else if (numPlayer == 2) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll))) {
						if (Board.itterations < 15) {
							Board.itterations++;
						} else {
							Board.itterations = 0;
							return -1;
						}
					}
					Board.boardA[playingPos].pop();
					if (!Board.boardA[playingPos - diceRoll].isEmpty() && numPlayer == 2
							&& Board.boardA[playingPos - diceRoll].peek() == 1) {
						if ((playingPos - diceRoll) > 17) {
							Board.finalPlayer1--;
						}
						Board.boardA[playingPos - diceRoll].pop();
						Board.outPlayer1++;
					}
					Board.boardA[playingPos - diceRoll].push(2);
					if (playingPos > 5 && (playingPos - diceRoll) < 6) {
						Board.finalPlayer2++;
					}
				}
			} else {
				// we need to check if there is piece from the other player
				// add an extra rule that the piece can go beyond the boundary
				// of the board
				if (numPlayer == 1) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll)) && playingPos + diceRoll < 24) {
						if (Board.itterations < 15) {
							Board.itterations++;
						} else {
							Board.itterations = 0;
							return -1;
						}
					}
					Board.boardA[playingPos].pop();
					if (playingPos + diceRoll > 23) {
						Board.winPlayer1++;
						Board.finalPlayer1--;
						return diceRoll;
					} else {
						if (!Board.boardA[playingPos + diceRoll].isEmpty()
								&& Board.boardA[playingPos + diceRoll].peek() == 2) {
							Board.boardA[playingPos + diceRoll].pop();
							Board.outPlayer2++;
						}
						Board.boardA[playingPos + diceRoll].push(1);
					}
				}
				if (numPlayer == 2) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) && playingPos - diceRoll >= 0) {
						if (Board.itterations < 15) {
							Board.itterations++;
						} else {
							Board.itterations = 0;
							return -1;
						}
					}
					Board.boardA[playingPos].pop();
					if (playingPos - diceRoll < 0) {
						Board.winPlayer2++;
						Board.finalPlayer2--;
						return diceRoll;
					} else {
						if (!Board.boardA[playingPos - diceRoll].isEmpty()
								&& Board.boardA[playingPos - diceRoll].peek() == 1) {
							Board.boardA[playingPos - diceRoll].pop();
							Board.outPlayer1++;
						}
						Board.boardA[playingPos - diceRoll].push(2);
					}
				}
			}
		}
		// we have pieces out of the board
		else {
			// if player 1 check if we can put pieces from 1-5
			if (numPlayer == 1) {
				if (checkIfPieceCanMove(1, diceRoll - 1)) {
					if (Board.boardA[diceRoll - 1].size() != 0) {
						if (Board.boardA[diceRoll - 1].peek() == 2) {
							Board.boardA[diceRoll - 1].pop();
							Board.outPlayer2++;
							Board.finalPlayer2--;
						}
					}
					Board.boardA[diceRoll - 1].push(1);
					Board.outPlayer1--;
				}
			}
			// if player 2 check if we can put pieces from 18-23
			if (numPlayer == 2) {
				if (checkIfPieceCanMove(2, 24 - diceRoll)) {
					if (Board.boardA[24 - diceRoll].size() != 0) {
						if (Board.boardA[24 - diceRoll].peek() == 1) {
							Board.boardA[24 - diceRoll].pop();
							Board.outPlayer1++;
							Board.finalPlayer1--;
						}
					}
					Board.boardA[24 - diceRoll].push(2);
					Board.outPlayer2--;
				}
			}
		}
		return diceRoll;
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
		System.out.println("final move"+finalMove);
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
