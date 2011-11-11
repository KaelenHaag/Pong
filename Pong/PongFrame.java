//Some code used from Andrew Davison's book Killer Game Programming in Java
//Website of book http://fivedots.coe.psu.ac.th/~ad/jg


import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.*;


public class PongFrame extends JFrame implements WindowListener
{
	private static int DEFAULT_FPS = 100;

	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;

	private StartPanel sp;
	private PongPanel pp;
	private OnlinePanel op;

	public PongFrame(long period)
	{
		super("Pong");
		createUserInterface(period);

		addWindowListener(this);
		pack();
		setResizable(false);
		setVisible(true);
	}

	private void createUserInterface(long fps)
	{
		Container contentPane = getContentPane();
		sp = new StartPanel(this, fps);
		contentPane.add(sp, "Center");
	}

	public void addPongPanel(PongPanel pp)
	{
		this.pp = pp;
		getContentPane().add(pp, "Center");
		getContentPane().remove(sp);
		pack();
	}

	public void addOnlinePanel(OnlinePanel op)
	{
		this.op = op;
		getContentPane().add(op, "Center");
		getContentPane().remove(sp);
		pack();
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
  	{  if(pp != null)pp.stopGame();  }

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
