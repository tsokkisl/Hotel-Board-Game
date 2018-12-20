/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class InterfaceController implements Initializable {
    private static Stage myStage;
    private int startingPosition;
    private Player currentPlayer;
    private int turn = 0;
    private Player[] players = new Player[3];
    private Hotel[] hotels = new Hotel[6];
    public static void setStage(Stage stage) {
         myStage = stage;
    }
    private static Board gameBoard = new Board();
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
    private  TextArea gameboard;
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
    
    private void reinitializeFunctionality() {
        rolldicebutton.setDisable(false);
        requestbuildbutton.setDisable(true);
        requestfrombankbutton.setDisable(true);
        buyplotbutton.setDisable(true);
        buyentrancebutton.setDisable(true);
        endroundbutton.setDisable(true);
        dicerollresult.setText("");
        buildrequest.setText("");
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
    @FXML
    private void handleGameStart(ActionEvent event) {
        //Game restart and initialize all values
        turn = 0;
        reinitializeFunctionality();
        setUpGameBoard();
        startingPosition = gameBoard.start;
        players[0] = new Player("Player1", "Blue", 12000, startingPosition);
        players[1] = new Player("Player2", "Red", 12000, startingPosition);
        players[2] = new Player("Player3", "Green", 12000, startingPosition);
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
        tabPane.setStyle(" -fx-background-color: #cfcfcf;");
        Button btn = new Button("Close");
        btn.setLayoutX(750);
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
            if (players[0].position + x < 180)
                players[0].position += x;
            else 
                players[0].position += x - 180;
            showPlayerActions(players[0].position);
        }
        if (currentPlayer.name == "Player2") {
            if (players[1].position + x < 180)
                players[1].position += x;
            else 
                players[1].position += x - 180;
            showPlayerActions(players[1].position);
        }
        if (currentPlayer.name == "Player3") {
            if (players[2].position + x < 180)
                players[2].position += x;
            else 
                players[2].position += x - 180;
            showPlayerActions(players[2].position);
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
        buyplotbutton.setDisable(true);
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
        String str = "MAX PROFIT OF PLAYERS" +'\n' + '\n' + '\n' + "Player1 : " + players[0].maxProfit + '\n'
        + "Player2 : " + players[1].maxProfit + '\n'
        + "Player3 : " + players[2].maxProfit + '\n';
        Popup popup = new Popup();
        Label l = new Label();
        l.setStyle(" -fx-background-color: #f2f2f2;-fx-font-weight: bold;");
        Button btn = new Button("Close");
        btn.setLayoutX(180);
        btn.setLayoutY(270);
        popup.getContent().add(l); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        l.setMinWidth(400); 
        l.setMinHeight(300);
        l.setText(str);
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
        String str = "NUMBER OF ENTRANCES FOR EACH PLAYER" +'\n' + '\n' + '\n' + "Player1 : " + players[0].entrances.size() + '\n'
        + "Player2 : " + players[1].entrances.size() + '\n'
        + "Player3 : " + players[2].entrances.size() + '\n';
        Popup popup = new Popup();
        Label l = new Label();
        l.setStyle(" -fx-background-color: #f2f2f2;-fx-font-weight: bold;");
        Button btn = new Button("Close");
        btn.setLayoutX(180);
        btn.setLayoutY(270);
        popup.getContent().add(l); 
        popup.getContent().add(btn);
        popup.setAutoHide(true);
        l.setMinWidth(400); 
        l.setMinHeight(300);
        l.setText(str);
        popup.show(myStage);
        EventHandler<ActionEvent> closeevent =  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) {     
                popup.hide(); 
            } 
        };
        btn.setOnAction(closeevent);
    }
    private void showPlayerActions(int p) {
        switch(gameBoard.board[p]) {
            case "S" : break;
            case "C" : buyentrancebutton.setDisable(false);
                       break;
            case "B" : requestfrombankbutton.setDisable(false);
                       break;
            case "H" : buyplotbutton.setDisable(false);
                       break;
            case "E" : buyentrancebutton.setDisable(false);
                       requestbuildbutton.setDisable(false);
                       break;
            case "F" : break;
            default  : break;
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
    
    private void outputBoard() {
        String b = "";
        int m = 0;
        for (int i = 0; i < 180; i++) { 
            b += "      [" + gameBoard.board[i] + "]      ";
            m++;
            if (m == 15) {
                b += '\n';
                b += '\n';
                b += '\n';
                m = 0;
            }
        }
        b = b.substring(0, b.length() - 4);
        gameboard.setText(b);
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
        startingPosition = gameBoard.start;
        players[0] = new Player("Player1", "Blue", 12000, startingPosition);
        players[1] = new Player("Player2", "Red", 12000, startingPosition);
        players[2] = new Player("Player3", "Green", 12000, startingPosition);
        player1.setText("Player1 :" + players[0].credits);
        player2.setText("Player2 :" + players[1].credits);
        player3.setText("Player3 :" + players[2].credits);
        currentPlayer = players[0];
        showCurrentPlayer(currentPlayer);
        hotels[0] = new Hotel("FUJIYAMA", 100, 500, 100, 2200, 100, new int[]{1400,1400}, new int[]{100,200}, 500, 400);
        hotels[1] = new Hotel("L'ETOILE", 3000, 1500, 250, 3300, 150, new int[]{2200,1800,1800,1800}, new int[]{300,300,300,450}, 4000, 750);
        hotels[2] = new Hotel("ROYAL", 2500, 1250, 300, 3600, 150, new int[]{2600,1800,1800}, new int[]{300,300,450}, 3000, 600);
        hotels[3] = new Hotel("BIG HOUSE", 3200, 1600, 200, 1800, 200, new int[]{1500,1800}, new int[]{200,300}, 2000, 400);
        hotels[4] = new Hotel("EAGLES", 1800, 900, 250, 2000, 150, new int[]{1500}, new int[]{250}, 1200, 400);
        hotels[5] = new Hotel("AFRICA", 1500, 750, 100, 1600, 100, new int[]{1000,1000}, new int[]{150,200}, 1200, 300);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeGame();
    }    
    
}
