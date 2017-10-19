/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import monopoly.model.Game;
import monopoly.model.GameStateChange;
import monopoly.model.LandProperty;
import monopoly.model.Player;
import monopoly.model.Property;

/**
 *
 * @author ramy
 */
public class MonopolyCanvas extends Canvas 
{
    double width, height;
    Monopoly monopoly = null;
    
    Image houseImage = null;
    Image getHouseImage()
    {
        if(houseImage == null)
        {
            houseImage = new Image("house.png");
        }
        return houseImage;
    }
    
    Image hotelImage = null;
    Image getHotelImage()
    {
        if(hotelImage == null)
        {
            hotelImage = new Image("hotel.png");
        }
        return hotelImage;
    }
    
    Image mortgageImage = null;
    Image getMortgageImage()
    {
        if(mortgageImage == null)
        {
            mortgageImage = new Image("mortgage.png");
        }
        return mortgageImage;
    }
    
    ArrayList<Image> largeImages = null;
    ArrayList<Image> getLargeImages()
    {
        if(largeImages == null)
        {
            largeImages = new ArrayList<>();
            for(int i=1; i<=6;i++)
            {
                String largeImageFilename = "" + i + "-64.png";
                Image largeImage = new Image(largeImageFilename);
                largeImages.add(largeImage);
            }
        }
        return largeImages;
    }
    
    ArrayList<Image> smallImages = null;
    ArrayList<Image> getSmallImages()
    {
        if(smallImages == null)
        {
            smallImages = new ArrayList<>();
            for(int i=1; i<=6;i++)
            {
                String smallImageFilename = "" + i + "-32.png";
                Image smallImage = new Image(smallImageFilename);
                smallImages.add(smallImage);
            }
        }
        return smallImages;
    }
    
    double flipX(double x)
    {
        return width - x;
    }
    
    double flipY(double y)
    {
        return height - y;
    }
    
    private ArrayList<Rectangle> rectangles = null;
    private ArrayList<Rectangle> getRectangles()
    {
        if(rectangles == null)
        {
            double w = width / 18.5 * 1.5;
            double h = w / 1.5 * 2.5;
            rectangles = new ArrayList<>();
           
            rectangles.add(new Rectangle(flipX(h),flipY(h),h,h));
            
            for(int i=1; i<=9; i++) {
                double x = flipX(h + i*w);
                double y = flipY(h);
                Rectangle rect = new Rectangle(x,y,w,h);
                rectangles.add(rect);
            }
            
            rectangles.add(new Rectangle(0, flipY(h),h,h));
            
            for(int i=1; i<=9; i++) {
                double x = 0;
                double y = flipY(h + i*w);
                Rectangle rect = new Rectangle(x,y,h,w);
                rectangles.add(rect);
            }
            
            rectangles.add(new Rectangle(0,0,h,h));
            
            for(int i=1; i<=9; i++) {
                double x = h + (i-1)*w;
                double y = 0;
                Rectangle rect = new Rectangle(x,y,w,h);
                rectangles.add(rect);
            }
            
            rectangles.add(new Rectangle(flipX(h),0,h,h));
            
            for(int i=1; i<=9; i++) {
                double x = flipX(h);
                double y = h + (i-1)*w;
                Rectangle rect = new Rectangle(x,y,h,w);
                rectangles.add(rect);
            }
            
        }
        return rectangles;
    }
    
