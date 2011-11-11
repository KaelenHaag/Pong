
import javax.swing.*;
import java.awt.event.*;
public class PlayerPaddle extends Paddle
{
	public PlayerPaddle(PongPanel pp, Side s)
	{
		super(pp, s);
		createKeyBindings(s);
	}

	//Originally in PongPanel class but moved to here because it allows us to create bindings for the player
	//for that specific player. Less hardcoding and more flexible
	private void createKeyBindings(Side s)
	{
		// TIME BETWEEN INPUTS FOR KEY HELD DOWN IS ~31 ns
		// To calc if key held down do  if(lastTime - e.getTime() <= ~40) (40 just to be safe)
		Action moveUp = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				moveUp();
			}
		};
		Action moveDown = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				moveDown();
			}
		};

		if(s == Side.LEFT)
		{
			pp.getInputMap().put(KeyStroke.getKeyStroke("W"), "upL");
			pp.getInputMap().put(KeyStroke.getKeyStroke("S"), "downL");
			pp.getActionMap().put("upL", moveUp);
			pp.getActionMap().put("downL", moveDown);
		}
		else
		{
			pp.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upR");
			pp.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downR");
			pp.getActionMap().put("upR", moveUp);
			pp.getActionMap().put("downR", moveDown);
		}
	}
}
