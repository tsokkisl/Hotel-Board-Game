/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Board {
    public String[][] board = new String[12][15];
    public Rect[][] boardgrid = new Rect[12][15];
    public int startX,bankX,townhallX,startY,bankY,townhallY;
    private static String cDir = new File("").getAbsolutePath();
    
    public void parseBoard() {
        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
        String folder = "";
        
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
}
