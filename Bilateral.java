import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bilateral {

    public static void main(String[] args) throws Exception {
        //since the project has one member in stockholm, one in london, the situation has a bipartite structure
        //to search for the minimum number employee to attend the meeting is basically a problem of searching minimum vertex cover of a bipartite graph.
        //according to konig's theorem, the problem of searching for maximum matching equals to the problem of searching for minimum vertex cover in bipartite graph.
        //maximum matching can be solved by search for maximum flow in a flow network using Hopcroft–Karp algorithm

        Bilateral bi=new Bilateral();
        bi.readInput();
        bi.Bicolorate();
        bi.hopcroftKarp();
        HashSet<Integer> result=bi.getMinimumCover();
        //bi.printResult(result);

        //throw new Exception(result.size()+"");

        bi.Bicolorate();
        bi.removeNode(9);
        //remove 1009

        bi.hopcroftKarp();
        HashSet<Integer> result1=bi.getMinimumCover();
        if(result.size()==(result1.size()+1)){
            result1.add(9);
            bi.printResult(result1);
        }else{
            bi.printResult(result);
        }
    }

    public void removeNode(int k){
        edges.set(k, new HashSet<Integer>());
        for(HashSet<Integer> set : edges){
                set.remove(k);
        }
    }

    private ArrayList<HashSet<Integer>> edges;
    int edgeCount;
    int vertexCount=2000;
    private int[] matching;
    private int[] dist;
    private int NIL =0;
    private int INF=-1;
    int n=1000;
    int m=1000;
    HashSet<Integer> leftVertices;
    HashSet<Integer> rightVertices;


    private void Bicolorate()     {
        leftVertices = new HashSet<Integer>();
        rightVertices = new HashSet<Integer>();

        int[] colors = new int[vertexCount];
        for (int i = 0; i < vertexCount; ++i)
            if (colors[i] == 0 && !BicolorateInternal(colors, i, 1)){
                //System.out.println("Graph is NOT bipartite.");
                //System.exit(0);
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

    public HashSet<Integer> getMinimumCover(){

        HashSet<Integer> vertexCoverResult=new HashSet<Integer>();

        HashSet<Integer> s=new HashSet<Integer>();
        for(int i=0;i<vertexCount;i++){
            if(matching[i]!=NIL){
                s.add(i);
                s.add(matching[i]);
                //System.out.println(i+"   "+matching[i]);
            }
        }

        
        for (int v: leftVertices)
            if (matching[v] ==NIL)
                DepthFirstSearch(v, true);


        leftVertices.removeAll(treeSet);
        rightVertices.retainAll(treeSet);
        leftVertices.addAll(rightVertices);
        vertexCoverResult = leftVertices;

        return vertexCoverResult;

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
                    DepthFirstSearch(u, false);
        } else if (matching[v] !=NIL)
            DepthFirstSearch(matching[v], true);
    }



    public void printResult(HashSet<Integer> vertexCoverResult){
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

    public int hopcroftKarp(){
        matching=new int[2000];
        dist=new int[2000];
        int matchcount = 0, i;
        // match[] is assumed NIL for all vertex in G
        while(bfs())
        for(i=0; i<n; i++)
            if(matching[i]==NIL && dfs(i))
                matchcount++;
        return matchcount;
    }

    public boolean bfs() {
    int i, u;
    LinkedList< Integer > Q=new LinkedList<Integer>();
    for(i=0; i<n; i++) {
        if(matching[i]==NIL) {
            dist[i] = 0;
            Q.push(i);
        }
        else dist[i] = INF;
    }
    dist[NIL] = INF;
    while(!Q.isEmpty()) {
        u = Q.pop();
        if(u!=NIL) {
            for(int v: edges.get(u)) {
                if(dist[matching[v]]==INF) {
                    dist[matching[v]] = dist[u] + 1;
                    Q.push(matching[v]);
                }
            }
        }
    }
    return (dist[NIL]!=INF);
    }

    public boolean dfs(int u) {
    if(u!=NIL) {
        for(int v:edges.get(u)) {

            if(dist[matching[v]]==dist[u]+1) {
                if(dfs(matching[v])) {
                    matching[v] = u;
                    matching[u] = v;
                    return true;
                }
            }
        }
        dist[u] = INF;
        return false;
    }
    return true;
}
  
}

