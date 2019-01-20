package hirschberg;

public class HirschbergAlign
{
    private Sequence[] sequences;
    private Sequence centerStarSeq;
    private int centerStarIndex;
    private SubstitutMatrixValues substitutMatrixVals;
    public static char indel = '-';

    public HirschbergAlign(Sequence sequences[], SubstitutMatrixValues substitutMatrixVals)
    {
        this.sequences = sequences;
        this.substitutMatrixVals = substitutMatrixVals;
        this.centerStarIndex =  findCenterStarIndex(this.sequences, this.substitutMatrixVals);
        this.centerStarSeq = this.sequences[this.centerStarIndex].clone();
    }

    public static Alignment HirschbergAlgorithm(Sequence seqA,
                                                Sequence seqB,
                                                SubstitutMatrixValues substitutMatrixVals)
    {
        Alignment result;

        if(seqA.getLength() == 0)
        {
            Sequence spaceSeq = new Sequence();
            spaceSeq.appendLoop(indel, seqB.getLength());
            result = new Alignment(spaceSeq, seqB);
        }
        else if(seqB.getLength() == 0)
        {
            Sequence spaceSeq = new Sequence();
            spaceSeq.appendLoop(indel, seqA.getLength());
            result = new Alignment(seqA, spaceSeq);
        }
        else if(seqA.getLength() == 1 || seqB.getLength() == 1)
            result = ifOneSeqWithLengthOfOne(seqA,seqB);
        else
        {
            int middleLengthA = seqA.getLength()/2;

            Double scoreLeft[] = calcAlignmentMatrix(seqA.subSequenceToEnd(middleLengthA), seqB, substitutMatrixVals);
            Double scoreRight[] = calcAlignmentMatrix(seqA.subSequenceFromBegin(middleLengthA).reversedStr(), seqB.reversedStr(), substitutMatrixVals);
            int midLenB = getMinIndex(addArrays(scoreLeft, getReverseArr(scoreRight)));

            Alignment alignmentA = HirschbergAlgorithm(seqA.subSequenceToEnd(middleLengthA), seqB.subSequenceToEnd(midLenB),
                    substitutMatrixVals);

            Alignment alignmentB =  HirschbergAlgorithm(seqA.subSequenceFromBegin(middleLengthA), seqB.subSequenceFromBegin(midLenB),
                    substitutMatrixVals);

            result = Alignment.addAlignments(alignmentA, alignmentB);
        }

        return result;
    }

    public static int findCenterStarIndex(Sequence sequences[], SubstitutMatrixValues substitutMatrixVals)
    {
        int index = -1;
        Double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < sequences.length; i++)
        {
        	Double distance = 0.0;
            for (int j = 0; j < sequences.length; j++)
            {
                if(i == j)
                    continue;

                distance  += HirschbergAlgorithm(sequences[i], sequences[j], substitutMatrixVals)
                        .getDistance(substitutMatrixVals);
            }

            if(distance < minDistance)
            {
                index = i;
                minDistance = distance;
            }
        }

