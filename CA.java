import java.awt.*;


public class CA {
 
    
    static byte[][] cells;
    static int rule=0;

    public static void CA(int n,int r,int[] ruleSet){
        Dimension dim= new Dimension(n*2,n*2);
        cells= new byte[256][(n*2)+1];

        cells[0][dim.width/2] = 1;

        drawCA(n,r,ruleSet);
       

    }

    private static byte rules(int r,int[] ruleSet,int lhs, int mid, int rhs){
        int index= (lhs << 2 | mid << 1 | rhs);
        return (byte) (ruleSet[r] >> index & 1);
    }

    static void drawCA(int n,int r, int[] ruleSet){
        int count=0;
        for (int i = 0; i < cells.length - 1; i++) {
            count++;
            if (count==n+1){
                return;
            }
            for (int c = 1; c < cells[i].length - 1; c++) {
                byte lhs = cells[i][c - 1];
                byte mid = cells[i][c];
                byte rhs = cells[i][c + 1];
                cells[i + 1][c] = rules(r,ruleSet,lhs, mid, rhs); // next generation
                if (cells[i][c] == 1) {
                    System.out.print("*");
                }
                else{
                    System.out.print(" ");
                }
                 
                
            }
            System.out.println("");
        }
    }

    public static void main(String[] args){
        int n= Integer.parseInt(args[0]);
        int r= Integer.parseInt(args[1]);
        final int[] ruleSet= new int[256];
        for(int i=0;i<256; ++i){
            ruleSet[i]=i;
        }
        CA(n,r,ruleSet);

    }

}