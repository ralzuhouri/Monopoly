/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author giacomosantarelli
 */
public class StationSlotView extends SlotView{
    
    public String title;
    public int revenue[];
    public int price;
    
    public StationSlotView(String title,int revenue[],int price){
        this.title = title;
        this.revenue = revenue;
        this.price = price;
    }
    
    public void draw(GraphicsContext gc){
        
        // Image
        Image station = new Image("station.gif");
        double width = rect.getWidth() * 0.4;
        double height = rect.getHeight() * 0.3;
        gc.drawImage(station, rect.getX() + rect.getWidth() * 0.5 - width / 2.0, rect.getY() + rect.getHeight() * 0.15, width,height);          
       
        // title        
        Font font = Font.font("Arial", FontWeight.BOLD, rect.getHeight()/15);
        gc.setFill(Color.BLACK);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(title, rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.10);
        
        // Revenue
        Font font1 = Font.font("Arial", FontWeight.THIN,rect.getHeight()/25);
        gc.setFont(font1);       
        //Font font2 = Font.font("Arial", FontWeight.LIGHT,rect.getHeight()/rect.getHeight()/20);
        
        for(int i=0;i<revenue.length;i++){
        
            gc.fillText("Se si possiedono "+(i+1)+" Stazioni:    "+revenue[i]+"€", rect.getX() + rect.getWidth() / 2.0, (rect.getY() + rect.getHeight() * 0.5)+ i * rect.getHeight() * 0.10);                   
        }
        
        // Price
        gc.setFont(font1);
        gc.fillText("Valore ipotecario " + price + " €", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.9);
        
        
        gc.setLineWidth(3);
        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    
    
    
}

