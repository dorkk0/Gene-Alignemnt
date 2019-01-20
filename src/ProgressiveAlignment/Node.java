package ProgressiveAlignment;

import javax.swing.JTree;

/**
 * A binary tree holding the results of joining.
 */

public class Node extends JTree 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Node mLeft;
    Node mRight;
    double mLeftDistance;
    double mRightDistance;
    String mLabel;
    int mNodeNumber;
    Alignment alignemnt;
    final int rootNum = -1;
   

    Node(Node left, Node right, Double mLeftDistance, Double mRightDistance, String label,Alignment alignment)
    {
        mLeft = left;
        mRight = right;
        this.mLeftDistance = mLeftDistance;
        this.mRightDistance = mRightDistance;
        this.alignemnt = alignment;
        mLabel = label;
        if(isNumeric(label))
            mNodeNumber = Integer.parseInt(label);
        else{
            mNodeNumber= rootNum;
        }
    }

    public void print(int count){
    	
    	
        if(this.mRight==null || this.mLeft==null){
            System.out.println("#"+ mLabel +"  "+ this.alignemnt.getsSequences());;
            return;
        }

        System.out.println("#"+mLabel + this.alignemnt.getsSequences() );

        System.out.print(mLabel+ " " );

        for(int i = 0 ; i<mRightDistance ; i++)
        {
            System.out.print("-");
        }
        System.out.print(String.format("%.2f",mRightDistance));
        for(int i = 0 ; i<mRightDistance+count ; i++)
        {
            System.out.print("-");
        }
        mRight.print(count +1);
        System.out.print(mLabel+ " ");


        for(int i = 0 ; i<mLeftDistance ; i++)
        {
            System.out.print("-");
        }
         System.out.print(String.format("%.2f",mLeftDistance));
        for(int i = 0 ; i<mLeftDistance+count ; i++)
        {
            System.out.print("-");
        }

        mLeft.print(count +1);
    }
    
    
    private String internalToString(StringBuilder sb, int count) {
        if(this.mRight==null || this.mLeft==null){
            sb.append("#"+ mLabel +"  "+ this.alignemnt.getsSequences()+"\n");;
            return "";
        }

        sb.append("#"+mLabel + this.alignemnt.getsSequences()+"\n" );

        sb.append(mLabel+ " " );

        for(int i = 0 ; i<mRightDistance ; i++)
        {
        	sb.append("-");
        }
        sb.append(String.format("%.2f",mRightDistance));
        for(int i = 0 ; i<mRightDistance+count ; i++)
        {
        	sb.append("-");
        }
        mRight.internalToString(sb,count +1);
        sb.append(mLabel+ " ");


        for(int i = 0 ; i<mLeftDistance ; i++)
        {
        	sb.append("-");
        }
        sb.append(String.format("%.2f",mLeftDistance));
        for(int i = 0 ; i<mLeftDistance+count ; i++)
        {
        	sb.append("-");
        }

        mLeft.internalToString(sb,count +1);
        return sb.toString();
    }
    
    public String toString() {
    	return internalToString(new StringBuilder(),0 );
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public void updateAlignemnt(Alignment newAlignemnt)
    {
        //TODO add new alignemnt to the Node
        this.alignemnt = newAlignemnt;
    }

    public Alignment guidedAlignment(){



        if(this.mRight==null || this.mLeft==null){
            return this.alignemnt;
        }
        this.alignemnt = alignemnt.Align(mRight.guidedAlignment() ,mLeft.guidedAlignment());

        return this.alignemnt;
    }

    
    public Alignment getAlignment()
    {
    	return this.alignemnt;
    }


}

