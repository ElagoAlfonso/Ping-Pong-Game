package Ping_Pong_Game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable  {
	
	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH =25;
	static final int PADDLE_HEIGHT = 100;
	
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle p1;
	Paddle p2;
	Ball ball;
	Score score;
	
	GamePanel(){
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH , GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
		
	}
	public void newBall(){
		
		random = new Random();
		ball = new Ball((GAME_WIDTH / 2)-(BALL_DIAMETER / 2),random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER,BALL_DIAMETER);	
		
	}
	public void newPaddles() {
		
		p1 = new Paddle(0,(GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		p2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH,(GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
	}
	public void paint(Graphics g) {
		
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0,0, this);
	}
	public void draw(Graphics g) {
		p1.draw(g);
		p2.draw(g);
		ball.draw(g);
		score.draw(g);
		
	}
	public void move() {
		p1.move();
		p2.move();
		ball.move();
		
	}
	public void checkCollision() {
		
		// bounce ball off top & bottom window edge
		if(ball.y <= 0) {
			ball.setYDirection(-ball.yVelocity);		
		}
		if(ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);				
		}
		//bounce ball off paddles
		if(ball.intersects(p1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity > 0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(p2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity > 0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		// This stop the paddle at window edges
		if(p1.y  <= 0)
			p1.y = 0;
		if (p1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			p1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		if(p2.y  <= 0)
			p2.y = 0;
		if (p2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			p2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		// Give a player 1 point & creates new paddle & ball
		if (ball.x <= 0) {
			score.player2++;
			newPaddles();
			newBall();
			System.out.println("Player 2: " + score.player2);
			
		}
		if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
			score.player1++;
			newPaddles();
			newBall();
			System.out.println("Player 1: " + score.player1);
		}
		
	}
	public void run() {
		// game loop 
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 /amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;				
			}			
		}		
	}
	
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			p1.keyPressed(e);
			p2.keyPressed(e);			
		}
		public void keyReleased(KeyEvent e) {
			p1.keyReleased(e);
			p2.keyReleased(e);
			
		}
	}		
}

