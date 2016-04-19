import java.util.Stack;

public class State {

	protected double utility;
	protected Stack<Integer>[] board;
	protected Integer start = 0;
	protected Integer end;
	protected Integer pieceLandedOn;
	protected Integer numOut;

	public State(Stack<Integer>[] board,int numOut, int start, int end, int pieceLandedOn) {
		this.start = start;
		this.end = end;
		this.pieceLandedOn = pieceLandedOn;
		this.board = board;
		this.numOut = numOut;
	}

	public boolean isTerminal() {
		if (Board.winPlayer1 == 15 || Board.winPlayer2 == 15) {
			return true;
		}
		return false;
	}

	// String representation of state
	@Override
	public String toString() {
		String s = start.toString();
		String e = end.toString();
		String pl = pieceLandedOn.toString();
		return s + "," + e + "," + pl;
	}

	public String getPieceColor(int player) {
		if (player == 1) {
			return "red";
		} else {
			return "white";
		}
	}

	/*
	 * Copy contents of a stack array into a new stack array
	 */
	public Stack<Integer>[] copyStack() {
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

	public State apply(RandomAgent agent, int numPlayer) {
		Integer[] changes = agent.applyAction(numPlayer);
		Stack<Integer>[] copy = copyStack();
		return new State(copy, changes[0], changes[1], changes[2], changes[3]);

	}

}
