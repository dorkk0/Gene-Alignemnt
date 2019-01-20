package GUI;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public MainFrame()
	{
		this.add(new MainPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);	
	}
	
	
	public static void main(String[] args)
	{
		MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
	}

}
