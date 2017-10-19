/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ramy
 */
public final class Game implements Serializable 
{    
    public static final long serialVersionUID = 45476467247565754L;
    
    public long serial = 0;
    
    // A certain number of properties are randomly assigned to each player
    public void assignRandomProperties(int numberOfPropertiesPerPlayer) throws IllegalArgumentException
    {
        ArrayList<Property> availableProperties = new ArrayList<Property>(this.getAllProperties());
        
        if(properties.size() < numberOfPropertiesPerPlayer * players.size())
        {
            throw new IllegalArgumentException("You're trying to assign more properties than the total available");
        }
        
        Random random = new Random();
        for(Player player:players)
        {
            for(int i=0; i<numberOfPropertiesPerPlayer; i++)
            {
                int index = random.nextInt(availableProperties.size());
                Property property = availableProperties.get(index);
                player.addProperty(property);
                availableProperties.remove(index);
            }
        }
    }
    
    public void assignRandomProperties()
    {
        ArrayList<String> colors = new ArrayList<>();
        colors.add(LandProperty.BLUE);
        colors.add(LandProperty.BROWN);
        colors.add(LandProperty.GREEN);
        colors.add(LandProperty.ORANGE);
        colors.add(LandProperty.PINK);
        colors.add(LandProperty.PURPLE);
        colors.add(LandProperty.RED);
        colors.add(LandProperty.YELLOW);
        
        ArrayList<ArrayList<LandProperty>> groupedLandProperties = new ArrayList<>();
        for(int i=0; i<colors.size(); i++) {
            groupedLandProperties.add(new ArrayList<LandProperty>());
        }
        
        for(Property p:this.getAllProperties())
        {
            if(p instanceof LandProperty)
            {
                LandProperty landP = (LandProperty)p;
                int index = colors.indexOf(landP.color);
                groupedLandProperties.get(index).add(landP);
            }
        }
        
        Random r = new Random();
        for(Player p:players) {
            int index = r.nextInt(groupedLandProperties.size());
            ArrayList<LandProperty> landProperties = groupedLandProperties.get(index);
            for(LandProperty landP:landProperties) {
                p.addProperty(landP);
            }
            groupedLandProperties.remove(index);
        }
    }
    
    public boolean isGameOver()
    {
        int counter = 0;
        for(Player p:players) {
            if(p.isInGame) {
                counter++;
            }
        }
        return counter < 2;
    }
    
