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
import java.net.URL;

public class Board {
    public String[] board;
    public int start;
    private static String cDir = new File("").getAbsolutePath();
    
    public Board(){
        board = new String[180];
    }
    public void parseBoard() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(cDir + "/src/hotelboardgame/boards/simple/board.txt")))) {
            String line,l = "";
            while ((line = reader.readLine()) != null) {
                l += line;
                l += ",";
            }
            board = l.split(",");
            
            for (int i = 0; i < 180; i++) {
                if (board[i].equals("S")) {
                    start = i;
                    break;
                }
            }
            System.out.println(start);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
