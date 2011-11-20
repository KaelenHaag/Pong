import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
		setLayout(null);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PongFrame.WIDTH, PongFrame.HEIGHT));

		setFocusable(true);
		requestFocus();

		host = new JButton("Host a Game");
		host.setBounds(225,150, 150, 23);
		host.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//featureNotAvailable();
				new PongHost();
				System.out.println("Contacting server...");
			}
		});
		add(host);

		join = new JButton("Join a Game");
		join.setBounds(225,200, 150, 23);
		join.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				featureNotAvailable();
				System.out.println("Contacting Server...");
			}
		});
		add(join);
	}
	
	private void featureNotAvailable()
	{
		JOptionPane.showMessageDialog(null, "Unfortunately, this feature is not available at this time.");
	}
}
