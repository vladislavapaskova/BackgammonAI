import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class ReinforcementLearner {

	LinkedHashMap<String, Double> states;
	protected Stack<State> moves;
	RandomAgent agent1;
	RandomAgent agent2;
	Board board = new Board();
	int count = 0;

	public ReinforcementLearner(int numPlayer) {
		moves = new Stack<State>();
		states = new LinkedHashMap<String, Double>();
		agent1 = new RandomAgent();
		agent2 = new RandomAgent();
		learn(10000, .5, .7, numPlayer);
	}

	public void printSet() {
		for (Map.Entry<String, Double> entry : states.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue() + "\n");
		}
		System.out.println("SIZE:" + states.size());
	}

	/*
	 * Play the game x number of times and learn q values for each "state" based
	 * off of the starting position, the ending position, and the color of the
	 * piece landed on, or if there was nothing there numPlayer is the player
	 * that the agent is
	 */
	public void learn(int itterations, Double gamma, Double alpha, int numPlayer) {
		System.out.println("start size " + states.size());
		State start = new State(Board.boardA, 0, -1, -1, -1);
		states.put(start.toString(), 0.0);
		for (int i = 0; i < itterations; i++) {
			moves.push(start);
			State current = start;
			while (Board.gameWon() == 0) {
				if (numPlayer == 1) {
					current = start.apply(agent1, 1);
					moves.push(current);
					if (states.get(current.toString()) == null) {
						states.put(current.toString(), 0.0);
					}
					agent2.movePiece(2);
				} else {
					current = start.apply(agent2, 2);
					moves.push(current);
					if (states.get(current.toString()) == null) {
						states.put(current.toString(), 0.0);
					}
					agent2.movePiece(1);
				}
			}
			moves.push(current);
			String previous = moves.pop().toString();
			if (numPlayer == 1) {
				if (Board.gameWon() == 1) {
					states.put(previous, 1000.0);
				} else {
					states.put(previous, -1000.0);
				}
			} else {
				if (Board.gameWon() == 1) {
					states.put(previous, -1000.0);
				} else {
					states.put(previous, 1000.0);
				}
			}
			Double utility = 0.0;
			int previousOut = 0;
			while (!moves.isEmpty()) {
				Double reward = 0.0;
				State s = moves.pop();
				if (numPlayer == 1) {
					if (s.pieceLandedOn == 2) {
						reward = 10.0;
					} else {
						reward = -1.0;
					}
				} else {
					if (s.pieceLandedOn == 1) {
						reward = 10.0;
					} else {
						reward = -1.0;
					}
				}
				previousOut = s.numOut;
				utility = states.get(s.toString());
				utility += alpha * (reward + (gamma * states.get(previous.toString())) - utility);
				states.put(s.toString(), utility);
				previous = s.toString();
			}
			Board.reset();
		}
		printSet();
	}

}