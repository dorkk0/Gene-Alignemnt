package hirschberg;

public class SubstitutMatrixValues
{
    public  Double match, mismatch, indel;

    public SubstitutMatrixValues(Double match, Double mismatch, Double indel)
    {
        this.match = match;
        this.mismatch = mismatch;
        this.indel = indel;
    }

    public  Double calculate(char chA, char chB)
    {
    	Double score;

        if(chA == chB)  // match
        	score = match;
        else if((chA == '-') || (chB == '-'))  // indel
        	score =  indel;
        else  // mismatch
        	score = mismatch;

        return score;
    }
}
