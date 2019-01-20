package ProgressiveAlignment;
import hirschberg.Sequence;
import hirschberg.SubstitutMatrixValues;
import java.util.*;
public class Alignment {

    private List<Sequence> sequences;
    private double[][] profile;
    private SubstitutMatrixValues costs;
    //C'tor
    public Alignment(List<Sequence> sequences, SubstitutMatrixValues costs) { 
        this.sequences = sequences;
        this.costs = costs;
    }

    //TODO build profile for alignment
    public void makeProfile(){ //Rows in matrix gonna be in alphabetic order and indel in last row

        int maxlength = sequences.get(0).getLength();
        double profile[][] = new double[5][maxlength];

        for( int i = 0; i<maxlength ; i++)
        {
            for(int j =0 ; j<sequences.size();j++)
            {
                if(sequences.get(j).getCharIndex(i) == 'A')
                {
                    double freq = 1.0/(double)sequences.size();
                    profile[0][i] = profile[0][i] + freq;
                }

                if(sequences.get(j).getCharIndex(i) == 'C')
                {
                    double freq = 1.0/(double)sequences.size();
                    profile[1][i] = profile[1][i] + freq;
                }

                if(sequences.get(j).getCharIndex(i) == 'G')
                {
                    double freq = 1.0/(double)sequences.size();
                    profile[2][i] = profile[2][i] + freq;
                }

                if(sequences.get(j).getCharIndex(i) == 'T')
                {
                    double freq = 1.0/(double)sequences.size();
                    profile[3][i] = profile[3][i] + freq;
                }

                if(sequences.get(j).getCharIndex(i) == '-')
                {
                    double freq = 1.0/(double)sequences.size();
                    profile[4][i] = profile[4][i] + freq;
                }
            }

            this.profile=profile;
        }
    }

    public List<Sequence> getsSequences()
    {
        return sequences;
    }
    //---------------------Utils-----------------------//

    public Alignment Align(Alignment alignmentA,Alignment alignmentB)
    {
        //Sequnce vs Sequence
        if(alignmentA.sequences.size() == 1 && alignmentB.sequences.size() == 1)
        {
            Alignment res = new Alignment(NeighborJoining.Align(alignmentA.sequences.get(0),alignmentB.sequences.get(0)), this.costs);
            res.makeProfile();
            return res;
        }


        alignmentA.makeProfile();
        alignmentB.makeProfile();

        int size_1 = alignmentA.sequences.get(0).getLength() +1;
        int size_2 = alignmentB.sequences.get(0).getLength() +1;

        Double mat[][] = new Double[size_1][size_2];
        Double indel  = costs.calculate('-','A');;

        mat[0][0] = 0.0;
        double indelVec[] = {0.0,0.0,0.0,0.0,1.0};

        for(int i = 1; i<size_1; i++)
        {
            mat[i][0] = mat[i-1][0] +  indel;
        }
        for(int j = 1; j <size_2; j++)
        {
            mat[0][j] = mat[0][j-1] + indel;
        }

        for(int i =1 ; i<size_1 ; i++)
        {
            for(int j=1; j<size_2; j++)
            {
                double match = mat[i-1][j-1] + distance(alignmentA.getVectorI(i-1),alignmentB.getVectorI(j-1));
                double delete = mat[i-1][j] +  distance(alignmentA.getVectorI(i-1),indelVec);
                double insert = mat[i][j-1] + distance(alignmentB.getVectorI(j-1),indelVec);

                mat[i][j] = NeighborJoining.min(match,delete,insert);
            }
        }

        int i = alignmentA.sequences.get(0).getLength();
        int j = alignmentB.sequences.get(0).getLength();
        Alignment tempA = new Alignment(new ArrayList<Sequence>(), this.costs);
        Alignment tempB = new Alignment(new ArrayList<Sequence>(), this.costs);
        while( i>0 || j>0 )
        {
            if(i>0 && j>0 && (mat[i][j] == mat[i-1][j-1] + distance(alignmentA.getVectorI(i-1),alignmentB.getVectorI(j-1))))
            {
                tempA.appendToAlignment(alignmentA,i-1);
                tempB.appendToAlignment(alignmentB,j-1);
                i=i-1;
                j=j-1;
            }
            else if(i > 0 && mat[i][j] == mat[i-1][j] + indel)
            {
                tempA.appendToAlignment(alignmentA,i-1);
                tempB.updateAlignment(alignmentB,j);
                i=i-1;
            }
            else
            {
                tempA.updateAlignment(alignmentA,i);
                tempB.appendToAlignment(alignmentB,j-1);
                j=j-1;

            }
        }

        tempA.reverseAlignment();
        tempB.reverseAlignment();


        return this.addAlignments(tempA,tempB);

    }

    private double[] getVectorI(int i){

        double res[] = new double[5];

        for(int j =0 ; j<5 ; j++){
            res[j] = profile[j][i];
        }
        return res;
    }

    private static double distance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);


    }

    public static void printMatrix(double [][] mat , int length)
    {
        for (int i =0 ;i<5; i++)
        {
            if( i ==0)
            {
                System.out.print("A ");
            }

            if( i ==1)
            {
                System.out.print("C ");
            }

            if( i ==2)
            {
                System.out.print("G ");
            }

            if( i ==3)
            {
                System.out.print("T ");
            }

            if( i ==4)
            {
                System.out.print("- ");
            }
            for(int j =0 ; j<length;j++){
                System.out.print(mat[i][j]+" ");
            }
            System.out.println();

        }
    }

    public void updateAlignment(Alignment b,int index){

    if(this.sequences.size()==0){
        for(int i =0 ; i <b.sequences.size() ; i++)
        {
            this.sequences.add(new Sequence(""));
        }
    }
        for (int i =0 ; i< sequences.size() ; i++)
        {
            this.sequences.get(i).appendToEnd('-');
        }
    }

    public void appendToAlignment(Alignment alignment,int index){

        if(this.sequences.size()==0){
            for(int i =0 ; i <alignment.sequences.size() ; i++)
            {
                this.sequences.add(new Sequence(""));
            }
        }

        for(int i =0; i<sequences.size();i++)
        {
            sequences.get(i).appendToEnd(alignment.sequences.get(i).getCharIndex(index));
        }

    }

    public void reverseAlignment()
    {
        for(int i =0 ; i<this.sequences.size();i++)
        {
            this.sequences.set(i,this.sequences.get(i).reversedStr());
        }
    }

    public Alignment addAlignments(Alignment a, Alignment b){

        Alignment tmp =  new Alignment(new ArrayList<Sequence>(), this.costs);


        for(int i = 0 ; i< a.sequences.size() ; i++){
            tmp.sequences.add(a.sequences.get(i));
        }

        for(int i = 0 ; i< b.sequences.size() ; i++){
            tmp.sequences.add(b.sequences.get(i));
        }

        return tmp;
    }

	public int getSequenceSize() {
		return this.sequences.size();
	}

	public Sequence getSequence(int i) {
		return sequences.get(i);
	}

	public int getLength() {
		return this.sequences != null && this.sequences.size() != 0 ? this.sequences.get(0).getLength() : 0;
	}

	public char get(int i, int j) {
		return this.sequences.get(i).getCharIndex(j);
	}

}


