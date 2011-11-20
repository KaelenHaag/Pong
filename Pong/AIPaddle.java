import java.awt.geom.*;
import java.awt.*;
import java.util.*;

public class AIPaddle extends Paddle
{
	private int previousDx;
	private int hitCount = 0;

	private Timer reactionTimer = new Timer(true);

	public AIPaddle(PongPanel pp, Side s)
	{
		super(pp, s);
		startTimer();
	}

	public void startTimer()
	{
		reactionTimer.schedule(new TimerTask()
		{
			public void run()
			{
				if(!pp.isPaused() && pp.isRunning())
				{
					adjustPosition(pp.getBall().getBallArea());
				}
			}
		}, 0, 70);
	}

	public void hitBall()
	{
		hitCount++;
//		if(hitCount >= 3)
//		{
//			reactionTimer.cancel();
//			reactionTimer.purge();
//		}
	}

	public void adjustPosition(Area ballArea)
	{
		int dy = (int)(ballArea.getBounds().getY() - paddleArea.getBounds().getY());

		if(needsToMove(dy))
		{
			if(dy > 0 && canMoveDown())
			{
				AffineTransform temp = AffineTransform.getTranslateInstance(0, this.dy);
				paddleArea.transform(temp);
			}
			else
			{
				if(dy <= 0 && canMoveUp())
				{
					AffineTransform temp = AffineTransform.getTranslateInstance(0, -this.dy);
					paddleArea.transform(temp);
				}
			}
		}
	}

	public boolean needsToMove(int dy)
	{
		int buffer = new Random().nextInt(hitCount + 10);
		return dy <= (0 - buffer) || (dy > paddleArea.getBounds().getHeight() - (20-buffer));
	}

	public void ballHasRespawned()
	{
		hitCount = 0;
	}

	public void stopTimer()
	{
		reactionTimer.cancel();
		reactionTimer.purge();
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.GREEN);
		g.fill(paddleArea);
	}
}