    public ArrayList<Rectangle> splitRect(Rectangle rect)
    {
        ArrayList<Rectangle> rects = new ArrayList<>();
        double x = rect.getX();
        double y = rect.getY();
        double w = rect.getWidth();
        double h = rect.getHeight();
        
        double size = 20.0;
        
        if(rect.getWidth() < rect.getHeight())
        {
            rects.add(new Rectangle(x + w / 4 - size / 2, y + h / 6 - size / 2, size, size ));
            rects.add(new Rectangle(x + w * 3/ 4 - size / 2, y + h / 6 - size / 2, size, size ));
            rects.add(new Rectangle(x + w / 4 - size / 2, y + h / 6 * 3 - size / 2, size, size ));
            rects.add(new Rectangle(x + w * 3/ 4 - size / 2, y + h / 6 * 3 - size / 2, size, size ));
            rects.add(new Rectangle(x + w / 4 - size / 2, y + h / 6 * 5 - size / 2, size, size ));
            rects.add(new Rectangle(x + w * 3 / 4 - size / 2, y + h / 6 * 5 - size / 2, size, size ));
        }
        else
        {
            rects.add(new Rectangle(x + w / 6 - size / 2, y + h / 4 - size / 2, size , size));
            rects.add(new Rectangle(x + w / 6 * 3 - size / 2, y + h / 4 - size / 2, size , size));
            rects.add(new Rectangle(x + w / 6 * 5 - size / 2, y + h / 4 - size / 2, size , size));
            rects.add(new Rectangle(x + w / 6 - size / 2, y + h / 4 * 3 - size / 2, size , size));
            rects.add(new Rectangle(x + w / 6 * 3 - size / 2, y + h / 4 * 3 - size / 2, size , size));
            rects.add(new Rectangle(x + w / 6 * 5 - size / 2, y + h / 4 * 3 - size / 2, size , size));
        }
        
        return rects;
    }
    
    public Rectangle[] getAreas(Rectangle rect, String direction) // "North", "West", "East", "South"
    // Return value: {walking area, building area}
    {
        Rectangle buildingArea = null;
        Rectangle walkingArea = null;
        double x = rect.getX();
        double y = rect.getY();
        double w = rect.getWidth();
        double h = rect.getHeight();
        double margin = 0.18;
        
        if(null == direction) {
        } else {
            switch (direction) {
                case "South":
                    buildingArea = new Rectangle(x, y, w, h * margin);
                    walkingArea = new Rectangle(x, y + h * margin, w, h * (1.0 - margin));
                    break;
                case "North":
                    buildingArea = new Rectangle(x, y + h * (1.0 - margin), w, h * margin);
                    walkingArea = new Rectangle(x, y, w, h * (1.0 - margin));
                    break;
                case "West":
                    buildingArea = new Rectangle(x + w * (1.0 - margin), y , w * margin, h);
                    walkingArea = new Rectangle(x, y, w * (1.0 - margin), h);
                    break;
                case "East":
                    buildingArea = new Rectangle(x, y, w * margin, h);
                    walkingArea = new Rectangle(x + w * margin, y, w * (1.0 - margin), h);
                    break;
                default:
                    break;
            }
            return new Rectangle[] {walkingArea, buildingArea};
        }
        
        return null;
    }
    
    int getNumberOfPlayers(int position)
    {
        int counter = 0;
        
        for(int i=0; i<Monopoly.game.players.size();i++)
        {
            Player player = Monopoly.game.players.get(i);
            if(player.position == position)
            {
                counter++;
            }
        }
        
        return counter;
    }
    
