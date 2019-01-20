package GUI;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import hirschberg.Alignment;
import hirschberg.HirschbergAlign;
import hirschberg.Sequence;
import hirschberg.SubstitutMatrixValues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class GuiPanel extends JPanel implements ActionListener
{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton startBtn;
	
    private GuiAlignmentPanel alignmentOutput;
    
    private TextArea scoreOutput;
    private TextArea sequencesInput;
    
    private TextArea centerStarOutput;
    private TextArea consensusStrsOutput;
    

    private JTextField t1, t2, t3;
    
    public GuiPanel()
    {
    	startBtn = new JButton("Start   ");
        startBtn.addActionListener(this);
        
        this.scoreOutput = new TextArea();
        this.sequencesInput = new TextArea();
        
        this.centerStarOutput = new TextArea();
        this.consensusStrsOutput = new TextArea();
        
        this.alignmentOutput = new GuiAlignmentPanel();
        
        t1 = new JTextField("Insert match here...");
        t2 = new JTextField("Insert mismatch here...");
        t3 = new JTextField("Insert indel here...");
        
        t1.setBounds(70,70,250,30); 
        t2.setBounds(70,70,250,30); 
        t3.setBounds(70,70,250,30); 
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //============================================input components==================================================

        JPanel inputPanel  = new JPanel();
        inputPanel.setPreferredSize(new Dimension(350,300));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(sequencesInput);
        add(makeBorder(inputPanel, "sequence input"));

        JPanel costPanel = new JPanel();
        costPanel.setPreferredSize(new Dimension(350,300));
        //costPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        costPanel.add(t1);
        costPanel.add(t2);
        costPanel.add(t3);
        add(makeBorder(costPanel, "Costs Input"));
        
        //============================================output components=================================================

        JPanel container = new JPanel();
        container.setMaximumSize(new Dimension(10,10));
        container.add(alignmentOutput);
        alignmentOutput.setPreferredSize(new Dimension(1000,400));
        JScrollPane sp = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setPreferredSize(new Dimension(1000, 500));
        scoreOutput.setPreferredSize(new Dimension(1000,20));
        centerStarOutput.setPreferredSize(new Dimension(1000,20));
        consensusStrsOutput.setPreferredSize(new Dimension(1000,20));
        add(makeBorder(sp,"alignment output"));  
        JPanel cont = new JPanel();
        cont.setLayout(new GridLayout(3,1));
        cont.setPreferredSize(new Dimension(1000,200));
        cont.add(makeBorder(scoreOutput,"score output"));
        cont.add(makeBorder(centerStarOutput,"center star output"));
        cont.add(makeBorder(consensusStrsOutput,"consensus output"));
        add(cont);
        add(startBtn);
    }

    public  JPanel makeBorder(Component c, String title)
    {
        JPanel panel = new JPanel();
        UIManager.getDefaults().put("TitledBorder.titleColor", Color.BLACK);
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder Title = BorderFactory.createTitledBorder(lowerEtched, title);
        Font titleFont = UIManager.getFont("TitledBorder.font");
        Title.setTitleFont(titleFont.deriveFont(Font.BOLD) );
        panel.setBorder(Title);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(c);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
    	Double match = Double.parseDouble(t1.getText()) ,mismatch= Double.parseDouble(t2.getText()), indel = Double.parseDouble(t1.getText());
        SubstitutMatrixValues substitutMatrixVals = new SubstitutMatrixValues(match, mismatch, indel);

        String input = sequencesInput.getText().
                replace("\n", "").
                replace("\r", "");

        ArrayList<String> sequencesStrings = new ArrayList<>(Arrays.asList(input.split(">")));
        sequencesStrings.removeIf(item -> item.trim().equals(""));

        Sequence[] sequences = new Sequence[sequencesStrings.size()];

        for(int i = 0; i < sequences.length; i++)
            sequences[i] = new Sequence(sequencesStrings.get(i));

        HirschbergAlign aligner = new HirschbergAlign(sequences, substitutMatrixVals);
        Alignment alignment = aligner.calcAlignmentMatrix();

        //set the output to the relevant component
        centerStarOutput.setText(aligner.getFinalCenterStarSeq().toString());
        consensusStrsOutput.setText(alignment.getConsensusString().toString());
        alignmentOutput.makeAlignment(alignment);
        scoreOutput.setText(alignment.getDistance(substitutMatrixVals) + "");
    }
}
