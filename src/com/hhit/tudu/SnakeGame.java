package com.hhit.tudu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.hhit.tools.FrameTools;

public class SnakeGame extends JFrame {
	public static final int WIDTH = 615;
	public static final int HEIGHT = 435;
	public static long startTime;
	public static long endTime;
	public static long currentTime;
	public static int score = 0;
	public  static final int GAMEOVER = 0;
	public static final int RUNNING = 2;
	public  static final int START = 1;
	public  static final int PAUSE = -1;
	public static int state = RUNNING;
	public static void main(String[] args) {
		Score scoreFrame = new Score();
		new SnakeGame();

	}

	// 1.初始化操作
	// 1.1窗口的设置
	public SnakeGame() {
		
		FrameTools.initFrame(this, WIDTH, HEIGHT, "贪吃蛇");
		// 窗口初始化
		MyPanel myPanel = new MyPanel();

		// 贪吃蛇初始化
		myPanel.initSnake();
		// 食物初始化
		myPanel.initFood();
		startTime = System.currentTimeMillis();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				int keyCode = e.getKeyCode();
				if (SnakeGame.state == SnakeGame.RUNNING) {
					switch (keyCode) {
					case KeyEvent.VK_LEFT:
						myPanel.setDir(myPanel.LEFT);
						break;
					case KeyEvent.VK_UP:
						myPanel.setDir(myPanel.UP);
						break;
					case KeyEvent.VK_RIGHT:
						myPanel.setDir(myPanel.RIGHT);
						break;
					case KeyEvent.VK_DOWN:
						myPanel.setDir(myPanel.DOWN);
						break;
					default:
						break;
					}
				}
				if (keyCode == KeyEvent.VK_ENTER) {
					myPanel.reInitPanel();
				}
				myPanel.repaint();

			}
		});
		add(myPanel);
	}
	// 1.2添加事件监听
	// 1.2.1如果方向和移动方向相反，则不移动
}

class MyPanel extends JPanel {
	private static final int CELL_X = 30;// 列数
	private static final int CELL_Y = 20;// 行数
	private static final int CELL_WIDTH = 20;
	private static final int CELL_HEIGHT = 20;
	private static final int INITSNAKELENGTH = 3;

	public static final int LEFT = -1;
	public static final int UP = 2;
	public static final int RIGHT = 1;
	public static final int DOWN = -2;



	
	Point food;

	LinkedList<Point> snakeList = new LinkedList<Point>();
	// 1.重写paint方法绘制图形
	// 初始化画纸
	boolean[][] map = new boolean[CELL_Y][CELL_X];

	public void initPanel() {
		for (int i = 0; i < CELL_Y; i++) {
			for (int j = 0; j < CELL_X; j++) {
				if (i == 0 || i == CELL_Y - 1 || j == 0 || j == CELL_X - 1) {
					map[i][j] = true;
				}
			}
		}
	}

	public void repainPanel() {
		if (!isEat()) {
			
			snakeList.removeLast();
		}else{
			SnakeGame.score++;
		}
		if (isDeath()) {
			SnakeGame.state = SnakeGame.GAMEOVER;
			SnakeGame.endTime = System.currentTimeMillis() - SnakeGame.startTime;
			snakeList = new LinkedList<Point>();
			SnakeGame.currentTime = SnakeGame.endTime;
			
		}
		repaint();
	}

	public void reInitPanel() {
		currentDir = RIGHT;
		SnakeGame.state = SnakeGame.RUNNING;
		snakeList = new LinkedList<Point>();
		SnakeGame.score = 0;
		SnakeGame.startTime = System.currentTimeMillis();
		initSnake();
		initFood();

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 1.1绘制边框和活动区
		if (SnakeGame.state == SnakeGame.START) {

		} else if (SnakeGame.state == SnakeGame.RUNNING) {
			initPanel();
			for (int i = 0; i < CELL_Y; i++) {
				for (int j = 0; j < CELL_X; j++) {
					if (map[i][j]) { // 如果true代表为墙
						g.setColor(Color.gray);
					} else {
						g.setColor(Color.white);
					}
					g.fill3DRect(j * CELL_WIDTH, i * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, true);

				}

			}
			// 1.2绘制食物
			g.setColor(Color.green);
			g.fill3DRect(food.x * CELL_WIDTH, food.y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, true);
			// 1.3绘制蛇
			// 1.3.1绘制蛇头
			g.setColor(Color.red);
			Point head = snakeList.getFirst();
			g.fill3DRect(head.x * CELL_WIDTH, head.y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, true);
			// 1.3.2绘制蛇身
			g.setColor(Color.orange);
			for (int i = 1; i < snakeList.size(); i++) {
				Point body = snakeList.get(i);
				g.fill3DRect(body.x * CELL_WIDTH, body.y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, true);
			}
		} else if (SnakeGame.state == SnakeGame.PAUSE) {

		} else if (SnakeGame.state == SnakeGame.GAMEOVER) {
			g.drawRect(0, 0, SnakeGame.WIDTH, SnakeGame.HEIGHT);
			g.setColor(Color.red);
			g.setFont(new Font("宋体", Font.BOLD, 36));
			g.drawString("GAME OVER", SnakeGame.WIDTH / 3, SnakeGame.HEIGHT / 2);
			g.setColor(Color.pink);
			g.setFont(new Font("宋体", Font.BOLD, 23));
			g.drawString("Enter重新开始游戏", SnakeGame.WIDTH / 2, SnakeGame.HEIGHT / 3 * 2);
			String string1 = "分数" + " " + SnakeGame.score;
			g.drawString(string1, SnakeGame.WIDTH / 8, SnakeGame.HEIGHT / 5);
			String string2;
			if(SnakeGame.currentTime%1000>=100){
				string2 = "时间" + " " + SnakeGame.currentTime/1000+"'"+SnakeGame.currentTime%1000;
			}else{
				string2 = "时间" + " " + SnakeGame.currentTime/1000+"'0"+SnakeGame.currentTime%1000;				
			}
			g.drawString(string2,SnakeGame.WIDTH/6,SnakeGame.HEIGHT/6*5);
		}
	}

