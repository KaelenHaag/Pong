import javax.swing.*;
import java.awt.*;

public class OnlinePanel extends JPanel
{
	private PongFrame pf;

	private JButton host;
	private JButton join;

	public OnlinePanel(PongFrame pf)
	{
		this.pf = pf;
		createUserInterface();
	}

	public void createUserInterface()
	{
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PongFrame.WIDTH, PongFrame.HEIGHT));

		setFocusable(true);
		requestFocus();

		host = new JButton("Host a Game");
		add(host, "Center");

		join = new JButton("Join a Game");
		add(join, "Center");
	}
}
