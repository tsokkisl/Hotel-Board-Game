package hotelboardgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Board {
    public String[][] board = new String[12][15];
    public Rect[][] boardgrid = new Rect[12][15];
    private int startX,bankX,townhallX,startY,bankY,townhallY;
    private static String cDir = new File("").getAbsolutePath();
    public String folder = "";
    
    public void parseBoard() {
        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
       
        if (n == 1) folder = "simple";
        else folder = "default";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(cDir + "/src/hotelboardgame/boards/" + folder + "/board.txt")))) {
            String line;
            int k = 0;
            String b[] = new String[15];
            while ((line = reader.readLine()) != null) {
                b = line.split(",");
                for(int j = 0; j < 15; j++) board[k][j] = b[j];
                k++;
            }
            
            for (int i = 0; i < 12; i++) {
                for(int j = 0; j < 15; j++) {
                    if (board[i][j].equals("S")) {
                        startX = i;
                        startY = j;
                    }
                    if (board[i][j].equals("B")) {
                        bankX = i;
                        bankY = j;
                    }
                    if (board[i][j].equals("C")) {
                        townhallX = i;
                        townhallY = j;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public void setPath(String s) {
        cDir = s;
    }
    public void setStartX(int x) {
        startX = x;
    }
    public void setStartY(int y) {
        startY = y;
    }
    public void setbankX(int x) {
        bankX = x;
    }
    public void setBankY(int y) {
        bankY = y;
    }
    public void setTownhallX(int x) {
        townhallX = x;
    }
    public void setTownhallY(int y) {
        townhallY = y;
    }
    public String getPath() {
        return cDir;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }
    public int getbankX() {
        return bankX;
    }
    public int getBankY() {
        return bankY;
    }
    public int getTownhallX() {
        return townhallX;
    }
    public int getTownhallY() {
        return townhallY;
    }
}
