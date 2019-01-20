package GUI;
import javax.swing.*;

//import ProgressiveAlignment.Alignment;

import java.awt.*;

public class GuiMatrixPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pan;

    public void printDMatrix(Double[][] matrix, int size)
    {
        if(pan != null)
            this.remove(pan);

        pan = new JPanel();
        pan.setPreferredSize(this.getSize());
        pan.setLayout(new GridLayout(size, size));
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                Double d = matrix[i][j];
                JLabel label = new JLabel(""+ d);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("courier new", Font.BOLD, 15));
                pan.add(label);
            }
        }
        this.add(pan);
        pan.setPreferredSize(new Dimension(15*size,15*size));
        pan.setMinimumSize(new Dimension(15*size,15*size));
        pan.setSize(new Dimension(15*size,15*size));
        this.revalidate();
        this.repaint();
    }
}
