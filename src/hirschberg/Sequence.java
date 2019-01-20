package hirschberg;

public class Sequence
{
    String strCopy;
    private StringBuilder sequence;

    //============================================Ctor==================================================================

    public Sequence(String seq)
    {
        this.sequence = new StringBuilder();
        this.sequence.append(seq);
    }

    public Sequence()
    {
        this("");
    }

    //============================================Logic=================================================================

    public Sequence clone()
    {
        Sequence seq = new Sequence(this.sequence.toString());

        return seq;
    }

    public Sequence reversedStr()
    {
        StringBuilder reversedStr = new StringBuilder(this.sequence);
        reversedStr.reverse();
        Sequence seq = new Sequence(reversedStr.toString());

        return seq;
    }

    public static Sequence merge(Sequence seqA, Sequence seqB)
    {
        Sequence seq = new Sequence(seqA.toString() + seqB.toString());

        return  seq;
    }

    public void insertInSpecificIndex(int index, char ch)
    {
        this.sequence.insert(index,ch);
        this.strCopy = null;
    }

    public void appendToEnd(char c)
    {
        sequence.append(c);
        this.strCopy = null;
    }

    public void appendLoop(char c , int n)
    {
        for( int i = 0 ; i < n ; i++)
        {
            sequence.append(c);
            this.strCopy = null;
        }
    }

    public String toString()
    {
        if(this.strCopy == null)
            this.strCopy = this.sequence.toString();

        return this.strCopy;
    }

    public Sequence subSequenceFromBegin(int begin)
    {
        Sequence seq = new Sequence(this.sequence.substring(begin));

        return seq;
    }

    public Sequence subSequenceToEnd(int end)
    {
        Sequence seq = new Sequence(this.sequence.substring(0,end));

        return seq;
    }


    public Double[][] makeProfile(){ //Rows in matrix gonna be in alphabetic order and indel in last row

        Double [][] res = new Double[this.getLength()][5];

        for(int i = 0;i<this.getLength();i++)
        {
            if(this.sequence.charAt(i) == 'A'){
                res[i][0] = 1.0;
            }

            if(this.sequence.charAt(i) == 'C'){
                res[i][1] = 1.0;
            }

            if(this.sequence.charAt(i) == 'G'){
                res[i][2] = 1.0;
            }

            if(this.sequence.charAt(i) == 'T'){
                res[i][3] = 1.0;
            }

            if(this.sequence.charAt(i) == '-'){
                res[i][4] = 1.0;
            }

        }
        return res;
    }

    public void setSequence(String seq){

        this.sequence.delete(0,sequence.length());
        this.sequence.append(seq);

    }

    public void removeIndels(){

        for(int i=0 ; i < this.sequence.length(); i++){
            if(sequence.charAt(i) == '-'){
                sequence.deleteCharAt(i);
            }
        }
    }

    //============================================Logic=================================================================

    public int getLength()
    {
        return this.sequence.length();
    }

    public char getCharIndex(int index)
    {
        return this.sequence.charAt(index);
    }
}
