/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ramy
 */
// Imprevisti
public class Chances implements Element, Serializable {
    private final ArrayList<GameAction> actions = new ArrayList<> ();
    public static final long serialVersionUID = 454764156584963754L;
    
    public Chances() {
        // Fill the actions array
        /* Example: 
        actions.add(new GameAction("Vinci 500€", (player,game) -> {
            player.money += 500;
        }));
        */
        
        actions.add(new GameAction("Otteni una carta per uscire di prigione.", (player,game) -> {
            player.prisonCards++;
        }));
        
        actions.add(new GameAction("Perdi 25€ per ogni casa e 100€ per ogni albergo che possedete.", (player,game) -> {
            int value;
            for(int i=0;i<player.properties.size();i++){
                if(player.properties.get(i) instanceof LandProperty){
                    value=((LandProperty)player.properties.get(i)).getNumberOfHouses();
                    if(value>0&&value<=4) player.money-=25*value;
                    else if(value==5) player.money-=100;
                }     
            }
        }));
        
        actions.add(new GameAction("Maturano le cedole dei vostri fondi immobiliari: Incassate 150€.", (player,game) -> {
            player.money+=150;
        }));
        
        actions.add(new GameAction("Andate in prigione direttamente e senza passare dal VIA!.", (player,game) -> {
            player.position=10;
            player.inPrison=true;
        }));
        
        actions.add(new GameAction("La banca vi paga un dividendo di 50€.", (player,game) -> {
            player.money+=50;
        }));
        
        actions.add(new GameAction("Andate alla Stazione Nord: Se passate dal VIA! ritirate 200€.", (player,game) -> {
            if (player.position>25) player.money+=200;
            player.position=35;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
        actions.add(new GameAction("Fate 3 passi indietro (con tanti auguri!).", (player,game) -> {
            player.position-=3;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
        /*actions.add(new GameAction("Avanzate fino alla stazione ferroviaria più vicina. \nSe è libera, potete acquisitarla dalla banca. \nSe è posseduta da un altro giocatore, pagate al proprietario il doppio dell'affitto che gli spetta normalmente", (player,game) -> {
            if (player.position<5||player.position>35) player.position=5;
            else if(player.position<15) player.position=15;
            else if(player.position<25) player.position=25;
            else player.position=35;
        
            
        }));*/
        actions.add(new GameAction("Andate fino a Largo Colombo: Se passate dal VIA! ritirate 200€.", (player,game) -> {
            if (player.position>24) player.money+=200;
            player.position=24;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
        actions.add(new GameAction("Andate avanti fino al VIA! e ritirate 200€.", (player,game) -> {
            player.position=0;
            player.money += 200;
        }));
        
        actions.add(new GameAction("Multa per eccesso di velocità: Pagate 15€.", (player,game) -> {
            player.money -= 15;
        }));
        
        actions.add(new GameAction("Siete stati promossi alla presidenza del consiglio di amministrazione: \nPagate 50€ ad ogni giocatore.", (player, game) -> {
            player.money-=50*game.players.size();
            Player g;
            for(int i=0;i<game.players.size();i++){
               g=game.players.get(i);
               if(g!=player) g.money+=50;
            }
        }));
        /*actions.add(new GameAction("Avanzate fino alla società più vicina. \nSe è libera, potete acquisitarla dalla banca. \nSe è posseduta da un altro giocatore, pagate al proprietario il doppio dell'affitto che gli spetta normalmente", (player,game) -> {
            if (player.position<12||player.position>28) player.position=12;
            else player.position=28;  
        }));*/
        
        actions.add(new GameAction("Andate fino al Parco della Vittoria: Se passate dal VIA! ritirate 200€.", (player,game) -> {
            if (player.position>39) player.money+=200;
            player.position=39;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
        actions.add(new GameAction("Andate fino a Corso Ateneo: Se passate dal VIA! ritirate 200€.", (player,game) -> {
            if (player.position>13) player.money+=200;
            player.position=13;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
    }
    
    public String getName() {
        return "Imprevisti";
    }
    
    @Override
    public String land(Player player, Game game) {
        Random random = new Random();
        int n = random.nextInt(actions.size());
        GameAction action = actions.get(n);
        action.invoke(player, game);
        return action.description;
    }
}
