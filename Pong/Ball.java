import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Ball
{
	private int dx, dy;
	private Timer movementTimer;
	private long movementTime = 30;

	private Area ballArea;

	private AIPaddle ai;

	private PongPanel pp;
	private long timePassed = 0;

	private final Random ran = new Random();

	public Ball(PongPanel pp)
	{
		dx = -5;
		dy = 5;

		this.pp = pp;

		//we want the ball to actually look like a ball rather than a square
		ballArea = new Area(new Ellipse2D.Double(PongFrame.WIDTH / 2, (ran.nextInt(39) * 10), 10, 10));
	}

	//Starts the movementTimer with a specified delay
	//Delay is a parameter because we play on altering the timer's interval time in game.
	//So we would pass the ball's previous time as the delay to make the increase less "drastic".
	public void startTimer(long delay)
	{
		movementTimer = new Timer(true);
		movementTimer.schedule(new TimerTask()
		{
			public void run()
			{
				if(!pp.isPaused() && pp.isRunning() && !pp.isGameOver())
				{
					timePassed += movementTime;
					move();
					if(timePassed >= 6000)
					{
						timePassed = 0;
						speedUp();
					}
				}
			}
		}, delay, movementTime);
	}

	//Speeds up the ball through decreasing the time interval between moves
	//Need to restart the timer for it to use the new time interval
	public void speedUp()
	{
		movementTimer.purge();
		movementTimer.cancel();
		long previousSpeed = movementTime;
		if(movementTime - 1 > 15)
			movementTime--;
		startTimer(0);
		System.out.println(movementTime);
	}

	public Timer getMovementTimer()
	{
		return movementTimer;
	}

	public void move()
	{
		Area temp = new Area(ballArea);
		AffineTransform tempT = AffineTransform.getTranslateInstance(dx, dy);
		temp.transform(tempT);

		//Tests if scored
		if(temp.getBounds().getX() >= PongFrame.WIDTH || temp.getBounds().getX() < 0)
		{
			pp.updateScore(dx);
			respawn();
		}
		else
		{
			//Tests if hit wall
			if(temp.getBounds().getY() + 10 > PongFrame.HEIGHT || temp.getBounds().getY() < 0)
			{
				dy = -dy;
			}

			//create a new area to test new transformations for collisions to paddle
			temp = new Area(ballArea);
			tempT = AffineTransform.getTranslateInstance(dx,dy);
			temp.transform(tempT);

			//Tests if hit paddle
			Paddle[] paddles = pp.getPaddles();
			for(int i = 0; i < paddles.length; i++)
			{
				if(temp.intersects(paddles[i].getPaddleArea().getBounds()))
				{
					if(paddles[i] instanceof AIPaddle)
					{
						((AIPaddle)paddles[i]).hitBall();
					}
					dx = -dx;
					break;
				}
			}
		}
		AffineTransform t = AffineTransform.getTranslateInstance(dx, dy);
		ballArea.transform(t);
	}

	//Respawn the ball
	//handles restarting the timer,creating a new area, and updating the score
	public void respawn()
	{
		stopTimer();

		if(pp.getAIPlayer() != null)
		{
			pp.getAIPlayer().ballHasRespawned();
		}

		ballArea = new Area(new Ellipse2D.Double(PongFrame.WIDTH / 2,
			(ran.nextInt(39) * 10), 10, 10));
		dx = -dx;
		movementTime = 30;
		timePassed = 0;
		startTimer(1000);
	}

	public void stopTimer()
	{
		movementTimer.cancel();
		movementTimer.purge();
	}

	public void addAIPaddle(AIPaddle ai)
	{
		this.ai = ai;
	}

	public Area getBallArea()
	{
		return ballArea;
	}

	public int getDX()
	{
		return dx;
	}

	public int getDY()
	{
		return dy;
	}

	public long getMovementTime()
	{
		return movementTime;
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLUE);
		g.fill(ballArea);
	}
}