	// 2.Food类
	// 2.1初始化食物坐标
	// 初始化食物
	public void initFood() {
		Random random = new Random();
		boolean flag = true;
		while (flag) {
			int x = random.nextInt(CELL_X);
			int y = random.nextInt(CELL_Y);
			if (x >= 1 && x < CELL_X - 1 && y >= 1 && y < CELL_Y - 1) {
				food = new Point(x, y);
				for (int i = 0; i < snakeList.size(); i++) {
					Point snake = snakeList.get(i);
					if (snake.equals(food)) {
						continue;
					}
					flag = false;
				}
			}
		}

	}

	// 3.Snake类
	// 3.1将snake的head初始化到窗口的中间
	// 3.2初始化两个身子
	public void initSnake() {
		int x = CELL_X / 2;
		int y = CELL_Y / 2;
		Point head = new Point(x, y);
		snakeList.add(head);
		for (int i = 1; i < INITSNAKELENGTH; i++) {
			Point body = new Point(x - i, y);
			snakeList.add(body);
		}
	}

	int currentDir = RIGHT;

	public void setDir(int dir) {
		// 如果方向不相反
		if (currentDir + dir != 0) {
			currentDir = dir;
			move(dir);
			repainPanel();
		}
	}

	public void move(int dir) {
		Point head = snakeList.getFirst();
		switch (dir) {

		case LEFT:
			snakeList.addFirst(new Point(head.x - 1, head.y));
			break;
		case UP:
			snakeList.addFirst(new Point(head.x, head.y - 1));
			break;
		case RIGHT:
			snakeList.addFirst(new Point(head.x + 1, head.y));
			break;
		case DOWN:
			snakeList.addFirst(new Point(head.x, head.y + 1));
			break;

		default:
			break;
		}
	}

	// 判断是否吃食物
	public boolean isEat() {
		Point head = snakeList.getFirst();
		if (head.equals(food)) {
			initFood();
			return true;
		}
		return false;
	}

	// 判断游戏是否结束
	public boolean isDeath() {
		Point head = snakeList.getFirst();
		if (head.x == 0 || head.x == CELL_X - 1 || head.y == 0 || head.y == CELL_Y - 1) {
			return true;
		}
		for (int i = 1; i < snakeList.size(); i++) {
			Point body = snakeList.get(i);
			if (head.equals(body)) {
				return true;
			}
		}
		return false;
	}
	// 4判断是否蛇的身子时候应该添加
	// 4.1如果没有迟到食物，蛇身子不变
	// 4/2如果迟到食物，蛇身增加
	// 5判断是否游戏结束
}

class Score extends JFrame {
	public static final int WIDTH = 215;
	public static final int HEIGHT = 235;

	public static void main(String[] agrs) {
		new Score();
	}

	Score() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frame_x = (dimension.width / 2 + SnakeGame.WIDTH / 2);
		int frame_y = (dimension.height - SnakeGame.HEIGHT) / 2;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocation(frame_x, frame_y);
		setVisible(true);

		ScorePanel scorePanel = new ScorePanel();
		add(scorePanel);
		scorePanel.change();

	}

	class ScorePanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.RED);
			g.setFont(new Font("宋体", Font.BOLD, 20));
			String string1 = "分数" + " " + SnakeGame.score;
			g.drawString(string1, Score.WIDTH / 8, Score.HEIGHT / 5);

			g.setColor(Color.blue);
			g.setFont(new Font("宋体", Font.BOLD, 20));
			String string2;
			if(SnakeGame.currentTime%1000>=100){
				string2 = "时间" + " " + SnakeGame.currentTime/1000+"'"+SnakeGame.currentTime%1000;
			}else{
				string2 = "时间" + " " + SnakeGame.currentTime/1000+"'0"+SnakeGame.currentTime%1000;				
			}
			g.drawString(string2, Score.WIDTH / 8, Score.HEIGHT / 3 * 2);

		}

		public void change() {
			new Thread() {
				public void run() {
					while (true) {

						try {
							
							if(SnakeGame.state == SnakeGame.RUNNING){
								SnakeGame.currentTime = System.currentTimeMillis() - SnakeGame.startTime;
							}
							repaint();
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
	}
}