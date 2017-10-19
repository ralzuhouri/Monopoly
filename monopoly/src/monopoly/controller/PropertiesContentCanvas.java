/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.controller;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.BROWN;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.ORANGE;
import static javafx.scene.paint.Color.PINK;
import static javafx.scene.paint.Color.PURPLE;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;
import javafx.scene.shape.Rectangle;
import monopoly.model.LandProperty;
import monopoly.model.Property;
import monopoly.model.Society;
import monopoly.model.Station;
import monopoly.view.PropertySlotView;
import monopoly.view.SocietySlotView;
import monopoly.view.StationSlotView;

/**
 *
 * @author giacomosantarelli
 */
public class PropertiesContentCanvas extends Canvas {
    
    
    ArrayList<Property> properties = null;
    void setProperties(ArrayList<Property> properties) {
        
        for(int i=0; i<3;i++)
        {
            for(int j=0; j<10;j++)
            {
                cardSelected[i][j] = false;
            }
        }
        
        this.properties = properties;
    }
   
    
    Rectangle[][] rect= new Rectangle[3][10];
    double[][] coordX = new double[3][10];
    double[][] coordY = new double[3][10];
    boolean cardSelected[][] = new boolean [3][10];
    Image check = new Image("check.png");
   
    public ArrayList<Property> getAllSelectedProperties()
    {
        ArrayList<Property> selectedProps = new ArrayList<>();
        
        for(int i=0; i<properties.size();i++)
        {
            if(cardSelected[i%3][i/3])
            {
                selectedProps.add(properties.get(i));
            }
        }
        
        return selectedProps;
    }

    ScrollPane tradePane;
    //PropertySlotView card[][]=new PropertySlotView[5][10];
    double width, height;
    
    public PropertiesContentCanvas(double width, double height){
        super(width,height);
        this.width = width;
        this.height = height;
    }
    
    void drawProperties()
    {
        if(properties == null) {
            return;
        }
        
        GraphicsContext gc = getGraphicsContext2D();
        
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, width, height);
        
        for(int y=0;y<10;y++){
            double offsety = y*(height/10)+ 15;
            //System.out.println(y);
            for(int x=0;x<3;x++){  
                
                /******* Parte modificata   ******/
                int sequenceIndex = x + y*3;
                if(sequenceIndex < properties.size())
                {
                    Property p = properties.get(sequenceIndex);
                    
                    if(p instanceof LandProperty)
                    {
                        LandProperty landProperty = (LandProperty)p;
                        
                        double offsetx = x*(width/3) + 15;
                        rect[x][y]= new Rectangle(offsetx,offsety,150,200);
                        
                        coordX[x][y] = offsetx;
                        coordY[x][y] = offsety;
                        
                        Color color = null;
                        switch(landProperty.color)
                        {
                            case "BROWN":
                                color = BROWN;
                                break;
                            case "BLUE":
                                color = BLUE;
                                break;
                            case "PINK":
                                color = PINK;
                                break;
                            case "ORANGE":
                                color = ORANGE;
                                break;
                            case "RED":
                                color = RED;
                                break;
                            case "YELLOW":
                                color = YELLOW;
                                break;
                            case "GREEN":
                                color = GREEN;
                                break;
                            case "PURPLE":
                                color = PURPLE;
                                break;
                        }
                        
                        //System.out.println(landProperty.name);
                        int revenueProperty[] = new int[] {0,0,0,0,0,0,0,0};
                        for(int i=0; i<landProperty.revenues.length;i++)
                        {
                            //System.out.println(landProperty.revenues[i]);
                            revenueProperty[i] = landProperty.revenues[i];
                        }
                        revenueProperty[6] = landProperty.value / 2;
                        revenueProperty[7] = landProperty.housePrice;
                        
                        PropertySlotView card = new PropertySlotView(landProperty.name,color,revenueProperty);
                        card.rect=rect[x][y];
                        
                        gc.setFill(WHITE);
                        gc.fillRect(coordX[x][y], coordY[x][y], 150, 200);
                        card.draw(gc);
                        
                        if(cardSelected[x][y]){
                            gc.drawImage(check, coordX[x][y],coordY[x][y],64,64);
                        }
   
                    }
                    else if(p instanceof Station){
                        Station station = (Station)p;
                        
                        double offsetx = x*(width/3) + 15;
                        rect[x][y]= new Rectangle(offsetx,offsety,150,200);
                        
                        coordX[x][y] = offsetx;
                        coordY[x][y] = offsety;
                        
                        StationSlotView sCard = new StationSlotView(station.name,station.revenues,station.value);
                        sCard.rect=rect[x][y];
                        
                        gc.setFill(WHITE);
                        gc.fillRect(coordX[x][y], coordY[x][y], 150, 200);
                        sCard.draw(gc);
                        
                        if(cardSelected[x][y]){
                            gc.drawImage(check, coordX[x][y],coordY[x][y],64,64);
                        }
                    }
                    else if(p instanceof Society){
                        Society society = (Society)p;
                        
                        double offsetx = x*(width/3) + 15;
                        rect[x][y] = new Rectangle(offsetx,offsety,150,200);
                        
                        coordX[x][y] = offsetx;
                        coordY[x][y] = offsety;
                        
                        SocietySlotView soCard = new SocietySlotView(society.name,society.value,society.revenues);
                        soCard.rect = rect[x][y];
                        
                        gc.setFill(WHITE);
                        gc.fillRect(coordX[x][y], coordY[x][y], 150, 200);
                        soCard.draw(gc);
                        
                        if(cardSelected[x][y]){
                            gc.drawImage(check, coordX[x][y],coordY[x][y],64,64);
                        }
                        
                    }
                }
                else 
                {
                    break;
                }
                /***************************/
                       
            }

        }
        
        /*
        
        this.setOnMouseClicked(event ->{
            double x = event.getX();
            //System.out.println(x);
            double y = event.getY();
            //System.out.println(y);
            
            for(int i=0;i<3;i++){
                for(int j=0;j<10;j++){
                    
                    int index = j*3 + i;
                     
                     
                    if(index < properties.size())
                    {
                        if((coordX[i][j]<x && x<coordX[i][j]+150) &&(coordY[i][j]<y && y<coordY[i][j]+200))
                        {
                            if(cardSelected[i][j]){ 
                                System.out.println("Deseleziona rettangolo x:"+coordX[i][j]+" y:"+coordY[i][j]);
                                cardSelected[i][j] = false;
                                drawProperties();
                            }
                            else
                            {
                                boolean canBuild = true;
                                Property p = properties.get(i + j*3);
                                if(p instanceof LandProperty)
                                {
                                    LandProperty prop = (LandProperty)p;
                                    if(prop.getNumberOfHouses() > 0)
                                    {
                                        canBuild = false;
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Azione non Disponibile");
                                        alert.setHeaderText("Non puoi scambiare una proprietà su cui hai costruito");
                                        alert.showAndWait();
                                    }
                                }
                                
                                if(canBuild)
                                {
                                    System.out.println("Seleziona rettangolo x:"+coordX[i][j]+" y:"+coordY[i][j]);
                                    cardSelected[i][j] = true;
                                    drawProperties();
                                }
                            }
                        }
                    }
                }
            }
            
        });*/
    }
    
}
