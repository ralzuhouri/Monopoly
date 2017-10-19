/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.controller;

import Communication.Communicator;
import Communication.CommunicatorDelegate;
import Communication.Message;
import Communication.Node;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import monopoly.model.Game;
import monopoly.model.Player;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import monopoly.model.GameStateChange;
import monopoly.model.TurnOutcome;

/**
 *
 * @author ramy
 */
public class Monopoly extends Application implements CommunicatorDelegate 
{
    public static Player player = null;
    public static Game game = null;
    
    // Graphic elements
    Group root;
    PropertiesGroup propertiesGroup = null;
    ScrollPane logPane;
    Button dice;
    Button pass;
    Button buyPropertyButton;
    Button propertiesButton;
    Button exitFromPrisonButton;
    Button bankruptButton;
    public MonopolyCanvas canvas;
    Label playerMoneyLabel;
    Label gameOverLabel;
    static Communicator comm;
    static int self;
    ArrayList<Label> playerLabels = new ArrayList<>();
    
    static private Color[] playerColors = null;
    static public Color[] getPlayerColors()
    {
        if(playerColors == null)
        {
            playerColors = new Color[]{Color.BLUE, Color.RED, Color.BROWN, Color.GREEN, Color.ORANGE, Color.PURPLE};
        }
        return playerColors;
    }
    
    public ArrayList<String> getActivePlayersNames()
    {
        ArrayList<String> names = new ArrayList<>();
        intersectPlayersAndNodes();
        for(int i=0; i<game.players.size(); i++) {
            Player p = game.players.get(i);
            if(p.isInGame || i == self) {
                names.add(p.name);
            }
        }
        return names;
    }
    
    @Override
    public void playersDisconnected(ArrayList<String> playerNames) {
        println("players disconnected");
        println(playerNames.toString());
        intersectPlayersAndNodes();
        refreshUI();
    }
    
