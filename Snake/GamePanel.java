package Snake;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	private GameMap gameMap;
    static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 175;
	static final int SCORE_MARGIN = 50;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	int speed = DELAY;
	int bestScore = 0;
	char direction = 'R';
	boolean running = false;
	boolean isPaused = false;
	boolean canChangeDirection = true;
	Timer timer;
	Random random;

    GamePanel(){
        random = new Random();
		gameMap = new GameMap();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
    }
    public void startGame(){
		// Đặt con rắn vào giữa màn hình
		int middleRow = SCREEN_HEIGHT / (2 * UNIT_SIZE);
		int middleCol = SCREEN_WIDTH / (2 * UNIT_SIZE);
		for (int i = 0; i < bodyParts; i++) {
			x[i] = middleCol * UNIT_SIZE - i * UNIT_SIZE;
			y[i] = middleRow * UNIT_SIZE;
		}

        newApple();
		running = true;
		speed = DELAY;
		timer = new Timer(speed, this);
		timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
		gameMap.draw(g, UNIT_SIZE);
		draw(g);
    }
    public void draw(Graphics g){
        if(running) {
			/* 
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

			// Display best score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			g.drawString("Best Score: " + bestScore, SCORE_MARGIN, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}

		if (isPaused) {
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 75));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Game is on pause", (SCREEN_WIDTH - metrics.stringWidth("Game is on pause")) / 2, SCREEN_HEIGHT / 2);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			g.drawString("Press 'P' to continue", (SCREEN_WIDTH - metrics.stringWidth("Press 'R' to continue")) / 2, SCREEN_HEIGHT / 2 + 100);
		}
    }
    public void newApple(){
		boolean appleOnSnake = true;
		while (appleOnSnake) {
			appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
			appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

			// Kiểm tra xem quả táo có nằm trên phần thân của rắn hay không
			appleOnSnake = false;
			for (int i = 0; i < bodyParts; i++) {
				if (x[i] == appleX && y[i] == appleY) {
					appleOnSnake = true;
					break;
				}
			}
		}
	}
    public void move(){
        for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

		// if (x[0] >= SCREEN_WIDTH) {
		// 	x[0] = 0;
		// } else if (x[0] < 0) {
		// 	x[0] = SCREEN_WIDTH - UNIT_SIZE;
		// }
	
		// if (y[0] >= SCREEN_HEIGHT) {
		// 	y[0] = 0;
		// } else if (y[0] < 0) {
		// 	y[0] = SCREEN_HEIGHT - UNIT_SIZE;
		// }
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			if (speed > 50) { // Đảm bảo tốc độ không quá nhanh
				speed -= 3; // Giảm tốc độ di chuyển
				timer.setDelay(speed); // Cập nhật thời gian trễ cho timer
			}
		}
    }
    public void checkCollisions(){
        //checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
    }
    public void gameOver(Graphics g){
        //Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

		//Best score
		if (applesEaten > bestScore) {
			bestScore = applesEaten;
		}
		
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

		// Replay prompt
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		g.drawString("Press 'R' to replay", (SCREEN_WIDTH - metrics2.stringWidth("Press 'R' to replay")) / 2, SCREEN_HEIGHT / 2 + 100);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
		if(!isPaused){
			if(running) {
				move();
				checkApple();
				checkCollisions();
			}else {
				showReplayDialog();
			}
		}
		repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_P) {
				togglePause(); // Khi nhấn "P", chuyển đổi trạng thái tạm dừng
			}
			if (key == KeyEvent.VK_ESCAPE) { // Nếu nhấn phím ESC, thoát game
				System.exit(0);
			}

			if (!isPaused) { // Chỉ xử lý phím di chuyển khi không tạm dừng
				switch (key) {
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_A:
						if (direction != 'R') {
							direction = 'L';
						}
						break;
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_D:
						if (direction != 'L') {
							direction = 'R';
						}
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_W:
						if (direction != 'D') {
							direction = 'U';
						}
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_S:
						if (direction != 'U') {
							direction = 'D';
						}
						break;
				}
			}
			if (key == KeyEvent.VK_R && !running) {
				replay();
			}
        }
    }
	public void replay() {
		speed = DELAY;
		bodyParts = 6;
		applesEaten = 0;
		direction = 'R';
		running = true;
		for (int i = 0; i < bodyParts; i++) {
			x[i] = 0;
			y[i] = 0;
		}
		newApple();
		timer.restart();
	}


	private void showReplayDialog() {
		int choice = JOptionPane.showConfirmDialog(this, "Game Over! Do you want to replay?", "Game Over", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			replay();
		} else {
			System.exit(0);
		}
	}
	private void togglePause() {
        isPaused = !isPaused; // Khi nhấn "P", chuyển đổi trạng thái tạm dừng
        if (!isPaused) {
            timer.start(); // Tiếp tục timer khi kết thúc tạm dừng
        }
        repaint();
    }
}
