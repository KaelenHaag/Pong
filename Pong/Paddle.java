
import java.awt.geom.*;
import java.awt.*;
public class Paddle
{
	protected PongPanel pp;
	protected final int dx = 0, dy = 20;

	//protected int score = 0;
	protected Area paddleArea;

	//Side the paddle is on
	public enum Side
	{
		LEFT, RIGHT
	}

	public Paddle(PongPanel pp, Side s)
	{
		this.pp = pp;
		createArea(s);
	}

	private void createArea(Side s)
	{
		int startingX;
		switch(s)
		{
			case LEFT: System.out.println("left");
					startingX = 0;
					break;
			case RIGHT: System.out.println("right");
					startingX = PongFrame.WIDTH - 10;
					break;

			default: startingX = 0;
		}

		int paddleHeight = PongFrame.HEIGHT / 5;
		int startingY = paddleHeight * 2;
		paddleArea = new Area(new Rectangle(startingX, startingY, 10, paddleHeight));
	}

	protected void moveUp()
	{
		if(canMoveUp())
		{
			AffineTransform t = AffineTransform.getTranslateInstance(dx, -dy);
			paddleArea.transform(t);
		}
	}

	protected void moveDown()
	{
		if(canMoveDown())
		{
			AffineTransform t = AffineTransform.getTranslateInstance(dx, dy);
			paddleArea.transform(t);
		}
	}

	public boolean canMoveUp()//int dy)
	{
		Rectangle bounds = paddleArea.getBounds();
		return bounds.getY() - dy >= 0;
	}

	public boolean canMoveDown()//int dy)
	{
		Rectangle bounds = paddleArea.getBounds();
		return bounds.getY() + bounds.getHeight() + dy <= PongFrame.HEIGHT;
	}

	public void hitBall()
	{
	}

	public Area getPaddleArea()
	{
		return paddleArea;
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.RED);
		g.fill(paddleArea);
	}
}
