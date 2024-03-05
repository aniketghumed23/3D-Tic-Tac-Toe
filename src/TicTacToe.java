import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe extends JFrame implements ActionListener
{

	private JButton resetGameBtn;
	private JPanel boardPanel, headingPanel, buttonPanel;
	private JLabel status, score, difficultyLevel, selectMarker;
	private JRadioButton oRadButton, xRadButton, cpuFirstButton, humanFirstButton, easyButton, mediumButton, hardButton;


	private char config[][][];				//for minimax algorithm
	private TicTacToeButton[][][] boardConfig;	//Button array

	char humanTurn = 'X';
	char computerTurn = 'O';

	private int humanScore = 0;				//Total human score
	private int computerScore = 0;			//Total API score

	private boolean humanFirst = true;
	private int difficulty = 2;
	private int totalLooksAhead = 2;
	private int lookAheadCounter = 0;

	int[] finalCombWin = new int[4];			//Final winning combination
	TicTacToeButton[] finalWinButton = new TicTacToeButton[4];

	public boolean winFlag = false;				//Variable to tell if a winning move has been achieved


	public static void main(String a[])
	{
		new TicTacToe();
	}

	/*
	 * TicTacToeButton is a private inner class that extends JButton and adds information vital to determine the location
	 * to send back to the main array
	 */
	private class TicTacToeButton extends JButton
	{
		public int boxRow;
		public int boxColumn;
		public int boxBoard;

	}

	/*
	 * OneMove is a class that holds information for one potential move. This is used to check if a certain
	 * move is a win
	 */
	public class OneMove
	{
		int board;
		int row;
		int column;
	}


	/*
	 * constructor
	 */
	public TicTacToe()
	{
		super("3D Tic-Tac-Toe!");
		setSize(600,800);

		//Background image
		ImageIcon background=new ImageIcon("backGround.png");
		Image img=background.getImage();
		Image temp=img.getScaledInstance(600,800,Image.SCALE_SMOOTH);
		background=new ImageIcon(temp);
		JLabel back=new JLabel(background);
		back.setLayout(null);
		back.setBounds(0,0,600,800);
		add(back);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setupBoard();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}


	/*
     * This class help to draw game board with Button
     */
	public class BoardPanel extends JPanel
	{
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));

			//Board 0
			g2.drawLine(163, 57, 383, 57); // 0th horizontal
			g2.drawLine(140, 85, 360, 85); // 1st horizontal
			g2.drawLine(110, 127, 330, 127); // 2nd horizontal
			g2.drawLine(80, 169, 300, 169); // 3rd horizontal
			g2.drawLine(50, 210, 270, 210); // 4th horizontal

			g2.drawLine(160, 60, 50, 210); // 0th vertical
			g2.drawLine(218, 60, 105, 210); // 1st vertical
			g2.drawLine(273, 60, 155, 210); // 2nd vertical
			g2.drawLine(327, 60, 210, 210); // 3rd vertical
			g2.drawLine(380, 60, 270, 210);// 4th vertical

			//Board 1
			g2.drawLine(160, 230, 380, 230); // 0th horizontal
			g2.drawLine(140, 260, 355, 260); // 1st horizontal
			g2.drawLine(110, 302, 328, 302); // 2nd horizontal
			g2.drawLine(80, 344, 298, 344); // 3rd horizontal
			g2.drawLine(50, 384, 268, 384); // 4th horizontal

			g2.drawLine(160, 230, 50, 385); // 0th vertical
			g2.drawLine(218, 230, 100, 385); // 1st vertical
			g2.drawLine(273, 230, 155, 385); // 2nd vertical
			g2.drawLine(327, 230, 208, 385); // 3rd vertical
			g2.drawLine(380, 230, 265, 385); // 4th vertical

			//Board 2
			g2.drawLine(160, 400, 380, 400); // 0th horizontal
			g2.drawLine(140, 435, 355, 435); // 1st horizontal
			g2.drawLine(110, 477, 325, 477); // 2nd horizontal
			g2.drawLine(80, 519, 295, 519); // 3rd horizontal
			g2.drawLine(55, 560, 265, 560); // 4th horizontal

			g2.drawLine(160, 400, 50, 560); // 0th vertical
			g2.drawLine(218, 400, 100, 560); // 1st vertical
			g2.drawLine(273, 400, 155, 560); // 2nd vertical
			g2.drawLine(327, 400, 210, 560); // 3rd vertical
			g2.drawLine(380, 400, 265, 560); // 4th vertical

			//Board 3
			g2.drawLine(160, 570, 380, 570); // 1st horizontal
			g2.drawLine(135, 610, 355, 610); // 1st horizontal
			g2.drawLine(110, 652, 325, 652); // 2nd horizontal
			g2.drawLine(80, 694, 295, 694); // 3rd horizontal
			g2.drawLine(55, 740, 265, 740); // 3rd horizontal

			g2.drawLine(160, 570, 50, 740); // 1st vertical
			g2.drawLine(218, 570, 100, 740); // 1st vertical
			g2.drawLine(273, 570, 155, 740); // 2nd vertical
			g2.drawLine(327, 570, 210, 740); // 3rd vertical
			g2.drawLine(380, 570, 265, 740); // 3rd vertical

			//Draws red line if the winFlag is true
			if(winFlag)
			{
				g2.setColor(Color.RED);
				g2.drawLine(finalWinButton[0].getBounds().x + 27, finalWinButton[0].getBounds().y + 20,
				finalWinButton[3].getBounds().x + 27, finalWinButton[3].getBounds().y + 20);
			}
		}
	}

	/*
	* setupBoard builds the GUI for game
	*/
	public void setupBoard()
	{
		//Creating the 2 arrays to represent the game
		config = new char[4][4][4];
		boardConfig = new TicTacToeButton[4][4][4];

		boardPanel = new BoardPanel();
		buttonPanel = new JPanel();
		headingPanel = new JPanel();

		//Reset button
		resetGameBtn = new JButton("New Game");
		resetGameBtn.setBounds(410, 450, 120, 60);
		resetGameBtn.addActionListener(new NewButtonListener());
		resetGameBtn.setName("newGameBtn");

		//X/O Radio Button
		selectMarker = new JLabel("Choose your marker:");
		xRadButton = new JRadioButton("X", true);
		oRadButton = new JRadioButton("O");
		selectMarker.setBounds(400, 350, 200,50);
		xRadButton.setBounds(410, 380, 50, 50);
		oRadButton.setBounds(470, 380, 50, 50);

		ButtonGroup xoSelect = new ButtonGroup();
		xoSelect.add(xRadButton);
		xoSelect.add(oRadButton);

		PieceListener xoListener = new PieceListener();
		xRadButton.addActionListener(xoListener);
		oRadButton.addActionListener(xoListener);

		humanFirstButton = new JRadioButton("Human First", true);
		cpuFirstButton = new JRadioButton("CPU First");
		humanFirstButton.setBounds(250, 110, 150, 40);
		cpuFirstButton.setBounds(250, 80, 150, 40);
		cpuFirstButton.setVisible(false);
		humanFirstButton.setVisible(false);

		ButtonGroup firstSelect = new ButtonGroup();
		firstSelect.add(cpuFirstButton);
		firstSelect.add(humanFirstButton);


		FirstListener firstListener = new FirstListener();
		cpuFirstButton.addActionListener(firstListener);
		humanFirstButton.addActionListener(firstListener);

		//Difficulty level radio buttons
		difficultyLevel = new JLabel("Difficulty Level:");
		easyButton = new JRadioButton("Easy");
		mediumButton = new JRadioButton("Difficult", true);
		hardButton = new JRadioButton("Insane");

		difficultyLevel.setBounds(400, 210, 160, 50);
		easyButton.setBounds(410, 240, 150, 40);
		mediumButton.setBounds(410, 270, 150, 40);
		hardButton.setBounds(410, 300, 150, 40);


		ButtonGroup difficultyGroup = new ButtonGroup();
		difficultyGroup.add(easyButton);
		difficultyGroup.add(mediumButton);
		difficultyGroup.add(hardButton);

		DifficultyListener difficultyListener = new DifficultyListener();
		easyButton.addActionListener(difficultyListener);
		mediumButton.addActionListener(difficultyListener);
		hardButton.addActionListener(difficultyListener);


		//Welcome heading
		status = new JLabel("                                         Welcome to 3D Tic-Tac-Toe!");
		status.setFont(new Font("Tahoma", Font.PLAIN, 12));

		//Current score panel
		score = new JLabel("                  Score Board:  Human: " + humanScore + "   Computer: " + computerScore);
		score.setFont(new Font("Tahoma", Font.BOLD, 15));

		//Variables that determine the locations of the TicTacToeButtons as they are placed within loops
		int rowShift = 25;
		int rowStart = 148;

		int xPos = 150;
		int yPos = 43;
		int width = 60;
		int height = 50;

		//Variables to keep track of the current button being placed
		int boardNum = 0;
		int rowNum = 0;
		int columnNum = 0;
		int boxCounter = 0;

		//Board loop
		for (int i = 0; i <= 3; i++)
		{
			//Row loop
			for (int j = 0; j <= 3; j++)
			{
				//Column loop
				for(int k = 0; k <= 3; k++)
				{
					//Creating the new button, setting it to be empty in both arrays
					config[i][j][k] = '-';
					boardConfig[i][j][k] = new TicTacToeButton();
					boardConfig[i][j][k].setFont(new Font("Arial Bold", Font.ITALIC, 20));
					boardConfig[i][j][k].setText("");
					//Making it transparent and add
					boardConfig[i][j][k].setContentAreaFilled(false);
					boardConfig[i][j][k].setBorderPainted(false);
					boardConfig[i][j][k].setFocusPainted(false);
					//Placing the button
					boardConfig[i][j][k].setBounds(xPos, yPos, width, height);
					//Setting information variables
					boardConfig[i][j][k].setName(Integer.toString(boxCounter));
					boardConfig[i][j][k].boxBoard = boardNum;
					boardConfig[i][j][k].boxRow = rowNum;
					boardConfig[i][j][k].boxColumn = columnNum;
					//Adding action listener
					boardConfig[i][j][k].addActionListener(this);

					//Bump the column number 1, move the position that the next button will be placed to the right, and add the current button to the panel
					columnNum++;
					boxCounter++;
					xPos += 52; // increases width withing row elements
					getContentPane().add(boardConfig[i][j][k]);
				}

				//Reset the column number, bump the row number one, move the position that the next button will be placed down and skew it so it matches with the game board
				columnNum = 0;
				rowNum++;
				xPos = rowStart -= rowShift;
				yPos += 41;
			}

			//Reset row numbers and row shifts
			rowNum = 0;
			rowShift = 28;
			rowStart = 150;
			boardNum++;
			xPos = rowStart;
			yPos += 10;
		}


		//Panel setup
		boardPanel.setVisible(true);
		headingPanel.setVisible(true);
		buttonPanel.setVisible(true);
		status.setVisible(true);
		difficultyLevel.setVisible(true);
		selectMarker.setVisible(true);


		headingPanel.setLayout(new GridLayout(2,1));
		headingPanel.add(status);
		headingPanel.add(score);
		headingPanel.setBounds(80, 0, 380, 30);


		add(xRadButton);
		add(oRadButton);
		add(humanFirstButton);
		add(cpuFirstButton);
		add(difficultyLevel);
		add(selectMarker);
		add(easyButton);
		add(mediumButton);
		add(hardButton);
		add(resetGameBtn);
		add(headingPanel);
		add(boardPanel);


		setVisible(true);
	}


	/*
	* PieceListener  changes the human and computer piece variables based on input from the user.
	*/
	class PieceListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			clearBoard();
			status.setForeground(Color.BLACK);
			status.setText("                                              Good luck!");

			if(xRadButton.isSelected())
			{
				humanTurn = 'X';
				computerTurn = 'O';
			}
			else
			{
				humanTurn = 'O';
				computerTurn = 'X';
			}

			if(!humanFirst)
			{
				if(difficulty == 3)
					computerPlays();
				else
					computerPlayRandom();
			}
		}
	}

	/*
	* DifficultyListener is for  difficulty level variable
	*/
	class DifficultyListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			clearBoard();
			status.setForeground(Color.BLACK);
			status.setText("                                              Good luck!");

			if(easyButton.isSelected())
			{
				difficulty = 1;
				totalLooksAhead = 1;
			}
			else if(mediumButton.isSelected())
			{
				difficulty = 2;
				totalLooksAhead = 2;
			}
			else
			{
				difficulty = 3;
				totalLooksAhead = 6;
			}

			if(!humanFirst)
			{
				if(difficulty == 3)
					computerPlays();
				else
					computerPlayRandom();
			}
		}
	}


	/*
	 * FirstListener sets the starting player based on the players input.
	 */
	class FirstListener implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			clearBoard();
			status.setForeground(Color.BLACK);
			status.setText("                      Good luck!");

			if(cpuFirstButton.isSelected())
			{
				humanFirst = false;

				if(!hardButton.isSelected())
					computerPlayRandom();
				else
					computerPlays();
			}
			else
			{
				humanFirst = true;
			}
		}
	}

	/*
	* When any of the button is played this will get call.
	*/
	public void actionPerformed(ActionEvent e)
	{

		//Getting the button clicked's information and setting the arrays accordingly
		TicTacToeButton button = (TicTacToeButton)e.getSource();
		config[button.boxBoard][button.boxRow][button.boxColumn] = humanTurn;
		boardConfig[button.boxBoard][button.boxRow][button.boxColumn].setText(Character.toString(humanTurn));
		boardConfig[button.boxBoard][button.boxRow][button.boxColumn].setEnabled(false);

		OneMove newMove = new OneMove();
		newMove.board = button.boxBoard;
		newMove.row = button.boxRow;
		newMove.column = button.boxColumn;

		if(checkWin(humanTurn, newMove))
		{
			status.setText("                        You beat me! Press New Game to play again.");
			status.setForeground(Color.RED);
			humanScore++;
			winFlag = true;
			disableBoard();
			updateScore();
		}
		else
		{
			computerPlays();
		}
	}



	class NewButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			clearBoard();
			//status.setForeground(Color.BLACK);
			status.setText("");

			if(!humanFirst)
			{
				if(difficulty == 3)
					computerPlays();
				else
					computerPlayRandom();
			}
		}
	}

	/*
	 * updateScore()
	 */
	public void updateScore()
	{
		score.setText("                  Score Board:   Human: " + humanScore + "   Computer: " + computerScore);
	}

	/*
	 * clearBoard() will clear the board reset the game
	 */
	public void clearBoard()
	{
		repaint();
		winFlag = false;
		lookAheadCounter = 0;

		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				for(int k = 0; k <= 3; k++)
				{
	    		config[i][j][k] = '-';
	    		boardConfig[i][j][k].setText("");
	    		boardConfig[i][j][k].setEnabled(true);
				}
			}
		}

		finalCombWin = new int[4];
	}

	/*
	 * disableBoard() is used to disable
	 */

	public void disableBoard()
	{
		int index = 0;
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				for(int k = 0; k <= 3; k++)
				{
					if(contains(finalCombWin, Integer.parseInt(boardConfig[i][j][k].getName())))
					{
						boardConfig[i][j][k].setEnabled(true);
						boardConfig[i][j][k].setForeground(Color.RED);
						finalWinButton[index] = boardConfig[i][j][k];
						index++;
					}
					else
					{
						boardConfig[i][j][k].setEnabled(false);
					}
				}
			}
		}

		repaint();

	}

	/*
	 * Private method contains() is used the cross verify all the variable of win combination .
	 */
	private boolean contains(int[] a, int k)
	{
		//Step through array
		for(int i : a)
		{	//Compare elements
			if(k == i)
				return true;
		}
		return false;
	}


	/*
	 *AI play random
	 */
	private void computerPlayRandom()
	{
		Random random = new Random();
		int row = random.nextInt(4);
		int column = random.nextInt(4);
		int board = random.nextInt(4);
		config[board][row][column] = computerTurn;
		boardConfig[board][row][column].setText(Character.toString(computerTurn));

		boardConfig[board][row][column].setEnabled(false);
	}

	private void computerPlays()
	{
		int bestScore;
		int hValue;
		OneMove nextMove;
		int bestScoreBoard = -1;
		int bestScoreRow = -1;
		int bestScoreColumn = -1;
		bestScore = -1000;

		check:
		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				for(int k = 0; k <= 3; k++)
				{
					if(config[i][j][k] == '-')
					{
						//Creating a new move on every empty position
						nextMove = new OneMove();
						nextMove.board = i;
						nextMove.row = j;
						nextMove.column = k;

						if(checkWin(computerTurn, nextMove))
						{
							//Leave the piece there if it is a win and end the game
							config[i][j][k] = computerTurn;
							boardConfig[i][j][k].setText(Character.toString(computerTurn));
							status.setText("                         I win! Press New Game to play again.");
							status.setForeground(Color.RED);
							winFlag = true;
							computerScore++;

							disableBoard();
							updateScore();
							break check;
						}
						else
						{

							if(difficulty != 1)
							{
								hValue = lookAhead(humanTurn, -1000, 1000);
							}
							else
							{

								hValue = heuristic();
							}

							lookAheadCounter = 0;

							if(hValue >= bestScore)
							{
								bestScore = hValue;
								bestScoreBoard = i;
								bestScoreRow = j;
								bestScoreColumn = k;
								config[i][j][k] = '-';
							}
							else
							{
								config[i][j][k] = '-';
							}
						}
					}
				}
			}
		}

		//If there is no possible winning move, make the move in the calculated best position.
		if(!winFlag)
		{
			config[bestScoreBoard][bestScoreRow][bestScoreColumn] = computerTurn;
			boardConfig[bestScoreBoard][bestScoreRow][bestScoreColumn].setText(Character.toString(computerTurn));

			boardConfig[bestScoreBoard][bestScoreRow][bestScoreColumn].setEnabled(false);
		}
	}

	/*
	 * lookAhead() generates all the possible moves
	 */
	private int lookAhead(char c, int a, int b)
	{

		int alpha = a;
		int beta = b;


		if(lookAheadCounter <= totalLooksAhead)
		{

			lookAheadCounter++;

			if(c == computerTurn)
			{
				int hValue;
				OneMove nextMove;

				for (int i = 0; i <= 3; i++)
				{
					for (int j = 0; j <= 3; j++)
					{
						for(int k = 0; k <= 3; k++)
						{
							if(config[i][j][k] == '-')
							{
								nextMove = new OneMove();
								nextMove.board = i;
								nextMove.row = j;
								nextMove.column = k;

								if(checkWin(computerTurn, nextMove))
								{
									config[i][j][k] = '-';
									return 1000;
								}
								else
								{
									//Recursive look ahead, placing human pieces next
									hValue = lookAhead(humanTurn, alpha, beta);
									if(hValue > alpha)
									{
										alpha = hValue;
										config[i][j][k] = '-';
									}
									else
									{
										config[i][j][k] = '-';
									}
								}

								if (alpha >= beta)
									break;
							}
						}
					}
				}

				return alpha;
			}

			else
			{
				int hValue;
				OneMove nextMove;

				for (int i = 0; i <= 3; i++)
				{
					for (int j = 0; j <= 3; j++)
					{
						for(int k = 0; k <= 3; k++)
						{

							if(config[i][j][k] == '-')
							{

								nextMove = new OneMove();
								nextMove.board = i;
								nextMove.row = j;
								nextMove.column = k;

								if(checkWin(humanTurn, nextMove))
								{
									config[i][j][k] = '-';
									return -1000;
								}
								else
								{
									//Recursive look ahead, placing computer pieces next
									hValue = lookAhead(computerTurn, alpha, beta);
									if(hValue < beta)
									{
										beta = hValue;
										config[i][j][k] = '-';
									}
									else
									{
										config[i][j][k] = '-';
									}
								}

								if (alpha >= beta)
									break;
							}
						}
					}
				}

				return beta;
			}
		}
		else
		{
			return heuristic();
		}
	}

	/*
	 * heuristic() simply uses the checkAvailable method .
	 */
	private int heuristic()
	{
		return (checkAvailable(computerTurn) - checkAvailable(humanTurn));
	}

	/*
	 * checkWin() takes in a character that will be checked for a win
	 */
	private boolean checkWin
	(char c, OneMove pos)
	{
		config[pos.board][pos.row][pos.column] = c;

		//Win table
		int[][] wins = {
				//Rows on single board
				{0 ,1 ,2 ,3} ,{4 ,5 ,6 ,7} ,{8 ,9 ,10 ,11} ,{12 ,13 ,14 ,15} ,{16 ,17 ,18 ,19} ,
				{20 ,21 ,22 ,23} ,{24 ,25 ,26 ,27} ,{28 ,29 ,30 ,31} ,{32 ,33 ,34 ,35} ,
				{36 ,37 ,38 ,39} ,{40 ,41 ,42 ,43} ,{44 ,45 ,46 ,47} ,{48 ,49 ,50 ,51} ,
				{52 ,53 ,54 ,55} ,{56 ,57 ,58 ,59} ,{60 ,61 ,62 ,63},

				//Columns on single board
				{0 ,4 ,8 ,12} ,{1 ,5 ,9 ,13} ,{2 ,6 ,10 ,14} ,{3 ,7 ,11 ,15} ,{16 ,20 ,24 ,28} ,
				{17 ,21 ,25 ,29} ,{18 ,22 ,26 ,30} ,{19 ,23 ,27 ,31} ,{32 ,36 ,40 ,44} ,{33 ,37 ,41 ,45} ,
				{34 ,38 ,42 ,46} ,{35 ,39 ,43 ,47} ,{48 ,52 ,56 ,60} ,{49 ,53 ,57 ,61} ,{50 ,54 ,58 ,62} ,
				{51 ,55 ,59 ,63} ,

				//Diagonals on single board
				{0 ,5 ,10 ,15} ,{3 ,6 ,9 ,12} ,{16 ,21 ,26 ,31} ,{19 ,22 ,25 ,28} ,{32 ,37 ,42 ,47} ,
				{35 ,38 ,41 ,44} ,{48 ,53 ,58 ,63} ,{51 ,54 ,57 ,60} ,

				//Straight down through boards

				{0 ,16 ,32 ,48} ,{1 ,17 ,33 ,49} ,{2 ,18 ,34 ,50} ,{3 ,19 ,35 ,51} ,
				{4 ,20 ,36 ,52} ,{5 ,21 ,37 ,53} ,{6 ,22 ,38 ,54} ,{7 ,23 ,39 ,55} ,{8 ,24 ,40 ,56} ,
				{9 ,25 ,41 ,57} ,{10 ,26 ,42 ,58} ,{11 ,27 ,43 ,59} ,{12 ,28 ,44 ,60} ,{13 ,29 ,45 ,61} ,
				{14 ,30 ,46 ,62} ,{15 ,31 ,47 ,63} ,

				// new 4*4*4 Diagonals through boards
				{0,20,40,60}, {1,21,41,61}, {2,22,42,62}, {3, 23, 43, 63}, {12,24,36,48}, {13,25, 37, 49},
				{14,26, 38,50}, {15,27, 39, 51}, {0,17,34,51}, {4,21,38,55}, {8,25,42,59}, {12,29,46,63},
				{3,18,33,48}, {7,22,37,52}, {11,26, 41, 56}, {15, 30,45,60}, {0,21,42,63}, {3,22,41,60},
				{12,25, 38, 51}, {15,26, 37, 48}
		};

		int[] gameBoard = new int[64];

		//Counter from 0 to 49, one for each win combo
		int counter = 0;


		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				for(int k = 0; k <= 3; k++)
				{
					if(config[i][j][k] == c)
					{
						gameBoard[counter] = 1;
					}
					else
					{
						gameBoard[counter] = 0;
					}
					counter++;
				}
			}
		}

		//For each possible win combination
		for (int i = 0; i <= 75; i++)
		{
			//Resetting counter to see if all 3 locations have been used
			counter = 0;
			for (int j = 0; j <= 3; j++)
			{
				//For each individual winning space in the current combination
				if(gameBoard[wins[i][j]] == 1)
				{
					counter++;

					finalCombWin[j] = wins[i][j];
					if(counter == 4)
					{
						return true;
					}
				}
			}
		}

		return false;
	}


	/*
	 * checkAvailable is very similar to checkWin()
	 */
	private int checkAvailable(char c)
	{
		int winCounter = 0;

		//Win table
		int[][] wins = {
				//Rows on single board
				{0 ,1 ,2 ,3} ,{4 ,5 ,6 ,7} ,{8 ,9 ,10 ,11} ,{12 ,13 ,14 ,15} ,{16 ,17 ,18 ,19} ,
				{20 ,21 ,22 ,23} ,{24 ,25 ,26 ,27} ,{28 ,29 ,30 ,31} ,{32 ,33 ,34 ,35} ,
				{36 ,37 ,38 ,39} ,{40 ,41 ,42 ,43} ,{44 ,45 ,46 ,47} ,{48 ,49 ,50 ,51} ,
				{52 ,53 ,54 ,55} ,{56 ,57 ,58 ,59} ,{60 ,61 ,62 ,63},

				//Columns on single board
				{0 ,4 ,8 ,12} ,{1 ,5 ,9 ,13} ,{2 ,6 ,10 ,14} ,{3 ,7 ,11 ,15} ,{16 ,20 ,24 ,28} ,
				{17 ,21 ,25 ,29} ,{18 ,22 ,26 ,30} ,{19 ,23 ,27 ,31} ,{32 ,36 ,40 ,44} ,{33 ,37 ,41 ,45} ,
				{34 ,38 ,42 ,46} ,{35 ,39 ,43 ,47} ,{48 ,52 ,56 ,60} ,{49 ,53 ,57 ,61} ,{50 ,54 ,58 ,62} ,
				{51 ,55 ,59 ,63} ,

				//Diagonals on single board
				{0 ,5 ,10 ,15} ,{3 ,6 ,9 ,12} ,{16 ,21 ,26 ,31} ,{19 ,22 ,25 ,28} ,{32 ,37 ,42 ,47} ,
				{35 ,38 ,41 ,44} ,{48 ,53 ,58 ,63} ,{51 ,54 ,57 ,60} ,

				//Straight down through boards

				{0 ,16 ,32 ,48} ,{1 ,17 ,33 ,49} ,{2 ,18 ,34 ,50} ,{3 ,19 ,35 ,51} ,
				{4 ,20 ,36 ,52} ,{5 ,21 ,37 ,53} ,{6 ,22 ,38 ,54} ,{7 ,23 ,39 ,55} ,{8 ,24 ,40 ,56} ,
				{9 ,25 ,41 ,57} ,{10 ,26 ,42 ,58} ,{11 ,27 ,43 ,59} ,{12 ,28 ,44 ,60} ,{13 ,29 ,45 ,61} ,
				{14 ,30 ,46 ,62} ,{15 ,31 ,47 ,63} ,

				// new 4*4*4 Diagonals through boards
				{0,20,40,60}, {1,21,41,61}, {2,22,42,62}, {3, 23, 43, 63}, {12,24,36,48}, {13,25, 37, 49},
				{14,26, 38,50}, {15,27, 39, 51}, {0,17,34,51}, {4,21,38,55}, {8,25,42,59}, {12,29,46,63},
				{3,18,33,48}, {7,22,37,52}, {11,26, 41, 56}, {15, 30,45,60}, {0,21,42,63}, {3,22,41,60},
				{12,25, 38, 51}, {15,26, 37, 48}
		};

		//Array that indicates all the spaces on the game board
		int[] gameBoard = new int[64];
		int counter = 0;


		for (int i = 0; i <= 3; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				for(int k = 0; k <= 3; k++)
				{
					if(config[i][j][k] == c || config[i][j][k] == '-')
						gameBoard[counter] = 1;
					else
						gameBoard[counter] = 0;

					counter++;
				}
			}
		}

		//For all possible win combination
		for (int i = 0; i <= 75; i++)
		{

			counter = 0;
			for (int j = 0; j <= 3; j++)
			{

				if(gameBoard[wins[i][j]] == 1)
				{
					counter++;

					finalCombWin[j] = wins[i][j];

					if(counter == 4)
						winCounter++;
				}
			}
		}

		return winCounter;
	}
}
