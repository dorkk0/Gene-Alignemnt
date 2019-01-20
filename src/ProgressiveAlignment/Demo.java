package ProgressiveAlignment;

import hirschberg.Sequence;

import java.util.ArrayList;
import java.util.List;

public class Demo {

	
	public Demo()
	{
		
	}
	/*
	public NeighborJoining run()
	{
		List<Alignment> alignments = new ArrayList<>();
        Sequence a = new Sequence("CTGA");
        List<Sequence> aL = new ArrayList<>();
        aL.add(a);
        alignments.add(new Alignment(aL));

        Sequence b = new Sequence("CATGGA");
        List<Sequence> bL = new ArrayList<>();
        bL.add(b);
        alignments.add(new Alignment(bL));

        Sequence c = new Sequence("CGA");
        List<Sequence> cL = new ArrayList<>();
        cL.add(c);
        alignments.add(new Alignment(cL));

            Sequence d = new Sequence("GA");
            List<Sequence> dL = new ArrayList<>();
            dL.add(d);
            alignments.add(new Alignment(dL));

            Sequence e = new Sequence("TA");
            List<Sequence> eL = new ArrayList<>();
            eL.add(e);
            alignments.add(new Alignment(eL));

            Sequence f = new Sequence("TGCTA");
            List<Sequence> fL = new ArrayList<>();
            fL.add(f);
            alignments.add(new Alignment(fL));

        NeighborJoining nj = new NeighborJoining(alignments);
        nj.printDistancesMatrix();
        System.out.println("----------------");

        nj.buildTreeNew();

        //nj.getRoot().print(0);
        return nj;
	}
	
	/*
    public static void main(String args[]) {
        List<Alignment> alignments = new ArrayList<>();
        Sequence a = new Sequence("CTGA");
        List<Sequence> aL = new ArrayList<>();
        aL.add(a);
        alignments.add(new Alignment(aL));

        Sequence b = new Sequence("CATGGA");
        List<Sequence> bL = new ArrayList<>();
        bL.add(b);
        alignments.add(new Alignment(bL));

        Sequence c = new Sequence("CGA");
        List<Sequence> cL = new ArrayList<>();
        cL.add(c);
        alignments.add(new Alignment(cL));

            Sequence d = new Sequence("GA");
            List<Sequence> dL = new ArrayList<>();
            dL.add(d);
            alignments.add(new Alignment(dL));

            Sequence e = new Sequence("TA");
            List<Sequence> eL = new ArrayList<>();
            eL.add(e);
            alignments.add(new Alignment(eL));

            Sequence f = new Sequence("TGCTA");
            List<Sequence> fL = new ArrayList<>();
            fL.add(f);
            alignments.add(new Alignment(fL));

        NeighborJoining nj = new NeighborJoining(alignments);
        nj.printDistancesMatrix();
        System.out.println("----------------");

        nj.buildTreeNew();

        nj.getRoot().print(0);
    }
    */
}




