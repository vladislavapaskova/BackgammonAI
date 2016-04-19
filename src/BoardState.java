import java.util.ArrayList;
import java.util.Stack;

public class BoardState {
	public Stack<Integer>[] gameBoard;
	
	// pieces out of the board
	public int outPlayer1;
	public int outPlayer2;

	// pieces that the player has managed to get out;count towards winning
	public int winPlayer1;
	public int winPlayer2;

	// pieces in final six lanes; we count those to see if we can start taking
	// pieces out towards winning
	public int finalPlayer1;
	public int finalPlayer2;
	
	public BoardState(Stack<Integer>[] board,int outPlayer1,int outPlayer2,int winPlayer1,int winPlayer2,int finalPlayer1,int finalPlayer2  ){
		gameBoard=copyStack(board);
		this.outPlayer1=outPlayer1;
		this.outPlayer2=outPlayer2;
		this.winPlayer1=winPlayer1;
		this.winPlayer2=winPlayer2;
		this.finalPlayer1=finalPlayer1;
		this.finalPlayer2=finalPlayer2;
		
	}
	
	public BoardState(Stack<Integer>[] stack){
		this.gameBoard=stack; 
	}
	
	public BoardState copyBoardState(BoardState board){
		
		Stack<Integer>[] stack=copyStack(board.gameBoard);
		BoardState newBS= new BoardState(stack); 
		newBS.outPlayer1=board.outPlayer1;
		newBS.outPlayer2=board.outPlayer2;
		newBS.winPlayer1=board.winPlayer1;
		newBS.winPlayer2=board.winPlayer2;
		newBS.finalPlayer1=board.finalPlayer1;
		newBS.finalPlayer2=board.finalPlayer2;
		
		return newBS; 
	}
	
