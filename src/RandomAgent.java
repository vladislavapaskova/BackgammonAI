import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class RandomAgent extends Agent {

	/*
	 * Moves to a the player to a random position from the possible moves it can
	 * make
	 */
	public int movePiece(int numPlayer) {
		int finalSix;
		int winPiece;
		ArrayList<Integer> availablePos;
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
		diceRoll = Board.dieRoll();
		if (outPieces == 0) {
			// check all of the pieces that the player can use
			availablePos = checkMovingPositions(numPlayer);
			// System.out.println(Board.printBoard(Board.boardA));
			arrString(availablePos);
			// System.out.println(Board.printBoard(Board.boardA));
			playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
			// we are not at the final stretch yet
			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos + diceRoll))) {
						if (Board.itterations < 15) {
							Board.itterations++;
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
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
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
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
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
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
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
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

	int count = 0;

	/*
	 * Used for reinforcement learning
	 */
	public Integer[] applyAction(int numPlayer) {
		Integer[] changes = new Integer[4];
		int start = -1;
		int end = -1;
		int finalSix;
		int winPiece;
		ArrayList<Integer> availablePos;
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
		diceRoll = Board.dieRoll();
		if (outPieces == 0) {
			availablePos = checkMovingPositions(numPlayer);
			playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					while (!checkIfPieceCanMove(1, (playingPos + diceRoll))) {
						if (Board.itterations < 15) {
							Board.itterations++;
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
						} else {
							// None of the pieces can move given the diceRoll
							Board.itterations = 0;
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = -10; // couldn't move anywhere
							changes[3] = -10;
							return changes;
						}
					}
					Board.boardA[playingPos].pop();
					start = playingPos;
					if (!Board.boardA[playingPos + diceRoll].isEmpty()
							&& Board.boardA[playingPos + diceRoll].peek() == 2) {
						if (playingPos + diceRoll < 6) {
							Board.finalPlayer2--;
						}
						Board.boardA[playingPos + diceRoll].pop();
						Board.outPlayer2++;
						changes[3] = 2;
					}
					Board.boardA[playingPos + diceRoll].push(1);
					end = playingPos + diceRoll;
					if (playingPos < 18 && (playingPos + diceRoll) > 17) {
						Board.finalPlayer1++;
					}
				} else if (numPlayer == 2) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll))) {
						if (Board.itterations < 15) {
							Board.itterations++;
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
						} else {
							Board.itterations = 0;
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = -10; // couldn't move anywhere
							changes[3] = -10;
							return changes;
						}
					}
					Board.boardA[playingPos].pop();
					start = playingPos;
					if (!Board.boardA[playingPos - diceRoll].isEmpty()
							&& Board.boardA[playingPos - diceRoll].peek() == 1) {
						if ((playingPos - diceRoll) > 17) {
							Board.finalPlayer1--;
						}
						Board.boardA[playingPos - diceRoll].pop();
						Board.outPlayer1++;
						changes[3] = 1;
					}
					Board.boardA[playingPos - diceRoll].push(2);
					end = playingPos - diceRoll;
					if (playingPos > 5 && (playingPos - diceRoll) < 6) {
						Board.finalPlayer2++;
					}
				}

			} else {
				// we need to check if there is piece from the other player
				// add an extra rule that the piece can go beyond the boundary
				// of the board
				if (numPlayer == 1) {
					while (!checkIfPieceCanMove(1, (playingPos + diceRoll)) && playingPos + diceRoll < 24) {
						if (Board.itterations < 15) {
							Board.itterations++;
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
						} else {
							Board.itterations = 0;
							break;
						}
						// System.out.println("HEREEE");
					}
					Board.boardA[playingPos].pop();
					if (playingPos + diceRoll > 23) {
						Board.winPlayer1++;
						Board.finalPlayer1--;
						if (Board.gameWon() != 0) {
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = 25; // out of board
							changes[3] = 25; // out of board
							return changes;
						} else {
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = 25; // game won
							changes[3] = 25; // game won
							return changes;
						}
					} else {
						if (!Board.boardA[playingPos + diceRoll].isEmpty()
								&& Board.boardA[playingPos + diceRoll].peek() == 2) {
							Board.boardA[playingPos + diceRoll].pop();
							Board.outPlayer2++;
							changes[3] = 2;
						}
						Board.boardA[playingPos + diceRoll].push(1);
						start = playingPos;
						end = playingPos + diceRoll;
					}
				} else if (numPlayer == 2) {
					while (!checkIfPieceCanMove(numPlayer, (playingPos - diceRoll)) && playingPos - diceRoll >= 0) {
						if (Board.itterations < 15) {
							Board.itterations++;
							playingPos = availablePos.get(0 + (int) (Math.random() * availablePos.size()));
						} else {
							Board.itterations = 0;
							break;
						}
					}
					Board.boardA[playingPos].pop();
					if (playingPos - diceRoll < 0) {
						Board.winPlayer2++;
						Board.finalPlayer2--;
						if (Board.gameWon() != 0) {
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = -2;
							changes[3] = -2;
							return changes;
						} else {
							changes[0] = outPieces;
							changes[1] = playingPos;
							changes[2] = -2; // game won
							changes[3] = -2;
							return changes;
						}
					} else {
						if (!Board.boardA[playingPos - diceRoll].isEmpty()
								&& Board.boardA[playingPos - diceRoll].peek() == 1) {
							Board.boardA[playingPos - diceRoll].pop();
							Board.outPlayer1++;
							changes[3] = 1;
						}
						Board.boardA[playingPos - diceRoll].push(2);
						start = playingPos;
						end = playingPos - diceRoll;
					}
				}
			}
		} else {
			if (numPlayer == 1) {
				// if player 1 check if we can put pieces from 1-5
				if (checkIfPieceCanMove(1, diceRoll - 1)) {
					if (Board.boardA[diceRoll - 1].size() != 0) {
						if (Board.boardA[diceRoll - 1].peek() == 2) {
							Board.boardA[diceRoll - 1].pop();
							changes[3] = 2;
							Board.outPlayer2++;
							Board.finalPlayer2--;
						}
					}
					Board.boardA[diceRoll - 1].push(1);
					end = diceRoll - 1;
					start = -1;
					Board.outPlayer1--;
				} else {
					changes[0] = outPieces;
					changes[1] = -1;
					changes[2] = -1;
					changes[3] = -1;
					return changes;

				}
			} else if (numPlayer == 2) {
				if (checkIfPieceCanMove(2, 24 - diceRoll)) {
					if (Board.boardA[24 - diceRoll].size() != 0) {
						if (Board.boardA[24 - diceRoll].peek() == 1) {
							Board.boardA[24 - diceRoll].pop();
							changes[3] = 1;
							Board.outPlayer1++;
							Board.finalPlayer1--;
						}
					}
					Board.boardA[24 - diceRoll].push(2);
					end = 24 - diceRoll;
					start = -1;
					Board.outPlayer2--;
				} else {
					changes[0] = outPieces;
					changes[1] = -1;
					changes[2] = -1;
					changes[3] = -1;
					return changes;
				}
			}

		}
		changes[0] = outPieces;
		changes[1] = start; // starting pos
		changes[2] = end; // ending pos
		if (changes[3] == null) {
			changes[3] = getEndColor(Board.boardA[end]);
		}
		return changes;

	}

	public int getOutPieces(int player) {
		if (player == 1) {
			return Board.outPlayer1;
		} else {
			return Board.outPlayer2;
		}
	}

	public String arrString(ArrayList list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i) + " , ";
		}
		return str;
	}

	public int getEndColor(Stack<Integer> stack) {
		if (stack.size() == 1) {
			return 0;
		} else {
			return stack.peek();
		}
	}

	@Override
	public String movePiece(int Player, int position, int dieRoll) {
		// TODO Auto-generated method stub
		return null;
	}

}
