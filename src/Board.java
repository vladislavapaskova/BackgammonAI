import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Board {

	// game board
	static Stack<Integer>[] boardA;

	// board size
	static final int BOARDSIZE = 24;

	// pieces out of the board
	static int outPlayer1;
	static int outPlayer2;

	// pieces that the player has managed to get out;count towards winning
	static int winPlayer1;
	static int winPlayer2;

	// pieces in final six lanes; we count those to see if we can start taking
	// pieces out towards winning
	static int finalPlayer1;
	static int finalPlayer2;
	
	static int diceRoll;
	static int itterations;

	/*
	 * Board constructor
	 */
	public Board() {
		initializeGame();
		itterations = 0;
		printBoard();
	}

	/*
	 * Initializes game
	 */
	private void initializeGame() {
		outPlayer1 = 0;
		outPlayer2 = 0;
		winPlayer1 = 0;
		winPlayer2 = 0;
		finalPlayer1 = 5;
		finalPlayer2 = 5;

		initializeBoardArray();
	}

	/*
	 * Returns players out
	 */
	public static int playersOut() {
		if (Board.outPlayer1 > 0) {
			return 1;
		} else if (Board.outPlayer2 > 0) {
			return 2;
		} else {
			return 0;
		}
	}
	/*
	 * Checks who won the game
	 */
	public static int gameWon() {
		if (Board.winPlayer1 == 15)
			return 1;
		if (Board.winPlayer2 == 15)
			return 2;
		else
			return 0;
	}

	/*
	 * This method initializes the initial board i.e. puts all of the pieces in
	 * the correct locations initializes all of the stacks
	 */
	private void initializeBoardArray() {
		boardA = new Stack[BOARDSIZE];

		for (int i = 0; i < BOARDSIZE; i++) {
			boardA[i] = new Stack<Integer>();
			if (i == 0) {
				putInitPieces(boardA[i], 2, 1);
			}
			if (i == 5 || i == 12) {
				putInitPieces(boardA[i], 5, 2);
			}
			if (i == 7) {
				putInitPieces(boardA[i], 3, 2);
			}
			if (i == 11 || i == 18) {
				putInitPieces(boardA[i], 5, 1);
			}
			if (i == 16) {
				putInitPieces(boardA[i], 3, 1);
			}
			if (i == 23) {
				putInitPieces(boardA[i], 2, 2);
			}

		}

	}

	/*
	 * Puts correct number of pieces in a given stack
	 */
	private Stack<Integer> putInitPieces(Stack<Integer> s, int numPieces, int numPlayer) {
		for (int i = 0; i < numPieces; i++) {
			s.push(numPlayer);
		}
		return s;
	}

	/*
	 * Prints the current state of the board
	 */
	public String printBoard() {
		String print = "";
		for (int i = 0; i < BOARDSIZE; i++) {
			print += Arrays.toString(boardA[i].toArray());
		}
		System.out.println(print);
		return print;
	}
	


	/*
	 * generates a random number from 1-6 for the die roll
	 */
	public static int dieRoll() {
		return (1 + (int) (Math.random() * 6));
	}

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


	public static void main(String[] args) {

	}

}
