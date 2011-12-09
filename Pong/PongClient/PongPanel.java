//Some code used from Andrew Davison's book Killer Game Programming in Java
//Website of book http://fivedots.coe.psu.ac.th/~ad/jg

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class PongPanel extends JPanel implements Runnable
{
	private static long MAX_STATS_INTERVAL = 1000000000L;
	private static int MAX_FRAME_SKIPS = 5;
	private static final int NO_DELAYS_PER_YIELD = 16;

	private static int NUM_FPS = 10;

	private Thread animator;

	private int leftPlayerScore = 0, rightPlayerScore = 0;

	private PongFrame pf;

	private long statsInterval = 0L;
	private long prevStatsTime;
	private long gameStartTime = 0L;
	private long totalElapsedTime = 0L;
	private int timeSpentInGame = 0;

	private long frameCount = 0L;
	private double fpsStore[];
	private long statsCount = 0;
	private double avgFPS = 0.0;

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;

	private volatile boolean isPaused = false;
	private volatile boolean running = false;
	private volatile boolean gameOver = false;

	private volatile PlayerPaddle p1, p2;
	private volatile AIPaddle ai1;
	private volatile Paddle paddles[] = new Paddle[2];
	private volatile Ball ball;

	private StartPanel.Choice c;

	private long period;

	private Graphics2D g;
	private Image img = null;

	private String statusMsg;
	private Font font;
	private FontMetrics metrics;

	private DecimalFormat df = new DecimalFormat("0.##");

	public PongPanel(PongFrame pf, long period)
	{
		this.pf = pf;
		this.period = period;

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PongFrame.WIDTH, PongFrame.HEIGHT));

		setFocusable(true);

		ball = new Ball(this);

		font = new Font("sanserif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

		fpsStore = new double[NUM_FPS];
		for(int i = 0; i < NUM_FPS; i++)
		{
			fpsStore[i] = 0.0;
		}

	}

	public PongPanel(PongFrame pf, long period, StartPanel.Choice c)
	{
		this(pf, period);
		this.c = c;
		switch(c)
		{
			case LOCAL:
				p1 = new PlayerPaddle(this, Paddle.Side.LEFT);
				p2 = new PlayerPaddle(this, Paddle.Side.RIGHT);
				paddles[0] = p1;
				paddles[1] = p2;
				break;

			case AI:
			case ONLINE:
				ai1 = new AIPaddle(this, Paddle.Side.LEFT);
				p1 = new PlayerPaddle(this, Paddle.Side.RIGHT);
				paddles[0] = ai1;
				paddles[1] = p1;
				break;
		}

	}

	public void addNotify()
	{
		super.addNotify();
		requestFocus();
		startGame();
	}

	public void removeNotify()
	{
		super.removeNotify();
		System.out.println("Remove notify");
		ball.stopTimer();
		if(ai1 != null)
			ai1.stopTimer();
	}

	public void startGame()
	{
		if(animator == null || !running)
		{
			animator = new Thread(this, "animator");
			animator.start();
		}
		ball.startTimer(1000);
	}

	public void run()
	{

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		gameStartTime = System.nanoTime();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;

		running = true;

		while(running)
		{
			gameRender();
			paintScreen();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;


			if(sleepTime > 0)
			{
				try
				{
					Thread.sleep(sleepTime/1000000L);
				}
				catch(InterruptedException e){}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else
			{
				excess -= sleepTime;
				overSleepTime = 0L;

				if(++noDelays >= NO_DELAYS_PER_YIELD)
				{
					Thread.yield();
					noDelays = 0;
				}
			}

			int skips = 0;
			while((excess > period) && (skips < MAX_FRAME_SKIPS))
			{
				excess -= period;
				skips++;
			}
			framesSkipped += skips;

			storeStats();

			beforeTime = System.nanoTime();
		}
		printStats();
		return;
		//System.exit(0);
	}

	private void gameRender()
	{
		if(img == null)
		{
			img = createImage(PongFrame.WIDTH, PongFrame.HEIGHT);
			if(img == null)
			{
				System.out.println("ruh roh!");
				return;
			}
			else
			{
				g = (Graphics2D)img.getGraphics();
			}

		}

		g.setColor(Color.WHITE);
		g.fillRect(0,0, PongFrame.WIDTH, PongFrame.HEIGHT);

		String msg = leftPlayerScore + " : " + rightPlayerScore;
		int x = (PongFrame.WIDTH - metrics.stringWidth(msg)) / 2;
		g.setFont(font);
		g.setColor(Color.ORANGE);
		g.drawString(msg, x, metrics.getHeight());

		statusMsg = "Waiting...";
		if(c == StartPanel.Choice.ONLINE && statusMsg != null)
		{

			 x = (PongFrame.WIDTH - metrics.stringWidth(statusMsg)) / 2;
			 g.drawString(statusMsg, x, (PongFrame.HEIGHT - metrics.getHeight() - 10));
		}


		if(isPaused)
		{
			g.drawString("Paused!", (PongFrame.WIDTH - metrics.stringWidth("Paused!")) / 2, (PongFrame.HEIGHT - metrics.getHeight()) / 2);
		}

		p1.draw(g);
		ball.draw(g);
		switch(c)
		{
			case LOCAL:
				p2.draw(g);
				break;

			case AI:
			case ONLINE:
				ai1.draw(g);
				break;
		}
	}

	private void paintScreen()
	{
		Graphics2D g;
		try
		{
			g = (Graphics2D)this.getGraphics();
			if((g != null) && (img != null))
			{
				g.drawImage(img, 0, 0, null);
			}
			g.dispose();
		}
		catch(NullPointerException e)
		{
			running = false;
		}
	}

	public void restartGame()
	{
		rightPlayerScore = 0;
		leftPlayerScore = 0;
		ball.respawn();
	}

	public void updateScore(int dx)
	{
		if(dx < 0)
		{
			rightPlayerScore++;
		}
		else
		{
			leftPlayerScore++;
		}

		if(rightPlayerScore >= 7 || leftPlayerScore >= 7)
		{
			restartGame();
		}
	}

	public void pauseGame()
	{
		isPaused = true;
	}

	public void resumeGame()
	{
		isPaused = false;
	}

	public void stopGame()
	{
		running = false;
		System.exit(0);
	}

	public boolean isPaused()
	{
		return isPaused;
	}

	public boolean isRunning()
	{
		return running;
	}

	public boolean isGameOver()
	{
		return gameOver;
	}

	public Paddle getPlayer1()
	{
		return p1;
	}

	public Paddle getPlayer2()
	{
		return p2;
	}

	public AIPaddle getAIPlayer()
	{
		return ai1;
	}

	public Paddle[] getPaddles()
	{
		return paddles;
	}

	public Ball getBall()
	{
		return ball;
	}

	public StartPanel.Choice getChoice()
	{
		return c;
	}

	private void storeStats()
	{
		frameCount++;
   		statsInterval += period;

    	if (statsInterval >= MAX_STATS_INTERVAL)
    	{     // record stats every MAX_STATS_INTERVAL
      		long timeNow = System.nanoTime();
      		timeSpentInGame = (int) ((timeNow - gameStartTime)/1000000000L);  // ns --> secs

      		long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
      		totalElapsedTime += realElapsedTime;

      		double timingError =
         		((double)(realElapsedTime - statsInterval) / statsInterval) * 100.0;

      		totalFramesSkipped += framesSkipped;

      		double actualFPS = 0;     // calculate the latest FPS
      		if (totalElapsedTime > 0)
      		{
        		actualFPS = (((double)frameCount / totalElapsedTime) * 1000000000L);
      		}

      		// store the latest FPS and UPS
      		fpsStore[ (int)statsCount%NUM_FPS ] = actualFPS;
      		statsCount = statsCount+1;

      		double totalFPS = 0.0;     // total the stored FPSs
      		for (int i=0; i < NUM_FPS; i++)
      		{
        		totalFPS += fpsStore[i];
      		}

      		if (statsCount < NUM_FPS)
      		{ // obtain the average FPS
        		avgFPS = totalFPS/statsCount;
      		}
      		else
      		{
        		avgFPS = totalFPS/NUM_FPS;
      		}

      		framesSkipped = 0;
      		prevStatsTime = timeNow;
      		statsInterval = 0L;   // reset
		}
	}

	private void printStats()
	{
		System.out.println("Frame Count/Loss: " + frameCount + " / " + totalFramesSkipped);
		System.out.println("Average FPS: " + df.format(avgFPS));
    	System.out.println("Time Spent: " + timeSpentInGame + " secs");
    	System.out.println("Player 1 Score: " + leftPlayerScore + " Player 2 Score: " + rightPlayerScore);
	}
}
