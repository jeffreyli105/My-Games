import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;
/*
 Jeffrey Li
 January 24, 2023
 Description:
 This is a chess game. In the menu, you can click on the buttons to go to their respective screens.
 In the "play" screen, select a piece by clicking on a piece(white goes first) and move it by clicking on the
 square you want the piece to be moved.
 */
public class Main extends JPanel implements ActionListener, KeyListener, MouseListener, Runnable{
	static JFrame frame;
	Thread thread;
	int screenWidth = 1000;
	int screenHeight = 562;
	int playX = 388, playY = 135; //x & y for the play button
	int screen = 1; //1 is main, 2 is play, 3 is about, 4 is rules
	int wTimer = 300, bTimer = 300; //White & Black timer
	int ind1 = 0, ind2 = 0; //Destination indices for a moved piece
	double[][] board = new double[8][8]; //Array representation of the board
	int[][] dangerForW = new int[8][8], dangerForB = new int[8][8]; //-1 for danger, 0 for nothing
	HashMap<Double, Piece> map = new HashMap<>(); //Map for the piece values to the piece variables
	ArrayList<int[]> valid; //Pair ArrayList to store valid moves
	int[] attackingWKing = {-1, -1}, attackingBKing = {-1, -1}; // Position for piece that's attacking a king
	int selectedInd1, selectedInd2; //selected indices in board
	int wCap = 0, bCap = 0, wScore = 0, bScore = 0; //Pieces captured by white & black, and their scores
	int win = -1; //-2 for spectating the game after someone won, -1 for no one, 0 for white, 1 for black
	int checkmateToggle = 0; //0 is for nothing, 1 is for a checkmate
	double selectedPiece; //Piece that's selected
	boolean selected = false, move = false, turn; //true is white, false is black
	Font myFont = new Font("Comic Sans MS", 1, 40); //Font for text
	static Image icon, defaultIcon, wWinScreen, bWinScreen, whiteCheck, blackCheck; //Images
	static Cursor cursor; //The cursor
	static Clip menuMusic, playMusic, moveMusic; //Sound files
	/*
	 White:
	 1.1 = pawn1, 1.2 = pawn2, 1.3 = pawn3, 1.4 = pawn4, 1.5 = pawn5, 1.6 = pawn6, 1.7 = pawn7, 1.8 = pawn8;
	 2.1 = rook1, 2.2 = rook2
	 3.1 = knight1, 3.2 = knight2
	 4.1 = bishop1, 4.2 = bishop2
	 5 = queen
	 6 = king
	 
	 Black:
	 7.1 = pawn1, 7.2 = pawn2, 7.3 = pawn3, 7.4 = pawn4, 7.5 = pawn5, 7.6 = pawn6, 7.7 = pawn7, 7.8 = pawn8;
	 8.1 = rook1, 8.2 = rook2
	 9.1 = knight1, 9.2 = knight2
	 10.1 = bishop1, 10.2 = bishop2
	 11 = queen
	 12 = king
	 */
	//Each index in the board storing the borders of the boxes
	Border[][] borders = new Border[8][8];
	//Images
	static Image menuBackground, mainTitle, playButton, aboutMeButton, rulesButton, exitButton, aboutMe, rules, play;
	//Black pieces
	static Image bRook1I, bRook2I, bKnight1I, bKnight2I, bBishop1I, bBishop2I, bQueenI, bKingI;
	static Image bPawn1I, bPawn2I, bPawn3I, bPawn4I, bPawn5I, bPawn6I, bPawn7I, bPawn8I;
	//White pieces
	static Image wRook1I, wRook2I, wKnight1I, wKnight2I, wBishop1I, wBishop2I, wQueenI, wKingI;
	static Image wPawn1I, wPawn2I, wPawn3I, wPawn4I, wPawn5I, wPawn6I, wPawn7I, wPawn8I;
	
	//Black pieces
	static Piece bRook1P, bRook2P, bKnight1P, bKnight2P, bBishop1P, bBishop2P, bQueenP, bKingP;
	static Piece bPawn1P, bPawn2P, bPawn3P, bPawn4P, bPawn5P, bPawn6P, bPawn7P, bPawn8P;
	//White pieces
	static Piece wRook1P, wRook2P, wKnight1P, wKnight2P, wBishop1P, wBishop2P, wQueenP, wKingP;
	static Piece wPawn1P, wPawn2P, wPawn3P, wPawn4P, wPawn5P, wPawn6P, wPawn7P, wPawn8P;
	
