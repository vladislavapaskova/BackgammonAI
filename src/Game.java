
public class Game {

	ExpectiminimaxAgent agent1;
	ReinforcementLearningAgent agent2;

	/*
	 * NOTE: The agents and game iterations can be changed here. For example you
	 * can choose two completely idfferent agents
	 */
	public Game() {
		Board.reset();
		agent1 = new ExpectiminimaxAgent();
		agent2 = new ReinforcementLearningAgent(2);
		System.out.println(getPercentageWon(100));
	}

	public double getPercentageWon(double itterations) {
		double timesWon = 0;
		for (int i = 0; i < itterations; i++) {
			int winner = playGame();
			if (winner == 1) {
				timesWon++;
			}
		}
		return timesWon / itterations;
	}

	public int playGame() {
		Board.reset();
		while (Board.gameWon() == 0) {
			agent1.movePiece(1);
			agent2.movePiece(2);
		}
		if (Board.gameWon() == 1) {
			return 1;
		} else {
			return 2;
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
	}

}