    @Override
    public void failedToDeliver(Message msg)
    {
        if(game.players.size() > 1)
        {
            Message msgCopy = new Message(msg);
            removeNode(msgCopy.receiver);
            String playerName = game.players.get(msgCopy.receiver).name;
            
            this.intersectPlayersAndNodes();
            
            if(game.isGameOver())
                return;
        
            try 
            {
                msgCopy.hops = 0;
                msgCopy.activePlayers = getActivePlayersNames();
                comm.send(msgCopy);
            } 
            catch (RemoteException ex) 
            {
                Logger.getLogger(Monopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void receiveChange(GameStateChange change)
    {
        System.err.println("Receive game state change with serial: " + change.newState.serial);
        if(change.type == GameStateChange.Type.Bankrupt)
        {
            if(!change.player1Name.equals(player.name) && change.newState.serial > game.serial)
            {   
                int index = -1;
                for(int i=0;i<game.players.size();i++) {
                    Player p = game.players.get(i);
                    if(p.name.equals(change.player1Name)) {
                        index = i;
                    }
                }
                
                removeNode(index);
                
                game = new Game(change.newState);
                player = game.players.get(self);
                
                log(change.description);
                this.intersectPlayersAndNodes();
                refreshUI();
            }
        }    
        else
        {
            println("Received change of type " + change.type);
            if(!change.player1Name.equals(player.name) && change.newState.serial > game.serial)
            {
                if(change.newState != null)
                {
                    println("Applying state change");
                    game = new Game(change.newState);
                    player = game.players.get(self);
                    log(change.description);
                    this.intersectPlayersAndNodes();
                    refreshUI();
                }
            }
        }
    }
    
    void sendChanges() 
    {
        println("Inside sendChanges()");
        try
        {
            println("Sto per inviare i cambiamenti");
            while(game.changes.size() > 0) 
            {
                println("Invio i cambiamenti");
                comm.sendGameStateChange(game.popStateChange(), getActivePlayersNames());
            }
        }
        catch(RemoteException e)
        {
            println("Error sending changes: " + e);
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException 
    {
        
        /************************* RMI ***************************/
        comm.delegate = this;
        
        /************************* Inizializzazione gioco **********************/
        
        player = game.players.get(comm.self);
        
        /*for(int i=0; i<game.elements.size(); i++)
        {
            if(game.elements.get(i) instanceof Property)
            {
                Property p = (Property) game.elements.get(i);
                int index = i % 1;//game.players.size();
                game.players.get(index).properties.add(p);
                p.owner = game.players.get(index);
            }
        }*/
        
        /************************* Inizializzazione grafica **********************/
        primaryStage.setTitle("Monopoly!!");

        root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        
        // Monopoli canvas view
        canvas = new MonopolyCanvas(700, 700);
        canvas.monopoly = this;
        root.getChildren().add(canvas);
        
        // Pulsante per comprare le proprietà
        buyPropertyButton = new Button("Compra Proprietà");
        buyPropertyButton.setLayoutX(715);
        buyPropertyButton.setLayoutY(672);
        buyPropertyButton.setOnAction((ActionEvent event) -> 
        {
            GameStateChange change = new GameStateChange(GameStateChange.Type.BuyProperty);
            change.player1Name = player.name;
            change.oldState = new Game(game);
            change.description = player.name + " ha comprato " + player.buyableProperty.name;
            
            String text = player.buyProperty();
            
            try 
            {
                game.serial++;
                change.newState = new Game(game);
                comm.sendGameStateChange(change,getActivePlayersNames());
            } 
            catch (RemoteException ex) 
            {
                Logger.getLogger(Monopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            refreshUI(); 
            log(text);
            logNewline();
        });
        root.getChildren().add(buyPropertyButton);
        
        // Logging scroll pane
        logPane = new ScrollPane();
        logPane.setLayoutX(720);
        logPane.setLayoutY(20);
        logPane.setPrefSize(350, 400);
        Text text = new Text("");
        text.setWrappingWidth(350);
        text.setFont(new Font(20));
        logPane.setContent(text);
        root.getChildren().add(logPane);
        
        // Money label
        playerMoneyLabel = new Label();
        playerMoneyLabel.setTextFill(getPlayerColors()[self]);
        playerMoneyLabel.setText(player.name + ": " + player.money + "€");
        playerMoneyLabel.setLayoutX(1090);
        playerMoneyLabel.setLayoutY(25);
        root.getChildren().add(playerMoneyLabel);
        
        // Dice button
        dice = new Button("Lancia i dadi");
        dice.setLayoutX(1090);
        dice.setLayoutY(60);
        boolean enabled = game.getPlayer() == player;
        
        /************************* Dice button handler ****************************/
        dice.setOnAction((ActionEvent event) -> 
        {
            TurnOutcome outcome = game.playTurn();
            String logText = "Hai lanciato i dadi. Risultato ottenuto: " + outcome.getDiceResult();
            if(outcome.isDouble()) 
            {
                logText += " (doppio)";
            }
            logText += ".";
            
            if(outcome.passedGo() == true) {
                logText += " Passi dal via e riscuoti 200€.";
            }
            
            log(logText);
            
            
            log(outcome.getDescription());
            
            refreshUI(); 
            sendChanges();
            
            logPane.setVvalue(1.0);
            logNewline();
        });
        root.getChildren().add(dice);
        
        // Pass button
        pass = new Button("Passa");
        pass.setLayoutX(1090);
        pass.setLayoutY(100);
        pass.setOnAction((ActionEvent event) -> 
        {
            if(game.pass()) 
            {
                log("Hai passato il turno.");
            } 
            else 
            {
                log("Non puoi passare il turno.");
            }
            refreshUI();
            
            println("Trying to send game state change");
            
            if(comm != null) 
            {
                sendChanges();
            }
            
        });
        root.getChildren().add(pass);
        
        // Bottone per gli scambi
        /*propertiesButton = new Button("Proprietà");
        root.getChildren().add(propertiesButton);
        propertiesButton.setLayoutX(1090);
        propertiesButton.setLayoutY(140);
        
        propertiesButton.setOnAction((ActionEvent event) -> {
            Stage s = new Stage();
            
            s.setWidth(1200);
            s.setHeight(800);
            
            propertiesGroup = new PropertiesGroup(this);
            Scene sceneTrade = new Scene(propertiesGroup);
            s.setScene(sceneTrade);
            
            s.show();
        });*/
        
        
        exitFromPrisonButton = new Button("Esci di prigione");
        root.getChildren().add(exitFromPrisonButton);
        exitFromPrisonButton.setLayoutX(1090);
        exitFromPrisonButton.setLayoutY(140);
        
        exitFromPrisonButton.setOnAction((ActionEvent event) -> 
        {    
            GameStateChange change = new GameStateChange(GameStateChange.Type.ExitFromPrison);
            change.player1Name = player.name;
            change.oldState = new Game(game);
            change.description = player.name + " è uscito di prigione";
            
            player.exitFromPrison();
            
            try 
            {
                game.serial++;
                change.newState = new Game(game);
                comm.sendGameStateChange(change,getActivePlayersNames());
                
                log("Sei uscito di prigione.");
            } catch (RemoteException ex) 
            {
                Logger.getLogger(Monopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            refreshUI();
        });
        
        bankruptButton = new Button("Bancarotta");
        root.getChildren().add(bankruptButton);
        bankruptButton.setLayoutX(1090);
        bankruptButton.setLayoutY(180);
        
        bankruptButton.setOnAction((ActionEvent event) -> 
        {
            GameStateChange change = new GameStateChange(GameStateChange.Type.Bankrupt);
            change.player1Name = player.name;
            change.oldState = new Game(game);
            change.description = player.name + " ha fatto bancarotta!";
            
            game.bankrupt();
            
            try 
            {
                game.serial++;
                change.newState = new Game(game);
                System.err.println("Sending bankrupt game state change");
                comm.sendGameStateChange(change,getActivePlayersNames());
            } 
            catch (RemoteException ex) 
            {
                Logger.getLogger(Monopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            /************ BISOGNA FAR TERMINARE IL SERVER E DISCONNETTERSI DAL NODO NEXT **************/
            log("Hai fatto bancarotta!");
            logNewline();
            refreshUI();
        });
        
        Button crashButton = new Button("Disconnetti");
        root.getChildren().add(crashButton);
        crashButton.setLayoutX(1090);
        crashButton.setLayoutY(220);
        crashButton.setOnAction((ActionEvent event) -> 
        {
            Runtime.getRuntime().exit(0);
        });
        
        // Money label
        /*playerMoneyLabel = new Label();
        playerMoneyLabel.setTextFill(getPlayerColors()[self]);
        playerMoneyLabel.setText(player.name + ": " + player.money + "€");
        playerMoneyLabel.setLayoutX(1090);
        playerMoneyLabel.setLayoutY(25);
        root.getChildren().add(playerMoneyLabel);*/
        gameOverLabel = new Label();
        Font font = new Font(24.0);
        gameOverLabel.setFont(font);
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setLayoutX(800);
        gameOverLabel.setLayoutY(500);
        gameOverLabel.setVisible(false);
        root.getChildren().add(gameOverLabel);
        
        
        for(int i=0; i<game.players.size(); i++)
        {
            Player player = game.players.get(i);
            Label label = new Label();
            playerLabels.add(label);
            
            label.setText(player.name);
            label.setTextFill(getPlayerColors()[i]);
            label.setLayoutY(430);
            label.setLayoutX(720 + i * 75);
            
            root.getChildren().add(label);
        }
        
        refreshUI();
        
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event) {
                Runtime.getRuntime().exit(0);
            }
        });
        
        if(self == 0) 
        {
            try 
            {
                GameStateChange change = new GameStateChange(GameStateChange.Type.Manage);
                change.player1Name = player.name;
                change.oldState = new Game(game);
                
                game.assignRandomProperties();   
                
                game.serial++;
                change.newState = new Game(game);
                change.description = "Le proprietà sono state assegnate in maniera casuale ai giocatori";
                comm.sendGameStateChange(change, this.getActivePlayersNames());
                canvas.refresh();
            } 
            catch (RemoteException ex) 
            {
                Logger.getLogger(Monopoly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void refreshUI() 
    {
        game.unlockTurn();
        playerMoneyLabel.setText(player.name + ": " + player.money + "€");
        this.canvas.refresh();
        
        if(game.getWinner() != null) 
        {
            gameOverLabel.setVisible(true);
            gameOverLabel.setText("Game Over. Vincitore: " + game.getWinner().name);
        }
        
        if(game.getPlayer() != player || game.isGameOver())
        {
            dice.setDisable(true);
            pass.setDisable(true);
            buyPropertyButton.setDisable(true);
            //propertiesButton.setDisable(true);
            exitFromPrisonButton.setDisable(true);
            bankruptButton.setDisable(true);
        }
        else
        {
            dice.setDisable(!game.canPlayerPlay);
            pass.setDisable(game.canPlayerPlay);
            buyPropertyButton.setDisable(!player.canBuyProperty());
            //propertiesButton.setDisable(false);
            
            
            boolean canExitFromPrison = player.canExitFromPrison() && game.canPlayerPlay && game.getTurn() == self;
            exitFromPrisonButton.setDisable(!canExitFromPrison);
            
            bankruptButton.setDisable(!game.canBankrupt());
        }
        
        for(int i=0; i<game.players.size(); i++) {
            if(!game.players.get(i).isInGame) {
                Label label = playerLabels.get(i);
                label.setTextFill(Color.gray(0.7));
            }
        }
    }
    
    public void log(String newText) 
    {
        boolean isMainThread = Thread.currentThread().getId() == 1;
        Text text = (Text) logPane.getContent();
        String newTextValue = text.getText() + newText;
        if(newText.length() > 0) {
            newTextValue += "\n";
        }
        text.setText(newTextValue);
    }
    
    public void logNewline()
    {
        log("------------------------------------");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException 
    {  
        self = Integer.parseInt(args[0]);
        ArrayList<Node> nodes = new ArrayList<>();
        
        for(int i = 1; i < args.length-1; i += 2)
        {
            String name = args[i];
            String ip = args[i+1];
            Node node = new Node(name,ip, 1100);
            nodes.add(node);
        }
        
        ArrayList<String> playerNames = new ArrayList<>();
        for(Node n:nodes) {
            playerNames.add(n.name);
        }
        
        game = new Game(playerNames.toArray(new String[playerNames.size()]));
        player = game.players.get(self);
        
        try
        {
            System.err.println("Self: " + self);
            comm = new Communicator(nodes, self);
            Thread.sleep(2500);
            comm.connectServer();
        }
        catch(RemoteException e)
        {
            System.err.println(e);
        }
        
        launch(args);
    }  
    
    // Deve essere chiamato prima di rimuovere il giocatore da game
    public void removeNode(int index)
    {       
        /*********************************** BISOGNA DISCONNETTERSI PRIMA ***************************/
        comm.nodes.get(index).isActive = false;
            
        if(comm.numberOfActiveNodes() > 1) 
        {
            comm.connectServer();
        }
    }
    
    public void intersectPlayersAndNodes()
    {
        boolean needToRefreshUI = false;
        for(int i=0; i<comm.nodes.size();i++)
        {
            Node node = comm.getNode(i);
            Player player = game.getPlayer(i);
            
            boolean isUp = node.isActive && player.isInGame;
            if(isUp == false && player.isInGame == true)
            {
                game.removePlayer(i);
                println(player.name + " removed from game");
                log(player.name + " si è disconnesso dal gioco");
                logNewline();
                needToRefreshUI = true;
            }
            
            player.isInGame = isUp;
            node.isActive = isUp;
        }
        
        if(needToRefreshUI) {
            game.unlockTurn();
            refreshUI();
        }
    }
    
    public void println(String str)
    {
        System.err.println(player.name + "> " + str);
    }
}
