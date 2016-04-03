
public class HumanAgent extends Agent {

	/*
	 * Moves the player at the passed in position to a new position, given the
	 * dice roll
	 */
	public String movePiece(int numPlayer, int pieceToMove, int diceRoll) {
		int finalSix;
		int winPiece;
		int playingPos;
		int outPieces;
		int movedIndex = -1;
		if (numPlayer == 1) {
			winPiece = Board.winPlayer1;
			finalSix = Board.finalPlayer1;
			outPieces = Board.outPlayer1;
		} else {
			winPiece = Board.winPlayer2;
			finalSix = Board.finalPlayer2;
			outPieces = Board.outPlayer2;
		}
		if (outPieces == 0) {
			playingPos = pieceToMove;
			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					if (!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll)) || !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					// take the piece from where it was
					Board.boardA[playingPos].pop();
					// check if the piece is on top of the other player's piece
					if (!Board.boardA[playingPos + diceRoll].isEmpty()
							&& Board.boardA[playingPos + diceRoll].peek() == 2) {
						Board.boardA[playingPos + diceRoll].pop();
						Board.outPlayer2++;
					}
					Board.boardA[playingPos + diceRoll].push(1);
					movedIndex = playingPos + diceRoll;
					// check if it is final six
					if (playingPos < 18 && (playingPos + diceRoll) > 17) {
						Board.finalPlayer1++;
					}
				} else if (numPlayer == 2) {
					if (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) || !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					Board.boardA[playingPos].pop();
					if (!Board.boardA[playingPos - diceRoll].isEmpty() && numPlayer == 2
							&& Board.boardA[playingPos - diceRoll].peek() == 1) {
						Board.boardA[playingPos - diceRoll].pop();
						Board.outPlayer1++;
					}
					Board.boardA[playingPos - diceRoll].push(2);
					movedIndex = playingPos - diceRoll;
					if (playingPos > 5 && (playingPos - diceRoll) < 6) {
						Board.finalPlayer2++;
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
					Board.boardA[playingPos].pop();
					if (playingPos + diceRoll > 23) {
						Board.winPlayer1++;
						Board.finalPlayer1--;
						return "-1";
					} else {
						if (!Board.boardA[playingPos + diceRoll].isEmpty()
								&& Board.boardA[playingPos + diceRoll].peek() == 2) {
							Board.boardA[playingPos + diceRoll].pop();
							Board.outPlayer2++;
						}
						Board.boardA[playingPos + diceRoll].push(1);
						movedIndex = playingPos + diceRoll;
					}
				}
				if (numPlayer == 2) {
					while ((!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) && playingPos - diceRoll >= 0)
							|| !canMove(numPlayer, pieceToMove)) {
						return "The piece chosen can not move. Please choose another piece!";
					}
					Board.boardA[playingPos].pop();
					if (playingPos - diceRoll < 0) {
						Board.winPlayer2++;
						Board.finalPlayer2--;
						return "-1";
					} else {
						if (!Board.boardA[playingPos - diceRoll].isEmpty()
								&& Board.boardA[playingPos - diceRoll].peek() == 1) {
							Board.boardA[playingPos - diceRoll].pop();
							Board.outPlayer1++;
						}
						Board.boardA[playingPos - diceRoll].push(2);
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
					if (Board.boardA[diceRoll - 1].size() != 0) {
						if (Board.boardA[diceRoll - 1].peek() == 2) {
							Board.boardA[diceRoll - 1].pop();
							Board.outPlayer2++;
							Board.finalPlayer2--;
						}
					}
					Board.boardA[diceRoll - 1].push(1);
					Board.outPlayer1--;
				} else {
					return "Sorry! You can't move any pieces on to the board";
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
				} else {
					return "Sorry! You can't move any pieces on to the board";
				}
			}
		}
		return Integer.toString(movedIndex);
	}

	@Override
	public int movePiece(int Player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