	public String movePiece(int numPlayer, int pieceToMove, int diceRoll) {

		int finalSix;
		int winPiece;
		int playingPos;
		int outPieces;
		int movedIndex = -1;

		if (numPlayer == 1) {
			winPiece = winPlayer1;
			finalSix = finalPlayer1;
			outPieces = outPlayer1;

		} else {
			winPiece = winPlayer2;
			finalSix = finalPlayer2;
			outPieces = outPlayer2;
		}

		if (outPieces == 0) {

			playingPos = pieceToMove;

			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					if (!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll)) || !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					// take the piece from where it was
					System.out.println(playingPos);
					gameBoard[playingPos].pop();
					// check if the piece is on top of the other player's piece
					if (!gameBoard[playingPos + diceRoll].isEmpty() && gameBoard[playingPos + diceRoll].peek() == 2) {
						gameBoard[playingPos + diceRoll].pop();
						outPlayer2++;
					}
					gameBoard[playingPos + diceRoll].push(1);
					movedIndex = playingPos + diceRoll;

					// check if it is final six
					if (playingPos < 18 && (playingPos + diceRoll) > 17) {
						finalPlayer1++;
					}
				} else if (numPlayer == 2) {
					if (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) || !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					gameBoard[playingPos].pop();
					if (!gameBoard[playingPos - diceRoll].isEmpty() && numPlayer == 2
							&& gameBoard[playingPos - diceRoll].peek() == 1) {
						gameBoard[playingPos - diceRoll].pop();
						outPlayer1++;
					}

					// fix this
					gameBoard[playingPos - diceRoll].push(2);
					movedIndex = playingPos - diceRoll;
					if (playingPos > 5 && (playingPos - diceRoll) < 6) {
						finalPlayer2++;
					}
				}

			} else {
				// we need to check if there is piece from the other player
				// add an extra rule that the piece can go beyond the boundary
				// of the board
				if (numPlayer == 1) {
					if ((!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll)) && playingPos + diceRoll < 23)
							|| !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					gameBoard[playingPos].pop();

					if (playingPos + diceRoll > 23) {
						winPlayer1++;
						finalPlayer1--;
						return "-1";
					} else {
						if (!gameBoard[playingPos + diceRoll].isEmpty() && gameBoard[playingPos + diceRoll].peek() == 2) {
							gameBoard[playingPos + diceRoll].pop();
							outPlayer2++;
						}
						gameBoard[playingPos + diceRoll].push(1);
						movedIndex = playingPos + diceRoll;
					}

				}
				if (numPlayer == 2) {
					while ((!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) && playingPos - diceRoll >= 0)
							|| !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					gameBoard[playingPos].pop();

					if (playingPos - diceRoll < 0) {
						winPlayer2++;
						finalPlayer2--;
						return "-1";
					} else {
						if (!gameBoard[playingPos - diceRoll].isEmpty() && gameBoard[playingPos - diceRoll].peek() == 1) {
							gameBoard[playingPos - diceRoll].pop();
							outPlayer1++;
						}
						gameBoard[playingPos - diceRoll].push(2);
						movedIndex = playingPos - diceRoll;
					}

				}
			}
		}
		// we have pieces out of the board
		else {
			// if player 1 check if we can put pieces from 1-5
			if (numPlayer == 1) {
				if (checkIfPieceCanMove(1, diceRoll - 1)) {
					if (gameBoard[diceRoll - 1].size() != 0) {
						if (gameBoard[diceRoll - 1].peek() == 2) {
							gameBoard[diceRoll - 1].pop();
							outPlayer2++;
							finalPlayer2--;
						}
					}
					gameBoard[diceRoll - 1].push(1);
					outPlayer1--;
				} else {
					return "Sorry! You can't move any pieces on to the board";
				}
			}
			// if player 2 check if we can put pieces from 18-23
			if (numPlayer == 2) {
				if (checkIfPieceCanMove(2, 24 - diceRoll)) {
					if (gameBoard[24 - diceRoll].size() != 0) {
						if (gameBoard[24 - diceRoll].peek() == 1) {
							gameBoard[24 - diceRoll].pop();
							outPlayer1++;
							finalPlayer1--;
						}
					}
					gameBoard[24 - diceRoll].push(2);
					outPlayer2--;
				} else {
					return "Sorry! You can't move any pieces on to the board";
				}
			}
		}
		return Integer.toString(movedIndex);

	}

	/*
	 * checks who won the game
	 */
	public int gameWon() {
		if (winPlayer1 == 15)
			return 1;
		if (winPlayer2 == 15)
			return 2;
		else
			return 0;
	}

	
	/*
	 * Returns a list of the possible pieces that can be moved
	 */
	public ArrayList<Integer> checkMovingPositions(int numPlayer) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < gameBoard.length; i++) {
			if (canMove(numPlayer, i)) {
				list.add(i);
			}
		}
		return list;

	}

	/*
	 * Checks if a piece can move from this position
	 */
	public boolean canMove(int numPlayer, int col) {
		if (gameBoard[col].isEmpty() || gameBoard[col].peek() != numPlayer) {
			return false;
		}
		return true;
	}

	/*
	 * Checks if the piece can move to the specified position
	 */
	public boolean checkIfPieceCanMove(int numPlayer, int col) {
		if (col < gameBoard.length && col >= 0) {
			if (gameBoard[col].isEmpty() || gameBoard[col].peek() == numPlayer)
				return true;
			else {
				if (gameBoard[col].size() == 1) {
					return true;
				} else {
					return false;
				}
			}
		} else
			return false;
	}
	
	public int playersOut() {
		if (outPlayer1 > 0) {
			return 1;
		} else if (outPlayer2 > 0) {
			return 2;
		} else {
			return 0;
		}
	}
	
	/*
	 * Copy contents of a stack array into a new stack array
	 */
	public Stack<Integer>[] copyStack(Stack<Integer>[] board) {
		Stack<Integer>[] copy = new Stack[24];
		Stack<Integer>[] temp = new Stack[24];
		for (int i = 0; i < board.length; i++) {
			temp[i] = new Stack();
		}
		for (int i = 0; i < board.length; i++) {
			Stack<Integer> stack = board[i];
			int size = stack.size();
			for (int j = 0; j < size; j++) {
				temp[i].push(stack.pop());
			}

		}
		for (int i = 0; i < board.length; i++) {
			copy[i] = new Stack();
		}
		for (int i = 0; i < board.length; i++) {
			Stack<Integer> stack = temp[i];
			int size = stack.size();
			for (int j = 0; j < size; j++) {
				copy[i].push(stack.peek());
				board[i].push(stack.pop());
			}
		}
		return copy;
	}

}
