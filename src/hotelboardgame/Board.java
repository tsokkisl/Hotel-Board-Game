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
    public String[] board = new String[180];
    public Rect[] boardgrid = new Rect[180];
    public int start,bank,townhall;
    private static String cDir = new File("").getAbsolutePath();
    
    public void parseBoard() {
        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
        String folder = "";
        
        if (n == 1) folder = "simple";
        else folder = "default";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(cDir + "/src/hotelboardgame/boards/" + folder + "/board.txt")))) {
            String line,l = "";
            while ((line = reader.readLine()) != null) {
                l += line;
                l += ",";
            }
            board = l.split(",");
            
            for (int i = 0; i < 180; i++) {
                if (board[i].equals("S")) {
                    start = i;
                }
                if (board[i].equals("B")) {
                    bank = i;
                }
                if (board[i].equals("C")) {
                    townhall = i;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
