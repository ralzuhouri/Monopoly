/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import monopoly.model.Player;

/**
 *
 * @author giacomosantarelli
 */
public class PropertiesGroup extends Group {
    
    Monopoly monopoly = null;
    public Player otherPlayer = null;
    PropertiesContentCanvas tradeCanvas1 = null;
    PropertiesContentCanvas tradeCanvas2 = null;
    
    // Giacomo - elementi grafici
    ScrollPane tradePane1; //trade proprie carte
    ScrollPane tradePane2; //trade carte avversario
    ComboBox<String> comboPlayer; //comboBox per scegliere il giocatore sulla schermata Affari
    Label labelMoney1; //Label relativa ai soldi del player corrente
    Label labelMoney2; //label relativa ai soldi del player avversario con cui fare affari
    Button acceptButton; //button che accetta l'affare
    TextField tradeMoney1;
    TextField tradeMoney2;
    Alert waitingAlert = null;
 
    void setTradeCanvas1(PropertiesContentCanvas tradeCanvas1) {
        this.tradeCanvas1 = tradeCanvas1;
    }
    
    void setTradeCanvas2(PropertiesContentCanvas tradeCanvas2) {
        this.tradeCanvas2 = tradeCanvas2;
    }
    
    //controllo se è un intero
    public boolean isInteger( String input ) {
            try {
               Integer.parseInt( input );
                return true;
            }
            catch( Exception e ) {
                return false;
            }
    }
    
    
    public PropertiesGroup(Monopoly monopoly)
    {
        this.monopoly = monopoly;
        
        // Trading scroll pane 1
        tradePane1 = new ScrollPane();
        tradePane1.setLayoutX(5);
        tradePane1.setLayoutY(50);
        tradePane1.setPrefSize(590,600);
        PropertiesContentCanvas contentCanvas1 = new PropertiesContentCanvas(590,3000);
        tradePane1.setContent(contentCanvas1);
        
        // Trading scroll pane 2
        tradePane2 = new ScrollPane();
        tradePane2.setLayoutX(605);
        tradePane2.setLayoutY(50);
        tradePane2.setPrefSize(590, 600);
        PropertiesContentCanvas contentCanvas2 = new PropertiesContentCanvas(590,3000);
        tradePane2.setContent(contentCanvas2);
        
        
        // Combo Box Scelta Player
        comboPlayer = new ComboBox<String>();
        comboPlayer.setLayoutX(605);
        comboPlayer.setLayoutY(15);
        comboPlayer.setPrefSize(150,20);
        
        boolean f = true;
        
        for(int i=0; i<monopoly.game.players.size();i++){
            Player p = monopoly.game.players.get(i);
            if(p.name != monopoly.player.name)
            {
                comboPlayer.getItems().add(p.name);
                if (f){
                    comboPlayer.setValue(monopoly.game.players.get(i).name);
                    f = false;
                    otherPlayer = monopoly.game.players.get(i);
                }
            }
        }
        
        comboPlayer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
        {
            public void changed(ObservableValue ov, Object oldPlayer, Object newPlayer) 
            {
                otherPlayer = monopoly.game.getPlayer((String)newPlayer);
                tradeCanvas2.setProperties(otherPlayer.properties);
                tradeCanvas2.drawProperties();
            }
        });   
        
        // Label 1
        labelMoney1 = new Label();
        labelMoney1.setLayoutX(5);
        labelMoney1.setLayoutY(660);
        labelMoney1.setText("Totale Soldi: "+ monopoly.player.money+"€");
        labelMoney1.setFont(new Font(20));
        
        // Label 2
        labelMoney2 = new Label();
        labelMoney2.setLayoutX(605);
        labelMoney2.setLayoutY(660);
        labelMoney2.setText("Totale Soldi: "+ otherPlayer.money+"€");
        labelMoney2.setFont(new Font(20));
        
        // Accept button
        //acceptButton = new Button("Conferma affare");
        //acceptButton.setLayoutX(1000);
        //acceptButton.setLayoutY(675);
        
        
        //Trade Money1 TextField
        //tradeMoney1 = new TextField();
        //tradeMoney1.setLayoutX(5);
        //tradeMoney1.setLayoutY(695);
        //tradeMoney1.setPrefSize(150,20);
        //tradeMoney1.setText("0");
        
        //Trade Money2 TextField
        //tradeMoney2 = new TextField();
        //tradeMoney2.setLayoutX(605);
        //tradeMoney2.setLayoutY(695);
        //tradeMoney2.setPrefSize(150,20);
        //tradeMoney2.setText("0");

        tradeCanvas1 = contentCanvas1;
        tradeCanvas2 = contentCanvas2;
        
        
        
        contentCanvas1.setProperties(monopoly.player.properties);
        contentCanvas2.setProperties(otherPlayer.properties);
        contentCanvas1.drawProperties();
        contentCanvas2.drawProperties();
        
        this.getChildren().add(tradePane1);
        this.getChildren().add(tradePane2);
        this.getChildren().add(comboPlayer);
        this.getChildren().add(labelMoney1);
        this.getChildren().add(labelMoney2);
        //this.getChildren().add(acceptButton);
        //this.getChildren().add(tradeMoney1);
        //this.getChildren().add(tradeMoney2);
      
    }
    
    public void refresh()
    {
        for(int i=0; i<monopoly.game.players.size();i++)
        {
            if(i != Monopoly.self) 
            {
                Player other = monopoly.game.players.get(i);
                if(other.isInGame) 
                {
                    otherPlayer = other;
                    comboPlayer.setValue(otherPlayer.name);
                    break;
                }
            }
        }
        
        tradeCanvas1.setProperties(monopoly.player.properties);
        tradeCanvas1.drawProperties();
                       
        tradeCanvas2.setProperties(otherPlayer.properties);
        tradeCanvas2.drawProperties();        
    }
        
}
