import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameWalkthrough extends JPanel {

	private File currentDiceRoll = new File("images/d1.png");
	private File finishedRed = new File("images/RedFinishedEmpty.png");
	private File finishedWhite = new File("images/WhiteFinishedEmpty.png");
	private BufferedImage diceImg;
	private JLabel dice;
	private int currentPlayer = 1;
	private Board board = new Board();
	private Image player1;
	private Image player2;
	private Image finishedRedImg;
	private Image finishedWhiteImg;
	private JPanel submitMovePanel;
	private JLabel piecesOut;
	private JLabel offBoardPiecesP1;
	private JLabel offBoardPiecesP2;
	private Timer timer;

	public GameWalkthrough() {
		super(new BorderLayout(0, 0));
		setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(191, 159, 82)));
		add(offBoardPieces(), BorderLayout.EAST);
		setBackground(new Color(7, 19, 48));
		startGame();
	}

	public void startGame(){
		timer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (board.gameWon() != 0) {
					JOptionPane.showMessageDialog(submitMovePanel,
							"Congratulations player " + board.gameWon() + "! You won!");
					timer.stop();
				}else{
				rollDiceandMove(currentPlayer);
				getNextPlayer();
				}
			}
		});
		timer.start();
	}

	public void rollDiceandMove(int currentPlayer){
		
		updateDiceRollPanel(board.movePiece(currentPlayer));
		if (board.winPlayer1 > 0 || board.winPlayer2 > 0) {
			getRedFinalPieces(board.winPlayer1);
			getWhiteFinalPieces(board.winPlayer2);
			try {
				finishedRedImg = ImageIO.read(finishedRed);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				finishedWhiteImg = ImageIO.read(finishedWhite);
			} catch (IOException e) {
				e.printStackTrace();
			}
			offBoardPiecesP1.setIcon(new ImageIcon(finishedRedImg));
			offBoardPiecesP2.setIcon(new ImageIcon(finishedWhiteImg));
		}
		repaint();
	
	}
	/*
	 * JPanel that contains everything involving the dice rolling and choosing
	 * of a piece to move/ move Ability to roll dice from 1-6 Ability to choose
	 * a piece to Move Ability to choose a location to move it to TODO- Break up
	 * method. It is a bit to long..
	 */
	public JPanel diceRollPanel() {
		JPanel diceRollPanel = new JPanel(new BorderLayout(50, 50));
		diceRollPanel.setBackground(new Color(11, 79, 45));
		diceRollPanel.setBorder(BorderFactory.createMatteBorder(0, 10, 10, 0, new Color(191, 159, 82)));
		((BorderLayout) diceRollPanel.getLayout()).setVgap(0);
		if (currentDiceRoll != null) {
			try {
				diceImg = ImageIO.read(currentDiceRoll);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dice = new JLabel(new ImageIcon(diceImg));
			diceRollPanel.add(dice, BorderLayout.NORTH);
		}
		piecesOut = new JLabel("", SwingConstants.CENTER);
		System.out.println(board.outPlayer1);
		piecesOut.setText("Pieces Out:   " + "Player 1: " + board.outPlayer1 + "  " + "Player 2: " + board.outPlayer2);
		piecesOut.setForeground(Color.white);
		piecesOut.setBackground(new Color(11, 79, 45));
		piecesOut.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, new Color(11, 79, 45)));
		diceRollPanel.add(piecesOut, BorderLayout.SOUTH);

		return diceRollPanel;
	}

	public void refreshoffBoardImages() {

	}

	/*
	 * Displays pieces which have moved to their final state
	 */
	public JPanel offBoardPieces() {
		JPanel offBoardPieces = new JPanel(new BorderLayout(0, 0));
		try {
			finishedRedImg = ImageIO.read(finishedRed);
		} catch (IOException e) {
			e.printStackTrace();
		}
		offBoardPiecesP1 = new JLabel();
		offBoardPiecesP1.setIcon(new ImageIcon(finishedRedImg));
		offBoardPiecesP1.setPreferredSize(new Dimension(250, 250));
		offBoardPiecesP1.setOpaque(true);
		offBoardPiecesP1.setBackground(new Color(15, 107, 44));
		offBoardPiecesP1.setBorder(BorderFactory.createMatteBorder(0, 10, 10, 0, new Color(191, 159, 82)));
		try {
			finishedWhiteImg = ImageIO.read(finishedWhite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		offBoardPiecesP2 = new JLabel();
		offBoardPiecesP2.setIcon(new ImageIcon(finishedWhiteImg));
		offBoardPiecesP2.setPreferredSize(new Dimension(250, 250));
		offBoardPiecesP1.setOpaque(true);
		offBoardPiecesP2.setBackground(new Color(15, 107, 44));
		offBoardPiecesP2.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(191, 159, 82)));
		offBoardPieces.add(offBoardPiecesP1, BorderLayout.NORTH);
		offBoardPieces.add(diceRollPanel(), BorderLayout.CENTER);
		offBoardPieces.add(offBoardPiecesP2, BorderLayout.SOUTH);
		return offBoardPieces;

	}

	/*
	 * Refreshes image according to dice number
	 */
	public void refreshDiceImage() {
		try {
			diceImg = ImageIO.read(currentDiceRoll);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dice.setIcon(new ImageIcon(diceImg));
		revalidate();

	}

	/*
	 * Gets image corresponding to dice for each dice roll
	 */
	public File updateDiceRollPanel(int diceRoll) {
		switch (diceRoll) {
		case 1:
			currentDiceRoll = new File("images/d1.png");
			break;
		case 2:
			currentDiceRoll = new File("images/d2.png");
			break;
		case 3:
			currentDiceRoll = new File("images/d3.png");
			break;
		case 4:
			currentDiceRoll = new File("images/d4.png");
			break;
		case 5:
			currentDiceRoll = new File("images/d5.png");
			break;
		case 6:
			currentDiceRoll = new File("images/d6.png");
			break;
		default:
			break;
		}
		refreshDiceImage();
		return currentDiceRoll;
	}

	/*
	 * Gets image corresponding to the number of red pieces that have been moved
	 * off the board
	 */
	public File getRedFinalPieces(int piecesFinished) {
		switch (piecesFinished) {
		case 1:
			finishedRed = new File("images/RedFinished1.png");
			break;
		case 2:
			finishedRed = new File("images/RedFinished2.png");
			break;
		case 3:
			finishedRed = new File("images/RedFinished3.png");
			break;
		case 4:
			finishedRed = new File("images/RedFinished4.png");
			break;
		case 5:
			finishedRed = new File("images/RedFinished5.png");
			break;
		case 6:
			finishedRed = new File("images/RedFinished6.png");
			break;
		case 7:
			finishedRed = new File("images/RedFinished7.png");
			break;
		case 8:
			finishedRed = new File("images/RedFinished8.png");
			break;
		case 9:
			finishedRed = new File("images/RedFinished9.png");
			break;
		case 10:
			finishedRed = new File("images/RedFinished10.png");
			break;
		case 11:
			finishedRed = new File("images/RedFinished11.png");
			break;
		case 12:
			finishedRed = new File("images/RedFinished12.png");
			break;
		case 13:
			finishedRed = new File("images/RedFinished13.png");
			break;
		case 14:
			finishedRed = new File("images/RedFinished14.png");
			break;
		case 15:
			finishedRed = new File("images/RedFinished15.png");
			break;
		default:
			break;
		}
		return finishedRed;
	}

	/*
	 * Gets image corresponding to the number of white pieces that have been
	 * moved off the board
	 */
	public File getWhiteFinalPieces(int piecesFinished) {
		switch (piecesFinished) {
		case 1:
			finishedWhite = new File("images/WhiteFinished1.png");
			break;
		case 2:
			finishedWhite = new File("images/WhiteFinished2.png");
			break;
		case 3:
			finishedWhite = new File("images/WhiteFinished3.png");
			break;
		case 4:
			finishedWhite = new File("images/WhiteFinished4.png");
			break;
		case 5:
			finishedWhite = new File("images/WhiteFinished5.png");
			break;
		case 6:
			finishedWhite = new File("images/WhiteFinished6.png");
			break;
		case 7:
			finishedWhite = new File("images/WhiteFinished7.png");
			break;
		case 8:
			finishedWhite = new File("images/WhiteFinished8.png");
			break;
		case 9:
			finishedWhite = new File("images/WhiteFinished9.png");
			break;
		case 10:
			finishedWhite = new File("images/WhiteFinished10.png");
			break;
		case 11:
			finishedWhite = new File("images/WhiteFinished11.png");
			break;
		case 12:
			finishedWhite = new File("images/WhiteFinished12.png");
			break;
		case 13:
			finishedWhite = new File("images/WhiteFinished13.png");
			break;
		case 14:
			finishedWhite = new File("images/WhiteFinished14.png");
			break;
		case 15:
			finishedWhite = new File("images/WhiteFinished15.png");
			break;
		default:
			break;
		}
		return finishedWhite;
	}

	/*
	 * Does the painting of the pieces on the board as well as the numbers and
	 * the boxes around positions to move (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		int count1p = 1;
		int count2p = 1;
		super.paintComponent(g);
		try {
			player1 = ImageIO.read(new File("images/bgP1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			player2 = ImageIO.read(new File("images/bgp2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 11; i >= 0; i--) {
			Stack<Integer> stack = board.boardA[i];
			Stack<Integer> copy = new Stack<Integer>();

			while (!stack.empty()) {
				copy.push(stack.pop());
			}
			int y = 625;
			while (!copy.isEmpty()) {
				Integer p = copy.pop();
				Image color;
				if (p.equals(1))
					color = player1;
				else
					color = player2;
				g.drawImage(color, count2p * 55, y, null);
				y -= 60;
				stack.push(p);
			}

			
			g.setColor(Color.white);
			g.drawString(i + "", count2p * 55, 690);

			if (count2p == 6)
				count2p += 2;
			else
				count2p++;

		}
		for (int i = 12; i < 24; i++) {
			Stack<Integer> stack = board.boardA[i];
			Stack<Integer> copy = new Stack<Integer>();
			while (!stack.empty()) {
				copy.push(stack.pop());
			}
			int y = 30;
			while (!copy.isEmpty()) {
				Integer p = copy.pop();
				Image color;
				if (p.equals(1))
					color = player1;
				else
					color = player2;
				g.drawImage(color, count1p * 55, y, null);
				y += 60;
				stack.push(p);
			}

			g.setColor(Color.white);
			g.drawString(i + "", count1p * 55, 25);

			if (count1p == 6)
				count1p += 2;
			else
				count1p++;

		}
		piecesOut.setText("Pieces Out:   " + "Player 1: " + board.outPlayer1 + "  " + "Player 2: " + board.outPlayer2);

	}

	public int getNextPlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 2;
			return currentPlayer;
		} else {
			currentPlayer = 1;
			return currentPlayer;
		}
	}

	public static void main(String[] args) {
		JFrame gameFrame = new JFrame();
		gameFrame.setResizable(false);
		gameFrame.setSize(1100, 740);
		gameFrame.add(new GameWalkthrough());
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
	}

}
