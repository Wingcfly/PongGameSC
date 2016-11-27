/*
 * PONG GAME REQUIREMENTS
 * This simple "tennis like" game features two paddles and a ball, 
 * the goal is to defeat your opponent by being the first one to gain 3 point,
 *  a player gets a point once the opponent misses a ball. 
 *  The game can be played with two human players, one on the left and one on 
 *  the right. They use keyboard to start/restart game and control the paddles. 
 *  The ball and two paddles should be red and separating lines should be green. 
 *  Players score should be blue and background should be black.
 *  Keyboard requirements:
 *  + P key: start
 *  + Space key: restart
 *  + W/S key: move paddle up/down
 *  + Up/Down key: move paddle up/down
 *  
 *  Version: 0.5
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Invisible Man
 *
 */

public class PongPanel extends JPanel implements ActionListener, KeyListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -1097341635155021546L;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;
	private boolean ballmove1;
	private boolean ballmove2;

	/** Background. */
	private Color backgroundColor = Color.BLACK;

	/** BallIcon. */
	ImageIcon imAmericanWhite = new ImageIcon("./BallTypeImage/AmericanBall.png");
	ImageIcon imBasketBall = new ImageIcon("./BallTypeImage/basketball.png");
	ImageIcon imTennisBall = new ImageIcon("./BallTypeImage/tennisball.png");
	ImageIcon imWhite = new ImageIcon();

	ImageIcon bgOutGame = new ImageIcon("Images/screen.jpg"); // background in showscreen
	ImageIcon bgInGame = new ImageIcon("Images/playing.png"); // background in game

	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	private int ballX = 240; // qua bong di chuyen tu vi tri trung tam khi bat
	// dau game
	private int ballY = 240; // ... //
	private int diameter = 50;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;

	/** Score */
	private int score = 3;

	/** Player 1's paddle: position and size */
	private int playerOneX = 0;
	private int playerOneY = 200;
	private int playerOneWidth = 10;
	private int playerOneHeight = 50;

	/** Player 2's paddle: position and size */
	private int playerTwoX = 480; // Doi chieu dai cua 2 thanh chan bang nhau
	private int playerTwoY = 200;
	private int playerTwoWidth = 10;
	private int playerTwoHeight = 50;

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;

	// declare Rectangle is Button
	Rectangle rctBall = new Rectangle(360, 5, 100, 30);
	// declare NumTypeball
	static int NumTypeBall;
	static boolean rectinBall = false;
	Rectangle rctPaddles = new Rectangle(250, 5, 100, 30);
	static int NumPaddlesColor;
	static boolean rectinPaddles = false;
	// declare PlayerName
	static String PlayerName1 = "#player1";
	static String PlayerName2 = "#player2";
	static String plName1;
	static String plName2;


	/** Construct a PongPanel. */
	public PongPanel() {
		setBackground(backgroundColor);

		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// call step() 60 fps
		Timer timer = new Timer(1000 / 60, this);
		timer.start();
	}

	/** Implement actionPerformed */
	public void actionPerformed(ActionEvent e) {
		step();
	}

	/** Repeated task */
	public void step() {

		if (playing) {

			/* Playing mode */

			// move player 1
			// Move up if after moving, paddle is not outside the screen
			if (wPressed && playerOneY - paddleSpeed >= 0) {
				playerOneY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (sPressed
					&& playerOneY + playerOneHeight + paddleSpeed <= getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (upPressed && playerTwoY - paddleSpeed >= 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed
					&& playerTwoY + playerTwoHeight + paddleSpeed <= getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameter + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameter + ballDeltaY;

			// Player 1's paddle position
			int playerOneRight = playerOneX + playerOneWidth;
			int playerOneTop = playerOneY;
			int playerOneBottom = playerOneY + playerOneHeight;

			// Player 2's paddle position
			float playerTwoLeft = playerTwoX;
			float playerTwoTop = playerTwoY;
			float playerTwoBottom = playerTwoY + playerTwoHeight;

			// ball bounces off top and bottom of screen
			if (nextBallTop < 0 || nextBallBottom > getHeight()) {
				ballDeltaY *= -1;
				// am thanh khi cham vao vien tren vien duoi
				Sound.play("Sound/pingpongsound2.wav");
			}

			// will the ball go off the left side?
			if (nextBallLeft < playerOneRight) {
				// is it going to miss the paddle?
				if (nextBallTop > playerOneBottom
						|| nextBallBottom < playerOneTop) {

					playerTwoScore++;
					Sound.play("Sound/pingpongsound.wav"); //ghi diem se co am thanh
					// Player 2 Win, restart the game
					if (playerTwoScore == score) {
						playing = false;
						gameOver = true;
						playerOneScore = 0;
						playerTwoScore = 0;
						// am thanh khi player 2 thang
						Sound.play("Sound/Victory_Fanfare.wav");
					}
					ballX = 240; // qua bong di chuyen tu vi tri trung tam khi
					// bat dau game
					ballY = 240; // ...
					ballmove1 = true;
					ballmove2 = false;
				} else {
					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 1
					// se bat lai
					// am thanh khi cham vao paddle player 1
					Sound.play("Sound/pingpongsound2.wav");
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom
						|| nextBallBottom < playerTwoTop) {

					playerOneScore++;
					Sound.play("Sound/pingpongsound.wav");
					// Player 1 Win, restart the game
					if (playerOneScore == score) {
						playing = false;
						gameOver = true;
						playerOneScore = 0;
						playerTwoScore = 0;
						// am thanh khi player 1 thang
						Sound.play("Sound/Victory_Fanfare.wav");
					}
					ballX = 240; // qua bong di chuyen tu vi tri trung tam khi
					// bat dau game
					ballY = 240; // ...
					ballmove1 = false;
					ballmove2 = true;
				} else {

					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 2
					// se bat lai
					// am thanh khi cham vao paddle player 2
					Sound.play("Sound/pingpongsound2.wav");
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;
		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	/** Paint the game screen. */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (showTitleScreen) {

			/* Show welcome screen */
			g.drawImage(bgOutGame.getImage(), 0, 0, 500, 500, null);
			// Draw game title and start message
			// FIXME Wellcome message below show smaller than game title
			g.setColor(Color.BLACK);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));// draw playerName
															// in the Title
															// Screen
			g.drawString(PlayerName1, 75, 80);
			g.drawString(PlayerName2, 300, 80);
		} else if (playing) {
			/* Game is playing */
			g.drawImage(bgInGame.getImage(), 0, 0, 500, 500, null); // background
																	// trong
																	// game
			// set the coordinate limit
			int playerOneRight = playerOneX + playerOneWidth;
			int playerTwoLeft = playerTwoX;
			// draw playerName in the Playing Screen
			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));// draw playerName
															// in the Title
															// Screen
			g.drawString(PlayerName1, 50, 425);
			g.drawString(PlayerName2, 320, 425);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
			// draw ballcolorbutton
			if (rectinBall == false) {
				g.fillRect(360, 5, 100, 30);
				g.setColor(Color.BLUE);
				g.drawString("Ball Color", 380, 25);
			} else {
				g.fillRect(350, 10, 110, 40);
				g.setColor(Color.BLUE);
				g.drawString("Ball Color", 370, 30);
			}
			if (rectinPaddles == false) {
				g.setColor(Color.BLUE);
				g.drawString("Paddles Color", 200, 25);
			} else {
				g.setColor(Color.BLUE);
				g.drawString("Paddles Color", 190, 30);
			}
			// draw dashed line down center
			g.setColor(Color.GREEN);// Fix duong lane trung tam thanh mau xanh
			for (int lineY = 0; lineY < getHeight(); lineY += 50) {
				g.drawLine(250, lineY, 250, lineY + 25);
			}

			// draw "goal lines" on each side
			g.setColor(Color.GRAY);// Fix 2 duong bien mau xam
			g.drawLine(playerOneRight, 0, playerOneRight, getHeight());
			g.drawLine(playerTwoLeft, 0, playerTwoLeft, getHeight());

			// draw the scores
			g.setColor(Color.BLUE); // diem so mau xanh duong
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 100, 100); // Player 1
			// score
			g.drawString(String.valueOf(playerTwoScore), 400, 100); // Player 2
			// score

			// draw the ball
			g.setColor(Color.RED);
			if (NumTypeBall == 0) {
				g.drawImage(imAmericanWhite.getImage(), ballX, ballY, diameter,
						diameter, null);
			} else if (NumTypeBall == 1) {
				g.drawImage(imBasketBall.getImage(), ballX, ballY, diameter,
						diameter, null);
			} else if (NumTypeBall == 2) {
				g.drawImage(imTennisBall.getImage(), ballX, ballY, diameter,
						diameter, null);
			}

			// color the paddles
			if (NumPaddlesColor == 0) {
				g.setColor(Color.WHITE);
			} else if (NumPaddlesColor == 1) {
				g.setColor(Color.BLUE);
			} else if (NumPaddlesColor == 2) {
				g.setColor(Color.RED);
			}
			// draw the paddles
			g.fillRect(playerOneX, playerOneY, playerOneWidth, playerOneHeight);
			g.fillRect(playerTwoX, playerTwoY, playerTwoWidth, playerTwoHeight);
		} else if (gameOver) {

			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 100, 100);
			g.drawString(String.valueOf(playerTwoScore), 400, 100);

			// Draw the winner name
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			if (playerOneScore > playerTwoScore) {
				g.drawString(PlayerName1 + " Wins!", 165, 200);
			} else {
				g.drawString(PlayerName2 + " Wins!", 165, 200);
			}

			// Draw Restart message
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
			// TODO Draw a restart message
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (showTitleScreen) {
			if (e.getKeyCode() == KeyEvent.VK_P) { // nhan phim P thuong khong
													// chay
				showTitleScreen = false;
				playing = true;
				// am thanh khi bat dau
				Sound.play("Sound/startsound.wav");
			}
			if (e.getKeyCode() == KeyEvent.VK_N) { // nhan phim N De set
													// NamePlayer
				PlayerNameWindow mainWidow = new PlayerNameWindow();
				mainWidow.setVisible(true);
			}
		} else if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				wPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				sPressed = true;
			}
		} else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameOver = false;
			showTitleScreen = true;
			playerOneY = 250;
			playerTwoY = 250;
			ballX = 240; // qua bong di chuyen tu vi tri trung tam khi bat dau
			// game
			ballY = 240; // ...
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			wPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sPressed = false; // nhấn nút S để di chuyển thanh chắn của Player 2
			// xuống
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (rctBall.contains(e.getX(), e.getY())) {
			BallColorWindow mainWidow = new BallColorWindow();
			mainWidow.setVisible(true);
		}
		if (rctPaddles.contains(e.getX(), e.getY())) {
			PaddlesColor mainWindow = new PaddlesColor();
			mainWindow.setVisible(true);
		}
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (rctBall.contains(e.getX(), e.getY())) {
			rctBall.setSize(100, 50);
			rctBall.setBounds(360, 5, 120, 50);
			rectinBall = true;
		} else {
			rectinBall = false;
		}
		if (rctPaddles.contains(e.getX(), e.getY())) {
			rctPaddles.setSize(100, 50);
			rctPaddles.setBounds(200, 5, 120, 50);
			rectinPaddles = true;
		} else {
			rectinPaddles = false;
		}
	}

}
