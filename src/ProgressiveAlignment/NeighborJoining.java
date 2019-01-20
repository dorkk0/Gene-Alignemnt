package ProgressiveAlignment;

import hirschberg.Sequence;
import hirschberg.SubstitutMatrixValues;

import java.util.ArrayList;
import java.util.List;

public class NeighborJoining {

    public List<Alignment> alignments;
    private static SubstitutMatrixValues costs;
    private Double[][] distance_mat;
    private Double[][] tmp_distance_mat;
    private Node[] nodeArr;
    private Node root;
    int originalSize;
    int size ;
    private int numOfNodes = 0;
    Double q[][];

    public NeighborJoining(List<Alignment> alignments, SubstitutMatrixValues costs) { //change so it receives the costs , also change in alignment

        this.alignments = alignments;
        this.costs = costs;
        nodeArr = new Node[alignments.size()];
        originalSize = alignments.size();
        calcDistanceMat();

    }

    public static List<Sequence> Align(Sequence seqA, Sequence seqB) {

        Sequence seqAtemp = new Sequence("");
        Sequence seqBtemp = new Sequence("");
        int size_1 = seqA.getLength() + 1;
        int size_2 = seqB.getLength() + 1;
        Double mat[][] = new Double[size_1][size_2];
        Double indel = costs.calculate('-', 'A');
        ;
        for (int i = 0; i < seqA.getLength() + 1; i++) {
            mat[i][0] = i * indel;
        }
        for (int j = 0; j < seqB.getLength() + 1; j++) {
            mat[0][j] = j * indel;
        }

        for (int i = 1; i < seqA.getLength() + 1; i++) {
            for (int j = 1; j < seqB.getLength() + 1; j++) {
                Double match = mat[i - 1][j - 1] + costs.calculate(seqA.getCharIndex(i - 1), seqB.getCharIndex(j - 1));
                Double delete = mat[i - 1][j] + indel;
                Double insert = mat[i][j - 1] + indel;

                mat[i][j] = min(match, delete, insert);
            }

        }

        int i = seqA.getLength();
        int j = seqB.getLength();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && (mat[i][j] == mat[i - 1][j - 1] + costs.calculate(seqA.getCharIndex(i - 1), seqB.getCharIndex(j - 1)))) {
                seqAtemp.appendToEnd(seqA.getCharIndex(i - 1));
                seqBtemp.appendToEnd(seqB.getCharIndex(j - 1));
                i = i - 1;
                j = j - 1;
            } else if (i > 0 && mat[i][j] == mat[i - 1][j] + indel) {
                seqAtemp.appendToEnd(seqA.getCharIndex(i - 1));
                seqBtemp.appendToEnd('-');
                i = i - 1;
            } else {
                seqAtemp.appendToEnd('-');
                seqBtemp.appendToEnd(seqB.getCharIndex(j - 1));
                j = j - 1;
            }
        }

        seqA.setSequence(seqAtemp.reversedStr().toString());
        seqB.setSequence(seqBtemp.reversedStr().toString());

        List<Sequence> res = new ArrayList<Sequence>();
        res.add(seqAtemp.reversedStr());
        res.add(seqBtemp.reversedStr());

        return res;

    }

    private Double calcSequencesDistance(Sequence seqA, Sequence seqB) {

        Double count = 0.0;

        for (int i = 0; i < seqA.getLength(); i++) {
            count += costs.calculate(seqA.getCharIndex(i), seqB.getCharIndex(i));
        }

        return count;
    }

    public void buildTreeNew() {
        size = originalSize;
        while (size > 2) {

            copyDistanceMat();//Copying the matrix to a temporary matrix

            this.q = new Double[distance_mat.length][distance_mat.length];//Initializing divergence matrix

            calcDivergenceMat(size);//Calculating divergence of all sequences based on distance matrix

            int min_pair[] = findMinPair(); //Finds minimum pair in matrix q

            Node newNode = makeParentNode(min_pair[0], min_pair[1]); // creating new Node with a new alignment

            decreaseDistanceMatrix();

            updateDistanceMatrix(min_pair[0], min_pair[1]); // updating distances of new node with old nodes

            if(size != originalSize-2) {
                updateNodeArr(min_pair[0], min_pair[1]); //Deceasing the size of nodeArr by 1 and copies it without two given indexes
                insertNewNode(newNode); //inserting new Node to the array at index 0, and prints if insertion fails
            }

            updateAlignmentsArray(min_pair[0], min_pair[1], newNode);/*Removing two alignments from @alignments and adding a new alignment
                                                                            from the new Node created before*/
            size--;
        }
        if(originalSize == 3){
            int index;
            if(alignments.get(0).getsSequences().size() == 1){
                index = 0;
            }
            else{
                index = 1;
            }
            nodeArr[1] = new Node(null,null,0.0,0.0,Integer.toString(numOfNodes++),
                    alignments.get(index));
        }
        double rootDistance = distance_mat[0][1]/2;
        root = new Node(nodeArr[0],nodeArr[1],rootDistance,rootDistance,Integer.toString(numOfNodes),
        alignments.get(0).Align(alignments.get(0),alignments.get(1)));

    }

    private void updateAlignmentsArray(int a, int b , Node newNode) {

        alignments.remove(a);
        alignments.remove(b-1);
        alignments.add(0,newNode.alignemnt);

    }

    private void insertNewNode(Node newNode) {

        if (nodeArr[0] == null) {
            nodeArr[0] = newNode;
        } else {
            System.out.println("Inserting new node " + newNode.mLabel + " has failed");
        }


    }

    private Node makeParentNode(int seqA, int seqB) {

        int divider = 2 * (size - 2);
        if (nodeArr[seqA] == null && nodeArr[seqB] == null) {

            Node newNodeA = new Node(null, null, 0.0, 0.0, Integer.toString(numOfNodes++),alignments.get(seqA) );
            Node newNodeB = new Node(null, null, 0.0, 0.0, Integer.toString(numOfNodes++),alignments.get(seqB));

            double parentNodeToSeqA, parentNodeToSeqB;
            parentNodeToSeqA = 0.5*(tmp_distance_mat[seqA][seqB])
                                    +(calcDistanceFromAll(seqA)-calcDistanceFromAll(seqB))*divider;
            parentNodeToSeqB = tmp_distance_mat[seqA][seqB] - parentNodeToSeqA;

            Node parent =  new Node(newNodeA, newNodeB, parentNodeToSeqA, parentNodeToSeqB, Integer.toString(numOfNodes++),
            		alignments.get(0).Align(alignments.get(seqA),alignments.get(seqB)));
            return parent;
        }


        else if (nodeArr[seqA] == null && nodeArr[seqB] != null) {

            Node newNodeA = new Node(null, null, 0.0, 0.0, Integer.toString(numOfNodes++),alignments.get(seqA) );

            double parentNodeToSeqA, parentNodeToSeqB;
            parentNodeToSeqA = 0.5*(tmp_distance_mat[seqA][seqB])
                    +(calcDistanceFromAll(seqA)-calcDistanceFromAll(seqB))*divider;
            parentNodeToSeqB = tmp_distance_mat[seqA][seqB] - parentNodeToSeqA;

            Node parent = new Node(newNodeA,nodeArr[seqB],parentNodeToSeqA,parentNodeToSeqB,Integer.toString(numOfNodes++)
                    ,alignments.get(0).Align(alignments.get(seqA),alignments.get(seqB)));

            return parent;


        }
         else if (nodeArr[seqA] != null && nodeArr[seqB] == null) {

            Node newNodeB = new Node(null, null, 0.0, 0.0, Integer.toString(numOfNodes++),alignments.get(seqB) );

            double parentNodeToSeqA, parentNodeToSeqB;
            parentNodeToSeqA = 0.5*(tmp_distance_mat[seqA][seqB])
                    +(calcDistanceFromAll(seqA)-calcDistanceFromAll(seqB))*divider;
            parentNodeToSeqB = tmp_distance_mat[seqA][seqB] - parentNodeToSeqA;
            Node parent = new Node(nodeArr[seqA],newNodeB,parentNodeToSeqA,parentNodeToSeqB,Integer.toString(numOfNodes++)
                    ,alignments.get(0).Align(alignments.get(seqA),alignments.get(seqB)));

            return parent;
        }
        else if(nodeArr[seqA] != null && nodeArr[seqB] != null){

            double parentNodeToSeqA, parentNodeToSeqB;
            parentNodeToSeqA = 0.5*(tmp_distance_mat[seqA][seqB])
                    +(calcDistanceFromAll(seqA)-calcDistanceFromAll(seqB))*divider;
            parentNodeToSeqB = tmp_distance_mat[seqA][seqB] - parentNodeToSeqA;
            Node parent = new Node(nodeArr[seqA],nodeArr[seqB],parentNodeToSeqA,parentNodeToSeqB,Integer.toString(numOfNodes++)
                    ,alignments.get(0).Align(alignments.get(seqA),alignments.get(seqB)));

            return parent;
        }

        System.out.println("Creating new node as failed at iteration - #" + numOfNodes);
        return null;
}

    public void calcDistanceMat(){
        List<Alignment> temp = new ArrayList<Alignment>();
        for(int i = 0 ; i < alignments.size() ; i ++)
        {
            temp.add(alignments.get(i));
        }

        distance_mat = new Double[this.alignments.size()][this.alignments.size()];

        for(int i = 0 ; i<temp.size(); i++) {
            for (int j = 0; j < temp.size(); j++) {
            	alignments.get(0).Align(temp.get(i), temp.get(j));
                distance_mat[i][j] = calcSequencesDistance(temp.get(i).getsSequences().get(0), temp.get(j).getsSequences().get(0));
                temp.get(i).getsSequences().get(0).removeIndels();
                temp.get(j).getsSequences().get(0).removeIndels();
                alignments.get(i).getsSequences().get(0).removeIndels();
                alignments.get(j).getsSequences().get(0).removeIndels();
            }
        }
    }

    private void copyDistanceMat() {

        tmp_distance_mat = new Double[distance_mat.length][distance_mat.length];

        for (int i = 0; i < distance_mat.length; i++)
        {
            for (int j = 0; j < distance_mat.length; j++)
            {
                tmp_distance_mat[i][j] = distance_mat[i][j];
            }
        }
    }

    public Node getRoot(){
        return this.root;
    }

    public void updateNodeArr(int nodeA, int nodeB){

        Node tmp[] = new Node[nodeArr.length];
        for(int i = 0 ; i< nodeArr.length ; i++){
            if(i!=nodeA || i!=nodeB)
            {
                tmp[i] = nodeArr[i];
            }
        }

        nodeArr =  new Node[tmp.length-1];
        int x = 1;
        for(int i=0; i<tmp.length; i++) {
            if(tmp[i]!=null){
                nodeArr[x] = tmp[i];
                x++;
            }
        }
    }

    public void decreaseDistanceMatrix() {
        distance_mat = new Double[distance_mat.length - 1][distance_mat.length - 1];
    }

    private void calcDivergenceMat(int size){
        for (int i = 0; i < distance_mat.length; i++) {
            for (int j = 0; j < distance_mat.length; j++) {
                if (i != j) {
                    if (distance_mat[i][j] != null && calcDistanceFromAll(i)!= null && calcDistanceFromAll(j) != null) {
                        q[i][j] = (size - 2) * distance_mat[i][j] - (calcDistanceFromAll(i) + calcDistanceFromAll(j));
                    }
                }
            }
        }
    }

    private Double calcDistanceFromAll(int index){
        Double res=0.0;
        for (int i = 0; i < distance_mat.length; i++) {
                if (distance_mat[i][0] != null) {
                    res += distance_mat[i][0];
                }
        }
        return res;
    }

    private  int[] findMinPair(){

        Double min_divergence = 0.0;
        int seqA = 0;
        int seqB = 0;
        for (int i = 0; i < q.length; i++) {
            for (int j = i + 1; j < q.length; j++) {
                if (q[i][j] != null) {
                    if (q[i][j] < min_divergence) {
                        seqA = i;
                        seqB = j;
                        min_divergence = q[i][j];
                    }
                }
            }
        }

        int res[] = {seqA,seqB};
        return res;

    }

    private void updateDistanceMatrix(int seqA, int seqB) {
        int x = 1, y = 1;
        for (int i = 0; i < tmp_distance_mat.length; i++) {
            if (i == seqA || i == seqB) {
                continue;
            } else {
                distance_mat[0][x] =
                        0.5 * (tmp_distance_mat[seqA][i] +
                                tmp_distance_mat[seqB][i] -
                                tmp_distance_mat[seqA][seqB]);
                x++;
            }
        }

        x = 1;
        for (int i = 1; i < tmp_distance_mat.length; i++) {
            if (i == seqA || i == seqB) {
                continue;
            } else {
                distance_mat[x][0] =
                        0.5 * (tmp_distance_mat[seqA][i] +
                                tmp_distance_mat[seqB][i] -
                                tmp_distance_mat[seqA][seqB]);
                x++;
            }
        }

        x = 1;

        for (int i = 0; i < tmp_distance_mat.length; i++) {
            y=1;
            if (i == seqA || i == seqB) {
                continue;
            }
            else {
                for (int j = 0; j < tmp_distance_mat.length; j++) {
                    if (j == seqA || j == seqB) {
                        continue;
                    } else {
                        distance_mat[x][y] = tmp_distance_mat[i][j];
                        y++;
                    }
                }
                x++;
            }
        }
    }

    public static Double min( Double... args){
        Double result = Double.MAX_VALUE;

        for( Double d: args)
            result = Math.min(result,d);

        return result;
    }

    //------------------- Prints -------------------------

    public void printDistancesMatrix(){


        for(int i=0 ; i<this.distance_mat.length ; i++)
        {
            System.out.print(i+1 + " ");
            for (int j=0 ; j<this.distance_mat.length ; j++)
            {
                if(distance_mat[i][j]!=null)
                    System.out.print(" "+ String.format("%.2f",distance_mat[i][j]));
                else
                    System.out.print(" null");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public String createDistanceMatrixString()
    {
    	String str = "";
    	for(int i=0 ; i<this.distance_mat.length ; i++)
        {
            str = str + (i+1) + " ";
            for (int j=0 ; j<this.distance_mat.length ; j++)
            {
                if(distance_mat[i][j]!=null)
                    str = str + " " + String.format("%.2f",distance_mat[i][j]);
                else
                    str = str + " null";
            }
            str = str + "\n";
        }
    	str = str + "\n";
    	return str;
    }
}
