package lottery;

import java.util.Scanner;

public class Lottery {

    public static void main(String[] args) {
        String strLine=null;
        Scanner scanner=new Scanner(System.in);
        if(scanner.hasNextLine()){
            strLine=scanner.nextLine();
        }

        //String strLine=args[0];

        String[] segs=strLine.split(" ");
        long m=Integer.parseInt(segs[0]); // total number of ppl
        long n=Integer.parseInt(segs[1]); // total winner
        long t=Integer.parseInt(segs[2]); // max ticket
        long p=Integer.parseInt(segs[3]); // ppl in the group

        long min_winner=(int)Math.ceil((double)p/(double)t);// need at least min_winner winners in the group

        double prob=0;

        long total=combination(m,n);


        if(min_winner<n/2){
            for(long i=0;i<min_winner;i++){
                prob+=combination(p,i)*combination(m-p, n-i);
            }
            prob=1-prob/total;

        }else{
            for(long i=min_winner;i<=Math.min(n,p);i++){
                prob+=combination(p,i)*combination(m-p, n-i);
            }
            prob=prob/total;
        }

        System.out.printf("%.10f\n",prob);
    }

    private static long combination(long n, long m){
        long r=1;
        for(long j=0;j<m;j++){
            r=r*(n-j)/(j+1);
        }
        return r;
    }

}