        return  index;
    }

    public  Alignment calcAlignmentMatrix()
    {
        if(this.sequences.length == 0)
            return null;

        if(this.sequences.length == 1)
            return new Alignment(this.sequences[0]);

        int centerIndex = findCenterStarIndex(this.sequences, this.substitutMatrixVals);

        Alignment result = new Alignment(this.sequences[centerIndex]);
        for( int i = 0; i <this.sequences.length; i++)
        {
            if(i == centerIndex)
                continue;

            Alignment alignment = HirschbergAlgorithm(result.getSequence(0), this.sequences[i], substitutMatrixVals);
            result.sequenceUpdate(0, alignment.getSequence(0));
            result.add(alignment.getSequence(1));
        }

        return result;
    }

    private static Double[] calcAlignmentMatrix(Sequence sequenceA,
                                                Sequence sequenceB,
                                                SubstitutMatrixValues substitutMatrixVals)
    {
        int index = 0;
        Double firstRow[] = new Double[sequenceA.getLength() + 1];
        Double finalLine[] = new Double[sequenceB.getLength() + 1];
        Double columnLine[] = new Double[sequenceB.getLength() + 1];

        firstRow[index] = 0.0;
        for(int i = 1; i <= sequenceA.getLength() ; i++)
            firstRow[i] = firstRow[i-1] + substitutMatrixVals.calculate(indel,sequenceA.getCharIndex(i-1));

        columnLine[index] = 0.0;
        for(int i = 1; i <= sequenceB.getLength() ; i++)
            columnLine[i] = columnLine[i-1] + substitutMatrixVals.calculate(sequenceB.getCharIndex(i-1),indel);

        for(int i = 1; i <= sequenceA.getLength(); i ++)
        {
            finalLine[0] = firstRow[i];

            for(int j = 1; j <= sequenceB.getLength() ; j++)
            {
                finalLine[j] = min(columnLine[j] + substitutMatrixVals.calculate(sequenceA.getCharIndex(i-1),
                        indel),
                        finalLine[j-1] + substitutMatrixVals.calculate(indel, sequenceB.getCharIndex(j-1)),
                        columnLine[j-1] + substitutMatrixVals.calculate(sequenceA.getCharIndex(i-1),
                                sequenceB.getCharIndex(j-1)));
            }

            columnLine = finalLine;
            finalLine = new Double[sequenceB.getLength()+1];
        }

        return columnLine;
    }

    private static Alignment alignCharWithSequence(char ch, Sequence seq)
    {
        Boolean inserted = false;
        Sequence result = new Sequence();

        for(int i = 0; i < seq.getLength(); i++)
        {
            if(!inserted && (ch == seq.getCharIndex(i) || i == seq.getLength()-1))
            {
                inserted = true;
                result.appendToEnd(ch);
            }
            else
                result.appendToEnd(indel);
        }

        return new Alignment(result, seq);
    }

    private static Alignment ifOneSeqWithLengthOfOne(Sequence seqA, Sequence seqB)
    {
        if(seqA.getLength() != 1 && seqB.getLength() != 1)
            throw new IllegalArgumentException("on of the sequences must be of getLength 1");

        Alignment result;

        if(seqA.getLength() == 1)
            result = alignCharWithSequence(seqA.getCharIndex(0), seqB);
        else
            result = Alignment.reverse(alignCharWithSequence(seqB.getCharIndex(0),seqA));

        return result;
    }

    //============================================Getters===============================================================

    public Sequence getCenterStarSeq()
    {
        return this.centerStarSeq;
    }
    
    public Sequence getFinalCenterStarSeq()
    {
        return this.sequences[this.centerStarIndex];
    }

    //============================================Helpers function======================================================

    public static int getMinIndex(Double args[])
    {
        int minIndex = 0;

        if (args.length == 0)
            return -1;

        for(int i = 1; i < args.length; i++)
        {
            if(args[i] < args[minIndex])
                minIndex = i;
        }

        return minIndex;
    }

    public static Double[] addArrays(Double arrA[], Double arrB[])
    {
        if(arrA.length != arrB.length)
            throw new IllegalArgumentException("The Arrays Must Be In The Same Length");

        Double result[] = new Double[arrA.length];

        for( int i = 0 ; i < arrA.length ; i ++)
            result[i] = arrA[i] + arrB[i];

        return result;
    }

    public static Double[] getReverseArr(Double arr[])
    {
    	Double[] reversedArr = new Double[arr.length];

        for(int i = arr.length-1 ; i >= 0 ; i--){
            reversedArr[arr.length-1-i] = arr[i];
        }

        return reversedArr;
    }

    public static Double min( Double... args)
    {
    	Double result = Double.MAX_VALUE;

        for( Double d: args)
            result = Math.min(result,d);

        return result;
    }
}
