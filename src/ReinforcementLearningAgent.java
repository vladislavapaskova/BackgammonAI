import java.util.ArrayList;

public class ReinforcementLearningAgent extends Agent {

	ReinforcementLearner learner;
	String key;

	public ReinforcementLearningAgent(int numPlayer) {
		learner = new ReinforcementLearner(numPlayer);
		System.out.println("DONE LEARNING!");
	}

	@Override
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
		diceRoll = Board.dieRoll();
		if (outPieces == 0) {
			if ((finalSix + winPiece) != 15) {
				availablePos = piecesThatCanMove(numPlayer, diceRoll);
			} else {
				availablePos = piecesThatCanMoveOffBoard(numPlayer, diceRoll);
			}
			if (availablePos.isEmpty()) {
				return -1;
			}
			Double best = Double.NEGATIVE_INFINITY;
			int bestPieceToMove = availablePos.get(0);
			Integer end;
			Integer endColor;
			for (int i = 0; i < availablePos.size(); i++) {
				Integer pos = availablePos.get(i);
				if (numPlayer == 1) {
					end = pos + diceRoll;
					if (end > 23) {
						end = 25;
					}
					if (end < 24) {
						if (Board.boardA[end].size() == 0) {
							endColor = 0;
						} else {
							endColor = Board.boardA[end].peek();
						}
					} else {
						endColor = 25;
					}
				} else {
					end = pos - diceRoll;
					if (end < 0) {
						end = -2;
					}
					if (end >= 0) {
						if (Board.boardA[end].size() == 0) {
							endColor = 0;
						} else {
							endColor = Board.boardA[end].peek();
						}
					} else {
						endColor = -2;
					}
				}
				key = pos.toString() + "," + end.toString() + "," + endColor.toString();
				Double temp = best.doubleValue();
				if (learner.states.get(key) != null) {
					best = java.lang.Math.max(learner.states.get(key), best);
				}
				if (temp.doubleValue() != best.doubleValue()) {
					bestPieceToMove = availablePos.get(i);
				}
			}
			playingPos = bestPieceToMove;
			if ((finalSix + winPiece) != 15) {
				if (numPlayer == 1) {
					Board.boardA[playingPos].pop();
					if (!Board.boardA[playingPos + diceRoll].isEmpty()
							&& Board.boardA[playingPos + diceRoll].peek() == 2) {
						if (playingPos + diceRoll < 6) {
							Board.finalPlayer2--;
						}
						Board.boardA[playingPos + diceRoll].pop();
						Board.outPlayer2++;
					}
					Board.boardA[playingPos + diceRoll].push(1);
					if (playingPos < 18 && (playingPos + diceRoll) > 17) {
						Board.finalPlayer1++;
					}
				} else if (numPlayer == 2) {
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
				if (numPlayer == 1) {
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
		} else {
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

	public ArrayList<Integer> checkMovingPositions(int numPlayer) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < Board.BOARDSIZE; i++) {
			if (canMove(numPlayer, i)) {
				list.add(i);
			}
		}
		return list;

	}

	public ArrayList<Integer> piecesThatCanMove(int numPlayer, int diceRoll) {
		ArrayList<Integer> movingPos = checkMovingPositions(numPlayer);
		ArrayList<Integer> piecesThatCanMove = new ArrayList<Integer>();
		if (numPlayer == 1) {
			for (int i = 0; i < movingPos.size(); i++) {
				if (movingPos.get(i) + diceRoll < 24) {
					if (checkIfPieceCanMove(numPlayer, movingPos.get(i) + diceRoll)) {
						piecesThatCanMove.add(movingPos.get(i));
					}
				}
			}
		} else {
			for (int i = 0; i < movingPos.size(); i++) {
				if (movingPos.get(i) - diceRoll >= 0) {
					if (checkIfPieceCanMove(numPlayer, movingPos.get(i) - diceRoll)) {
						piecesThatCanMove.add(movingPos.get(i));
					}
				}
			}
		}
		return piecesThatCanMove;
	}

	public ArrayList<Integer> piecesThatCanMoveOffBoard(int numPlayer, int diceRoll) {
		ArrayList<Integer> movingPos = checkMovingPositions(numPlayer);
		ArrayList<Integer> piecesThatCanMove = new ArrayList<Integer>();
		if (numPlayer == 1) {
			for (int i = 0; i < movingPos.size(); i++) {
				if (movingPos.get(i) + diceRoll < 24) {
					if (checkIfPieceCanMove(numPlayer, movingPos.get(i) + diceRoll)) {
						piecesThatCanMove.add(movingPos.get(i));
					}
				} else {
					piecesThatCanMove.add(movingPos.get(i));
				}
			}
		} else {
			for (int i = 0; i < movingPos.size(); i++) {
				if (movingPos.get(i) - diceRoll >= 0) {
					if (checkIfPieceCanMove(numPlayer, movingPos.get(i) - diceRoll)) {
						piecesThatCanMove.add(movingPos.get(i));
					}
				} else {
					piecesThatCanMove.add(movingPos.get(i));
				}
			}
		}
		return piecesThatCanMove;
	}

	/*
	 * Checks if a piece can move from this position
	 */
	public boolean canMove(int numPlayer, int col) {
		if (Board.boardA[col].isEmpty() || Board.boardA[col].peek() != numPlayer) {
			return false;
		}
		return true;
	}

	/*
	 * Checks if the piece can move to the specified position
	 */
	public boolean checkIfPieceCanMove(int numPlayer, int col) {
		if (col < Board.boardA.length && col >= 0) {
			if (Board.boardA[col].isEmpty() || Board.boardA[col].peek() == numPlayer)
				return true;
			else {
				if (Board.boardA[col].size() == 1) {
					return true;
				} else {
					return false;
				}
			}
		} else
			return false;
	}

	@Override
	public String movePiece(int Player, int position, int dieRoll) {
		// TODO Auto-generated method stub
		return null;
	}

	public String arrString(ArrayList list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i) + " , ";
		}
		return str;
	}

}
