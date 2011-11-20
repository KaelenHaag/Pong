//Some code used from Andrew Davison's book Killer Game Programming in Java
//Website of book http://fivedots.coe.psu.ac.th/~ad/jg

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
//import java.applet.*;
import java.util.*;


public class PongFrame extends JFrame implements WindowListener
{


	private static int DEFAULT_FPS = 100;

	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;

	private StartPanel sp;
	private PongPanel pp;
	private OnlinePanel op;
	private JPanel currentPanel;

	private JMenuBar optionsBar;
	private JButton backButton;
	Stack<JPanel> menus = new Stack<JPanel>();

	public PongFrame(long period)
	{
		super("Pong");

		createUserInterface(period);
		
		//I know im going to online have one panel(menu/game) being shown at a time so when a panel is being added I can remove the previous one.
		getContentPane().addContainerListener(new ContainerListener()
		{
			public void componentAdded(ContainerEvent e)
			{
				e.getContainer().remove(currentPanel);
				if(e.getChild() instanceof PongPanel)
				{
					pp = (PongPanel)e.getChild();
					menus.push(pp);
					currentPanel = pp;
					backButton.setEnabled(true);
				}
				else
				{
					if(e.getChild() instanceof OnlinePanel)
					{
						op = (OnlinePanel)e.getChild();
						menus.push(op);
						currentPanel = op;
						backButton.setEnabled(true);
					}
					else
					{
						if(e.getChild() instanceof StartPanel)
						{
							backButton.setEnabled(false);
							currentPanel = sp;
						}
					} 
				}
				validate();
				
			}
			
			public void componentRemoved(ContainerEvent e)
			{
				if(e.getChild() instanceof PongPanel)
				{
					pp = null;
				}
				else
				{
					if(e.getChild() instanceof OnlinePanel)
					{
						op = null;
					}
				}
			}
		});
		
		
		addWindowListener(this);
		pack();
		
		
		
		//Code from http://www.java2s.com/Code/Java/Swing-JFC/CenterOnScreen.htm
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);

		setResizable(false);
		setVisible(true);
	}

	private void createUserInterface(long fps)
	{
		Container contentPane = getContentPane();
		sp = new StartPanel(this, fps);
		menus.push(sp);
		contentPane.add(sp, "Center");
		currentPanel = sp;

		backButton = new JButton("Back");
		backButton.setEnabled(false);
		backButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				backButtonActionPerformed(e);
			}
		});

		optionsBar = new JMenuBar();
		setJMenuBar(optionsBar);
		optionsBar.add(backButton);
	}

	private void backButtonActionPerformed(ActionEvent e)
	{
		System.out.println(Thread.activeCount());
		
		// Main idea here is that the start panel should never be removed because there wouldn't be anymore menus!
		if(!(menus.peek() instanceof StartPanel))
		{
			menus.pop(); //Gets rid of the currentPanel from the stack
			getContentPane().add(menus.peek());
			repaint();
		}

	}

	public JButton getBackButton()
	{
		return backButton;
	}

	public long getPeriod()
	{
		return (long)1000.0/DEFAULT_FPS;
	}

	//Implemented WindowListener methods
	public void windowActivated(WindowEvent e)
	{if(pp != null)pp.resumeGame();}

	public void windowDeactivated(WindowEvent e)
	{if(pp != null)pp.pauseGame();}

	public void windowDeiconified(WindowEvent e)
	{if(pp != null)pp.pauseGame();}

	public void windowIconified(WindowEvent e)
	{if(pp != null)pp.resumeGame();}

	public void windowClosing(WindowEvent e)
  	{
  		 if(pp != null)
  		 {
  		 	pp.stopGame();
  		 }
  		 else
  		 {
  		 	System.exit(0);
  		 }
  	}

  	public void windowClosed(WindowEvent e) {}
  	public void windowOpened(WindowEvent e) {}

	public static void main(String[] args)
	{
		int fps = DEFAULT_FPS;

		long period = (long)1000.0/fps;
		System.out.println("fps: " + fps + " period: " + period + " ms");

		new PongFrame(period * 1000000L);
	}
}
