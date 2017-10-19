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
// Probabilità
public class CommunityChest implements Element, Serializable {
    
    private final ArrayList<GameAction> actions = new ArrayList<> ();
    public static final long serialVersionUID = 45476417247563754L;
    
    public CommunityChest() {
        // Fill the actions array
        
        // Example: 
        actions.add(new GameAction("Vinci 500€.", (player,game) -> {
            player.money += 500;
        }));
        
        actions.add(new GameAction("Paghi il conto del medico.Perdi 20€.", (player,game) -> {
            player.money -= 50;
        }));
        
        actions.add(new GameAction("Vai direttamente in prigione senza pasare per la uscita e senza raccogliere 200€.", (player,game) -> {
            player.position=10;
            player.inPrison=true;
        }));
        
        actions.add(new GameAction("Andate avanti fino al VIA! e ritirate 200€.", (player,game) -> {
            player.position=0;
            player.money += 200;
        }));
        
        actions.add(new GameAction("Andate fino a Vicolo Corto: Se passate dal VIA!, ritirate 200€.", (player,game) -> {
            if (player.position>1) player.money+=200;
            player.position=1;
            Element elem = game.elements.get(player.position);
            elem.land(player, game);
        }));
        
        actions.add(new GameAction("Vinci 100€.", (player,game) -> {
            player.money+=100;
        }));
        
        actions.add(new GameAction("Perdi 50€.", (player,game) -> {
            player.money-=50;
        }));
        
        actions.add(new GameAction("Perdi 40€ per ogni casa e 115€ per ogni albergo che possedete.", (player,game) -> {
            int value;
            for(int i=0;i<player.properties.size();i++){
                if(player.properties.get(i) instanceof LandProperty){
                    value=((LandProperty)player.properties.get(i)).getNumberOfHouses();
                    if(value>0&&value<=4) player.money-=40*value;
                    else if(value==5) player.money-=115;
                }     
            }
        }));
        
        actions.add(new GameAction("Vai a la uscita.", (player,game) -> {
            player.position=0;
        }));
        
        actions.add(new GameAction("Otteni una carta per uscire di prigione.", (player,game) -> {
            player.prisonCards++;
        }));
        
        actions.add(new GameAction("Vinci 10€.", (player,game) -> {
            player.money+=10;
        }));
        
        actions.add(new GameAction("Vinci 50€.", (player,game) -> {
            player.money+=50;
        }));
        
        actions.add(new GameAction("Perdi 10€.", (player,game) -> {
            player.money-=10;
        }));
        
        actions.add(new GameAction("Vinci 10€ per ogni giocatore.", (player,game) -> {
            player.money+=10*game.players.size();
            Player g;
            for(int i=0;i<game.players.size();i++){
               g=game.players.get(i);
               if(g!=player) g.money-=10;
            }
        }));
        
        actions.add(new GameAction("Vinci 200€", (player,game) -> {
            player.money+=200;
        }));
        
        actions.add(new GameAction("Vinci 20€", (player,game) -> {
            player.money+=20;
        }));
        
        actions.add(new GameAction("Vinci 25€", (player,game) -> {
            player.money+=25;
        }));
        
        actions.add(new GameAction("Perdi 100€", (player,game) -> {
            player.money-=100;
        }));
    }
    
    public String getName() {
        return "Probabilità";
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
