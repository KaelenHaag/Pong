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

	private JMenuBar optionsBar;
	private JButton backButton;
	Stack<JPanel> menus = new Stack<JPanel>();

	public PongFrame(long period)
	{
		super("Pong");

		createUserInterface(period);

		addWindowListener(this);
		pack();
		
		
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

	public void addPongPanel(PongPanel pp)
	{
		this.pp = pp;
		getContentPane().add(pp, "Center");
		getContentPane().remove(sp);
		menus.push(pp);
		backButton.setEnabled(true);
		validate();
	}

	public void addOnlinePanel(OnlinePanel op)
	{
		this.op = op;
		getContentPane().add(op, "Center");
		getContentPane().remove(sp);
		menus.push(op);
		backButton.setEnabled(true);
		validate();
		repaint();
	}

	private void backButtonActionPerformed(ActionEvent e)
	{
		System.out.println(Thread.activeCount());
		ContainerListener[] temp = getContainerListeners();
		//System.out.println(menus.peek() instanceof StartPanel);
		for(int i = 0; i < temp.length; i++)
		{
			System.out.println(temp[i]);
		}
		if(!(menus.peek() instanceof StartPanel))		// Main idea here is that the start panel should never be removed because there wouldn't be anymore menus!
		{
			JPanel panelToBeRemoved = menus.pop();
			getContentPane().remove(panelToBeRemoved);
			getContentPane().add(menus.peek());
			validate();
			repaint();
			if(panelToBeRemoved instanceof PongPanel)
			{
				pp = null;
				panelToBeRemoved = null;
			}

			if(menus.peek() instanceof StartPanel)
			{
				backButton.setEnabled(false);
			}
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
