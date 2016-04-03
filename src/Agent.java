import java.util.ArrayList;

public abstract class Agent {

	protected Board board;
	
	//Note: Only one of these will be called depending on the agent
	public abstract int movePiece(int Player);
	public abstract String movePiece(int Player, int position, int dieRoll);
	
	/*
	 * Returns a list of the possible pieces that can be moved
	 */
	public ArrayList<Integer> checkMovingPositions(int numPlayer) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < Board.BOARDSIZE; i++) {
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

}
