
public class Game {

	RandomAgent agent2;
    ExpectiminimaxAgent agent1;
	//ReinforcementLearningAgent agent2;
	//RandomAgent agent1;

	//
	public Game() {
		// learner = new ReinforcementLearner();
		Board.reset();
		agent1 = new ExpectiminimaxAgent();
		//agent1 = new RandomAgent();
		agent2 = new RandomAgent();
		System.out.println(Board.printBoard(Board.boardA));
		//agent2 = new ReinforcementLearningAgent(2);
//		System.out.println("agent2 " + agent2.learner.states.size());
	System.out.println(getPercentageWon(1000));
//	 System.out.println("agent1 " + agent1.learner.states.size());
//	 System.out.println("agent2 " + agent2.learner.states.size());
//	 if(agent1.learner.states.keySet().equals(agent2.learner.states.keySet())){
//	 System.out.println("equal");
//	 }else{
//	 System.out.println("notequal");
	// }

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
