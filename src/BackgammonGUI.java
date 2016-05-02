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
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BackgammonGUI extends JPanel {

	private File currentDiceRoll = new File("images/d1.png");
	private File finishedRed = new File("images/RedFinishedEmpty.png");
	private File finishedWhite = new File("images/WhiteFinishedEmpty.png");
	private BufferedImage diceImg;
	private Image roll;
	private JLabel dice;
	private int currentPlayer = 1;
	private JButton rollDice;
	private Board board = new Board();
	private Image player1;
	private Image player2;
	private Image finishedRedImg;
	private Image finishedWhiteImg;
	private JTextField pieceToMove;
	private JPanel submitPanel;
	private JPanel submitMovePanel;
	private int numToMove = -1;
	private boolean moving1;
	private boolean moving2;
	private int diceRoll;
	private int movedPosition;
	private JLabel piecesOut;
	private JLabel offBoardPiecesP1;
	private JLabel offBoardPiecesP2;
	private Agent agent1;
	private Agent agent2;
	private Image title;
	private Image game1p;
	private Image game2p;
	private Image game3p;

	public BackgammonGUI() {
		super(new BorderLayout(0, 0));
		setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(191, 159, 82)));
		add(offBoardPieces(), BorderLayout.EAST);
		add(initGamePanel(), BorderLayout.CENTER);
		setBackground(new Color(7, 19, 48));

	}

	public JPanel initGamePanel() {
		JPanel background = new JPanel();
		background.setBackground(new Color(10, 25, 64));
		background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
		File back = new File("images/Backgammon.png");
		try {
			title = ImageIO.read(back);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel game = new JLabel(new ImageIcon(title));
		background.add(game);
		File ranvsran = new File("images/rand.png");
		try {
			game1p = ImageIO.read(ranvsran);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JButton rvsr = new JButton(new ImageIcon(game1p));
		rvsr.setBorderPainted(false);
		rvsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("here");
				agent1 = new HumanAgent();
				agent2 = new RandomAgent();
				remove(background);
				repaint();
			}
		});
		background.add(rvsr);
		File revsran = new File("images/exp.png");
		try {
			game2p = ImageIO.read(revsran);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JButton reinvsran = new JButton(new ImageIcon(game2p));
		reinvsran.setBorderPainted(false);
		reinvsran.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				agent1 = new HumanAgent();
				agent2 = new ExpectiminimaxAgent();
				remove(background);
				repaint();

			}
		});
		background.add(reinvsran);
		File expvsre = new File("images/reinf.png");
		try {
			game3p = ImageIO.read(expvsre);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JButton evsr = new JButton(new ImageIcon(game3p));
		evsr.setBorderPainted(false);
		evsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				agent1 = new HumanAgent();
				agent2 = new ReinforcementLearningAgent(2);
				remove(background);
				repaint();

			}
		});
		background.add(evsr);
		return background;

	}

	/*
	 * JPanel that contains everything involving the dice rolling and choosing
	 * of a piece to move/ move Ability to roll dice from 1-6 Ability to choose
	 * a piece to Move Ability to choose a location to move it to.
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
		File file;
		try {
			if (currentPlayer == 1) {
				file = new File("images/p1roll.png");
			} else {
				file = new File("images/p2roll.png");
			}
			roll = ImageIO.read(file);
		} catch (IOException ex) {

		}
		pieceToMove = new JTextField("Piece you wish to move");
		pieceToMove.setColumns(14);
		pieceToMove.setForeground(new Color(94, 94, 94));
		JButton submit = new JButton("Submit");
		submit.setForeground(new Color(94, 94, 94));
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(currentPlayer);
				if (currentPlayer == 1) {
					numToMove = Integer.parseInt(pieceToMove.getText());
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!agent1.canMove(1, numToMove)) {
						JOptionPane.showMessageDialog(submitMovePanel,
								"There is no piece there to move! Please choose another piece!");
						pieceToMove.setText("Piece you wish to move");
						return;
					} else {
						String result = agent1.movePiece(currentPlayer, numToMove, diceRoll);
						if (board.gameWon() != 0) {
							JOptionPane.showMessageDialog(submitMovePanel,
									"Congratulations player " + board.gameWon() + "! You won!");
						}
						if (!result.equals("Sorry! You can't move any pieces on to the board")
								&& !result.equals("The piece chosen can not move. Please choose another piece!")) {
							movedPosition = Integer.parseInt(result);

							moving1 = true;
							moving2 = true;

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

						} else {
							JOptionPane.showMessageDialog(submitMovePanel, result);
							pieceToMove.setText("Piece you wish to move");
							return;

						}
						submitPanel.setVisible(false);
						rollDice.setVisible(true);
						diceRollPanel.add(rollDice);
						pieceToMove.setText("Piece you wish to move");
						agent2.movePiece(2);
					}
					repaint();
				}
			}

		});
		submitPanel = new JPanel(new BorderLayout(0, 0));
		submitPanel.setBackground(new Color(11, 79, 45));
		submitPanel.add(submit, BorderLayout.EAST);
		submitPanel.add(pieceToMove, BorderLayout.WEST);
		piecesOut = new JLabel("", SwingConstants.CENTER);
		piecesOut.setText("Pieces Out:   " + "Player 1: " + board.outPlayer1 + "  " + "Player 2: " + board.outPlayer2);
		piecesOut.setForeground(Color.white);
		piecesOut.setBackground(new Color(11, 79, 45));
		piecesOut.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, new Color(11, 79, 45)));
		rollDice = new JButton();
		rollDice.setBorderPainted(false);
		rollDice.setIcon(new ImageIcon(roll));
		rollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diceRoll = 1 + (int) (Math.random() * 6);
				updateDiceRollPanel(diceRoll);
				if (currentPlayer == 1 && board.playersOut() != 1) {
					rollDice.setVisible(false);
					submitPanel.setVisible(true);
					diceRollPanel.add(submitPanel);
				} else {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					submitPanel.setVisible(false);
					rollDice.setVisible(true);
					diceRollPanel.add(rollDice);
					agent1.movePiece(1, -1, diceRoll);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					agent2.movePiece(2);
				}
				repaint();
			}

		});
		diceRollPanel.add(rollDice, BorderLayout.CENTER);
		diceRollPanel.add(piecesOut, BorderLayout.SOUTH);

		return diceRollPanel;
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
		File file;
		dice.setIcon(new ImageIcon(diceImg));
		if (currentPlayer == 1) {
			file = new File("images/p1roll.png");
		} else {
			file = new File("images/p2roll.png");
		}
		try {
			roll = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rollDice.setIcon(new ImageIcon(roll));
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
	 * the boxes around positions to move
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
			if ((i == numToMove || i == movedPosition) && (moving1 == true || moving2 == true)) {
				if (i == numToMove) {
					g.setColor(Color.yellow);
					g.drawRect((count2p * 55) - 3, (685 - 60 * (stack.size() + 1)) - 3, 55, 55);
					moving1 = false;
				}
				if (i == movedPosition) {
					g.setColor(Color.cyan);
					if (movedPosition > 5) {
						g.drawRect((12 - movedPosition) * 55 - 3,
								(685 - 60 * board.boardA[movedPosition].size() - 1) - 3, 55, 55);
					} else {
						g.drawRect((13 - movedPosition) * 55 - 3,
								(685 - 60 * board.boardA[movedPosition].size() - 1) - 3, 55, 55);
					}
					moving2 = false;
				}
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

			if ((i == numToMove || i == movedPosition) && (moving1 == true || moving2 == true)) {
				if (i == numToMove) {
					g.setColor(Color.yellow);
					if (stack.size() == 1) {
						g.drawRect(count1p * 55 - 3, 60 * (stack.size()) - 33, 55, 55);
					} else {
						g.drawRect(count1p * 55 - 3, 60 * (stack.size() + 1) - 33, 55, 55);
					}
					moving1 = false;
				}
				if (i == movedPosition) {
					g.setColor(Color.cyan);
					if (movedPosition > 17) {
						g.drawRect((movedPosition - 10) * 55 - 3, 60 * (board.boardA[movedPosition].size()) - 33, 55,
								55);
					} else {
						g.drawRect((movedPosition - 11) * 55 - 3, 60 * (board.boardA[movedPosition].size()) - 33, 55,
								55);
					}
					moving2 = false;
				}
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

	/*
	 * Gets the next player, given the current player
	 */
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
		gameFrame.add(new BackgammonGUI());
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
	}

}
