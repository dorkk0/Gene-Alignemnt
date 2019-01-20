package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ProgressiveAlignment.Alignment;
import ProgressiveAlignment.Demo;
import ProgressiveAlignment.NeighborJoining;
import ProgressiveAlignment.Node;
import hirschberg.HirschbergAlign;
import hirschberg.Sequence;
import hirschberg.SubstitutMatrixValues;

public class ProgAlPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton startBtn;
	
	private JTextField t1, t2, t3;

    private TextArea sequencesInput;
    
    private TextArea treeOutput;

	private GuiProgAlignmentPanel alignmentOutput;
    private TextArea matrixOutput;


    public ProgAlPanel()
    {
    	startBtn = new JButton("Start   ");
        startBtn.addActionListener(this);
        
        t1 = new JTextField("Insert match here...");
        t2 = new JTextField("Insert mismatch here...");
        t3 = new JTextField("Insert indel here...");
        
        t1.setBounds(70,70,250,30); 
        t2.setBounds(70,70,250,30); 
        t3.setBounds(70,70,250,30); 
        
        this.sequencesInput = new TextArea();
        this.treeOutput = new TextArea();
        
        this.alignmentOutput = new GuiProgAlignmentPanel();
        this.matrixOutput = new TextArea();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //============================================input components==================================================

        JPanel inputPanel  = new JPanel();
        inputPanel.setPreferredSize(new Dimension(350,300));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(sequencesInput);
        add(makeBorder(inputPanel, "Sequence input"));

        JPanel costPanel = new JPanel();
        costPanel.setLayout(new GridLayout());
        costPanel.setPreferredSize(new Dimension(200,80));
        //costPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        costPanel.add(t1);
        costPanel.add(t2);
        costPanel.add(t3);
        add(makeBorder(costPanel, "Costs Input"));
        
        //============================================output components=================================================

        JPanel container = new JPanel();
        container.setMaximumSize(new Dimension(10,10));
        container.add(alignmentOutput);
        JScrollPane sp = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setPreferredSize(new Dimension(200, 200));
        add(makeBorder(sp,"Alignment output"));
        
        JPanel container1 = new JPanel();
        matrixOutput.setMaximumSize(new Dimension(10,10));
        matrixOutput.setPreferredSize(new Dimension(1000,350));
        container1.add(matrixOutput);
        JScrollPane sp1 = new JScrollPane(matrixOutput,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp1.setPreferredSize(new Dimension(200, 200));
        add(makeBorder(matrixOutput,"Distance matrix output"));

        JPanel container2 = new JPanel();
        container2.setMaximumSize(new Dimension(10,10));
        container2.add(treeOutput);
        JScrollPane sp2 = new JScrollPane(container2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp1.setPreferredSize(new Dimension(200, 200));
        //add(makeBorder(sp2,"Tree output"));
        
        /*
        container2.setPreferredSize(new Dimension(350,300));
        container2.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        container2.add(sequencesInput);
        add(makeBorder(container2, "Sequence input"));
        */
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
    	
    	String s1 = t1.getText();
    	String s2 = t2.getText();
    	String s3 = t3.getText();
    	
    	Double match = Double.parseDouble(s1);
    	Double mismatch = Double.parseDouble(s2);
    	Double indel = Double.parseDouble(s3);
    	
    	SubstitutMatrixValues substitutMatrixVals = new SubstitutMatrixValues(match, mismatch, indel);
    	
        String input = sequencesInput.getText().
                replace("\n", "").
                replace("\r", "");

        ArrayList<String> sequencesStrings = new ArrayList<>(Arrays.asList(input.split(">")));
        sequencesStrings.removeIf(item -> item.trim().equals(""));

        Sequence[] sequences = new Sequence[sequencesStrings.size()];

        for(int i = 0; i < sequences.length; i++)
            sequences[i] = new Sequence(sequencesStrings.get(i));
        
        /*
        HirschbergAlign aligner = new HirschbergAlign(sequences, substitutMatrixVals);
        Alignment alignment = aligner.calcAlignmentMatrix();

        //set the output to the relevant component
        centerStarOutput.setText(aligner.getFinalCenterStarSeq().toString());
        consensusStrsOutput.setText(alignment.getConsensusString().toString());
        alignmentOutput.makeAlignment(alignment);
        scoreOutput.setText(alignment.getDistance(substitutMatrixVals) + "");
        */
    	
    	List<Alignment> alignments = new ArrayList<>();
    	
    	for(int i = 0; i < sequences.length; i++)
        {
            List<Sequence> aL = new ArrayList<>();
            aL.add(sequences[i]);
            alignments.add(new Alignment(aL, substitutMatrixVals));
        }

        NeighborJoining nj = new NeighborJoining(alignments, substitutMatrixVals);
        nj.printDistancesMatrix();
        System.out.println("----------------");
        String distanceMatrix = nj.createDistanceMatrixString();
        
        nj.buildTreeNew();
        matrixOutput.setText(distanceMatrix);
        Node root = nj.getRoot();
        treeOutput.setText(root.toString());
        root.print(0);
      
        JFrame frame = new JFrame();
        treeOutput.setPreferredSize(new Dimension(1000,1000));
        frame.add(treeOutput);
        frame.pack();
        frame.setVisible(true);
        
        alignmentOutput.makeAlignment(root.getAlignment());
        
    }

}
