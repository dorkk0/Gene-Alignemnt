package hirschberg;

import java.util.*;

public class Alignment
{
    private List<Sequence> sequences;
    private Double distance;

    //============================================Ctor==================================================================

    public Alignment(Sequence ... sequences)
    {
        this(new ArrayList<Sequence>(Arrays.asList(sequences)));
    }

    public Alignment(List<Sequence> sequences)
    {
        this.sequences = sequences;

        for(int i = 1; i < sequences.size(); i++)
        {
            if(sequences.get(i).getLength() != sequences.get(i-1).getLength())
                throw new IllegalArgumentException("All sequences must be of the same getLength");
        }
    }

    public Alignment(Sequence seqA, Sequence seqB)
    {
        this( Arrays.asList(seqA,seqB));
    }

    //============================================Logical Operations====================================================

    public void sequenceUpdate(int index, Sequence newSequence)
    {
        Sequence oldSequence = this.sequences.get(index);

        for(int i = 0; i < newSequence.getLength(); i++)
        {
            if(i >= oldSequence.getLength() || oldSequence.getCharIndex(i) != newSequence.getCharIndex(i))
            {
                for(Sequence seq : sequences)
                    seq.insertInSpecificIndex(i, newSequence.getCharIndex(i));
            }
        }
    }

    public static Alignment addAlignments(Alignment alignA, Alignment alignB)
    {
        if(alignA.getSequenceSize() != alignB.getSequenceSize())
            throw new IllegalArgumentException("The Alignments Must Have The Same Number Of Sequences To addAlignments");

        List<Sequence> addedSequences = new ArrayList<Sequence>();

        for(int i = 0; i < alignA.getSequenceSize() ; i++)
            addedSequences.add(Sequence.merge(alignA.getSequence(i), alignB.getSequence(i)));

        Alignment align = new Alignment(addedSequences);

        return align;
    }

    public static Alignment reverse(Alignment original)
    {
        List<Sequence> reverse = new ArrayList<>(original.sequences);
        Collections.reverse(reverse);
        Alignment align = new Alignment(reverse);

        return align;
    }

    public void add(Sequence sequence)
    {
        if(sequence.getLength() != sequences.get(0).getLength())
            throw new IllegalArgumentException("Can not addAlignments a sequence with different getLength");

        sequences.add(sequence);
    }

    //============================================Getters===============================================================

    public Double getDistance(SubstitutMatrixValues substitutMatrixVals)
    {
        this.distance = 0d;

        for(int i = 0; i < this.sequences.size(); i++)
        {
            for(int j = i+1; j < this.sequences.size(); j++)
            {
                for(int k = 0; k < this.sequences.get(i).getLength(); k++)
                    this.distance += substitutMatrixVals.calculate(this.sequences.get(i).getCharIndex(k), this.sequences.get(j).getCharIndex(k));
            }
        }

        return distance;
    }

    public int getSequenceSize()
    {
        return this.sequences.size();
    }

    public Sequence getSequence(int index)
    {
        return sequences.get(index);
    }

    public int getLength()
    {
        return this.sequences != null && this.sequences.size() != 0 ? this.sequences.get(0).getLength() : 0;
    }

    public char get(int i, int j)
    {
        return this.sequences.get(i).getCharIndex(j);
    }

    public Sequence getConsensusString()
    {
        Sequence consensus = new Sequence();

        for(int j = 0; j < this.getLength(); j++)
        {
            HashMap<Character, Integer> counts = new HashMap<>();
            for(int i = 0; i < this.getSequenceSize(); i++)
            {
                char c = this.get(i,j);

                if(c == '-')
                    continue;

                if(!counts.containsKey(c))
                    counts.put(c,0);

                counts.put(c,counts.get(c) + 1);
            }
            char dominant = '-';
            int count = 0;
            for(Map.Entry<Character,Integer> e: counts.entrySet())
            {
                if(count < e.getValue())
                {
                    count = e.getValue();
                    dominant = e.getKey();
                }
            }
            consensus.appendToEnd(dominant);
        }

        return consensus;
    }
}
