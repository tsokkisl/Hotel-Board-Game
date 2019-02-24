/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class InterfaceController implements Initializable {
    private static Stage myStage;
    private int startingPositionX;
    private int startingPositionY;
    private Player currentPlayer;
    private int turn = 0;
    private Player[] players = new Player[3];
    private Hotel[] hotels;
    public static void setStage(Stage stage) {
         myStage = stage;
    }
    private static Board gameBoard = new Board();
    private int plot1x = -1, plot2x = -1, plot1y = -1, plot2y = -1, plotToBuyX, plotToBuyY, hotelX, hotelY, hotelToBuildX, hotelToBuildY;
    private ArrayList<Entrance> randomEntrance = new ArrayList<Entrance>();
    private static int availableHotels;
    private static Paint previousColor;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private Timer timer = new Timer();
    
    /**
     * Initializes the controller class.
     */
    @FXML
    private Label dicerollresult;
    @FXML
    private Label buildrequest;
    @FXML
    private  Label player1;
    @FXML
    private  Label player2;
    @FXML
    private  Label player3;
    @FXML
    private Label buyplotmessage;
    @FXML
    private Label buildentrancemessage;
    @FXML
    private Label availablehotels;
    @FXML
    private Label timertext;
    @FXML
    private Label creditspayedforstayinginahotel;
    @FXML
    private Button rolldicebutton;
    @FXML
    private Button requestbuildbutton;
    @FXML
    private Button requestfrombankbutton;
    @FXML
    private Button buyplotbutton;
    @FXML
    private Button buyentrancebutton;
    @FXML
    private Button endroundbutton;
    @FXML
    private  GridPane gp;
    
    private void reinitializeFunctionality() {
        rolldicebutton.setDisable(false);
        requestbuildbutton.setDisable(true);
        requestfrombankbutton.setDisable(true);
        buyplotbutton.setDisable(true);
        buyentrancebutton.setDisable(true);
        endroundbutton.setDisable(true);
        dicerollresult.setGraphic(null);
        buildrequest.setText("");
        buyplotmessage.setText("");
        buildentrancemessage.setText("");
        creditspayedforstayinginahotel.setText("");
        for(int i = 0; i < 12; i++) { 
            for(int j = 0; j < 15; j++) {
                if(gameBoard.boardgrid[i][j].rec.getStroke() == Color.BLACK) {
                    gameBoard.boardgrid[i][j].rec.setStrokeWidth(0);
                }
            }
        }
    }
    private String hotelDetails(Hotel h) {
        String s = "";
        s = "Name: " + h.name + '\n' + "Plot Cost: " + h.plotCost + '\n' + "Required Plot Cost: " + h.requiredPlotCost + '\n'
        + "Gate Cost: " + h.entranceCost + '\n' + "Basic Build Cost: " + h.buildCost[0] + '\n' + "Basic Build Rent: " + h.rentCost[0] + '\n'
        + "Build Cost: " + h.buildCost[h.buildCost.length - 1] + '\n' + "Max Rent: " + h.rentCost[h.rentCost.length - 1];
        for(int i = 1; i < h.buildCost.length - 1; i++)
            s = s + '\n' + "Upgrade level " + (i + 1) + " Cost: " + h.buildCost[i] + '\n'
            + "Upgrade level " + (i + 1) + " Rent: " + h.rentCost[i];
        return s;
    }
    private String hotelInfo(Hotel h) {
        String s = "";
        s = "Name: " + h.name + '\n' + "Owner: " + h.hotelOwner + '\n' + "Max upgrade level: " + h.maxUpgradeLevel + '\n'
        + "Current upgrade level: " + h.currentUpgradeLevel + '\n';
        return s;
    }
    @FXML
    private void handleGameStart(ActionEvent event) {
        //Game restart and initialize all values
        turn = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 15; j++) {
                gameBoard.boardgrid[i][j].rec.setStrokeWidth(0);             
            }
        }
        setUpGameBoard();
        startingPositionX = gameBoard.startX;
        startingPositionY = gameBoard.startY;
        players[0] = new Player("Player1", "Blue", 12000, startingPositionX, startingPositionY, Color.BLUE);
        players[1] = new Player("Player2", "Red", 12000, startingPositionX, startingPositionY, Color.RED);
        players[2] = new Player("Player3", "Green", 12000, startingPositionX, startingPositionY, Color.GREEN);
        gameBoard.boardgrid[gameBoard.startX][gameBoard.startY].stack.getChildren().addAll(players[2].pawn, players[1].pawn, players[0].pawn);
        currentPlayer = players[0];
        showCurrentPlayer(currentPlayer);
        parseHotels(gameBoard.folder);
        updateCreditLabels();
        timertext.setText("Total Time: 00:00:00");
        availableHotels = hotels.length;
        availablehotels.setText("Available Hotels: " + availableHotels);
        timer.cancel();
        timer.purge();
        seconds = 0;
        minutes = 0;
        hours = 0;
        timer = new Timer();
        reinitializeFunctionality();
        startTimer();
        howToPlay();
    }
    @FXML
    private void handleGameStop(ActionEvent event) {
        //Disable functionality
        rolldicebutton.setDisable(true);
        requestbuildbutton.setDisable(true);
        buyplotbutton.setDisable(true);
        requestfrombankbutton.setDisable(true);
        buyentrancebutton.setDisable(true);
        endroundbutton.setDisable(true);
        //Stop timer
        timer.cancel();
        timer.purge();
    }
    @FXML
    private void handleCardShow(ActionEvent event) {
        Tab[] tabs = new Tab[hotels.length];
        TabPane tabPane = new TabPane();
        String s = "";
        for (int i = 0; i < hotels.length; i++) {
            tabs[i] = new Tab();
            tabs[i].setText("Hotel " + hotels[i].number);
            s = hotelDetails(hotels[i]);
            TextArea ta = new TextArea(s);
            ta.setDisable(true);
            tabs[i].setContent(ta);
            tabPane.getTabs().add(tabs[i]);
        }
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(768);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(850); 
        tabPane.setMinHeight(600);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    @FXML
    private void handleHotelInfoShow(ActionEvent event) {
        Tab[] tabs = new Tab[hotels.length];
        TabPane tabPane = new TabPane();
        String s = "";
        for (int i = 0; i < hotels.length; i++) {
            tabs[i] = new Tab();
            tabs[i].setText("Hotel " + (hotels[i].number));
            s = hotelInfo(hotels[i]);
            TextArea ta = new TextArea(s);
            ta.setDisable(true);
            tabs[i].setContent(ta);
            tabPane.getTabs().add(tabs[i]);
        }
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
         btn.setLayoutX(768);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(850); 
        tabPane.setMinHeight(300);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    @FXML
    private void handleGameExit(ActionEvent event) {
        //exits game
        Platform.exit();
        timer.cancel();
    }
    @FXML
    private void handleDiceRoll(ActionEvent event) {
        Random ran = new Random();
        int x = ran.nextInt(6) + 1;
        if (currentPlayer.name.equals("Player1")) {          
            move(players[0], x);
            checkForOtherPlayersOnTheSamePosition(players[0]);
            gameBoard.boardgrid[players[0].positionX][players[0].positionY].stack.getChildren().addAll(players[0].pawn);
            if (gameBoard.board[players[0].positionX][players[0].positionY].equals("E")) {
                checkForEntranceAndPay(players[0]);
            }
            showPlayerActions(players[0]);
        }
        if (currentPlayer.name.equals("Player2")) {
            move(players[1], x);
            checkForOtherPlayersOnTheSamePosition(players[1]);
            gameBoard.boardgrid[players[1].positionX][players[1].positionY].stack.getChildren().addAll(players[1].pawn);
            if (gameBoard.board[players[1].positionX][players[1].positionY].equals("E")) {
                checkForEntranceAndPay(players[1]);
            }
            showPlayerActions(players[1]);
        }
        if (currentPlayer.name.equals("Player3")) {
            move(players[2], x);
            checkForOtherPlayersOnTheSamePosition(players[2]);
            gameBoard.boardgrid[players[2].positionX][players[2].positionY].stack.getChildren().addAll(players[2].pawn);
            if (gameBoard.board[players[2].positionX][players[2].positionY].equals("E")) {
                checkForEntranceAndPay(players[2]);
            }
            showPlayerActions(players[2]);
        }
        String imgPath = "";
        switch(x) {
            case 1: imgPath = "dice/1.png";
                    break;
            case 2: imgPath = "dice/2.png";
                    break;
            case 3: imgPath = "dice/3.png";
                    break;
            case 4: imgPath = "dice/4.png";
                    break;
            case 5: imgPath = "dice/5.png";
                    break;
            case 6: imgPath = "dice/6.png";
                    break;
        }
        dicerollresult.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(imgPath))));
        rolldicebutton.setDisable(true);        
    }
    @FXML
    private void handleBuildRequest(ActionEvent event) {
        String s = gameBoard.board[hotelToBuildX][hotelToBuildY];
        int i = 0;
        for (int k = 0; k < hotels.length; k++) {
               if (hotels[k].number == Integer.parseInt(s))
                   i = k;
        };
        if(entranceBordersWithHotel(currentPlayer, hotels[i])) {
            if (currentPlayer.name.equals(hotels[i].plot.owner)) {
                Random ran = new Random();
                int x = ran.nextInt(100) + 1;
                if (x <= 50) {
                    if (currentPlayer.name.equals("Player1"))buildOrUpgradeHotel(players[0], hotels[i], 1);
                    else if (currentPlayer.name.equals("Player2"))buildOrUpgradeHotel(players[1], hotels[i], 1);
                    else buildOrUpgradeHotel(players[2], hotels[i], 1);
                }
                else if (x <= 70) {
                    buildrequest.setText("Declined build");
                }
                else if (x <= 85) {
                    if (currentPlayer.name.equals("Player1"))buildOrUpgradeHotel(players[0], hotels[i], 1);
                    else if (currentPlayer.name.equals("Player2"))buildOrUpgradeHotel(players[1], hotels[i], 1);
                    else buildOrUpgradeHotel(players[2], hotels[i], 1);
                }
                else {
                    if (currentPlayer.name.equals("Player1"))buildOrUpgradeHotel(players[0], hotels[i], 1);
                    else if (currentPlayer.name.equals("Player2"))buildOrUpgradeHotel(players[1], hotels[i], 1);
                    else buildOrUpgradeHotel(players[2], hotels[i], 1);
                }
                requestbuildbutton.setDisable(true);
                buyentrancebutton.setDisable(true);
                updateCreditLabels();
            }
            else {
                buildrequest.setText("Plot not owned");
            }
        }
        else {
            buildrequest.setText("Invalid plot");
        }
    }
    @FXML
    private void handleBuyPlot(ActionEvent event) {
        
        if (currentPlayer.name.equals("Player1")) {
            checkForAvailablePlot(players[0]);
        }
        else if (currentPlayer.name.equals("Player2")) {
            checkForAvailablePlot(players[1]);
        }
        else {
            checkForAvailablePlot(players[2]);
        }
        updateCreditLabels();
    }
    @FXML
    private void handleBuyEntrance(ActionEvent event) {
        findEntrances(currentPlayer);
        buyentrancebutton.setDisable(true);
        requestbuildbutton.setDisable(true);  
    }
    @FXML
    private void handleRequest1000FromBank(ActionEvent event) {
        if (currentPlayer.name.equals("Player1")) {
            players[0].credits += 1000;
            if (players[0].maxProfit < players[0].credits) players[0].maxProfit = players[0].credits;
        }
        if (currentPlayer.name.equals("Player2")) {
            players[1].credits += 1000;
            if (players[1].maxProfit < players[1].credits) players[1].maxProfit = players[1].credits;
        }
        if (currentPlayer.name.equals("Player3")) {
            players[2].credits += 1000;
            if (players[2].maxProfit < players[2].credits) players[2].maxProfit = players[2].credits;
        }
        updateCreditLabels();
        requestfrombankbutton.setDisable(true);
    }
    @FXML
    private void handleEndRound(ActionEvent event) {
        int eliminated = 0, winner = 0;
        checkForEliminatedPlayers();
        for (int i = 0; i < players.length; i++)
            if (players[i].hasLost) eliminated++;
            else winner = i;
        if (eliminated < 2) {
            turn++;
            if (turn == 3) turn = 0;
            while (players[turn].hasLost) {
                turn++;
                if (turn == 3) turn = 0;
            }
            currentPlayer = players[turn];
            showCurrentPlayer(currentPlayer);
            reinitializeFunctionality();
        }
        else {
            gameEnded(players[winner]);
        }
    }
    @FXML
    private void handleMaxProfitShow(ActionEvent event) {
        String s = "MAX PROFIT OF PLAYERS" +'\n' + '\n' + '\n' + "Player1 : " + players[0].maxProfit + '\n'
        + "Player2 : " + players[1].maxProfit + '\n'
        + "Player3 : " + players[2].maxProfit + '\n';   
        TabPane tabPane = new TabPane();
        Tab t = new Tab();
        TextArea ta = new TextArea(s);
        ta.setDisable(true);
        t.setContent(ta);
        tabPane.getTabs().add(t);
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(530);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(400); 
        tabPane.setMinHeight(300);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    @FXML
    private void handleNumberOfEntrancesShow(ActionEvent event) {
        String s = "NUMBER OF ENTRANCES FOR EACH PLAYER" +'\n' + '\n' + '\n' + "Player1 : " + players[0].entrances.size() + '\n'
        + "Player2 : " + players[1].entrances.size() + '\n'
        + "Player3 : " + players[2].entrances.size() + '\n';
        TabPane tabPane = new TabPane();
        Tab t = new Tab();
        TextArea ta = new TextArea(s);
        ta.setDisable(true);
        t.setContent(ta);
        tabPane.getTabs().add(t);
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(530);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(400); 
        tabPane.setMinHeight(300);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    private void checkForOtherPlayersOnTheSamePosition (Player p) {
        if (p.name.equals("Player1")) {
            if ((players[1].positionX == p.positionX && players[1].positionY == p.positionY) || (players[2].positionX == p.positionX && players[2].positionY == p.positionY)) {
                move(players[0], 1);
                if ((players[1].positionX == p.positionX && players[1].positionY == p.positionY) || (players[2].positionX == p.positionX && players[2].positionY == p.positionY)) {
                    move(players[0], 1);
                }
            }
        }
        else if (p.name.equals("Player2")) {
            if ((players[0].positionX == p.positionX && players[0].positionY == p.positionY) || (players[2].positionX == p.positionX && players[2].positionY == p.positionY)) {
                move(players[1], 1);
                if ((players[0].positionX == p.positionX && players[0].positionY == p.positionY) || (players[2].positionX == p.positionX && players[2].positionY == p.positionY)) {
                   move(players[1], 1);
                }
            }
        }
        else if (p.name.equals("Player3")) {
            if ((players[0].positionX == p.positionX && players[0].positionY == p.positionY) || (players[1].positionX == p.positionX && players[1].positionY == p.positionY)) {
                move(players[2], 1);
                if ((players[0].positionX == p.positionX && players[0].positionY == p.positionY) || (players[1].positionX == p.positionX && players[1].positionY == p.positionY)) {
                   move(players[2], 1);
                }
            }
        }
    }
    private void showPlayerActions(Player p) {
        //check for townhall
        if (gameBoard.townhallY <= p.positionY && !p.passedTownHall) {
            buyentrancebutton.setDisable(false);
            p.passedTownHall = true;
        }
        //check for bank
        if (gameBoard.bankY >= p.positionY && !p.passedBank) {
            requestfrombankbutton.setDisable(false);
            p.passedBank = true;
        } 
        if (gameBoard.board[p.positionX][p.positionY].equals("H")) buyplotbutton.setDisable(false);
        if (gameBoard.board[p.positionX][p.positionY].equals("E")) {
            buyentrancebutton.setDisable(false);
            requestbuildbutton.setDisable(false);
            String s = "", n = "";
            boolean t = false;
            if (p.hotels.size() > 0) {
                s = "Hotels for upgrade : \n";
                for(int i = 0; i < p.hotels.size(); i++) s += p.hotels.get(i).name + " ";
                s += "\n\n";
                
            }
            for (int i = 0; i < hotels.length; i++) {
                if (hotels[i].plot.owner.equals(p.name) && !hotels[i].plot.isConstructed) {
                   t = true;
                   n += hotels[i].name + " "; 
                }
            }
            if (t) s += "Plots for build : \n" + n;
            buildrequest.setText(s);
        }
        endroundbutton.setDisable(false);
    }
    private void showCurrentPlayer(Player p) {
        if (p.name.equals("Player1")) {
            player1.setStyle("-fx-background-color: yellow;");
        }
        else {
            player1.setStyle("-fx-background-color: inherit;");
        }
        if (p.name.equals("Player2")) {
            player2.setStyle("-fx-background-color: yellow;");
        }
        else {
            player2.setStyle("-fx-background-color: inherit;");
        }
        if (p.name.equals("Player3")) {
            player3.setStyle("-fx-background-color: yellow;");
        }
        else {
            player3.setStyle("-fx-background-color: inherit;");
        }
    }
    private void move(Player p, int k) {
        for (int i = 0; i < k; i++) {
            if ((gameBoard.board[p.positionX][p.positionY + 1].equals("S") || gameBoard.board[p.positionX][p.positionY + 1].equals("B") || gameBoard.board[p.positionX][p.positionY + 1].equals("C") || gameBoard.board[p.positionX][p.positionY + 1].equals("H") || gameBoard.board[p.positionX][p.positionY + 1].equals("E")) && (p.positionY + 1 != p.prevPositionY || p.positionX != p.prevPositionX)) {
                p.prevPositionY = p.positionY;
                p.prevPositionX = p.positionX;
                p.positionY++;
            }
            else if ((gameBoard.board[p.positionX + 1][p.positionY].equals("S") || gameBoard.board[p.positionX + 1][p.positionY].equals("B") || gameBoard.board[p.positionX + 1][p.positionY].equals("C") || gameBoard.board[p.positionX + 1][p.positionY].equals("H") || gameBoard.board[p.positionX + 1][p.positionY].equals("E")) && (p.positionX + 1 != p.prevPositionX || p.positionY != p.prevPositionY)) {         
                p.prevPositionY = p.positionY;
                p.prevPositionX = p.positionX;
                p.positionX++;
            }
            else if ((gameBoard.board[p.positionX][p.positionY - 1].equals("S") || gameBoard.board[p.positionX][p.positionY - 1].equals("B") || gameBoard.board[p.positionX][p.positionY - 1].equals("C") || gameBoard.board[p.positionX][p.positionY - 1].equals("H") || gameBoard.board[p.positionX][p.positionY - 1].equals("E")) && (p.positionY - 1 != p.prevPositionY || p.positionX != p.prevPositionX)) {
                p.prevPositionY = p.positionY;
                p.prevPositionX = p.positionX;
                p.positionY--;
            }
            else if ((gameBoard.board[p.positionX - 1][p.positionY].equals("S") || gameBoard.board[p.positionX - 1][p.positionY].equals("B") || gameBoard.board[p.positionX - 1][p.positionY].equals("C") || gameBoard.board[p.positionX - 1][p.positionY].equals("H") || gameBoard.board[p.positionX - 1][p.positionY].equals("E")) && (p.positionX - 1 != p.prevPositionX || p.positionY != p.prevPositionY)) {
                p.prevPositionY = p.positionY;
                p.prevPositionX = p.positionX;
                p.positionX--;
            }
            if (gameBoard.board[p.positionX][p.positionY].equals("S")) {
                p.passedBank = false;
            }
            if (gameBoard.board[p.positionX][p.positionY].equals("C")) {
                p.passedTownHall = false;
            }
        }
    }
    private void outputBoard() {
        for (int i = 0; i < 12; i ++){
            for (int j = 0; j < 15; j++){
                Rect r = new Rect();
                r.rec.setWidth(57);
                r.rec.setHeight(48);
                switch (gameBoard.board[i][j]) {
                    case "S" : r.rec.setFill(Color.BLACK);
                               break;
                    case "C" : r.rec.setFill(Color.BROWN);
                               r.image = new ImageView(new Image(getClass().getResourceAsStream("special-buildings/townhall.png")));
                               break;
                    case "B" : r.rec.setFill(Color.ORANGE);
                               r.image = new ImageView(new Image(getClass().getResourceAsStream("special-buildings/bank.png")));
                               break;
                    case "H" : r.rec.setFill(Color.PURPLE);
                               break;
                    case "E" : r.rec.setFill(Color.YELLOW);
                               break;
                    case "F" : r.rec.setFill(Color.GRAY);
                               break;
                    default  : r.rec.setFill(Color.rgb(204, 255, 204));
                               r.text.setText("H" + gameBoard.board[i][j]);
                               break;
                }
                r.stack.getChildren().addAll(r.rec, r.text);
                if (gameBoard.board[i][j].equals("B")) r.stack.getChildren().addAll(r.image);
                else if (gameBoard.board[i][j].equals("C")) r.stack.getChildren().addAll(r.image);
                gameBoard.boardgrid[i][j] = r;
                GridPane.setRowIndex(r.stack, i);
                GridPane.setColumnIndex(r.stack, j);
                gp.getChildren().addAll(r.stack);
            }
        } 
    }
    private void setUpGameBoard() {
        //INITIALIZE THE BOARD
        gameBoard.parseBoard();
        //OUTPUT BOARD
        outputBoard();   
    }
    private void initializeGame() {
        setUpGameBoard();
        startingPositionX = gameBoard.startX;
        startingPositionY = gameBoard.startY;
        players[0] = new Player("Player1", "Blue", 12000, startingPositionX, startingPositionY, Color.BLUE);
        players[1] = new Player("Player2", "Red", 12000, startingPositionX, startingPositionY, Color.RED);
        players[2] = new Player("Player3", "Green", 12000, startingPositionX, startingPositionY, Color.GREEN);
        gameBoard.boardgrid[gameBoard.startX][gameBoard.startY].stack.getChildren().addAll(players[2].pawn, players[1].pawn, players[0].pawn);
        updateCreditLabels();
        currentPlayer = players[0];
        showCurrentPlayer(currentPlayer);
        parseHotels(gameBoard.folder);
        timertext.setText("Total Time: 00:00:00");
        availableHotels = hotels.length;
        availablehotels.setText("Available Hotels: " + availableHotels);
        reinitializeFunctionality();
        startTimer();
    }
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 15; j++) {
                if (gameBoard.boardgrid[i][j].rec.getStroke() == Color.BLACK) {
                    gameBoard.boardgrid[i][j].rec.setStroke(previousColor);
                }
            }
        }
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != gp) {
            Node parent = clickedNode.getParent();
            while (parent != gp) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            plotToBuyY = GridPane.getColumnIndex(clickedNode);
            plotToBuyX = GridPane.getRowIndex(clickedNode);
            hotelX = plotToBuyX;
            hotelY = plotToBuyY;
            hotelToBuildX = plotToBuyX;
            hotelToBuildY = plotToBuyY;
        }
        if (!gameBoard.boardgrid[plotToBuyX][plotToBuyY].text.getText().contains("H")) {
            gameBoard.boardgrid[plotToBuyX][plotToBuyY].rec.setStroke(Color.BLACK);
            gameBoard.boardgrid[plotToBuyX][plotToBuyY].rec.setStrokeWidth(2);
        }
        else {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 15; j++) {
                    if (gameBoard.board[plotToBuyX][plotToBuyY].equals(gameBoard.board[i][j])) {
                        previousColor = gameBoard.boardgrid[i][j].rec.getStroke();
                        gameBoard.boardgrid[i][j].rec.setStroke(Color.BLACK);
                        gameBoard.boardgrid[i][j].rec.setStrokeWidth(2);
                    }
                }
            }
        }
    }
    private void checkForAvailablePlot(Player p) {
       if (gameBoard.boardgrid[p.positionX - 1][p.positionY].text.getText().contains("H")) {
           if (plot1x == -1) {
               plot1x = p.positionX - 1;
               plot1y = p.positionY;
           }
           else if (plot1x != p.positionX - 1) {
               plot2x = p.positionX - 1;
               plot2y = p.positionY;
           }
       }
       if (gameBoard.boardgrid[p.positionX][p.positionY + 1].text.getText().contains("H")) {
           if (plot1x == -1) {
               plot1x = p.positionX;
               plot1y = p.positionY + 1;
           }
           else if (plot1y != p.positionY + 1) {
               plot2x = p.positionX;
               plot2y = p.positionY + 1;
           }
       }
       if (gameBoard.boardgrid[p.positionX + 1][p.positionY].text.getText().contains("H")) {
           if (plot1x == -1) {
               plot1x = p.positionX + 1;
               plot1y = p.positionY;
           }
           else if (plot1x != p.positionX + 1) {
               plot2x = p.positionX + 1;
               plot2y = p.positionY;
           }
       }
       if (gameBoard.boardgrid[p.positionX][p.positionY - 1].text.getText().contains("H")) {
           if (plot1x == -1) {
               plot1x = p.positionX;
               plot1y = p.positionY - 1;
           }
           else if (plot1y != p.positionY - 1) {
               plot2x = p.positionX;
               plot2y = p.positionY - 1;
           }
       }
       
       String s = gameBoard.board[plotToBuyX][plotToBuyY];
       int i = 0;
       if (((gameBoard.board[plotToBuyX][plotToBuyY].equals(gameBoard.board[plot1x][plot1y])) || (gameBoard.board[plotToBuyX][plotToBuyY].equals(gameBoard.board[plot2x][plot2y])))) {
           for (int k = 0; k < hotels.length; k++) {
               if (hotels[k].number == Integer.parseInt(s))
                   i = k;
           }
           if (!hotels[i].plot.isConstructed) {
               
                if (!hotels[i].plot.isOwned) {
                    p.credits -= hotels[i].plotCost;
                    hotels[i].plot.isOwned = true;
                    buyplotmessage.setText("Plot bought from " + p.name + "\n and payed " + hotels[i].plotCost + " to bank");
                }
                else {
                    int l = 0;
                    for (int n = 0; n < players.length; n++) {
                        if (hotels[i].plot.owner.equals(players[n].name)) {
                            players[n].credits += hotels[i].requiredPlotCost;
                            l = n;
                        }
                    }
                    p.credits -= hotels[i].requiredPlotCost;
                    buyplotmessage.setText("Plot bought from " + p.name + "\n and payed " + hotels[i].requiredPlotCost + " to " + players[l].name);
                }
                colorPlot(s, p);
                buyplotbutton.setDisable(true);
                hotels[i].plot.owner = p.name;
           }
           else {
               buyplotmessage.setText("Plot already constructed");
           }
       }
       else {
           buyplotmessage.setText("Invalid plot");
       }
       plot1x = -1;
       plot2x = -1;
       plot1y = -1;
       plot2y = -1;
    }
    private void colorPlot(String s, Player p) {
        Color c;
        if (p.name.equals("Player1")) {
            c = Color.BLUE;
        }
        else if (p.name.equals("Player2")) {
            c = Color.RED;
        }
        else {
            c = Color.GREEN;
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 15; j++) {
                if (gameBoard.board[i][j].equals(s)) {
                    gameBoard.boardgrid[i][j].rec.setStroke(c);
                    gameBoard.boardgrid[i][j].rec.setStrokeWidth(2);
                }
            }
        }  
    }
    private void colorHotel(Player p, Hotel h) {
        Color c;
        if (p.name.equals("Player1")) {
            c = Color.rgb(0, 0, 255, (h.currentUpgradeLevel) * 0.15f);
        }
        else if (p.name.equals("Player2")) {
            c = Color.rgb(255, 0, 0, (h.currentUpgradeLevel) * 0.15f);
        }
        else {
            c = Color.rgb(0, 255, 0, (h.currentUpgradeLevel) * 0.15f);
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 15; j++) {
                if (gameBoard.board[i][j].equals(Integer.toString(h.number))) {
                    gameBoard.boardgrid[i][j].rec.setFill(c);
                }
            }
        }  
    }
    private void findEntrances(Player p) {
        int entrancePrice = 0;
        String hotel = "";
        boolean hasHotel = false;
        for (int k = 0; k < hotels.length; k++) {
            if (gameBoard.board[hotelX][hotelY].equals(Integer.toString(hotels[k].number))) {
                entrancePrice = hotels[k].entranceCost;
                hotel = hotels[k].name;
            }
        }
        for (int k = 0; k < p.hotels.size(); k++) {
            if (gameBoard.board[hotelX][hotelY].equals(Integer.toString(p.hotels.get(k).number))) {
                hasHotel = true;
            }
        }
        if (hasHotel) {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 15; j++) {
                    if (gameBoard.board[i][j].equals("E") && !gameBoard.boardgrid[i][j].hasEntrance && !gameBoard.boardgrid[i][j].belongsToTheBank) {
                        if (gameBoard.board[i - 1][j].equals(gameBoard.board[hotelX][hotelY])) {
                            Entrance e = new Entrance(hotel, entrancePrice, i, j); 
                            randomEntrance.add(e);
                        }
                        else if (gameBoard.board[i + 1][j].equals(gameBoard.board[hotelX][hotelY])) {
                            Entrance e = new Entrance(hotel, entrancePrice, i, j);
                            randomEntrance.add(e);
                        }
                        else if (gameBoard.board[i][j - 1].equals(gameBoard.board[hotelX][hotelY])) {
                            Entrance e = new Entrance(hotel, entrancePrice, i, j);
                            randomEntrance.add(e);
                        }
                        else if (gameBoard.board[i][j + 1].equals(gameBoard.board[hotelX][hotelY])) {
                            Entrance e = new Entrance(hotel, entrancePrice, i, j);
                            randomEntrance.add(e);
                        }
                    }
                }
            }
            if (randomEntrance.size() != 0) {
                Random ran = new Random();
                int x = ran.nextInt(randomEntrance.size());

                if (p.name.equals("Player1")) {
                    randomEntrance.get(x).owner = "Player1";
                    players[0].credits -= randomEntrance.get(x).price;
                    players[0].entrances.add(randomEntrance.get(x));
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStroke(Color.BLUE);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStrokeWidth(2);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].hasEntrance = true;
                }
                if (p.name.equals("Player2")) {
                    randomEntrance.get(x).owner = "Player2";
                    players[1].credits -= randomEntrance.get(x).price;
                    players[1].entrances.add(randomEntrance.get(x));
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStroke(Color.RED);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStrokeWidth(2);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].hasEntrance = true;
                }
                if (p.name.equals("Player3")) {
                    randomEntrance.get(x).owner = "Player3";
                    players[2].credits -= randomEntrance.get(x).price;
                    players[2].entrances.add(randomEntrance.get(x));
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStroke(Color.GREEN);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].rec.setStrokeWidth(2);
                    gameBoard.boardgrid[randomEntrance.get(x).entranceX][randomEntrance.get(x).entranceY].hasEntrance = true;
                }
                randomEntrance.clear();
                updateCreditLabels();
            }
            else {
                buildentrancemessage.setText("No available space");
            }
        }
        else {
            buildentrancemessage.setText("No hotel owned");
        }
    }
    public void buildOrUpgradeHotel(Player p, Hotel h, int n) {
        if (h.currentUpgradeLevel < h.buildCost.length) {
            if (n == 1) {
               if (p.credits - h.buildCost[h.currentUpgradeLevel] > 0) {
                   p.credits -= h.buildCost[h.currentUpgradeLevel];
                   buildrequest.setText("Accepted build");
                   colorHotel(p, h);
               }
               else {
                   buildrequest.setText("Not enough credits");
               }
            }
            else if (n == 2) {
                buildrequest.setText("Free build");
                colorHotel(p, h);
            }
            else if (n == 3) {
                if (p.credits - (h.buildCost[h.currentUpgradeLevel] + (0.15 * h.buildCost[h.currentUpgradeLevel])) > 0) {
                   p.credits -= (h.buildCost[h.currentUpgradeLevel] + (0.15 * h.buildCost[h.currentUpgradeLevel]));
                   buildrequest.setText("Over priced build");
                   colorHotel(p, h);
               }
               else {
                   buildrequest.setText("Not enough credits");
               }
            }
            if (h.currentUpgradeLevel == 0) {
                availableHotels--;
                availablehotels.setText("Available Hotels: " + availableHotels);
            }
            h.currentUpgradeLevel++;
            h.plot.isConstructed = true;
            h.hotelOwner = p.name;
            p.hotels.add(h);
        }
        else {
            buildrequest.setText("Hotel is max!");
        }
    }
    private void checkForEntranceAndPay(Player p) {
        int i,k;
        if (p.name.equals("Player1")) {
            i = 1;
            k = 2;
        }
        else if (p.name.equals("Player2")) {
            i = 0;
            k = 2;
        }
        else {
            i = 0;
            k = 1;
        }
        for (int m = 0; m < players[i].entrances.size(); m++) {
            if (p.positionX == players[i].entrances.get(m).entranceX && p.positionY == players[i].entrances.get(m).entranceY){
                for (int l = 0; l < hotels.length; l++) {
                    if (hotels[l].name.equals(players[i].entrances.get(m).hName)){
                        Random r = new Random();
                        int x = r.nextInt(6) + 1;
                        p.credits -= hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x;
                        players[i].credits += hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x;
                        String s = p.name + " stayed at " + hotels[l].name + " hotel owned by " + players[i].name + " for " + x + " night(s) \n with rent " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] + " per night and payed " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x + " credits. "
                                + players[i].name + " got " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x + " credits ";
                        if (players[i].credits > players[i].maxProfit) {
                            players[i].maxProfit = players[i].credits;
                        }
                        creditspayedforstayinginahotel.setText(s);
                        updateCreditLabels();
                    }
                }
            }
        }
        for (int m = 0; m < players[k].entrances.size(); m++) {
            if (p.positionX == players[k].entrances.get(m).entranceX && p.positionY == players[k].entrances.get(m).entranceY){
                for (int l = 0; l < hotels.length; l++) {
                    if (hotels[l].name.equals(players[k].entrances.get(m).hName)){
                        Random r = new Random();
                        int x = r.nextInt(6) + 1;
                        p.credits -= hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x;
                        players[k].credits += hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x;
                        String s = p.name + " stayed at " + hotels[l].name + " hotel owned by " + players[k].name + " for " + x + " day(s) \n with rent " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] + " per night and payed " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x + " credits. "
                                + players[k].name + " got " + hotels[l].rentCost[hotels[l].currentUpgradeLevel - 1] * x + " credits ";
                        if (players[k].credits > players[k].maxProfit) {
                            players[k].maxProfit = players[k].credits;
                        }
                        creditspayedforstayinginahotel.setText(s);
                        updateCreditLabels();
                    }
                }
            }
        }
    }
    private void updateCreditLabels() {
        player1.setText("Player1: " + players[0].credits);
        player2.setText("Player2: " + players[1].credits);
        player3.setText("Player3: " + players[2].credits);
    }
    private void checkForEliminatedPlayers() {
        for (int i = 0; i < players.length; i++) {
            if (players[i].credits <= 0) {
                players[i].hasLost = true;
                players[i].credits = 0;
                updateCreditLabels();
                removeHotelsAndEntrances(players[i]);
                gameBoard.boardgrid[players[i].positionX][players[i].positionY].stack.getChildren().remove(players[i].pawn);
                players[i].positionX = 0;
                players[i].positionY = 0;
            }
        }
    }
    private void removeHotelsAndEntrances(Player p) {
        for (int i = 0 ; i < p.hotels.size(); i++) {
            p.hotels.get(i).hotelOwner = "Bank";
            for (int j = 0; j < 12; j++) {
                for (int k = 0; k < 15; k++) {
                    if (gameBoard.board[j][k].equals(Integer.toString(p.hotels.get(i).number))) {
                        gameBoard.boardgrid[j][k].rec.setFill(Color.rgb(204, 255, 204));
                        gameBoard.boardgrid[j][k].rec.setStrokeWidth(0);
                    }
                }
            }
        }
        for (int i = 0 ; i < p.entrances.size(); i++) {
            for (int j = 0; j < 12; j++) {
                for (int k = 0; k < 15; k++) {
                    if (j == p.entrances.get(i).entranceX && k == p.entrances.get(i).entranceY) {
                        gameBoard.boardgrid[i][j].belongsToTheBank = true;
                        gameBoard.boardgrid[i][j].rec.setStroke(Color.rgb(51, 26, 0));
                    }
                }
            }
        }
        p.entrances.clear();
    }
    private void gameEnded(Player p) {
        rolldicebutton.setDisable(true);
        requestbuildbutton.setDisable(true);
        buyplotbutton.setDisable(true);
        requestfrombankbutton.setDisable(true);
        buyentrancebutton.setDisable(true);
        endroundbutton.setDisable(true);
        timer.cancel();
        timer.purge();
        String s = "WINNER IS" +'\n' + '\n' + "Name : " + p.name + '\n' + "Profit : " + p.credits;   
        TabPane tabPane = new TabPane();
        Tab t = new Tab();
        TextArea ta = new TextArea(s);
        ta.setDisable(true);
        t.setContent(ta);
        tabPane.getTabs().add(t);
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(530);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(400); 
        tabPane.setMinHeight(300);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }  
    private void parseHotels(String s) {
        final File folder = new File(gameBoard.cDir + "/src/hotelboardgame/boards/" + s);
        int numberOfHotels = folder.listFiles().length - 1;
        hotels = new Hotel[numberOfHotels];
        int k = 0;
        for (final File file : folder.listFiles()) {
            if (!file.getName().equals("board.txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(folder + "/" + file.getName()))) {
                    String b[] = new String[2];
                    String name = reader.readLine();
                    b = reader.readLine().split(",");
                    int plotCost = Integer.parseInt(b[0]);
                    int requiredPlotCost = Integer.parseInt(b[1]);
                    int entranceCost = Integer.parseInt(reader.readLine());
                    int m = 0;
                    String line = "";
                    ArrayList<Integer> bc = new ArrayList<Integer>();
                    ArrayList<Integer> rc = new ArrayList<Integer>();
                    while ((line = reader.readLine()) != null) {
                        String c[] = new String[2];
                        c = line.split(",");    
                        bc.add(Integer.parseInt(c[0]));
                        rc.add(Integer.parseInt(c[1]));
                        m++;
                    }
                    int buildCost[] = new int[m];
                    int rentCost[] = new int[m];
                    for (int i = 0; i < bc.size(); i ++) {
                        buildCost[i] = bc.get(i);
                        rentCost[i] = rc.get(i);
                    }
                    hotels[k] = new Hotel(name, plotCost, requiredPlotCost, entranceCost, buildCost, rentCost, Integer.parseInt(file.getName().substring(0, file.getName().length() - 4)));
                } catch (IOException e) {
                    e.printStackTrace();
                  } 
                k++;
            }
        }
    }
    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        seconds++;
                        if(seconds == 60) {
                            seconds = 0;
                            minutes++;
                        }
                        if(minutes == 60) {
                            minutes = 0;
                            hours++;
                        }
                        timertext.setText("Total Time: " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    }
                });
            }
        }, 0, 1000);
    }
    private boolean entranceBordersWithHotel(Player p, Hotel h) {
        if (gameBoard.board[p.positionX - 1][p.positionY].equals(Integer.toString(h.number))) {
           return true;
        }
        else if (gameBoard.board[p.positionX][p.positionY + 1].equals(Integer.toString(h.number))) {
           return true;
        }
        else if (gameBoard.board[p.positionX + 1][p.positionY].equals(Integer.toString(h.number))) {
           return true;
        }
        else if (gameBoard.board[p.positionX][p.positionY - 1].equals(Integer.toString(h.number))) {
           return true;
        }
        return false;
    }
    public static void howToPlay() {
        String s = "1) Roll the dice \n"
                + "2) You can either build/upgrade a hotel, buy a plot or build an entrance\n"
                + "3) Click on the grid to choose a hotel, a plot or a cell for entrance\n"
                + "4) After you finish your moves click on the end round button\n"
                + "5) Click on GAME or STATISTICS menus to obtain information \nabout the players and the hotels\n\n"
                + "HAVE FUN!!!";
        TabPane tabPane = new TabPane();
        Tab t = new Tab();
        TextArea ta = new TextArea(s);
        ta.setDisable(true);
        t.setText("How To Play");
        t.setContent(ta);
        tabPane.getTabs().add(t);
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(710);
        btn.setLayoutY(2);
        btn.setMinWidth(80);
        btn.setMinHeight(40);
        popup.getContent().add(tabPane); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        tabPane.setMinWidth(800); 
        tabPane.setMinHeight(600);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeGame();
    } 
    
}
