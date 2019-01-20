package GUI;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class ProgAlFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public ProgAlFrame()
    {
        this.add(new ProgAlPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void start()
    {
    	ProgAlFrame progAlFrame = new ProgAlFrame();
    	progAlFrame.setVisible(true);
    }

}