    void drawHouses()
    {
        if(monopoly == null)
        {
            return;
        }
        GraphicsContext gc = getGraphicsContext2D();
        for(int i=0; i<Monopoly.game.elements.size(); i++)
        {
            Rectangle totalRect = getRectangles().get(i);
            
            String direction;
            if(i < 10)
            {
                direction = "South";
            }
            else if(i<20)
            {
                direction = "West";
            }
            else if(i<30)
            {
                direction = "North";
            }
            else 
            {
                direction = "East";
            }
            
            Rectangle[] areas = getAreas(totalRect,direction);
            
            Rectangle buildingArea = areas[1];
            double x = buildingArea.getX();
            double y = buildingArea.getY();
            double w = buildingArea.getWidth();
            double h = buildingArea.getHeight();
                
            ArrayList<Rectangle> houseRects = new ArrayList<>();
                
            if("North".equals(direction) || "South".equals(direction))
            {
                for(int j = 0; j < 4; j++)
                {
                    Rectangle houseRect = new Rectangle(x + j * w / 4.0, y, w / 4.0, h);
                    houseRects.add(houseRect);
                }
            }
            else
            {
                for(int j = 0; j < 4; j++)
                {
                    Rectangle houseRect = new Rectangle(x, y + h / 4.0 * j, w , h / 4.0);
                    houseRects.add(houseRect);
                }
            }
            
            if(Monopoly.game.elements.get(i) instanceof Property)
            {
                Property prop = (Property) Monopoly.game.elements.get(i);
                // Stroke the rect?
                if(prop.owner != null)
                {
                    int index = Monopoly.game.players.indexOf(prop.owner);
                    
                    
                    if(index >= 0 && index <= Monopoly.game.players.size() - 1)
                    {
                        Color color = monopoly.getPlayerColors()[index];
                        gc.setLineWidth(2.0);
                        gc.setStroke(color);
                        gc.strokeRect(totalRect.getX(), totalRect.getY(), totalRect.getWidth(), totalRect.getHeight());
                    
                        if(prop.mortgaged)
                        {
                            gc.drawImage(getMortgageImage(), totalRect.getX() + totalRect.getWidth() / 2.0 - 28.0, totalRect.getY() + totalRect.getHeight() / 2.0 - 28.0, 56, 56);
                        }
                    }
                    else
                    {
                    }
                }
            }
            
            if (monopoly.game.elements.get(i) instanceof LandProperty)
            {
                LandProperty prop = (LandProperty) Monopoly.game.elements.get(i);
                
                // Draw the houses
                if(prop.getNumberOfHouses() < 5)
                {
                    for(int j=1; j<=4 && j <= prop.getNumberOfHouses(); j++)
                    {
                        Rectangle houseRect = houseRects.get(j-1);
                        gc.drawImage(getHouseImage(), houseRect.getX(), houseRect.getY(), houseRect.getWidth(), houseRect.getHeight());
                    }
                }
                else
                {
                    double hotelX = buildingArea.getX();
                    double hotelY = buildingArea.getY();
                    double hotelWidth = buildingArea.getWidth();
                    double hotelHeight = buildingArea.getHeight();
                    
                    if(hotelHeight > hotelWidth)
                    {
                        hotelY += hotelHeight / 4.0;
                        hotelHeight /= 2.0;
                    }
                    else
                    {
                        hotelX += hotelWidth / 4.0;
                        hotelWidth /= 2.0;
                    }
                    
                    gc.drawImage(getHotelImage(), hotelX, hotelY, hotelWidth, hotelHeight);
                }
            }
        }
    }
    
    void drawImages()
    {
        if(monopoly == null)
        {
            return;
        }
        
        int counter = 0;
        
        for(int i=0; i<Monopoly.game.players.size(); i++) 
        {
            Player player = Monopoly.game.players.get(i);
            if(player.isInGame)
            {
                GraphicsContext gc = getGraphicsContext2D();
                Rectangle totalRect = getRectangles().get(player.position);
            
                ArrayList<Rectangle> rects;
            
                if(player.position % 10 == 0)
                {
                    rects = splitRect(totalRect);
                }
                else 
                {
                    String direction;
                
                    if(player.position < 10)
                    {
                        direction = "South";
                    }
                    else if(player.position < 20)
                    {
                        direction = "West";
                    }
                    else if(player.position < 30)
                    {
                        direction = "North";
                    }
                    else 
                    {
                        direction = "East";
                    }
                
                
                    Rectangle[] areas = getAreas(totalRect, direction);
                    rects = splitRect(areas[0]);                

                }
            
                Rectangle rect = rects.get(counter);
                counter++;
            
                Image image = getSmallImages().get(i);
                
                gc.drawImage(image, rect.getX(), rect.getY(),rect.getWidth(), rect.getHeight());
            }
        }
    }
    
