package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton hirshButton;
	private JButton progButton; 

	private JPanel buttons;
	
	
	public MainPanel()
	{
		
		buttons = new JPanel(new GridLayout(1,0));
		hirshButton = new JButton("Hirschberg Alignment");
		progButton = new JButton("Progressive Alignment");
		
		buttons.add(hirshButton);
		buttons.add(progButton);
		
		hirshButton.addActionListener(this);
		progButton.addActionListener(this);
		
		this.add(buttons,BorderLayout.CENTER);
	    
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if(source == hirshButton)
		{
			GuiFrame.start();
		}
		else
		{
			ProgAlFrame.start();
		}

	}
	
}
