import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Lingyu Wang
 */
public class Bestbefore {

    public static void main(String[] args) throws Exception {

        String strLine=null;
        Scanner scanner=new Scanner(System.in);
        if(scanner.hasNextLine()){
            strLine=scanner.nextLine();
        }

        //throw(new Exception(strLine));

            if(strLine!=null){
                ArrayList<int[]> possibledates=new ArrayList<int[]>();
                String[] segs = strLine.split("/");
                int year;
                int month;
                int[] date;

                //first find all the possible dates. the max possibility is 6

                for(int i=0;i<3;i++){
                    if((year=getYear(segs[i]))!=-1){
                        for(int j=0;j<3;j++){
                            if(j!=i){
                                if((month=getMonth(segs[j]))!=-1){
                                    for(int k=0; k<3;k++){
                                        if(k!=i&&k!=j){
                                            if((date=getDate(segs[k],year, month))!=null){
                                                possibledates.add(date);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int[] earliest=new int[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
                for( int[] pd : possibledates){
                    if(earlierThan(pd, earliest)) earliest=pd;
                }
                if(earliest[0]!=Integer.MAX_VALUE){
                    System.out.println(earliest[0]+"-"+String.format("%02d",earliest[1])+"-"+String.format("%02d",earliest[2]));
                }else{
                    System.out.println(strLine+" is illegal");
                }
            }
    }

    public static boolean earlierThan(int[] date1, int[] date2){
        if(date1[0]<date2[0]){
            return true;
        }else if(date1[0]>date2[0]){
            return false;
        }else {
            if(date1[1]<date2[1]){
                return true;
            }else if(date1[1]>date2[1]){
                return false;
            }else {
                if(date1[2]<date2[2]){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }

    public static int getMonth(String str){
        if(str.length()>2||str.length()<1) return -1;
        else{
            try{
                int month=Integer.parseInt(str);
                if(month>=1&&month<=12){
                    return month;
                }else{
                    return -1;
                }
            }catch(Exception ex){
                return -1;
            }
        }
    }

    public static boolean isLeap(int year){
        if(year%400==0){
            return true;
        }else if(year%100==0){
            return false;
        }else if(year%4==0){
            return true;
        }else{
            return false;
        }

    }


    public static int[] getDate(String str, int year, int month){
        if(str.length()>2||str.length()<1) return null;
        else{
            try{
                int day=Integer.parseInt(str);
                int maxday;
                if(month==1||month==3||month==5||month==7||month==8||month==10||month==12) maxday=31;
                else{
                    if(month==2){
                        if(isLeap(year)){
                            maxday=29;
                        }else{
                            maxday=28;
                        }
                    }else{
                        maxday=30;
                    }
                }

                if(day>=1&&day<=maxday){
                    return new int[]{year, month, day};
                }else{
                    return null;
                }
            }catch(Exception ex){
                return null;
            }
        }
    }


    public static int getYear(String str){
        if(str.length()>4||str.length()<1||str.contains("-")) return -1;
        else{
            try{
                int year=Integer.parseInt(str);
                if(year>3000||year<0){
                    return -1;
                }else{
                    if(year<2000){
                        year+=2000;
                    }
                    return year;
                }
            }catch(Exception ex){
                return -1;
            }
        }
    }

}
