package GUI;
import javax.swing.*;

import java.awt.*;

public class GuiFrame extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuiFrame()
    {
        this.add(new GuiPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void start()
    {
    	GuiFrame mainFrame = new GuiFrame();
        mainFrame.setVisible(true);
    }
    
    /*
     * public static void main(String args[])
    {
    	GuiFrame mainFrame = new GuiFrame();
        mainFrame.setVisible(true);
    }
     */
}
