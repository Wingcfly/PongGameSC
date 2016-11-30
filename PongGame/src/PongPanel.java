
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
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Invisible Man
 *
 */

public class PongPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -1097341635155021546L;

	private boolean showTitleScreen = true;
	private boolean playing;
	private boolean gameOver;
	private boolean ballmove1;
	private boolean ballmove2;
	private int speedball = 3;

	private int xRandom;
	private int yRandom;
	private int timeDisplayMPlus;
	private int diameterMPlus = 50;
	private boolean showRandom;
	private int iOptionRandom = 1; // cac tuy chon khi dung vao minus & plus
	private int iDimension = 1; // chieu di cua ball

	/** Background. */
	private Color backgroundColor = Color.BLACK;

	/** BallIcon. */
	ImageIcon imAmericanBall = new ImageIcon("./BallTypeImage/AmericanBall.png");
	ImageIcon imBasketBall = new ImageIcon("./BallTypeImage/basketball.png");
	ImageIcon imTennisBall = new ImageIcon("./BallTypeImage/tennisball.png");
	ImageIcon imWhite = new ImageIcon();

	ImageIcon bgOutGame = new ImageIcon("Images/screen.jpg"); // background in
																// showscreen
	ImageIcon bgInGame = new ImageIcon("Images/playing.png"); // background in
																// game
	ImageIcon bgp1win = new ImageIcon("Images/a.png");
	ImageIcon bgp2win = new ImageIcon("Images/b.png");
	ImageIcon imMinus = new ImageIcon("Images/minus.png");
	ImageIcon imPlus = new ImageIcon("Images/plus.png");

	/** State on the control keys. */
	private boolean upPressed;
	private boolean downPressed;
	private boolean wPressed;
	private boolean sPressed;

	/** The ball: position, diameter */
	private int ballX = 225; // qua bong di chuyen tu vi tri trung tam khi bat
	// dau game
	private int ballY = 225; // ... //
	private int diameter = 50;
	private int ballDeltaX = -1;
	private int ballDeltaY = 3;

	/** Score */
	private int score = 1;

	/** Player 1's paddle: position and size */
	private int playerOneX = 0;
	private int playerOneY = 195;
	private int playerOneWidth = 10;
	private int playerOneHeight = 60;

	/** Player 2's paddle: position and size */
	private int playerTwoX = 480; // Doi chieu dai cua 2 thanh chan bang nhau
	private int playerTwoY = 195;
	private int playerTwoWidth = 10;
	private int playerTwoHeight = 60;

	/** Speed of the paddle - How fast the paddle move. */
	private int paddleSpeed = 5;

	/** Player score, show on upper left and right. */
	private int playerOneScore;
	private int playerTwoScore;

	// declare Rectangle is Button
	Rectangle rctBall = new Rectangle(360, 5, 100, 30);
	// declare NumTypeball
	static int NumTypeBall = 0;
	static boolean rectinBall = false;
	Rectangle rctPaddles = new Rectangle(250, 5, 100, 30);
	static int NumPaddlesColor;
	static boolean rectinPaddles = false;
	// declare PlayerName
	static String PlayerName1 = "#player1";
	static String PlayerName2 = "#player2";
	// declare Random
	private int randomTime = ThreadLocalRandom.current().nextInt(1, 5) * 1000;
	private int xObjectFL;
	private int yObjectFL;
	private int countdown = 0;
	private double xcenterA, ycenterA, xcenterB, ycenterB;
	private boolean flagdrawObjectFL = false;
	private boolean flagHadObjectFL = false;
	private static int fps = 1000 / 60;
	private int typeContentFL = ThreadLocalRandom.current().nextInt(1, 3);
	// declare IconImage FOR CONTENT
	ImageIcon imFastContent = new ImageIcon("./IconContent/fastIcon.png");
	ImageIcon imSlowContent = new ImageIcon("./IconContent/slowIcon.png");
	// call step() 60 fps
	Timer timer = new Timer(fps, this);

	private Timer timerofObjectF = new Timer(1000 / 60, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			timerofObjectL.stop();
			countdown = countdown - 10;
			if (countdown == -10) {
				timer.setDelay(10);
				paddleSpeed = 3;
			} else if (countdown <= -5000) {
				timer.setDelay(1000 / 60);
				paddleSpeed = 5;
				countdown = 0;
				timerofObjectF.stop();
			}

		}
	});
	private Timer timerofObjectL = new Timer(1000 / 60, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			timerofObjectF.stop();
			countdown = countdown - 10;
			if (countdown == -10) {
				timer.setDelay(28);
			} else if (countdown <= -5000) {
				timer.setDelay(1000 / 60);
				countdown = 0;
				timerofObjectL.stop();
			}

		}
	});

	/** Construct a PongPanel. */
	public PongPanel() {
		// setBackground(backgroundColor);

		// listen to key presses
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

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
			if (sPressed && playerOneY + playerOneHeight + paddleSpeed <= getHeight()) {
				playerOneY += paddleSpeed;
			}

			// move player 2
			// Move up if after moving paddle is not outside the screen
			if (upPressed && playerTwoY - paddleSpeed >= 0) {
				playerTwoY -= paddleSpeed;
			}
			// Move down if after moving paddle is not outside the screen
			if (downPressed && playerTwoY + playerTwoHeight + paddleSpeed <= getHeight()) {
				playerTwoY += paddleSpeed;
			}

			/*
			 * where will the ball be after it moves? calculate 4 corners: Left,
			 * Right, Top, Bottom of the ball used to determine whether the ball
			 * was out yet
			 */
			int nextBallLeft = ballX + ballDeltaX;
			int nextBallRight = ballX + diameterMPlus + ballDeltaX;
			// FIXME Something not quite right here
			int nextBallTop = ballY + ballDeltaY;
			int nextBallBottom = ballY + diameterMPlus + ballDeltaY;

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
				if (nextBallTop > playerOneBottom || nextBallBottom < playerOneTop) {

					playerTwoScore++;
					playerOneHeight = 60;
					playerTwoHeight = 60;
					iDimension = 1;
					Sound.play("Sound/pingpongsound.wav"); // ghi diem se co am
															// thanh
					// Player 2 Win, restart the game
					if (playerTwoScore == score) {
						playing = false;
						gameOver = true;
						// am thanh khi player 2 thang
						Sound.play("Sound/Victory_Fanfare.wav");
					}
					ballX = 225; // qua bong di chuyen tu vi tri trung tam khi
					// bat dau game
					ballY = 225; // ...
					ballmove1 = true;
					ballmove2 = false;
				} else {
					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 1
					// se bat lai
					// am thanh khi cham vao paddle player 1
					Sound.play("Sound/pingpongsound2.wav");
					iDimension = 2;
				}
			}

			// will the ball go off the right side?
			if (nextBallRight > playerTwoLeft) {
				// is it going to miss the paddle?
				if (nextBallTop > playerTwoBottom || nextBallBottom < playerTwoTop) {
					playerOneScore++;
					iDimension = 2;
					playerOneHeight = 60;
					playerTwoHeight = 60;
					Sound.play("Sound/pingpongsound.wav");
					// Player 1 Win, restart the game
					if (playerOneScore == score) {
						playing = false;
						gameOver = true;
						// am thanh khi player 1 thang
						Sound.play("Sound/Victory_Fanfare.wav");
					}
					ballX = 225; // qua bong di chuyen tu vi tri trung tam khi
					// bat dau game
					ballY = 225; // ...
					ballmove1 = false;
					ballmove2 = true;
				} else {

					// If the ball hitting the paddle, it will bounce back
					ballDeltaX *= -1; // bong cham vao thanh chan cua player 2
					iDimension = 1;
					// se bat lai
					// am thanh khi cham vao paddle player 2
					Sound.play("Sound/pingpongsound2.wav");
				}
			}

			// move the ball
			ballX += ballDeltaX;
			ballY += ballDeltaY;
		}
		// RandomTime
		randomTime = randomTime - 10;
		if (randomTime <= 0) {
			if (flagdrawObjectFL == false) {
				if (flagHadObjectFL == true) {

				} else {
					xObjectFL = ThreadLocalRandom.current().nextInt(100, 400 + 1);
					yObjectFL = ThreadLocalRandom.current().nextInt(0, 400 + 1);
					flagdrawObjectFL = true;
					xcenterB = xObjectFL + diameterMPlus / 2;
					ycenterB = yObjectFL + diameterMPlus / 2;

				}

			}
			if (randomTime < -5000) {
				randomTime = ThreadLocalRandom.current().nextInt(1, 5) * 1000;
				flagdrawObjectFL = false;
				flagHadObjectFL = false;
				typeContentFL = ThreadLocalRandom.current().nextInt(1, 3);
			}

		}

		timeDisplayMPlus -= 1000 / 60;
		if (timeDisplayMPlus < 0) {
			if (!showRandom) {
				showRandom = true;
				iOptionRandom = ThreadLocalRandom.current().nextInt(1, 2 + 1);
				xRandom = ThreadLocalRandom.current().nextInt(100, 400 + 1);
				yRandom = ThreadLocalRandom.current().nextInt(0, 400 + 1);
			} else {
				Point ballCenter = new Point(ballX + diameterMPlus / 2, ballY + diameterMPlus / 2);
				Point ranCenter = new Point(xRandom + diameterMPlus / 2, yRandom + diameterMPlus / 2);
				double distance2center = getPointDistance(ballCenter, ranCenter);
				if (distance2center < (diameterMPlus / 2 + diameterMPlus / 2)) {
					if (iOptionRandom == 1) {
						if (iDimension == 1) {
							if (playerOneHeight >= 30) {
								playerOneHeight -= 15;
							} else {
							}
						} else {
							if (playerTwoHeight >= 30) {
								playerTwoHeight -= 15;
							} else {
							}
						}

						showRandom = false;
						timeDisplayMPlus = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;

					} else if (iOptionRandom == 2) {
						if (iDimension == 1) {
							if (playerOneHeight <= 120) {
								playerOneHeight += 30;
							} else {
							}
						} else {
							if (playerTwoHeight <= 120) {
								playerTwoHeight += 30;
							} else {
							}
						}

						showRandom = false;
						timeDisplayMPlus = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;

					}
				}
				if (timeDisplayMPlus < -10000) {
					showRandom = false;
					timeDisplayMPlus = ThreadLocalRandom.current().nextInt(5, 15 + 1) * 1000;
				}
			}
		}

		// stuff has moved, tell this JPanel to repaint itself
		repaint();
	}

	public double getPointDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
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
			g.setColor(Color.BLACK);
			if (rectinBall == false) {
				g.fillRect(360, 5, 100, 30);
				g.setColor(Color.BLUE);
				g.drawString("Ball Color", 380, 25);
			} else {
				g.fillRect(350, 10, 110, 40);
				g.setColor(Color.RED);
				g.drawString("Ball Color", 370, 30);
			}
			if (rectinPaddles == false) {
				g.setColor(Color.BLUE);
				g.drawString("Paddles Color", 30, 25);
			} else {
				g.setColor(Color.RED);
				g.drawString("Paddles Color", 20, 30);
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
				g.drawImage(imAmericanBall.getImage(), ballX, ballY, diameterMPlus, diameterMPlus, null);
			} else if (NumTypeBall == 1) {
				g.drawImage(imBasketBall.getImage(), ballX, ballY, diameterMPlus, diameterMPlus, null);
			} else if (NumTypeBall == 2) {
				g.drawImage(imTennisBall.getImage(), ballX, ballY, diameterMPlus, diameterMPlus, null);
			}
			xcenterA = ballX + diameterMPlus / 2;
			ycenterA = ballY + diameterMPlus / 2;
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

			// draw the ObjectFL
			if (flagdrawObjectFL == true) {
				// g.fillOval(xObjectFL, yObjectFL, diameter, diameter);
				if (typeContentFL == 1) {
					g.drawImage(imFastContent.getImage(), xObjectFL, yObjectFL, diameterMPlus, diameterMPlus, null);
					if (Math.sqrt(
							Math.pow(xcenterA - xcenterB, 2) + Math.pow(ycenterA - ycenterB, 2)) <= diameterMPlus) {
						flagdrawObjectFL = false;
						flagHadObjectFL = true;
						timerofObjectF.start();
					}
				} else {
					g.drawImage(imSlowContent.getImage(), xObjectFL, yObjectFL, diameterMPlus, diameterMPlus, null);
					if (Math.sqrt(
							Math.pow(xcenterA - xcenterB, 2) + Math.pow(ycenterA - ycenterB, 2)) <= diameterMPlus) {
						flagdrawObjectFL = false;
						flagHadObjectFL = true;
						timerofObjectL.start();
					}
				}
			}
			if (showRandom) {
				if (iOptionRandom == 1) {
					g.drawImage(imMinus.getImage(), xRandom, yRandom, diameterMPlus, diameterMPlus, null);
					// g.fillOval(xRandom, yRandom, diameterRan, diameterRan);
				} else {
					g.drawImage(imPlus.getImage(), xRandom, yRandom, diameterMPlus, diameterMPlus, null);
					// g.setColor(Color.BLUE);
					// g.fillOval(xRandom, yRandom, diameterRan, diameterRan);
				}
			}
		} else if (gameOver) {

			/* Show End game screen with winner name and score */

			// Draw scores
			// TODO Set Blue color
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
			g.drawString(String.valueOf(playerOneScore), 100, 100);
			g.drawString(String.valueOf(playerTwoScore), 400, 100);
			

			// Draw the winner name
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
			g.setColor(Color.RED);
			
			if (playerOneScore > playerTwoScore) {
				g.drawImage(bgp1win.getImage(), 0, 0, 500, 500, null);
				
				g.drawString(PlayerName1 + " Wins!", 165, 400);
			} else {
				g.drawImage(bgp2win.getImage(), 0, 0, 500, 500, null);
				g.drawString(PlayerName2 + " Wins!", 165, 400);
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
			playerOneHeight = 60;
			playerTwoHeight = 60;
			playerOneScore = 0;
			playerTwoScore = 0;
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
			rctPaddles.setBounds(20, 5, 120, 50);
			rectinPaddles = true;
		} else {
			rectinPaddles = false;
		}
	}

}