	public Main(){
		//sets up JPanel
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setVisible(true);
		
		//Loading in the images
		MediaTracker tracker = new MediaTracker(this);
		menuBackground = Toolkit.getDefaultToolkit().getImage("menuBackground.gif");
		tracker.addImage(menuBackground, 0);
		mainTitle = Toolkit.getDefaultToolkit().getImage("chessmasterLogo.png");
		tracker.addImage(mainTitle, 1);
		playButton = Toolkit.getDefaultToolkit().getImage("playButton.PNG");
		tracker.addImage(playButton, 2);
		aboutMeButton = Toolkit.getDefaultToolkit().getImage("aboutMeButton.PNG");
		tracker.addImage(aboutMeButton, 3);
		rulesButton = Toolkit.getDefaultToolkit().getImage("rulesButton.png");
		tracker.addImage(rulesButton, 4);
		exitButton = Toolkit.getDefaultToolkit().getImage("exitButton.PNG");
		tracker.addImage(exitButton, 5);
		aboutMe = Toolkit.getDefaultToolkit().getImage("aboutMe.png");
		tracker.addImage(aboutMe, 6);
		rules = Toolkit.getDefaultToolkit().getImage("rules.png");
		tracker.addImage(rules, 7);
		play = Toolkit.getDefaultToolkit().getImage("playScreen.png");
		tracker.addImage(play, 8);
		bRook1I = Toolkit.getDefaultToolkit().getImage("blackRook.PNG");
		tracker.addImage(bRook1I, 9);
		bRook2I = Toolkit.getDefaultToolkit().getImage("blackRook.PNG");
		tracker.addImage(bRook2I, 10);
		bKnight1I = Toolkit.getDefaultToolkit().getImage("blackKnight.PNG");
		tracker.addImage(bKnight1I, 11);
		bKnight2I = Toolkit.getDefaultToolkit().getImage("blackKnight.PNG");
		tracker.addImage(bKnight2I, 12);
		bBishop1I = Toolkit.getDefaultToolkit().getImage("blackBishop.PNG");
		tracker.addImage(bBishop1I, 13);
		bBishop2I = Toolkit.getDefaultToolkit().getImage("blackBishop.PNG");
		tracker.addImage(bBishop2I, 14);
		bQueenI = Toolkit.getDefaultToolkit().getImage("blackQueen.PNG");
		tracker.addImage(bQueenI, 15);
		bKingI = Toolkit.getDefaultToolkit().getImage("blackKing.PNG");
		tracker.addImage(bKingI, 16);
		bPawn1I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn1I, 17);
		bPawn2I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn2I, 18);
		bPawn3I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn3I, 19);
		bPawn4I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn4I, 20);
		bPawn5I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn5I, 21);
		bPawn6I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn6I, 22);
		bPawn7I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn7I, 23);
		bPawn8I = Toolkit.getDefaultToolkit().getImage("blackPawn.PNG");
		tracker.addImage(bPawn8I, 24);
		wRook1I = Toolkit.getDefaultToolkit().getImage("whiteRook.PNG");
		tracker.addImage(wRook1I, 25);
		wRook2I = Toolkit.getDefaultToolkit().getImage("whiteRook.PNG");
		tracker.addImage(wRook2I, 26);
		wKnight1I = Toolkit.getDefaultToolkit().getImage("whiteKnight.PNG");
		tracker.addImage(wKnight1I, 27);
		wKnight2I = Toolkit.getDefaultToolkit().getImage("whiteKnight.PNG");
		tracker.addImage(wKnight2I, 28);
		wBishop1I = Toolkit.getDefaultToolkit().getImage("whiteBishop.PNG");
		tracker.addImage(wBishop1I, 29);
		wBishop2I = Toolkit.getDefaultToolkit().getImage("whiteBishop.PNG");
		tracker.addImage(wBishop2I, 30);
		wQueenI = Toolkit.getDefaultToolkit().getImage("whiteQueen.PNG");
		tracker.addImage(wQueenI, 31);
		wKingI = Toolkit.getDefaultToolkit().getImage("whiteKing.PNG");
		tracker.addImage(wKingI, 32);
		wPawn1I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn1I, 33);
		wPawn2I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn2I, 34);
		wPawn3I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn3I, 35);
		wPawn4I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn4I, 36);
		wPawn5I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn5I, 37);
		wPawn6I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn6I, 38);
		wPawn7I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn7I, 39);
		wPawn8I = Toolkit.getDefaultToolkit().getImage("whitePawn.PNG");
		tracker.addImage(wPawn8I, 40);
		icon = Toolkit.getDefaultToolkit().getImage("icon.png");
		tracker.addImage(icon, 41);
		defaultIcon = Toolkit.getDefaultToolkit().getImage("defaultIcon.png");
		tracker.addImage(defaultIcon, 42);
		wWinScreen = Toolkit.getDefaultToolkit().getImage("whiteWinScreen.png");
		tracker.addImage(wWinScreen, 43);
		bWinScreen = Toolkit.getDefaultToolkit().getImage("blackWinScreen.png");
		tracker.addImage(bWinScreen, 44);
		whiteCheck = Toolkit.getDefaultToolkit().getImage("whiteCheck.png");
		tracker.addImage(whiteCheck, 45);
		blackCheck = Toolkit.getDefaultToolkit().getImage("blackCheck.png");
		tracker.addImage(blackCheck, 46);
		
		//White pieces
		wRook1P = new Piece(wRook1I, "W", 223, 492 + 58, true, false);
		//White Knight
		wKnight1P = new Piece(wKnight1I, "W", 293 + 5, 492 + 53, true, false);
		//White Bishop
		wBishop1P = new Piece(wBishop1I, "W", 363 + 5, 492 + 53, true, false);
		//White Queen
		wQueenP = new Piece(wQueenI, "W", 433 + 10, 492 + 48, true, false);
		//White King
		wKingP = new Piece(wKingI, "W", 503 + 5, 492 + 53, true, false);
		//White Bishop
		wBishop2P = new Piece(wBishop2I, "W", 573 + 5, 492 + 53, true, false);
		//White Knight
		wKnight2P = new Piece(wKnight2I, "W", 643 + 5, 492 + 53, true, false);
		//White Rook
		wRook2P = new Piece(wRook2I, "W", 713, 492 + 58, true, false);
		//White Pawns
		wPawn1P = new Piece(wPawn1I, "W", 233, 422 + 58, true, false);
		wPawn2P = new Piece(wPawn2I, "W", 303, 422 + 58, true, false);
		wPawn3P = new Piece(wPawn3I, "W", 373, 422 + 58, true, false);
		wPawn4P = new Piece(wPawn4I, "W", 443, 422 + 58, true, false);
		wPawn5P = new Piece(wPawn5I, "W", 513, 422 + 58, true, false);
		wPawn6P = new Piece(wPawn6I, "W", 583, 422 + 58, true, false);
		wPawn7P = new Piece(wPawn7I, "W", 653, 422 + 58, true, false);
		wPawn8P = new Piece(wPawn8I, "W", 723, 422 + 58, true, false);
		
		//Black pieces
		//Black Rook
		bRook1P = new Piece(bRook1I, "B", 223, 2 + 53, true, false);
		//Black Knight
		bKnight1P = new Piece(bKnight1I, "B", 293, 2 + 50, true, false);
		//Black Bishop
		bBishop1P = new Piece(bBishop1I, "B", 363, 2 + 50, true, false);
		//Black Queen
		bQueenP = new Piece(bQueenI, "B", 433, 2 + 50, true, false);
		//Black King
		bKingP = new Piece(bKingI, "B", 503, 2 + 50, true, false);
		//Black Bishop
		bBishop2P = new Piece(bBishop2I, "B", 573, 2 + 50, true, false);
		//Black Knight
		bKnight2P = new Piece(bKnight2I, "B", 643, 2 + 50, true, false);
		//Black Rook
		bRook2P = new Piece(bRook2I, "B", 713, 2 + 53, true, false);
		//Black Pawns
		bPawn1P = new Piece(bPawn1I, "B", 233, 72 + 53, true, false);
		bPawn2P = new Piece(bPawn2I, "B", 303, 72 + 53, true, false);
		bPawn3P = new Piece(bPawn3I, "B", 373, 72 + 53, true, false);
		bPawn4P = new Piece(bPawn4I, "B", 443, 72 + 53, true, false);
		bPawn5P = new Piece(bPawn5I, "B", 513, 72 + 53, true, false);
		bPawn6P = new Piece(bPawn6I, "B", 583, 72 + 53, true, false);
		bPawn7P = new Piece(bPawn7I, "B", 653, 72 + 53, true, false);
		bPawn8P = new Piece(bPawn8I, "B", 723, 72 + 53, true, false);
		
		//Initialize map
		map.put(1.1, wPawn1P); map.put(1.2, wPawn2P); map.put(1.3, wPawn3P); map.put(1.4, wPawn4P);
		map.put(1.5, wPawn5P); map.put(1.6, wPawn6P); map.put(1.7, wPawn7P); map.put(1.8, wPawn8P);
		map.put(2.1, wRook1P); map.put(2.2, wRook2P);
		map.put(3.1, wKnight1P); map.put(3.2, wKnight2P);
		map.put(4.1, wBishop1P); map.put(4.2, wBishop2P);
		map.put(5.0, wQueenP);
		map.put(6.0, wKingP);
		
		map.put(7.1, bPawn1P); map.put(7.2, bPawn2P); map.put(7.3, bPawn3P); map.put(7.4, bPawn4P);
		map.put(7.5, bPawn5P); map.put(7.6, bPawn6P); map.put(7.7, bPawn7P); map.put(7.8, bPawn8P);
		map.put(8.1, bRook1P); map.put(8.2, bRook2P);
		map.put(9.1, bKnight1P); map.put(9.2, bKnight2P);
		map.put(10.1, bBishop1P); map.put(10.2, bBishop2P);
		map.put(11.0, bQueenP);
		map.put(12.0, bKingP);
		
		//Initialize borders
		borders[0][0] = new Border(227, 298, 123, 53);
		for(int i = 1; i < 8; i++) borders[i][0] = new Border(227, 298, borders[i - 1][0].y1 + 70, borders[i - 1][0].y2 + 70);
		for(int i = 1; i < 8; i++) borders[0][i] = new Border(borders[0][i - 1].x1 + 71, borders[0][i - 1].x2 + 71, 123, 53);
		for(int i = 1; i < 8; i++) {
			for(int j = 1; j < 8; j++) {
				borders[i][j] = new Border(borders[i][j - 1].x1 + 71, borders[i][j - 1].x2 + 71, borders[i - 1][j].y1 + 70, borders[i - 1][j].y2 + 70);
			}
		}
		
		// Wait until all of the images are loaded
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
		}
		
		//Main menu and timer settings menu
		JMenuItem one, three, ten;
		one = new JMenuItem("1 min");
		three = new JMenuItem("3 mins");
		ten = new JMenuItem("10 mins");
		
		JMenu timerSettings = new JMenu("Timer Settings");
		timerSettings.add(one);
		timerSettings.add(three);
		timerSettings.add(ten);
		
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(timerSettings);
		
		// Set the menu bar for this frame to mainMenu
		frame.setJMenuBar(mainMenu);
		
		one.setActionCommand("One");
		one.addActionListener(this);
		three.setActionCommand("Three");
		three.addActionListener(this);
		ten.setActionCommand("Ten");
		ten.addActionListener(this);
		
		//Adding sound files into game
		try {
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File("menuMusic.wav"));
			menuMusic = AudioSystem.getClip();
			menuMusic.open(sound);
			sound = AudioSystem.getAudioInputStream(new File("playMusic.wav"));
			playMusic = AudioSystem.getClip();
			playMusic.open(sound);
			sound = AudioSystem.getAudioInputStream(new File("moveMusic.wav"));
			moveMusic = AudioSystem.getClip();
			moveMusic.open(sound);
			menuMusic.start();
		} catch (Exception e) {
		}
		/*
		 White:
		 1.1 = pawn1, 1.2 = pawn2, 1.3 = pawn3, 1.4 = pawn4, 1.5 = pawn5, 1.6 = pawn6, 1.7 = pawn7, 1.8 = pawn8;
		 2.1 = rook1, 2.2 = rook2
		 3.1 = knight1, 3.2 = knight2
		 4.1 = bishop1, 4.2 = bishop2
		 5 = queen
		 6 = king
		 
		 Black:
		 7.1 = pawn1, 7.2 = pawn2, 7.3 = pawn3, 7.4 = pawn4, 7.5 = pawn5, 7.6 = pawn6, 7.7 = pawn7, 7.8 = pawn8;
		 8.1 = rook1, 8.2 = rook2
		 9.1 = knight1, 9.2 = knight2
		 10.1 = bishop1, 10.2 = bishop2
		 11 = queen
		 12 = king
		 */
		//Reset everything
		reset();
		//Start the new thread
		thread = new Thread(this);
		thread.start();
	}
	public static void main(String[] args){
		//The following lines creates your window
		//makes a brand new JFrame
		frame = new JFrame ("Chess Master");
		//makes a new copy of your "game" that is also a JPanel
		Main myPanel = new Main ();
		//so your JPanel to the frame so you can actually see it

		frame.add(myPanel);
		//so you can actually get keyboard input
		frame.addKeyListener(myPanel);
		//so you can actually get mouse input
		frame.addMouseListener(myPanel);
		//self explanatory. You want to see your frame
		frame.setVisible(true);
		//some weird method that you must run
		frame.pack();
		//place your frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		//without this, your thread will keep running even when you windows is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//self explanatory. You don't want to resize your window because
		//it might mess up your graphics and collisions
		frame.setResizable(false);

	}
	//Description: Draws images onto screen.
	//Parameters: The graphic variable, g used for drawing.
	//Return: nothing.
	public void paintComponent(Graphics g) {
		//1 is main, 2 is play, 3 is about, 4 is rules
		super.paintComponent(g);
		repaint();
		if (screen == 1) {
			//Main menu background
			g.drawImage(menuBackground, 0, 0, this);
			//Main title
			g.drawImage(mainTitle, 180, 10, this);
			//Play button
			g.drawImage(playButton, playX, playY, this);
			//About me button
			g.drawImage(aboutMeButton, playX, playY + 110, this);
			//Rules button
			g.drawImage(rulesButton, playX, playY + 220, this);
			//Exit button
			g.drawImage(exitButton, playX, playY + 330, this);
		}else if(screen == 2) {
			//Play
			//Loop music
			if(playMusic.getFramePosition() == playMusic.getFrameLength()) {
				playMusic.setFramePosition(0);
				playMusic.start();
			}
			//If a piece is being moved...
			if(move) {
				Piece curr = map.get(selectedPiece);
				//Castling
				//White
				if(selectedPiece == 6 && ind1 == 7 && ind2 == 2 && !map.get(6.0).moved) {
					//Left rook
					if(board[7][1] == 0 && board[7][3] == 0 && board[7][2] == 6 && !map.get(2.1).moved) {
						wRook1P.i = borders[7][3].x1; wRook1P.j = borders[7][3].y2;
						wKingP.i = borders[7][2].x1; wKingP.j = borders[7][2].y2;
						wKingP.moved = true; wRook1P.moved = true;
						board[7][2] = 6;
						board[7][4] = 0;
						board[7][3] = 2.2;
						board[7][0] = 0;
					}
				}else if(selectedPiece == 6 && ind1 == 7 && ind2 == 6 && !map.get(6.0).moved) {
					//Right rook
					if(board[7][5] == 0 && board[7][6] == 6 && !map.get(2.2).moved) {
						wRook2P.i = borders[7][5].x1; wRook2P.j = borders[7][5].y2;
						wKingP.i = borders[7][6].x1; wKingP.j = borders[7][6].y2;
						wKingP.moved = true; wRook2P.moved = true;
						board[7][6] = 6;
						board[7][4] = 0;
						board[7][5] = 2.2;
						board[7][7] = 0;
					}
				}else if(selectedPiece == 12 && ind1 == 0 && ind2 == 2 && !map.get(12.0).moved) {
					//Black
					//Left rook
					if(board[0][1] == 0 && board[0][3] == 0 && board[0][2] == 12 && !map.get(8.1).moved) {
						bRook1P.i = borders[0][3].x1; bRook2P.j = borders[0][3].y2;
						bKingP.i = borders[0][2].x1; bKingP.j = borders[0][2].y2;
						bKingP.moved = true; bRook1P.moved = true;
						board[0][2] = 12;
						board[0][4] = 0;
						board[0][3] = 8.1;
						board[0][0] = 0;
					}
				}else if(selectedPiece == 12 && ind1 == 0 && ind2 == 6 && !map.get(12.0).moved) {
					//Right rook
					if(board[0][5] == 0 && board[0][6] == 12 && !map.get(8.2).moved) {
						bRook2P.i = borders[0][5].x1; bRook2P.j = borders[0][5].y2;
						bKingP.i = borders[0][6].x1; bKingP.j = borders[0][6].y2;
						bKingP.moved = true; bRook2P.moved = true;
						board[0][6] = 12;
						board[0][4] = 0;
						board[0][5] = 8.2;
						board[0][7] = 0;
					}
				}else{
					//Not castling
					curr.i = borders[ind1][ind2].x1;
					curr.j = borders[ind1][ind2].y2;
					curr.moved = true;
				}
				//Play move music and set move to false
				moveMusic.setFramePosition(0);
				moveMusic.start();
				move = false;
			}
			//Draw play screen
			g.drawImage(play, 0, 0, this);
			// White Rook
			if(wRook1P.alive) {
				g.drawImage(wRook1I, wRook1P.i, wRook1P.j - 58, this);
			}
			// White Knight
			if(wKnight1P.alive) {
				g.drawImage(wKnight1I, wKnight1P.i - 5, wKnight1P.j - 53, this);
			}
			// White Bishop
			if(wBishop1P.alive) {
				g.drawImage(wBishop1I, wBishop1P.i - 5, wBishop1P.j - 53, this);
			}
			// White Queen
			if(wQueenP.alive) {
				g.drawImage(wQueenI, wQueenP.i - 10, wQueenP.j - 48, this);
			}
			// White King
			if(wKingP.alive) {
				g.drawImage(wKingI, wKingP.i - 5, wKingP.j - 53, this);
			}
			// White Bishop
			if(wBishop2P.alive) {
				g.drawImage(wBishop2I, wBishop2P.i - 5, wBishop2P.j - 53, this);
			}
			// White Knight
			if(wKnight2P.alive) {
				g.drawImage(wKnight2I, wKnight2P.i - 5, wKnight2P.j - 53, this);
			}
			// White Rook
			if(wRook2P.alive) {
				g.drawImage(wRook2I, wRook2P.i, wRook2P.j - 58, this);
			}
			// White Pawns
			if(wPawn1P.alive) {
				g.drawImage(wPawn1I, wPawn1P.i, wPawn1P.j - 58, this);
			}
			if(wPawn2P.alive) {
				g.drawImage(wPawn2I, wPawn2P.i, wPawn2P.j - 58, this);
			}
			if(wPawn3P.alive) {
				g.drawImage(wPawn3I, wPawn3P.i, wPawn3P.j - 58, this);
			}
			if(wPawn4P.alive) {
				g.drawImage(wPawn4I, wPawn4P.i, wPawn4P.j - 58, this);
			}
			if(wPawn5P.alive) {
				g.drawImage(wPawn5I, wPawn5P.i, wPawn5P.j - 58, this);
			}
			if(wPawn6P.alive) {
				g.drawImage(wPawn6I, wPawn6P.i, wPawn6P.j - 58, this);
			}
			if(wPawn7P.alive) {
				g.drawImage(wPawn7I, wPawn7P.i, wPawn7P.j - 58, this);
			}
			if(wPawn8P.alive) {
				g.drawImage(wPawn8I, wPawn8P.i, wPawn8P.j - 58, this);
			}
			
			// Black Rook
			if(bRook1P.alive) {
				g.drawImage(bRook1I, bRook1P.i, bRook1P.j - 53, this);
			}
			// Black Knight
			if(bKnight1P.alive) {
				g.drawImage(bKnight1I, bKnight1P.i, bKnight1P.j - 50, this);
			}
			// Black Bishop
			if(bBishop1P.alive) {
				g.drawImage(bBishop1I, bBishop1P.i, bBishop1P.j - 50, this);
			}
			// Black Queen
			if(bQueenP.alive) {
				g.drawImage(bQueenI, bQueenP.i, bQueenP.j - 50, this);
			}
			// Black King
			if(bKingP.alive) {
				g.drawImage(bKingI, bKingP.i, bKingP.j - 50, this);
			}
			// Black Bishop
			if(bBishop2P.alive) {
				g.drawImage(bBishop2I, bBishop2P.i, bBishop2P.j - 50, this);
			}
			// Black Knight
			if(bKnight2P.alive) {
				g.drawImage(bKnight2I, bKnight2P.i, bKnight2P.j - 50, this);
			}
			// Black Rook
			if(bRook2P.alive) {
				g.drawImage(bRook2I, bRook2P.i, bRook2P.j - 53, this);
			}
			// Black Pawns
			if(bPawn1P.alive) {
				g.drawImage(bPawn1I, bPawn1P.i, bPawn1P.j - 53, this);
			}
			if(bPawn2P.alive) {
				g.drawImage(bPawn2I, bPawn2P.i, bPawn2P.j - 53, this);
			}
			if(bPawn3P.alive) {
				g.drawImage(bPawn3I, bPawn3P.i, bPawn3P.j - 53, this);
			}
			if(bPawn4P.alive) {
				g.drawImage(bPawn4I, bPawn4P.i, bPawn4P.j - 53, this);
			}
			if(bPawn5P.alive) {
				g.drawImage(bPawn5I, bPawn5P.i, bPawn5P.j - 53, this);
			}
			if(bPawn6P.alive) {
				g.drawImage(bPawn6I, bPawn6P.i, bPawn6P.j - 53, this);
			}
			if(bPawn7P.alive) {
				g.drawImage(bPawn7I, bPawn7P.i, bPawn7P.j - 53, this);
			}
			if(bPawn8P.alive) {
				g.drawImage(bPawn8I, bPawn8P.i, bPawn8P.j - 53, this);
			}
			//Set font and background colour
			g.setFont(myFont);
			setForeground(Color.black);
			//Draw the number of captured pieces in each team
			g.drawString("" + wCap, 105, 470);
			g.drawString("" + bCap, 895, 175);
			//White timer
			int wMin = wTimer / 60, wSec = wTimer % 60;
			g.drawString(String.format("%02d:%02d", wMin, wSec), 65, 405);
			//Black timer
			int bMin = bTimer / 60, bSec = bTimer % 60;
			g.drawString(String.format("%02d:%02d", bMin, bSec), 855, 110);
			
			//Replacing danger array
			replaceDanger();
			
			//If white king is in danger...
			if(kingInDanger() == 0) {
				int cOrM = checkOrMateOnW();
				if(cOrM == 2) {
					//Checkmate
					win = 1;
					bScore++;
					checkmateToggle = 1;
				}else if(cOrM == 1) {
					//Check
					g.drawImage(blackCheck, 0, 0, this);
				}
			}else if(kingInDanger() == 1) {
				//If black king is in danger...
				int cOrM = checkOrMateOnB();
				if(cOrM == 2) {
					//Checkmate
					win = 0;
					wScore++;
					checkmateToggle = 1;
				}else if(cOrM == 1) {
					//Check
					g.drawImage(whiteCheck, 0, 0, this);
				}
			}		
			if(win == 0) {
				//White wins
				g.drawImage(wWinScreen, 0, 0, this);
				g.drawString("" + wScore, 407, 370);
				g.drawString("" + bScore, 583, 370);
			}else if(win == 1) {
				//Black wins
				g.drawImage(bWinScreen, 0, 0, this);
				g.drawString("" + bScore, 407, 370);
				g.drawString("" + wScore, 583, 370);
			}
		}else if(screen == 3) {
			//About
			g.drawImage(aboutMe, 0, 0, this);
		}else if(screen == 4) {
			//Rules
			g.drawImage(rules, 0, 0, this);
		}
	}
	//Description: Tracks when mouse is clicked.
	//Paramters: Variable, e used for tracking the mouse when it is clicked.
	//Return: nothing.
	@Override
	public void mouseClicked(MouseEvent e) {
		//1 is main, 2 is play, 3 is about, 4 is rules
		int x = e.getX(), y = e.getY();
		if(screen == 1) {
			if(x >= playX + 10 && x <= playX + 230 && y >= playY + 40 && y <= playY + 133) {
				//Chess 1v1 screen
				menuMusic.stop();
				playMusic.setFramePosition (0);
				playMusic.start();
				reset();
				wScore = 0; bScore = 0;
				screen = 2;
			}else if(x >= playX + 10 && x <= playX + 230 && y >= playY+ 148 && y <= playY+ 133 + 105) {
				//About me screen
				screen = 3;
			}else if(x >= playX + 10 && x <= playX + 230 && y >= playY+ 148 + 110 && y <= playY+ 133 + 105 + 105) {
				//Rules screen
				screen = 4;
			}else if(x >= playX + 10 && x <= playX + 230 && y >= playY+ 148 + 110 + 108 && y <= playY+ 133 + 105 + 105 + 110) {
				System.exit(0);
			}
		}else if(screen == 2) {
			if(x >= 801 && x <= 1001 && y >= 531 && y <= 609) {
				//Exit button
				playMusic.stop();
				menuMusic.setFramePosition(0);
				menuMusic.start();
				screen = 1;
			}else if(x >= 801 && x <= 1001 && y >= 448 && y <= 526) {
				//Restart button
				playMusic.setFramePosition (0);
				playMusic.start();
				reset();
			}else if(win == -1 && x >= 64 && x <= 192 && y >= 354 && y <= 402) {
				//White resign button
				bScore++;
				win = 1;
			}else if(win == -1 && x >= 855 && x <= 982 && y >= 251 && y <= 300) {
				//Black resign button
				wScore++;
				win = 0;
			}else if(win >= 0 && x >= 642 && x <= 678 && y >= 225 && y <= 260) {
				//Spectate after exiting the win screen
				win = -2;
			}else if(win == -1){
				//Finding borders for destination selected positions
loop:			for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						Border b = borders[i][j];
						if(x >= b.x1 && x <= b.x2 && y >= b.y2 && y <= b.y1) {
							ind1 = i; ind2 = j;
							break loop;
						}
					}
				}
				if(selected) {
				//If they click a valid box, move it
					if(isValid(ind1, ind2)) {
						boolean ok = true;
						//Check on white and forcing player to block
						if(turn && checkOrMateOnW() == 1) {
							int origY = selectedInd1, origX = selectedInd2, destY = ind1, destX = ind2;
							double origPiece = board[origY][origX], destPiece = board[destY][destX];
							int[][] dangerOnW = dangerForW, dangerOnB = dangerForB;
							
							board[origY][origX] = 0;
							board[destY][destX] = origPiece;
							replaceDanger();
							
							if(tmpCheckOrMateOnW() >= 1) ok = false;
							
							board[origY][origX] = origPiece;
							board[destY][destX] = destPiece;
							dangerForW = dangerOnW; dangerForB = dangerOnB;
							
						}if(!turn && checkOrMateOnB() == 1) {
							//Check on black and forcing player to block
							int origY = selectedInd1, origX = selectedInd2, destY = ind1, destX = ind2;
							double origPiece = board[origY][origX], destPiece = board[destY][destX];
							int[][] dangerOnW = dangerForW, dangerOnB = dangerForB;
							
							board[origY][origX] = 0;
							board[destY][destX] = origPiece;
							replaceDanger();
							
							if(tmpCheckOrMateOnB() >= 1) ok = false;
							
							board[origY][origX] = origPiece;
							board[destY][destX] = destPiece;
							dangerForW = dangerOnW; dangerForB = dangerOnB;
						}
						if(ok) {
							//If it's ok to move...
							if(win == 0) wScore++;
							if(win == 1) bScore++;
							//Replace danger
							replaceDanger();
							if(board[ind1][ind2] > 0) {
								map.get(board[ind1][ind2]).alive = false;
								if(board[ind1][ind2] >= 1 && board[ind1][ind2] <= 6) bCap++;
								else wCap++;
							}
							selectedPiece = board[selectedInd1][selectedInd2];
							board[ind1][ind2] = board[selectedInd1][selectedInd2];
							board[selectedInd1][selectedInd2] = 0;
							move = true;
							turn = !turn;
						}
					}
					//If a piece is not selected, go back to default cursor
					Point hotspot = new Point (0, 0);
					Toolkit toolkit = Toolkit.getDefaultToolkit ();
					cursor = toolkit.createCustomCursor (defaultIcon, hotspot, "defIcon");
					frame.setCursor (cursor);
					selected = false;
				}else if(board[ind1][ind2] > 0) {
					//If a piece is being selected...
					if((turn && board[ind1][ind2] >= 1 && board[ind1][ind2] <= 6)
					|| (!turn && board[ind1][ind2] >= 7 && board[ind1][ind2] <= 12)) {
						Point hotspot = new Point (0, 0);
						Toolkit toolkit = Toolkit.getDefaultToolkit ();
						cursor = toolkit.createCustomCursor (icon, hotspot, "icon");
						frame.setCursor (cursor);
						//Check whether a piece occupies at board[ind1][ind2]
						//If so, set "selected" to true
						selected = true;
						//Create list for valid moves
						replaceDanger();
						valid = getValid(ind1, ind2);
						selectedInd1 = ind1; selectedInd2 = ind2;
					}
				}
			}
		}else if(screen == 3) {
			//About me screen
			if(x >= playX + 18 && x <= playX + 220 && y >= playY+ 148 + 110 + 110 && y <= playY + 133 + 105 + 105 + 105) {
				screen = 1;
			}
		}else if(screen == 4) {
			//Rules screen
			if(x >= 438 && x <= 580 && y >= 534 && y <= 581) {
				screen = 1;
			}
		}
	}
	//Description: resets all variables and data structures.
	//Parameters: none.
	//Return: none.
	public void reset() {
		board[0][0] = 8.1; board[0][1] = 9.1; board[0][2] = 10.1; board[0][3] = 11; board[0][4] = 12; board[0][5] = 10.2; board[0][6] = 9.2; board[0][7] = 8.2;
		board[1][0] = 7.1; board[1][1] = 7.2; board[1][2] = 7.3; board[1][3] = 7.4; board[1][4] = 7.5; board[1][5] = 7.6; board[1][6] = 7.7; board[1][7] = 7.8;
		
		board[6][0] = 1.1; board[6][1] = 1.2; board[6][2] = 1.3; board[6][3] = 1.4; board[6][4] = 1.5; board[6][5] = 1.6; board[6][6] = 1.7; board[6][7] = 1.8;
		board[7][0] = 2.1; board[7][1] = 3.1; board[7][2] = 4.1; board[7][3] = 5; board[7][4] = 6; board[7][5] = 4.2; board[7][6] = 3.2; board[7][7] = 2.2;
		
		for(int i = 2; i < 6; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = 0;
			}
		}
		
		turn = true;
		wCap = 0; bCap = 0;
		wTimer = 300; bTimer = 300;
		win = -1;
		checkmateToggle = 0;
		attackingWKing = new int[2]; attackingBKing = new int[2];
		Arrays.fill(attackingWKing, -1); Arrays.fill(attackingBKing, -1);
		replaceDanger();
		
		//White pieces
		wRook1P.i = 223; wRook1P.j = 492 + 58; wRook1P.alive = true; wRook1P.moved = false;
		//White Knight
		wKnight1P.i = 293 + 5; wKnight1P.j = 492 + 53; wKnight1P.alive = true; wKnight1P.moved = false;
		//White Bishop
		wBishop1P.i = 363 + 5; wBishop1P.j = 492 + 53; wBishop1P.alive = true; wBishop1P.moved = false;
		//White Queen
		wQueenP.i = 433 + 10; wQueenP.j = 492 + 48; wQueenP.alive = true; wQueenP.moved = false;
		//White King
		wKingP.i = 503 + 5; wKingP.j = 492 + 53; wKingP.alive = true; wKingP.moved = false;
		//White Bishop
		wBishop2P.i = 573 + 5; wBishop2P.j = 492 + 53; wBishop2P.alive = true; wBishop2P.moved = false;
		//White Knight
		wKnight2P.i = 643 + 5; wKnight2P.j = 492 + 53; wKnight2P.alive = true; wKnight2P.moved = false;
		//White Rook
		wRook2P.i = 713; wRook2P.j = 492 + 58; wRook2P.alive = true; wRook2P.moved = false;
		//White Pawns
		wPawn1P.i = 233; wPawn1P.j = 422 + 58; wPawn1P.alive = true; wPawn1P.moved = false;
		wPawn2P.i = 303; wPawn2P.j = 422 + 58; wPawn2P.alive = true; wPawn2P.moved = false;
		wPawn3P.i = 373; wPawn3P.j = 422 + 58; wPawn3P.alive = true; wPawn3P.moved = false;
		wPawn4P.i = 443; wPawn4P.j = 422 + 58; wPawn4P.alive = true; wPawn4P.moved = false;
		wPawn5P.i = 513; wPawn5P.j = 422 + 58; wPawn5P.alive = true; wPawn5P.moved = false;
		wPawn6P.i = 583; wPawn6P.j = 422 + 58; wPawn6P.alive = true; wPawn6P.moved = false;
		wPawn7P.i = 653; wPawn7P.j = 422 + 58; wPawn7P.alive = true; wPawn7P.moved = false;
		wPawn8P.i = 723; wPawn8P.j = 422 + 58; wPawn8P.alive = true; wPawn8P.moved = false;
		
		//Black pieces
		//Black Rook
		bRook1P.i = 223; bRook1P.j = 2 + 53; bRook1P.alive = true; bRook1P.moved = false;
		//Black Knight
		bKnight1P.i = 293; bKnight1P.j = 2 + 50; bKnight1P.alive = true; bKnight1P.moved = false;
		//Black Bishop
		bBishop1P.i = 363; bBishop1P.j = 2 + 50; bBishop1P.alive = true; bBishop1P.moved = false;
		//Black Queen
		bQueenP.i = 433; bQueenP.j = 2 + 50; bQueenP.alive = true; bQueenP.moved = false;
		//Black King
		bKingP.i = 503; bKingP.j = 2 + 50; bKingP.alive = true; bKingP.moved = false;
		//Black Bishop
		bBishop2P.i = 573; bBishop2P.j = 2 + 50; bBishop2P.alive = true; bBishop2P.moved = false;
		//Black Knight
		bKnight2P.i = 643; bKnight2P.j = 2 + 50; bKnight2P.alive = true; bKnight2P.moved = false;
		//Black Rook
		bRook2P.i = 713; bRook2P.j = 2 + 53; bRook2P.alive = true; bRook2P.moved = false;
		//Black Pawns
		bPawn1P.i = 233; bPawn1P.j = 72 + 53; bPawn1P.alive = true; bPawn1P.moved = false;
		bPawn2P.i = 303; bPawn2P.j = 72 + 53; bPawn2P.alive = true; bPawn2P.moved = false;
		bPawn3P.i = 373; bPawn3P.j = 72 + 53; bPawn3P.alive = true; bPawn3P.moved = false;
		bPawn4P.i = 443; bPawn4P.j = 72 + 53; bPawn4P.alive = true; bPawn4P.moved = false;
		bPawn5P.i = 513; bPawn5P.j = 72 + 53; bPawn5P.alive = true; bPawn5P.moved = false;
		bPawn6P.i = 583; bPawn6P.j = 72 + 53; bPawn6P.alive = true; bPawn6P.moved = false;
		bPawn7P.i = 653; bPawn7P.j = 72 + 53; bPawn7P.alive = true; bPawn7P.moved = false;
		bPawn8P.i = 723; bPawn8P.j = 72 + 53; bPawn8P.alive = true; bPawn8P.moved = false;
	}
	//Description: Checking for a check or a checkmate on White without changing outside variables & data structures.
	//Paramters: none.
	//Return: 1 = check, 2 = checkmate, -1 for none
	public int tmpCheckOrMateOnW() {
		//If white king is in danger...
		if(kingInDanger() == 0) {
			int[] pos = getWKingPos();
			//If king can't move & no pieces are blocking(Check if blocks between attacking piece & king are dangerForB[i1][i2] = -1
			if(getValid(pos[0], pos[1]).size() == 0) {
				//Check for if a king is being attacked
				findAttacker();
				int attackerY = attackingWKing[0], attackerX = attackingWKing[1];
				if(attackerY == -1 && attackerX == -1) return -1;
				int yDir = 0, xDir = 0; //0 for same, 1 for inc, -1 for dec
				if(attackerY > pos[0]) yDir = -1;
				else if(attackerY < pos[0]) yDir = 1;
				else if(attackerY == pos[0]) yDir = 0;

				if(attackerX > pos[1]) xDir = -1;
				else if(attackerX < pos[1]) xDir = 1;
				else if(attackerX == pos[1]) xDir = 0;

				int x = attackerX, y = attackerY;
				boolean checkmate = true;
				while(true) {
					if(y == pos[0] && x == pos[1]) break;
					if(dangerForB[y][x] == -1) checkmate = false;

					if(yDir == 1) y++;
					else if(yDir == -1) y--;

					if(xDir == 1) x++;
					else if(xDir == -1) x--;
				}
				if(checkmate && checkmateToggle == 0) {
					return 2;
				}else if(checkmateToggle == 0) return 1;
			}
		}else return -1;
		return -1;
	}
	//Description: Checking for a check or a checkmate on Black without changing outside variables & data structures.
	//Paramters: none.
	//Return: 1 = check, 2 = checkmate, -1 for none
	public int tmpCheckOrMateOnB() {
		//1 for check, 2 for checkmate, -1 for none
		if(kingInDanger() == 1) {
			int[] pos = getBKingPos();
			//If king can't move & no pieces are blocking(Check if blocks between attacking piece & king are dangerForB[i1][i2] = -1
			if(getValid(pos[0], pos[1]).size() == 0) {
				//Check for if a king is being attacked
				findAttacker();
				int attackerY = attackingBKing[0], attackerX = attackingBKing[1];
				if(attackerY == -1 && attackerX == -1) return -1;
				int yDir = 0, xDir = 0; //0 for same, 1 for inc, -1 for dec
				if(attackerY > pos[0]) yDir = -1;
				else if(attackerY < pos[0]) yDir = 1;
				else if(attackerY == pos[0]) yDir = 0;

				if(attackerX > pos[1]) xDir = -1;
				else if(attackerX < pos[1]) xDir = 1;
				else if(attackerX == pos[1]) xDir = 0;

				int y = attackerY, x = attackerX;
				boolean checkmate = true;
				while(true) {
					if(y == pos[0] && x == pos[1]) break;
					if(Math.abs(y - pos[0]) == 1 && Math.abs(x - pos[1]) == 1) {
						//Check if any black piece(except for king) can attack (x, y)
						loop:						for(int i = 0; i < 8; i++) {
							for(int j = 0; j < 8; j++) {
								if(board[i][j] >= 7 && board[i][j] <= 11) {
									for(int[] v : getValid(i, j)) {
										if(v[0] == y && v[1] == x) {
											checkmate = false;
											break loop;
										}
									}
								}
							}
						}
					}else if(dangerForW[y][x] == -1) {
						checkmate = false;
					}
					if(yDir == 1) y++;
					else if(yDir == -1) y--;

					if(xDir == 1) x++;
					else if(xDir == -1) x--;
				}
				if(checkmate && checkmateToggle == 0) {
					return 2;
				}else if(checkmateToggle == 0) return 1;
			}else {
				return 1;
			}
		}else return -1;
		return -1;
	}
	//Description: Checking for a check or a checkmate on White
	//Paramters: none.
	//Return: 1 = check, 2 = checkmate, -1 for none
	public int checkOrMateOnW() {
		//1 = check, 2 = checkmate, -1 for none
		if(kingInDanger() == 0) {
			int[] pos = getWKingPos();
			//If king can't move & no pieces are blocking(Check if blocks between attacking piece & king are dangerForB[i1][i2] = -1
			if(getValid(pos[0], pos[1]).size() == 0) {
				//Check for if a king is being attacked
				findAttacker();
				int attackerY = attackingWKing[0], attackerX = attackingWKing[1];
				if(attackerY == -1 && attackerX == -1) return -1;
				int yDir = 0, xDir = 0; //0 for same, 1 for inc, -1 for dec
				if(attackerY > pos[0]) yDir = -1;
				else if(attackerY < pos[0]) yDir = 1;
				else if(attackerY == pos[0]) yDir = 0;

				if(attackerX > pos[1]) xDir = -1;
				else if(attackerX < pos[1]) xDir = 1;
				else if(attackerX == pos[1]) xDir = 0;

				int x = attackerX, y = attackerY;
				boolean checkmate = true;
				while(true) {
					if(y == pos[0] && x == pos[1]) break;
					if(dangerForB[y][x] == -1) checkmate = false;

					if(yDir == 1) y++;
					else if(yDir == -1) y--;

					if(xDir == 1) x++;
					else if(xDir == -1) x--;
				}
				if(checkmate && checkmateToggle == 0) {
					return 2;
				}else if(checkmateToggle == 0) return 1;
			}
		}else return -1;
		return -1;
	}
	//Description: Checking for a check or a checkmate on Black
	//Paramters: none.
	//Return: 1 = check, 2 = checkmate, -1 for none
	public int checkOrMateOnB() {
		//1 for check, 2 for checkmate, -1 for none
		if(kingInDanger() == 1) {
			int[] pos = getBKingPos();
			//If king can't move & no pieces are blocking(Check if blocks between attacking piece & king are dangerForB[i1][i2] = -1
			if(getValid(pos[0], pos[1]).size() == 0) {
				//Check for if a king is being attacked
				findAttacker();
				int attackerY = attackingBKing[0], attackerX = attackingBKing[1];
				if(attackerY == -1 && attackerX == -1) return -1;
				int yDir = 0, xDir = 0; //0 for same, 1 for inc, -1 for dec
				if(attackerY > pos[0]) yDir = -1;
				else if(attackerY < pos[0]) yDir = 1;
				else if(attackerY == pos[0]) yDir = 0;

				if(attackerX > pos[1]) xDir = -1;
				else if(attackerX < pos[1]) xDir = 1;
				else if(attackerX == pos[1]) xDir = 0;

				int y = attackerY, x = attackerX;
				boolean checkmate = true;
				while(true) {
					if(y == pos[0] && x == pos[1]) break;
					if(Math.abs(y - pos[0]) == 1 && Math.abs(x - pos[1]) == 1) {
						//Check if any black piece(except for king) can attack (x, y)
				 loop:for(int i = 0; i < 8; i++) {
							for(int j = 0; j < 8; j++) {
								if(board[i][j] >= 7 && board[i][j] <= 11) {
									for(int[] v : getValid(i, j)) {
										if(v[0] == y && v[1] == x) {
											checkmate = false;
											break loop;
										}
									}
								}
							}
						}
					}else if(dangerForW[y][x] == -1) {
						checkmate = false;
					}
					if(yDir == 1) y++;
					else if(yDir == -1) y--;

					if(xDir == 1) x++;
					else if(xDir == -1) x--;
				}
				if(checkmate && checkmateToggle == 0) {
					return 2;
				}else if(checkmateToggle == 0) return 1;
			}else {
				return 1;
			}
		}else return -1;
		return -1;
	}
	//Description: Finding the attacking piece's location when a king is being checked.
	//Paramters: none.
	//Return: none.
	public void findAttacker() {
		boolean found = false;
		int[] pos = new int[2];
loop:	for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				ArrayList<int[]> valid = getValid(i, j);
				//White
				if(board[i][j] >= 1 && board[i][j] <= 6) {
					pos = getBKingPos();
					for(int[] a : valid) {
						if(a[0] == pos[0] && a[1] == pos[1]) {
							attackingBKing[0] = i; attackingBKing[1] = j;
							found = true;
							break loop;
						}
					}
				}
				//Black
				if(board[i][j] >= 7 && board[i][j] <= 12) {
					pos = getWKingPos();
					for(int[] a : valid) {
						if(a[0] == pos[0] && a[1] == pos[1]) {
							attackingWKing[0] = i; attackingWKing[1] = j;
							found = true;
							break loop;
						}
					}
				}
			}
		}
		//If nothing is attacking a king
		if(!found) {
			attackingWKing[0] = -1; attackingWKing[1] = -1;
			attackingBKing[0] = -1; attackingBKing[1] = -1;
		}
	}
	//Description: Replacing the danger array.
	//Paramters: none.
	//Return: none.
	public void replaceDanger() {
		dangerForW = new int[8][8]; dangerForB = new int[8][8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] > 0) {
					//There's a piece
					for(int[] a : getValid(i, j)) {
						if(board[i][j] >= 1 && board[i][j] <= 6) dangerForB[a[0]][a[1]] = -1;
						if(board[i][j] >= 7 && board[i][j] <= 12) dangerForW[a[0]][a[1]] = -1;
					}
				}
			}
		}
		//Extra code to fix the black king checkmate bug
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] >= 1 && board[i][j] <= 6) {
					for(int[] a : getValid(i, j)) {
						dangerForB[a[0]][a[1]] = -1;
					}
				}
			}
		}
	}
	//Description: Finding if a king is in danger or not.
	//Paramters: none.
	//Return: -1 for not in danger, 0 for white king in danger, 1 for black king in danger.
	public int kingInDanger() {
		int[] wKingPos = getWKingPos();
		int wI = wKingPos[0], wJ = wKingPos[1];
		
		int[] bKingPos = getBKingPos();
		int bI = bKingPos[0], bJ = bKingPos[1];
		
		replaceDanger();
		
		if(dangerForW[wI][wJ] == -1) return 0;
		if(dangerForB[bI][bJ] == -1) return 1;
		return -1;
	}
	//Description: Gets the position on the board for the white king.
	//Parameters: none.
	//Return: indices of the white king as a pair.
	public int[] getWKingPos() {
		//Find wKing's location on board
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] == 6) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
	//Description: Gets the position on the board for the black king.
	//Parameters: none.
	//Return: indices of the black king as a pair.
	public int[] getBKingPos() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == 12) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
	//Description: Printing the board array(for debugging purposes)
	//Parameters: none.
	//Return: none.
	public void printBoard() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	//Description: Checking if a set of indices is valid.
	//Paramters: the indices being checked.
	//Return: true or false.
	public boolean isValid(int i1, int i2) {
		for(int[] v : valid) {
			if(i1 == v[0] && i2 == v[1]) {
				return true;
			}
		}
		return false;
	}
	//Description: Storing all valid moves for a given piece in a pair ArrayList.
	//Parameters: the indices being checked.
	//Return: A pair ArrayList with all the valid moves.
	public ArrayList<int[]> getValid(int y, int x){
		ArrayList<int[]> ans = new ArrayList<>();
		int[][] b = new int[8][8];
		//Flooring all values in board array for simplification
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				b[i][j] = (int)Math.floor(board[i][j]);
			}
		}
		int piece = b[y][x];
		//White pieces
		if(piece >= 1 && piece <= 6) {
			Set<Integer> moves = new HashSet<>();
			for(int i = 7; i <= 12; i++) moves.add(i);
			//Pawn
			if(piece == 1) {
				if(b[y - 1][x] == 0 && y - 1 >= 0) {
					ans.add(new int[] {y - 1, x});
					if(y == 6 && b[y - 2][x] == 0) {
						ans.add(new int[] {y - 2, x});
					}
				}
				if(y - 1 >= 0 && x - 1 >= 0 && b[y - 1][x - 1] >= 7 && b[y - 1][x - 1] <= 12) {
					ans.add(new int[] {y - 1, x - 1});
				}
				if(y - 1 >= 0 && x + 1 < 8 && b[y - 1][x + 1] >= 7 && b[y - 1][x + 1] <= 12) {
					ans.add(new int[] {y - 1, x + 1});
				}
			}else if(piece == 2) {
				//Rook
				//Left
				for(int i = x - 1; i >= 0; i--) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Right
				for(int i = x + 1; i < 8; i++) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Up
				for(int i = y - 1; i >= 0; i--) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
				//Down
				for(int i = y + 1; i < 8; i++) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
			}else if(piece == 3) {
				//Knight
				//Vertical 2, horizontal 1
				if(y - 2 >= 0 && x - 1 >= 0 && (moves.contains(b[y - 2][x - 1]) || b[y - 2][x - 1] == 0)) ans.add(new int[] {y - 2, x - 1});
				if(y - 2 >= 0 && x + 1 < 8 && (moves.contains(b[y - 2][x + 1]) || b[y - 2][x + 1] == 0)) ans.add(new int[] {y - 2, x + 1});
				if(y + 2 < 8 && x - 1 >= 0 && (moves.contains(b[y + 2][x - 1])|| b[y + 2][x - 1] == 0)) ans.add(new int[] {y + 2, x - 1});
				if(y + 2 < 8 && x + 1 < 8 && (moves.contains(b[y + 2][x + 1]) || b[y + 2][x + 1] == 0)) ans.add(new int[] {y + 2, x + 1});
				
				//Vertical 1, horizontal 2
				if(y - 1 >= 0 && x - 2 >= 0 && (moves.contains(b[y - 1][x - 2]) || b[y - 1][x - 2] == 0)) ans.add(new int[] {y - 1, x - 2});
				if(y - 1 >= 0 && x + 2 < 8 && (moves.contains(b[y - 1][x + 2]) || b[y - 1][x + 2] == 0)) ans.add(new int[] {y - 1, x + 2});
				if(y + 1 < 8 && x - 2 >= 0 && (moves.contains(b[y + 1][x - 2]) || b[y + 1][x - 2] == 0)) ans.add(new int[] {y + 1, x - 2});
				if(y + 1 < 8 && x + 2 < 8 && (moves.contains(b[y + 1][x + 2]) || b[y + 1][x + 2] == 0)) ans.add(new int[] {y + 1, x + 2});
			}else if(piece == 4) {
				//Bishop
				//Top left
				for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Top right
				for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom left
				for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom right
				for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
			}else if(piece == 5) {
				//Queen
				//Top left
				for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Top right
				for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom left
				for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom right
				for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Left
				for(int i = x - 1; i >= 0; i--) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Right
				for(int i = x + 1; i < 8; i++) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Up
				for(int i = y - 1; i >= 0; i--) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
				//Down
				for(int i = y + 1; i < 8; i++) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
			}else if(piece == 6) {
				//King
				//Top left
				if(y + 1 < 8 && x - 1 >= 0 && (moves.contains(b[y + 1][x - 1]) || b[y + 1][x - 1] == 0) && dangerForW[y + 1][x - 1] == 0) {
					ans.add(new int[] {y + 1, x - 1});
				}
				//Top right
				if(y + 1 < 8 && x + 1 < 8 && (moves.contains(b[y + 1][x + 1]) || b[y + 1][x + 1] == 0) && dangerForW[y + 1][x + 1] == 0) {
					ans.add(new int[] {y + 1, x + 1});
				}
				//Bottom left
				if(y - 1 >= 0 && x - 1 >= 0 && (moves.contains(b[y - 1][x - 1]) || b[y - 1][x - 1] == 0) && dangerForW[y - 1][x - 1] == 0) {
					ans.add(new int[] {y - 1, x - 1});
				}
				//Bottom right
				if(y - 1 >= 0 && x + 1 < 8 && (moves.contains(b[y - 1][x + 1]) || b[y - 1][x + 1] == 0) && dangerForW[y - 1][x + 1] == 0) {
					ans.add(new int[] {y - 1, x + 1});
				}
				//Left
				if(x - 1 >= 0 && (moves.contains(b[y][x - 1]) || b[y][x - 1] == 0) && dangerForW[y][x - 1] == 0) {
					ans.add(new int[] {y, x - 1});
				}
				//Right
				if(x + 1 < 8 && (moves.contains(b[y][x + 1]) || b[y][x + 1] == 0) && dangerForW[y][x + 1] == 0) {
					ans.add(new int[] {y, x + 1});
				}
				//Up
				if(y - 1 >= 0 && (moves.contains(b[y - 1][x]) || b[y - 1][x] == 0) && dangerForW[y - 1][x] == 0) {
					ans.add(new int[] {y - 1, x});
				}
				//Down
				if(y + 1 < 8 && (moves.contains(b[y + 1][x]) || b[y + 1][x] == 0) && dangerForW[y + 1][x] == 0) {
					ans.add(new int[] {y + 1, x});
				}
				//Castling
				if(!map.get(6.0).moved) {
					//Left rook
					if(board[7][1] == 0 && board[7][1] == 0 && board[7][3] == 0 && !map.get(2.1).moved) {
						ans.add(new int[] {7, 2});
					}
					//Right rook
					if(board[7][5] == 0 && board[7][6] == 0 && !map.get(2.2).moved) {
						ans.add(new int[] {7, 6});
					}
				}
			}
		}else {
			//Black pieces
			Set<Integer> moves = new HashSet<>();
			for(int i = 1; i <= 6; i++) moves.add(i);
			//Pawn
			if(piece == 7) {
				if(b[y + 1][x] == 0 && y + 1 < 8) {
					ans.add(new int[] {y + 1, x});
					if(y == 1 && b[y + 2][x] == 0) {
						ans.add(new int[] {y + 2, x});
					}
				}
				if(y + 1 < 8 && x - 1 >= 0 && b[y + 1][x - 1] >= 1 && b[y + 1][x - 1] <= 6) {
					ans.add(new int[] {y + 1, x - 1});
				}
				if(y + 1 < 8 && x + 1 < 8 && b[y + 1][x + 1] >= 1 && b[y + 1][x + 1] <= 6) {
					ans.add(new int[] {y + 1, x + 1});
				}
			}else if(piece == 8) {
				//Rook
				//Left
				for(int i = x - 1; i >= 0; i--) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Right
				for(int i = x + 1; i < 8; i++) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Up
				for(int i = y - 1; i >= 0; i--) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
				//Down
				for(int i = y + 1; i < 8; i++) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
			}else if(piece == 9) {
				//Knight
				//Vertical 2, horizontal 1
				if(y - 2 >= 0 && x - 1 >= 0 && (moves.contains(b[y - 2][x - 1]) || b[y - 2][x - 1] == 0)) ans.add(new int[] {y - 2, x - 1});
				if(y - 2 >= 0 && x + 1 < 8 && (moves.contains(b[y - 2][x + 1]) || b[y - 2][x + 1] == 0)) ans.add(new int[] {y - 2, x + 1});
				if(y + 2 < 8 && x - 1 >= 0 && (moves.contains(b[y + 2][x - 1])|| b[y + 2][x - 1] == 0)) ans.add(new int[] {y + 2, x - 1});
				if(y + 2 < 8 && x + 1 < 8 && (moves.contains(b[y + 2][x + 1]) || b[y + 2][x + 1] == 0)) ans.add(new int[] {y + 2, x + 1});
				
				//Vertical 1, horizontal 2
				if(y - 1 >= 0 && x - 2 >= 0 && (moves.contains(b[y - 1][x - 2]) || b[y - 1][x - 2] == 0)) ans.add(new int[] {y - 1, x - 2});
				if(y - 1 >= 0 && x + 2 < 8 && (moves.contains(b[y - 1][x + 2]) || b[y - 1][x + 2] == 0)) ans.add(new int[] {y - 1, x + 2});
				if(y + 1 < 8 && x - 2 >= 0 && (moves.contains(b[y + 1][x - 2]) || b[y + 1][x - 2] == 0)) ans.add(new int[] {y + 1, x - 2});
				if(y + 1 < 8 && x + 2 < 8 && (moves.contains(b[y + 1][x + 2]) || b[y + 1][x + 2] == 0)) ans.add(new int[] {y + 1, x + 2});
			}else if(piece == 10) {
				//Bishop
				//Top left
				for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Top right
				for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom left
				for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom right
				for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
			}else if(piece == 11) {
				//Queen
				//Top left
				for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Top right
				for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom left
				for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Bottom right
				for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
					if(moves.contains(b[i][j]) || b[i][j] == 0) ans.add(new int[] {i, j});
					else break;
					if(moves.contains(b[i][j])) break;
				}
				//Left
				for(int i = x - 1; i >= 0; i--) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Right
				for(int i = x + 1; i < 8; i++) {
					if(!moves.contains(b[y][i]) && b[y][i] != 0) break;
					ans.add(new int[] {y, i});
					if(b[y][i] != 0) break;
				}
				//Up
				for(int i = y - 1; i >= 0; i--) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
				//Down
				for(int i = y + 1; i < 8; i++) {
					if(!moves.contains(b[i][x]) && b[i][x] != 0) break;
					ans.add(new int[] {i, x});
					if(b[i][x] != 0) break;
				}
			}else if(piece == 12) {
				//King
				//Top left
				if(y + 1 < 8 && x - 1 >= 0 && (moves.contains(b[y + 1][x - 1]) || b[y + 1][x - 1] == 0) && dangerForB[y + 1][x - 1] == 0) {
					ans.add(new int[] {y + 1, x - 1});
				}
				//Top right
				if(y + 1 < 8 && x + 1 < 8 && (moves.contains(b[y + 1][x + 1]) || b[y + 1][x + 1] == 0) && dangerForB[y + 1][x + 1] == 0) {
					ans.add(new int[] {y + 1, x + 1});
				}
				//Bottom left
				if(y - 1 >= 0 && x - 1 >= 0 && (moves.contains(b[y - 1][x - 1]) || b[y - 1][x - 1] == 0) && dangerForB[y - 1][x - 1] == 0) {
					ans.add(new int[] {y - 1, x - 1});
				}
				//Bottom right
				if(y - 1 >= 0 && x + 1 < 8 && (moves.contains(b[y - 1][x + 1]) || b[y - 1][x + 1] == 0) && dangerForB[y - 1][x + 1] == 0) {
					ans.add(new int[] {y - 1, x + 1});
				}
				//Left
				if(x - 1 >= 0 && (moves.contains(b[y][x - 1]) || b[y][x - 1] == 0) && dangerForB[y][x - 1] == 0) {
					ans.add(new int[] {y, x - 1});
				}
				//Right
				if(x + 1 < 8 && (moves.contains(b[y][x + 1]) || b[y][x + 1] == 0) && dangerForB[y][x + 1] == 0) {
					ans.add(new int[] {y, x + 1});
				}
				//Up
				if(y - 1 >= 0 && (moves.contains(b[y - 1][x]) || b[y - 1][x] == 0) && dangerForB[y - 1][x] == 0) {
					ans.add(new int[] {y - 1, x});
				}
				//Down
				if(y + 1 < 8 && (moves.contains(b[y + 1][x]) || b[y + 1][x] == 0) && dangerForB[y + 1][x] == 0) {
					ans.add(new int[] {y + 1, x});
				}
				//Castling
				if(!map.get(12.0).moved) {
					//Left rook
					if(board[0][1] == 0 && board[0][1] == 0 && board[0][3] == 0 && !map.get(8.1).moved) {
						ans.add(new int[] {0, 2});
					}
					//Right rook
					if(board[0][5] == 0 && board[0][6] == 0 && !map.get(8.2).moved) {
						ans.add(new int[] {0, 6});
					}
				}
			}
		}
		return ans;
	}
	//Description: Stores the bottom left and top right positions of a square in the board.
	public class Border{
		int x1, y1, x2, y2;
		Border(int x1, int x2, int y1, int y2){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
	//Description: For a piece, stores the image, team, coordinates, if it's alive or not, and if it's moved.
	public class Piece{
		Image image;
		String team;
		int i, j;
		boolean alive, moved;
		Piece(Image image, String team, int i, int j, boolean alive, boolean moved){
			this.image = image;
			this.team = team;
			this.i = i;
			this.j = j;
			this.alive = alive;
			this.moved = moved;
		}
	}
	//Description: Function for threading.
	//Parameters: none.
	//Return: none.
	@Override
	public void run() {
		initialize();
		boolean toggle = false;
		while(true) {
			//main game loop
			update();
			this.repaint();
			try {
//				Thread.sleep(1000/60);
				//White timer
				if(turn && win == -1) {
					wTimer--;
					if(wTimer == 0) {
						bScore++;
						win = 1;
					}
				}
				//Black timer
				if(!turn && win == -1) {
					bTimer--;
					if(bTimer == 0) {
						wScore++;
						win = 0;
					}
				}
				Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	//Description: Assigns actions for when a menu item is clicked in the timer settings.
	//Parameters: The action event.
	//Return: none.
	@Override
	public void actionPerformed(ActionEvent e) {
		String eventName = e.getActionCommand();
		//1 minute setting clicked
		if(eventName.equals("One")) {
			reset();
			wTimer = 60; bTimer = 60;
		}else if (eventName.equals("Three")) {
			//Three minutes setting clicked
			reset();
			wTimer = 180; bTimer = 180;
		}else if(eventName.equals("Ten")) {
			//Ten minutes setting clicked
			reset();
			wTimer = 600; bTimer = 600;
		}
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void initialize() {
		//setups before the game starts running
		
	}
	
	public void update() {
		//update stuff
		
	}

}
