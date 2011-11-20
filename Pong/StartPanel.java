
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class StartPanel extends JPanel
{
	private long period;
	private PongFrame pf;

	private JButton localMultiChoice;
	private JButton pvCPUChoice;
	private JButton onlineMultiChoice;

	public enum Choice
	{
		LOCAL, AI, ONLINE
	}

	public StartPanel(PongFrame pf, long period)
	{
		this.pf = pf;
		this.period = period;

		createUserInterface();
	}

	private void createUserInterface()
	{

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PongFrame.WIDTH, PongFrame.HEIGHT));

		setFocusable(true);
		requestFocus();

		localMultiChoice = new JButton("Local Multiplayer");
		localMultiChoice.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pf.addPongPanel(new PongPanel(pf, period, Choice.LOCAL));
			}
		});
		add(localMultiChoice, "Center");

		pvCPUChoice = new JButton("Player V CPU");
		pvCPUChoice.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pf.addPongPanel(new PongPanel(pf, period, Choice.AI));
			}
		});
		add(pvCPUChoice,"Center");

		onlineMultiChoice = new JButton("Online Multiplayer");
		onlineMultiChoice.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pf.addOnlinePanel(new OnlinePanel(pf));
			}
		});
		add(onlineMultiChoice, "Center");
	}
}
