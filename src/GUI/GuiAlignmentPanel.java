package GUI;
import javax.swing.*;

import hirschberg.Alignment;

import java.awt.*;

public class GuiAlignmentPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pan;

    public void makeAlignment(Alignment alignment)
    {
        if(pan != null)
            this.remove(pan);

        pan = new JPanel();
        pan.setPreferredSize(this.getSize());
        pan.setLayout(new GridLayout(alignment.getSequenceSize(), alignment.getSequence(0).getLength()));
        for(int i = 0; i < alignment.getSequenceSize(); i++)
        {
            for(int j = 0; j < alignment.getLength(); j++)
            {
                char c = alignment.get(i,j);
                JLabel label = new JLabel(""+ c);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("courier new", Font.BOLD, 15));
                pan.add(label);
            }
        }
        this.add(pan);
        pan.setPreferredSize(new Dimension(15*alignment.getLength(),15*alignment.getSequenceSize()));
        pan.setMinimumSize(new Dimension(15*alignment.getLength(),15*alignment.getSequenceSize()));
        pan.setSize(new Dimension(15*alignment.getLength(),15*alignment.getSequenceSize()));
        this.revalidate();
        this.repaint();
    }
}