    public MonopolyCanvas(double width, double height) 
    {
        super(width,height);
        
        this.width = width;
        this.height = height;
        
        refresh();
        
        this.setOnMouseClicked((MouseEvent event) -> 
        {
            double x = event.getX();
            double y = event.getY();
            
            for(int i=0; i<getRectangles().size(); i++)
            {
                Rectangle rect = getRectangles().get(i);
                double rectW = rect.getWidth();
                double rectH = rect.getHeight();
                double rectX = rect.getX();
                double rectY = rect.getY();
                
                if(x >= rectX && y >= rectY && x <= rectX + rectW && y <= rectY + rectH)
                {
                    if(Monopoly.game.elements.get(i) instanceof Property)
                    {
                        Property p = (Property) Monopoly.game.elements.get(i);
                        
                        if(p.owner == Monopoly.player && Monopoly.game.getTurn() == Monopoly.self)
                        {
                            if(Monopoly.player.canUnmortgageProperty(p))
                            {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.getDialogPane().setPrefWidth(450);
                                alert.setTitle("Conferma Azione");
                                alert.setHeaderText("Proprietà: " + p.name);
                                alert.setContentText("Che azione vuoi eseguire?"); 
                                
                                ButtonType unmortgageButton = new ButtonType("Rimuovi Ipoteca");
                                ButtonType cancelButton = new ButtonType("Annulla");
                                
                                alert.getButtonTypes().setAll(unmortgageButton, cancelButton);
                                Optional<ButtonType> result = alert.showAndWait();
                                
                                if(result.get() == unmortgageButton)
                                {
                                    GameStateChange change = new GameStateChange(GameStateChange.Type.Unmortgage);
                                    change.player1Name = Monopoly.player.name;
                                    change.oldState = new Game(Monopoly.game);
                                    change.description = Monopoly.player.name + " ha rimosso l'ipoteca da " + p.name;
                                    
                                    Monopoly.player.unmortgageProperty(p);
                                    monopoly.log("Hai rimosso l'ipoteca da " + p.name + ".");
                                    monopoly.logNewline();
                                    if(monopoly.playerMoneyLabel != null)
                                    {
                                        monopoly.playerMoneyLabel.setText("Soldi: " + Monopoly.player.money + "€");
                                    }
                                    
                                    try 
                                    {
                                        Monopoly.game.serial++;
                                        change.newState = new Game(Monopoly.game);
                                        Monopoly.comm.sendGameStateChange(change,monopoly.getActivePlayersNames());
                                    } 
                                    catch (RemoteException ex) 
                                    {
                                        Logger.getLogger(MonopolyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    
                                    monopoly.refreshUI();
                                }
                            }
                            else
                            {
                                LandProperty prop = null;
                                if(p instanceof LandProperty)
                                {
                                    prop = (LandProperty) Monopoly.game.elements.get(i);
                                }
                                
                                if(prop == null || prop.getNumberOfHouses() == 0)
                                {
                                    // Si può ipotecare, oppure si può aggiungere una casa
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.getDialogPane().setPrefWidth(450);
                                    alert.setTitle("Conferma Azione");
                                    alert.setHeaderText("Proprietà: " + p.name);
                                    alert.setContentText("Che azione vuoi eseguire?");
                                    
                                    ButtonType mortgageButton = new ButtonType("Ipoteca");
                                    ButtonType buildButton = new ButtonType("Costruisci");
                                    ButtonType cancelButton = new ButtonType("Annulla");
                                    
                                    if(prop != null && Monopoly.player.canBuild(prop.color))
                                    {
                                        alert.getButtonTypes().setAll(mortgageButton, buildButton, cancelButton);
                                    }
                                    else
                                    {
                                        alert.getButtonTypes().setAll(mortgageButton, cancelButton);
                                    }
                                    
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if(result.get() == mortgageButton)
                                    {
                                        
                                        GameStateChange change = new GameStateChange(GameStateChange.Type.Mortgage);
                                        change.player1Name = Monopoly.player.name;
                                        change.oldState = new Game(Monopoly.game);
                                        change.description = Monopoly.player.name + " ha ipotecato " + p.name;
                                        
                                        Monopoly.player.mortgageProperty(p);
                                        monopoly.log("Hai ipotecato " + p.name + ".");
                                        monopoly.logNewline();
                                        if(monopoly.playerMoneyLabel != null)
                                        {
                                            monopoly.playerMoneyLabel.setText("Soldi: " + Monopoly.player.money + "€");
                                        }
                                        
                                        try 
                                        {
                                            Monopoly.game.serial++;
                                            change.newState = new Game(Monopoly.game);
                                            Monopoly.comm.sendGameStateChange(change,monopoly.getActivePlayersNames());
                                        } 
                                        catch (RemoteException ex) 
                                        {
                                            Logger.getLogger(MonopolyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                        monopoly.refreshUI();
                                    }
                                    else if(result.get() == buildButton)
                                    {
                                        GameStateChange change = new GameStateChange(GameStateChange.Type.Build);
                                        change.player1Name = Monopoly.player.name;
                                        change.oldState = new Game(Monopoly.game);
                                        change.description = Monopoly.player.name + " ha costruito una casa su " + p.name;
                                        
                                        Monopoly.player.build(prop.color);
                                        monopoly.log("Hai costruito su " + p.name + ".");
                                        monopoly.logNewline();
                                        if(monopoly.playerMoneyLabel != null)
                                        {
                                            monopoly.playerMoneyLabel.setText("Soldi: " + monopoly.player.money + "€");
                                        }
                                        
                                        try 
                                        {
                                            Monopoly.game.serial++;
                                            change.newState = new Game(Monopoly.game);
                                            Monopoly.comm.sendGameStateChange(change,monopoly.getActivePlayersNames());
                                        } 
                                        catch (RemoteException ex) 
                                        {
                                            Logger.getLogger(MonopolyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                        monopoly.refreshUI();
                                    }
                                }
                                else if(prop != null && prop.getNumberOfHouses() > 0)
                                {
                                    // Si può aggiungere o togliere una casa
                                    // Si può ipotecare, oppure si può aggiungere una casa
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.getDialogPane().setPrefWidth(450);
                                    alert.setTitle("Conferma Azione");
                                    alert.setHeaderText("Proprietà: " + prop.name);
                                    alert.setContentText("Che azione vuoi eseguire?");
                                    
                                    ButtonType deconstructButton = new ButtonType("Rimuovi una Casa");
                                    ButtonType buildButton = new ButtonType("Costruisci");
                                    ButtonType cancelButton = new ButtonType("Annulla");
                                    
                                    if(Monopoly.player.canBuild(prop.color))
                                    {
                                        alert.getButtonTypes().setAll(deconstructButton, buildButton, cancelButton);
                                    }
                                    else if(Monopoly.player.canDeconstruct(prop.color))
                                    {
                                        alert.getButtonTypes().setAll(deconstructButton, cancelButton);
                                    }
                                    
                                    
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if(result.get() == deconstructButton)
                                    {
                                        GameStateChange change = new GameStateChange(GameStateChange.Type.Deconstruct);
                                        change.player1Name = Monopoly.player.name;
                                        change.oldState = new Game(Monopoly.game);
                                        change.description = Monopoly.player.name + " ha tolto una casa da " + p.name;
                                        
                                        Monopoly.player.deconstruct(prop.color);
                                        monopoly.log("Hai tolto una casa da " + prop.name);
                                        monopoly.logNewline();
                                        if(monopoly.playerMoneyLabel != null)
                                        {
                                            monopoly.playerMoneyLabel.setText("Soldi: " + Monopoly.player.money + "€");
                                        }
                                        
                                        try 
                                        {
                                            Monopoly.game.serial++;
                                            change.newState = new Game(Monopoly.game);
                                            Monopoly.comm.sendGameStateChange(change, monopoly.getActivePlayersNames());
                                        } 
                                        catch (RemoteException ex) 
                                        {
                                            Logger.getLogger(MonopolyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                        monopoly.refreshUI();
                                    }
                                    else if(result.get() == buildButton)
                                    {
                                        GameStateChange change = new GameStateChange(GameStateChange.Type.Build);
                                        change.player1Name = Monopoly.player.name;
                                        change.oldState = new Game(Monopoly.game);
                                        change.description = Monopoly.player.name + " ha costruito una casa su " + p.name;
                                        
                                        Monopoly.player.build(prop.color);
                                        monopoly.log("Hai costruito su " + prop.name);
                                        monopoly.logNewline();
                                        if(monopoly.playerMoneyLabel != null)
                                        {
                                            monopoly.playerMoneyLabel.setText("Soldi: " + Monopoly.player.money + "€");
                                        }
                                        
                                        try 
                                        {
                                            Monopoly.game.serial++;
                                            change.newState = new Game(Monopoly.game);
                                            Monopoly.comm.sendGameStateChange(change, monopoly.getActivePlayersNames());
                                        } 
                                        catch (RemoteException ex) 
                                        {
                                            Logger.getLogger(MonopolyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                        monopoly.refreshUI();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    public void refresh()
    {
        // activePlayer va evidenziato (è clickabile)
        // Disegna le immagini delle pedine (riempie l'array allPlayerImages)
        Image tableImage = new Image("tabellone.png",width,height,false,false);
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(tableImage,0,0);
        drawHouses();
        drawImages();
    }

}