    public Player getWinner()
    {
        if(isGameOver()) 
        {
            for(Player p:players) {
                if(p.isInGame) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public boolean canPlayerPlay = true;
    
    public ArrayList<Element> elements = null;
    public ArrayDeque<GameStateChange> changes = new ArrayDeque<>();
    public GameStateChange popStateChange() {
        return changes.pop();
    }
    
    public ArrayList<Property> properties = null;
    public ArrayList<Property> getAllProperties() 
    {
        if(properties == null)
        {
            properties = new ArrayList<>();
            for(int i=0; i<elements.size();i++) {
                if(elements.get(i) instanceof Property) {
                    Property p = (Property) elements.get(i);
                    properties.add(p);
                }
            }
        }
        return properties;
    }
    
    public Property getPropertyByName(String name)
    {
        for(Property p : getAllProperties())
        {
            if(p.name.equals(name))
            {
                return p;
            }
        }
        return null;
    }
    
    public ArrayList<Player> players = new ArrayList<>();
    
    public void unlockTurn()
    {
        if(!players.get(turn).isInGame && !isGameOver()) 
        {
            canPlayerPlay = true;
            do
            {
                turn++;
                turn = turn % players.size();
            }while(!getPlayer(turn).isInGame);
        }
    }
    
    int turn = 0;
    public int getTurn()
    {
        return turn;
    }
    
    public Player getPlayer()
    {
        return players.get(turn);
    }
    
    public Player getPlayer(int index)
    {
        return players.get(index);
    }
    
    public Player getPlayerByName(String name) 
    {
        for(Player player:players) {
            if(player.name == name) {
                return player;
            }
        }
        return null;
    }
    
    public int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
    
    // Ritorna true se il turno è finito (il turno non finisce se il giocatore fa un doppio)
    public TurnOutcome playTurn() 
    {        
        if(canPlayerPlay)
        {
            boolean finished = true;
            Player player = getPlayer();
            int n1 = rollDice();
            int n2 = rollDice();
            canPlayerPlay = false;
            
            GameStateChange change = new GameStateChange(GameStateChange.Type.Move);
            change.oldState = new Game(this);
            change.player1Name = player.name;
            
            player.buyableProperty = null;
        
            if(player.inPrison) 
            {
                if(n1 == n2) {
                    player.inPrison = false;
                    player.prisonTurnsCounter = 0;
                    change.description = player.name + " ha ottenuto " + (n1+n2) + " (doppio): esce di prigione.";
                } else {
                    player.prisonTurnsCounter++;
                    if(player.prisonTurnsCounter != 3) 
                    {
                        canPlayerPlay = false;
                        this.serial++;
                        change.newState = new Game(this);
                        change.description = player.name + " ha ottenuto " + (n1+n2) + ": rimane in prigione.";
                        changes.add(change);
                        return new TurnOutcome(n1+n2, true, n1 == n2, "Resti in prigione.",false);
                    } 
                    else {
                        player.inPrison = false;
                        player.prisonTurnsCounter = 0;
                        change.description = player.name + " esce di prigione e lancia i dadi. Risultato: " + (n1+n2) + ".";
                    }
                }
            } else if(n1 == n2) {
                player.doubleCounter++;
                finished = false;
                if(player.doubleCounter == 3) 
                {
                    player.inPrison = true;
                    player.position = 10;
                    player.doubleCounter = 0;
                    change.description = player.name + " ha ottenuto doppio per la terza volta: va in prigione.";
                    this.serial++;
                    change.newState = new Game(this);
                    changes.add(change);
                    return new TurnOutcome(n1+n2, true, n1 == n2, "Hai fatto doppio: vai in prigione.",false);
                }
                else
                {
                    canPlayerPlay = true;
                }
            }
            else
            {
                change.description = player.name + " ha ottenuto " + (n1+n2) + ".";
            }
        
            player.position+= n1 + n2;
            boolean passedGo = false;
            
            if(player.position > 39) {
                player.position = player.position % 40;
                player.money += 200;
                change.description += "Passa dal via e riscuote 200€.";
                passedGo = true;
            }
            
            Element element = elements.get(player.position);
            String description = element.land(player, this);
        
            if(finished) {
                player.doubleCounter = 0;
            }
        
            if(change.description != null) {
                change.description += " " + description;
            }
            else{
                change.description = description;
            }
            this.serial++;
            change.newState = new Game(this);
            changes.add(change);
            return new TurnOutcome(n1+n2, finished, n1 == n2, description, passedGo);
        }
        return null;
    }
    
    public boolean canBankrupt()
    {
        return true;
    }
    
    public void bankrupt()
    {
        removePlayer(turn);
    }
    
    public void removePlayer(int index)
    {
        Player p = this.players.get(index);
        
        for(Property prop:p.properties)
        {
            prop.owner = null;
            prop.mortgaged = false;
            
            if(prop instanceof LandProperty) {
                LandProperty landP = (LandProperty) prop;
                landP.setNumberOfHouses(0);
            }
        }
        
        p.properties = new ArrayList<>();
        p.isInGame = false;
    }
    
    public boolean pass()
    {
        if(isGameOver() || canPlayerPlay) {
            return false;
        }
        
        Player player = getPlayer();
        if(player.money < 0)
        {
            return false;
        }
        
        GameStateChange change = new GameStateChange(GameStateChange.Type.Pass);
        change.oldState = new Game(this);
        change.player1Name = player.name;
        
        
        do
        {
            turn++;
            turn = turn % players.size();
        }while(!getPlayer(turn).isInGame);
        
        
        player.buyableProperty = null;
        canPlayerPlay = true;
        
        this.serial++;
        change.newState = new Game(this);
        change.description = player.name + " ha passato il turno.";
        changes.add(change);
        
        return true;
    }
    
    public int getIndexOfPlayer(String playerName)
    {   
        for(int i=0; i<players.size(); i++)
        {
            Player player = players.get(i);
            if(player.name.equals(playerName))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    public Player getPlayer(String name) {
        return getPlayerByName(name);
    }
    
    public Game(String ... playerNames) throws IllegalArgumentException
    {
        if(playerNames.length >= 2)
        {
            initializeElements();
            for (String playerName : playerNames) 
            {
                Player player = new Player(playerName);
                this.players.add(player);
            }
        }
        else
        {
            throw new IllegalArgumentException("A game must have at least two players");
        }
    }
    
    public Game(Game other)
    {
        super();
        initializeElements();
        this.turn = other.turn;
        this.serial = other.serial;
        this.canPlayerPlay = other.canPlayerPlay;
        
        for(Player player : other.players)
        {
            Player playerCopy = new Player(player.name);
            playerCopy.isInGame = player.isInGame;
            playerCopy.position = player.position;
            playerCopy.money = player.money;
            playerCopy.inPrison = player.inPrison;
            playerCopy.prisonCards = player.prisonCards;
            playerCopy.doubleCounter = player.doubleCounter;
            playerCopy.prisonTurnsCounter = player.prisonTurnsCounter;
            
            if(player.buyableProperty != null)
            {
                playerCopy.buyableProperty = getPropertyByName(player.buyableProperty.name);
            }
            
            for(Property property : player.properties)
            {
                Property propertyCopy = getPropertyByName(property.name);
                propertyCopy.mortgaged = property.mortgaged;
                playerCopy.addProperty(propertyCopy);
                
                if(property instanceof LandProperty)
                {
                    int n = ((LandProperty)property).getNumberOfHouses();
                    ((LandProperty)propertyCopy).setNumberOfHouses(n);
                }
            }
            
            this.players.add(playerCopy);
        }
        
    }
    
    public void makeTrade(Trade trade)
    {
        GameStateChange change = new GameStateChange(GameStateChange.Type.TradeAccepted);
        change.oldState = new Game(this);
        change.player1Name = trade.getPlayer1Name();
        change.player2Name = trade.getPlayer2Name();
        
        for(int i=0; i<trade.getPlayer1PropertyNames().size();i++) 
        {
            Property p = getPropertyByName(trade.getPlayer1PropertyNames().get(i));
                    
            this.getPlayer(trade.getPlayer1Name()).properties.remove(p);
            this.getPlayer(trade.getPlayer2Name()).properties.add(p);
            p.owner = this.getPlayer(trade.getPlayer2Name());
        }
        
        for(int i=0; i<trade.getPlayer2PropertyNames().size(); i++) 
        {
            Property p = getPropertyByName(trade.getPlayer2PropertyNames().get(i));
            this.getPlayer(trade.getPlayer2Name()).properties.remove(p);
            this.getPlayer(trade.getPlayer1Name()).properties.add(p);
            p.owner = this.getPlayer(trade.getPlayer1Name());
        }
        
        this.getPlayer(trade.getPlayer1Name()).money += trade.getPlayer2Money();
        this.getPlayer(trade.getPlayer1Name()).money -= trade.getPlayer1Money();
        
        this.getPlayer(trade.getPlayer2Name()).money += trade.getPlayer1Money();
        this.getPlayer(trade.getPlayer2Name()).money -= trade.getPlayer2Money();
        
        change.description = trade.getDescription();
        this.serial++;
        change.newState = new Game(this);
        changes.add(change);
    }
    
    private void initializeElements()
    {    
        elements = new ArrayList<>();
        
        // Fill the elements array
        int[] revenues=new int[6];
        int[] Srevenues=new int[4];
        Srevenues[0]=25;
        Srevenues[1]=50;
        Srevenues[2]=100;
        Srevenues[3]=200;
        int[] socRevenues= new int[] {50,100};
        
        GameAction go = new GameAction("VIA!", (player,game) -> {});
        elements.add(new CustomElement(go));
                
        revenues[0]=2;
        revenues[1]=10;
        revenues[2]=30;
        revenues[3]=90;
        revenues[4]=160;
        revenues[5]=250;
        elements.add(new LandProperty("Vicolo Corto", 60, "BROWN", revenues,50));
                
        elements.add(new CommunityChest());
                
        revenues[0]=4;
        revenues[1]=20;
        revenues[2]=60;
        revenues[3]=180;
        revenues[4]=320;
        revenues[5]=450;
        elements.add(new LandProperty("Vicolo Stretto", 60, "BROWN", revenues,50));
                
        GameAction tax = new GameAction("Tassa patrimoniale: 400€.", (player,game) -> {
            player.money-= 200;
        });
        elements.add(new CustomElement(tax));
                
        elements.add(new Station("Stazione Est", 200, Srevenues));
                
        revenues[0]=6;
        revenues[1]=30;
        revenues[2]=90;
        revenues[3]=270;
        revenues[4]=400;
        revenues[5]=550;
        elements.add(new LandProperty("Bastioni Gran Sasso", 100, "BLUE", revenues,100));
                
        elements.add(new Chances());
                
        elements.add(new LandProperty("Viale Monterosa", 100, "BLUE", revenues,100));
                
        revenues[0]=8;
        revenues[1]=40;
        revenues[2]=100;
        revenues[3]=300;
        revenues[4]=450;
        revenues[5]=600;
        elements.add(new LandProperty("Viale Vesuvio", 120, "BLUE", revenues,100));
                
        GameAction visiting = new GameAction("Transito", (player,game) -> {});
        elements.add(new CustomElement(visiting));
                
        revenues[0]=10;
        revenues[1]=50;
        revenues[2]=150;
        revenues[3]=450;
        revenues[4]=625;
        revenues[5]=750;
        elements.add(new LandProperty("Via Accademia", 140, "PINK", revenues,100));
                
        elements.add(new Society("Società Elettrica",150,socRevenues));

        elements.add(new LandProperty("Corso Ateneo", 140, "PINK", revenues,100));
                
        revenues[0]=12;
        revenues[1]=60;
        revenues[2]=180;
        revenues[3]=500;
        revenues[4]=700;
        revenues[5]=900;
        elements.add(new LandProperty("Piazza Università", 160, "PINK", revenues,100));
                
        elements.add(new Station("Stazione Sud", 200, Srevenues));
                
        revenues[0]=14;
        revenues[1]=70;
        revenues[2]=200;
        revenues[3]=550;
        revenues[4]=750;
        revenues[5]=950;
        elements.add(new LandProperty("Via Verdi", 180, "ORANGE", revenues,100));
                
        elements.add(new CommunityChest());
                
        elements.add(new LandProperty("Corso Rafaello", 180, "ORANGE", revenues,100));
                
        revenues[0]=16;
        revenues[1]=80;
        revenues[2]=220;
        revenues[3]=600;
        revenues[4]=800;
        revenues[5]=1000;
        elements.add(new LandProperty("Piazza Dante", 200, "ORANGE", revenues,100));
                
        GameAction parking = new GameAction("Posteggio Gratuito.", (player,game) -> {});
        elements.add(new CustomElement(parking));
                
        revenues[0]=18;
        revenues[1]=90;
        revenues[2]=250;
        revenues[3]=700;
        revenues[4]=875;
        revenues[5]=1050;
        elements.add(new LandProperty("Via Marco Polo", 220, "RED", revenues,150));
                
        elements.add(new Chances());

        elements.add(new LandProperty("Corso Magellano", 220, "RED", revenues,150));
                
        revenues[0]=20;
        revenues[1]=100;
        revenues[2]=300;
        revenues[3]=750;
        revenues[4]=925;
        revenues[5]=1100;
        elements.add(new LandProperty("Largo Colombo", 240, "RED", revenues,150));
                
        elements.add(new Station("Stazione Ovest", 200, Srevenues));
                
        revenues[0]=22;
        revenues[1]=110;
        revenues[2]=330;
        revenues[3]=800;
        revenues[4]=975;
        revenues[5]=1150;
        
        elements.add(new LandProperty("Viale Costantino", 260, "YELLOW", revenues,150));
                
        elements.add(new LandProperty("Viale Traiano", 260, "YELLOW", revenues,150));
                
        elements.add(new Society("Società Acqua Potabile",150,socRevenues));
                
        revenues[0]=24;
        revenues[1]=120;
        revenues[2]=360;
        revenues[3]=850;
        revenues[4]=1025;
        revenues[5]=1200;
        elements.add(new LandProperty("Piazza Giulio Cesare", 280, "YELLOW", revenues,150));
                
        GameAction goToJail = new GameAction("In Prigione!", (player,game) -> {
            player.inPrison = true;
            player.position = 10;
        });
        elements.add(new CustomElement(goToJail));
                
        revenues[0]=26;
        revenues[1]=130;
        revenues[2]=390;
        revenues[3]=900;
        revenues[4]=1100;
        revenues[5]=1275;
        elements.add(new LandProperty("Via Roma", 300, "GREEN", revenues,200));
                
        elements.add(new LandProperty("Corso Impero", 300, "GREEN", revenues,200));
                
        elements.add(new CommunityChest());
                
        revenues[0]=28;
        revenues[1]=150;
        revenues[2]=450;
        revenues[3]=1000;
        revenues[4]=1200;
        revenues[5]=1400;
        elements.add(new LandProperty("Largo Augusto", 320, "GREEN", revenues,200));
                
        elements.add(new Station("Stazione Nord", 200, Srevenues));
                
        elements.add(new Chances());
                
        revenues[0]=35;
        revenues[1]=175;
        revenues[2]=500;
        revenues[3]=1100;
        revenues[4]=1300;
        revenues[5]=1500;
        elements.add(new LandProperty("Viale dei Giardini", 350, "PURPLE", revenues,200));
        
        
        GameAction tax2 = new GameAction("Tassa di lusso: 200€.", (player,game) -> {
            player.money-= 100;
        });
        elements.add(new CustomElement(tax2));
                
        revenues[0]=50;
        revenues[1]=200;
        revenues[2]=600;
        revenues[3]=1400;
        revenues[4]=1700;
        revenues[5]=2000;
        elements.add(new LandProperty("Parco della Vittoria", 400, "PURPLE", revenues,200));      
    }
    
}

