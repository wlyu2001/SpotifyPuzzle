
package bilateral;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Integer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bilateral {

    public static void main(String[] args) {
        //since the project has one member in stockholm, one in london, the situation has a bipartite structure
        //to search for the minimum number employee to attend the meeting is basically a problem of searching minimum vertex cover of a bipartite graph.
        //according to konig's theorem, the problem of searching for maximum matching equals to the problem of searching for minimum vertex cover in bipartite graph.
        //maximum matching can be solved by search for maximum flow in a flow network.

        Bilateral bi=new Bilateral();
        bi.readInput1(args[0]);
        try{
            bi.findVertexCover();
            bi.printResult();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private int vertexCount=2000;
    private int edgeCount;
    private ArrayList<HashSet<Integer>> edges;
    private HashSet<Integer> leftVertices;
    private HashSet<Integer> rightVertices;
    private HashSet<Integer> vertexCoverResult;
    private int[] matching;

    public void printResult(){
        System.out.println(vertexCoverResult.size());
        for(int r : vertexCoverResult){
            System.out.println(r+1000);
        }
    }

    public void readInput(){

        String strLine=null;
        Scanner scanner=new Scanner(System.in);
        if(scanner.hasNextLine()){
            edgeCount=Integer.parseInt(scanner.nextLine());
        }
        edges=new ArrayList<HashSet<Integer>>();
        for(int i=0;i<vertexCount;i++){
            edges.add(new HashSet<Integer>());
        }
        while(scanner.hasNextLine()){
            String[] segs=scanner.nextLine().split(" ");
            int left=Integer.parseInt(segs[0])-1000;
            int right=Integer.parseInt(segs[1])-1000;
            if(edges.get(right)==null) edges.set(right, new HashSet<Integer>());
            if(edges.get(left)==null) edges.set(left, new HashSet<Integer>());
            edges.get(right).add(left);
            edges.get(left).add(right);
        }
    }

        public void readInput1(String file){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            edgeCount = Integer.parseInt(br.readLine());
            edges=new ArrayList<HashSet<Integer>>();
            for(int i=0;i<vertexCount;i++){
                edges.add(new HashSet<Integer>());
            }
            while ((line = br.readLine()) != null) {
                String[] segs = line.split(" ");
                int left = Integer.parseInt(segs[0]) - 1000;
                int right = Integer.parseInt(segs[1]) - 1000;
                if (edges.get(right) == null) {
                    edges.set(right, new HashSet<Integer>());
                }
                if (edges.get(left) == null) {
                    edges.set(left, new HashSet<Integer>());
                }
                edges.get(right).add(left);
                edges.get(left).add(right);
            }
        } catch (Exception ex) {
            Logger.getLogger(Bilateral.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Bilateral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private void findVertexCover(){
        findBipartiteMatching();
        
        for (int v: leftVertices)
            if (matching[v] < 0)
                DepthFirstSearch(v, true);
        
        leftVertices.removeAll(treeSet);
        rightVertices.retainAll(treeSet);
        leftVertices.addAll(rightVertices);
        vertexCoverResult = leftVertices;
    }
    HashSet<Integer> treeSet = new HashSet<Integer>();

    private void DepthFirstSearch(int v, boolean left)
    {
        if (treeSet.contains(v))
            return;
        treeSet.add(v);
        if (left) {
            for (int u: edges.get(v))
                if (u != matching[v])
                    DepthFirstSearch(u, true);
        } else if (matching[v] >= 0)
            DepthFirstSearch(matching[v], false);
    }

    private void findBipartiteMatching(){

        Bicolorate();

        matching=new int[vertexCount];
        for(int i=0;i<vertexCount;i++){
            matching[i]=-1;
        }
        int count=0;
        for(int i : leftVertices){
            boolean[] seen = new boolean[vertexCount];
            if (BipartiteMatchingInternal(seen, i)) count++;
        }
    }

    private void Bicolorate()
    {
        leftVertices = new HashSet<Integer>();
        rightVertices = new HashSet<Integer>();

        int[] colors = new int[vertexCount];
        for (int i = 0; i < vertexCount; ++i)
            if (colors[i] == 0 && !BicolorateInternal(colors, i, 1)){
                System.out.println("Graph is NOT bipartite.");
                System.exit(0);
            }
    }

    private boolean BicolorateInternal(int[] colors, int i, int color)
    {
        if (colors[i] == 0) {
            if (color == 1) leftVertices.add(i);
            else rightVertices.add(i);
            colors[i] = color;
        } else if (colors[i] != color)
            return false;
        else
            return true;
        for (int j: edges.get(i))
            if (!BicolorateInternal(colors, j, 3 - color))
                return false;
        return true;
    }

    private boolean BipartiteMatchingInternal(boolean[] seen, int u)
    {
        for (int v: edges.get(u)) {
            if (seen[v]) continue;
            seen[v] = true;
            if (matching[v] < 0 || BipartiteMatchingInternal(seen, matching[v])) {
                matching[u] = v;
                matching[v] = u;
                return true;
            }
        }
        return false;
    }

}

