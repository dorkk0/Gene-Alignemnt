package GUI;
import javax.swing.*;
import java.awt.*;

public class GuiTextArea extends JTextArea
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuiTextArea()
    {
        this.setVisible(true);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLineWrap(true);
        this.setFocusable(true);
    }
}
