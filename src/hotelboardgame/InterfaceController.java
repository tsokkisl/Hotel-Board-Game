/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class InterfaceController implements Initializable {
    private static Stage myStage;
    private int startingPositionX;
    private int startingPositionY;
    private Player currentPlayer;
    private int turn = 0;
    private Player[] players = new Player[3];
    private Hotel[] hotels = new Hotel[6];
    public static void setStage(Stage stage) {
         myStage = stage;
    }
    private static Board gameBoard = new Board();
    private int plot1x = -1, plot2x = -1, plot1y = -1, plot2y = -1, plotToBuyX, plotToBuyY;
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
        dicerollresult.setText("");
        buildrequest.setText("");
        buyplotmessage.setText("");
    }
    private String hotelDetails(Hotel h) {
        String s = "";
        s = "Name: " + h.name + '\n' + "Plot Cost: " + h.plotCost + '\n' + "Required Plot Cost: " + h.requiredPlotCost + '\n'
        + "Gate Cost: " + h.entranceCost + '\n' + "Basic Build Cost: " + h.basicBuildCost + '\n' + "Basic Build Rent: " + h.basicBuildRent + '\n'
        + "Build Cost: " + h.outsideBuildCost + '\n' + "Max Rent: " + h.maxRent;
        for(int i = 0; i < h.upgradeBuildCost.length; i++)
            s = s + '\n' + "Upgrade level " + (i + 1) + " Cost: " + h.upgradeBuildCost[i] + '\n'
            + "Upgrade level " + (i + 1) + " Rent: " + h.upgradeBuildRent[i];
        return s;
    }
    private String hotelInfo(Hotel h) {
        String s = "";
        s = "Name: " + h.name + '\n' + "Owner: " + "None" + '\n' + "Max upgrade level: " + "4" + '\n'
        + "Current upgrade level: " + "2" + '\n';
        return s;
    }
    @FXML
    private void handleGameStart(ActionEvent event) {
        //Game restart and initialize all values
        turn = 0;
        reinitializeFunctionality();
        setUpGameBoard();
        startingPositionX = gameBoard.startX;
        startingPositionY = gameBoard.startY;
        players[0] = new Player("Player1", "Blue", 12000, startingPositionX, startingPositionY, Color.BLUE);
        players[1] = new Player("Player2", "Red", 12000, startingPositionX, startingPositionY, Color.RED);
        players[2] = new Player("Player3", "Green", 12000, startingPositionX, startingPositionY, Color.GREEN);
        gameBoard.boardgrid[gameBoard.startX][gameBoard.startY].stack.getChildren().addAll(players[2].pawn, players[1].pawn, players[0].pawn);
        currentPlayer = players[0];
        showCurrentPlayer(currentPlayer);
        player1.setText("Player1 :" + players[0].credits);
        player2.setText("Player2 :" + players[1].credits);
        player3.setText("Player3 :" + players[2].credits);
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
    }
    @FXML
    private void handleCardShow(ActionEvent event) {
        Tab[] tabs = new Tab[6];
        TabPane tabPane = new TabPane();
        String s = "";
        for(int i = 0; i < 6; i++) {
            tabs[i] = new Tab();
            tabs[i].setText("Hotel " + (i + 1));
            s = hotelDetails(hotels[i]);
            TextArea ta = new TextArea(s);
            ta.setDisable(true);
            tabs[i].setContent(ta);
            tabPane.getTabs().add(tabs[i]);
        }
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(720);
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
    @FXML
    private void handleHotelInfoShow(ActionEvent event) {
        Tab[] tabs = new Tab[6];
        TabPane tabPane = new TabPane();
        String s = "";
        for(int i = 0; i < 6; i++) {
            tabs[i] = new Tab();
            tabs[i].setText("Hotel " + (i + 1));
            s = hotelInfo(hotels[i]);
            TextArea ta = new TextArea(s);
            ta.setDisable(true);
            tabs[i].setContent(ta);
            tabPane.getTabs().add(tabs[i]);
        }
        Popup popup = new Popup(); 
        tabPane.setStyle(" -fx-background-color: #cfcfcf;-fx-font: 18 algerian;");
        Button btn = new Button("Close");
        btn.setLayoutX(720);
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
    @FXML
    private void handleGameExit(ActionEvent event) {
        //exits game
        Platform.exit();
    }
    @FXML
    private void handleDiceRoll(ActionEvent event) {
        Random ran = new Random();
        int x = ran.nextInt(6) + 1;
        if (currentPlayer.name == "Player1") {          
            move(players[0], x);
            checkForOtherPlayersOnTheSamePosition(players[0]);
            gameBoard.boardgrid[players[0].positionX][players[0].positionY].stack.getChildren().addAll(players[0].pawn);
            showPlayerActions(players[0]);
        }
        if (currentPlayer.name == "Player2") {
            move(players[1], x);
            checkForOtherPlayersOnTheSamePosition(players[1]);
            gameBoard.boardgrid[players[1].positionX][players[1].positionY].stack.getChildren().addAll(players[1].pawn);
            showPlayerActions(players[1]);
        }
        if (currentPlayer.name == "Player3") {
            move(players[2], x);
            checkForOtherPlayersOnTheSamePosition(players[2]);
            gameBoard.boardgrid[players[2].positionX][players[2].positionY].stack.getChildren().addAll(players[2].pawn);
            showPlayerActions(players[2]);
        }
        dicerollresult.setText(Integer.toString(x));
        rolldicebutton.setDisable(true);        
    }
    @FXML
    private void handleBuildRequest(ActionEvent event) {
        Random ran = new Random();
        int x = ran.nextInt(100) + 1;
        if (x <= 50) buildrequest.setText("Accepted build");
        else if(x <= 70) buildrequest.setText("Declined build");
        else if (x <= 85) buildrequest.setText("Free build");
        else buildrequest.setText("Overpriced build");
        requestbuildbutton.setDisable(true);
    }
    @FXML
    private void handleBuyPlot(ActionEvent event) {
        
        if (currentPlayer.name.equals("Player1")) {
            checkForAvailablePlot(players[0]);
            player1.setText("Player1 :" + players[0].credits);
        }
        else if (currentPlayer.name.equals("Player2")) {
            checkForAvailablePlot(players[1]);
            player2.setText("Player2 :" + players[1].credits);
        }
        else {
            checkForAvailablePlot(players[2]);
            player3.setText("Player3 :" + players[2].credits);
        }
    }
    @FXML
    private void handleBuyEntrance(ActionEvent event) {
        Entrance e = new Entrance(hotels[0], 500);
        if (currentPlayer.name == "Player1") {
            players[0].credits -= e.price;
            players[0].entrances.add(e);
            player1.setText("Player1 :" + players[0].credits);
        }
        if (currentPlayer.name == "Player2") {
            players[1].credits -= e.price;
            players[1].entrances.add(e);
            player2.setText("Player2 :" + players[1].credits);
        }
        if (currentPlayer.name == "Player3") {
            players[2].credits -= e.price;
            players[2].entrances.add(e);
            player3.setText("Player3 :" + players[2].credits);
        }
        buyentrancebutton.setDisable(true);
    }
    @FXML
    private void handleRequest1000FromBank(ActionEvent event) {
        if (currentPlayer.name == "Player1") {
            players[0].credits += 1000;
            players[0].maxProfit += 1000;
            player1.setText("Player1 :" + players[0].credits);
        }
        if (currentPlayer.name == "Player2") {
            players[1].credits += 1000;
            players[1].maxProfit += 1000;
            player2.setText("Player2 :" + players[1].credits);
        }
        if (currentPlayer.name == "Player3") {
            players[2].credits += 1000;
            players[2].maxProfit += 1000;
            player3.setText("Player3 :" + players[2].credits);
        }
        requestfrombankbutton.setDisable(true);
    }
    @FXML
    private void handleEndRound(ActionEvent event) {
        turn++;
        if (turn == 3) turn = 0;
        currentPlayer = players[turn];
        showCurrentPlayer(currentPlayer);
        reinitializeFunctionality();
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
        btn.setLayoutX(720);
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
        btn.setLayoutX(720);
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
    private void checkForOtherPlayersOnTheSamePosition (Player p) {
        if(p.name.equals("Player1")) {
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
        if (gameBoard.townhallY > gameBoard.startY) {
            if (gameBoard.townhallY <= p.positionY && !p.changedRoundForTownHall) {
                buyentrancebutton.setDisable(false);
                p.changedRoundForTownHall = true;
            }
        }
        else if (gameBoard.townhallY < gameBoard.startY) {
            if (gameBoard.townhallY >= p.positionY && !p.passedTownHall) {
                buyentrancebutton.setDisable(false);
                p.passedTownHall = true;
            }
        }
        //check for bank
        if (gameBoard.bankY > gameBoard.startY) {
            if (gameBoard.bankY <= p.positionY && !p.changedRoundForBank) {
                requestfrombankbutton.setDisable(false);
                p.changedRoundForBank = true;
            }
        }
        else if (gameBoard.bankY < gameBoard.startY) {
            if (gameBoard.bankY >= p.positionY && !p.passedBank) {
                requestfrombankbutton.setDisable(false);
                p.passedBank = true;
            }
        }
        if (gameBoard.board[p.positionX][p.positionY].equals("H")) buyplotbutton.setDisable(false);
        if (gameBoard.board[p.positionX][p.positionY].equals("E")) {
            buyentrancebutton.setDisable(false);
            requestbuildbutton.setDisable(false);
        }
        endroundbutton.setDisable(false);
    }
    private void showCurrentPlayer(Player p) {
        if (p.name == "Player1") {player1.setStyle("-fx-background-color: yellow;");}
        else {player1.setStyle("-fx-background-color: inherit;");}
        if (p.name == "Player2") {player2.setStyle("-fx-background-color: yellow;");}
        else {player2.setStyle("-fx-background-color: inherit;");}
        if (p.name == "Player3") {player3.setStyle("-fx-background-color: yellow;");}
        else {player3.setStyle("-fx-background-color: inherit;");}
    }
    private void move(Player p, int k) {
        for(int i = 0; i < k; i++) {
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
        }
    }
    private void outputBoard() {
        for (int i = 0; i < 12; i ++){
            for (int j = 0; j < 15; j++){
                Rect r = new Rect();
                r.rec.setWidth(57);
                r.rec.setHeight(48);
                switch(gameBoard.board[i][j]) {
                    case "S" : r.rec.setFill(Color.BLACK);
                               r.text.setText("START");
                               break;
                    case "C" : r.rec.setFill(Color.BROWN);
                               r.text.setText("MAYOR");
                               break;
                    case "B" : r.rec.setFill(Color.ORANGE);
                               r.text.setText("BANK");
                               break;
                    case "H" : r.rec.setFill(Color.PURPLE);
                               r.text.setText("PLOT");
                               break;
                    case "E" : r.rec.setFill(Color.YELLOW);
                               r.text.setText("GATE");
                               break;
                    case "F" : r.rec.setFill(Color.GRAY);
                               r.text.setText("FREE");
                               break;
                    default  : r.rec.setFill(Color.rgb(128, 255, 187));
                               r.text.setText("H" + gameBoard.board[i][j]);
                               break;
                }
                r.stack.getChildren().addAll(r.rec, r.text);
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
        reinitializeFunctionality();
        startingPositionX = gameBoard.startX;
        startingPositionY = gameBoard.startY;
        players[0] = new Player("Player1", "Blue", 12000, startingPositionX, startingPositionY, Color.BLUE);
        players[1] = new Player("Player2", "Red", 12000, startingPositionX, startingPositionY, Color.RED);
        players[2] = new Player("Player3", "Green", 12000, startingPositionX, startingPositionY, Color.GREEN);
        gameBoard.boardgrid[gameBoard.startX][gameBoard.startY].stack.getChildren().addAll(players[2].pawn, players[1].pawn, players[0].pawn);
        player1.setText("Player1 :" + players[0].credits);
        player2.setText("Player2 :" + players[1].credits);
        player3.setText("Player3 :" + players[2].credits);
        currentPlayer = players[0];
        showCurrentPlayer(currentPlayer);
        hotels[0] = new Hotel("FUJIYAMA", 100, 500, 100, 2200, 100, new int[]{1400,1400}, new int[]{100,200}, 500, 400, 1);
        hotels[1] = new Hotel("L'ETOILE", 3000, 1500, 250, 3300, 150, new int[]{2200,1800,1800,1800}, new int[]{300,300,300,450}, 4000, 750, 3);
        hotels[2] = new Hotel("ROYAL", 2500, 1250, 300, 3600, 150, new int[]{2600,1800,1800}, new int[]{300,300,450}, 3000, 600, 5);
        hotels[3] = new Hotel("BIG HOUSE", 3200, 1600, 200, 1800, 200, new int[]{1500,1800}, new int[]{200,300}, 2000, 400, 9);
        hotels[4] = new Hotel("EAGLES", 1800, 900, 250, 2000, 150, new int[]{1500}, new int[]{250}, 1200, 400, 10);
        hotels[5] = new Hotel("AFRICA", 1500, 750, 100, 1600, 100, new int[]{1000,1000}, new int[]{150,200}, 1200, 300, 11);
    }
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != gp) {
            Node parent = clickedNode.getParent();
            while (parent != gp) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            plotToBuyY = GridPane.getColumnIndex(clickedNode);
            plotToBuyX = GridPane.getRowIndex(clickedNode);
        }
    }
    private void checkForAvailablePlot(Player p) {
       if(gameBoard.boardgrid[p.positionX - 1][p.positionY].text.getText().contains("H")) {
           if(plot1x == -1) {
               plot1x = p.positionX - 1;
               plot1y = p.positionY;
           }
           else if (plot1x != p.positionX - 1) {
               plot2x = p.positionX - 1;
               plot2y = p.positionY;
           }
       }
       if (gameBoard.boardgrid[p.positionX][p.positionY + 1].text.getText().contains("H")) {
           if(plot1x == -1) {
               plot1x = p.positionX;
               plot1y = p.positionY + 1;
           }
           else if (plot1y != p.positionY + 1) {
               plot2x = p.positionX;
               plot2y = p.positionY + 1;
           }
       }
       if (gameBoard.boardgrid[p.positionX + 1][p.positionY].text.getText().contains("H")) {
           if(plot1x == -1) {
               plot1x = p.positionX + 1;
               plot1y = p.positionY;
           }
           else if (plot1x != p.positionX + 1) {
               plot2x = p.positionX + 1;
               plot2y = p.positionY;
           }
       }
       if (gameBoard.boardgrid[p.positionX][p.positionY - 1].text.getText().contains("H")) {
           if(plot1x == -1) {
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
       if (((plotToBuyX == plot1x && plotToBuyY == plot1y) || (plotToBuyX == plot2x && plotToBuyY == plot2y))) {
           for (int k = 0; k < 6; k++) {
               if(hotels[k].number == Integer.parseInt(s))
                   i = k;
           };
           if (!hotels[i].plot.isConstructed) {
               
                if (!hotels[i].plot.isOwned) {
                    p.credits -= hotels[i].plotCost;
                    hotels[i].plot.isOwned = true;
                }
                else {
                    p.credits -= hotels[i].requiredPlotCost;
                }
                colorPlot(s, p);
                buyplotmessage.setText("Plot bought");
                buyplotbutton.setDisable(true);
           }
           else {
               buyplotmessage.setText("Invalid plot");
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
    void colorPlot(String s, Player p) {
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
        System.out.println(c);
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 15; j++) {
                if (gameBoard.board[i][j].equals(s)) {
                    gameBoard.boardgrid[i][j].rec.setFill(c);
                }
            }
        }  
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeGame();
    } 
    
}
