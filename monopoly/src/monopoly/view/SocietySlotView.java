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
public class SocietySlotView extends SlotView{
    
    public String title;
    public int price;
    public int revenues[];
    
    public SocietySlotView(String title,int price,int revenues[]){
        this.title = title;
        this.price = price;
        this.revenues = revenues;
        
    }
    
    public void draw(GraphicsContext gc){
        
        // Name
        Font font = Font.font("Arial", FontWeight.BOLD, rect.getHeight()/15);
        gc.setFill(Color.BLACK);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(title, rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.10);
        
        
        
        // Image
        if(title == "Società Elettrica"){
            //System.out.print(gc);
            Image eSociety = new Image("esociety.png");
            double width = rect.getWidth() * 0.4;
            double height = rect.getHeight() * 0.3;
            gc.drawImage(eSociety, rect.getX() + rect.getWidth() * 0.5 - width / 2.0, rect.getY() + rect.getHeight() * 0.25, width,height);
        }
        else{
            Image wSociety = new Image("wsociety.png");
            double width = rect.getWidth() * 0.4;
            double height = rect.getHeight() * 0.3;
            gc.drawImage(wSociety, rect.getX() + rect.getWidth() * 0.5 - width / 2.0, rect.getY() + rect.getHeight() * 0.25, width,height);
            
        }
        
        // Text
        Font font1 = Font.font("Arial", FontWeight.THIN,rect.getHeight()/20);
        gc.setFill(Color.BLACK);
        gc.setFont(font1);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Rendita con una società: "+revenues[0]+"\n Rendita con due società: "+revenues[1], rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.70);
        
           
        
        // Price
        //Font font2 = Font.font("Arial", FontWeight.LIGHT,rect.getHeight()/rect.getHeight()/20);
        gc.setFont(font1);
        gc.fillText("Valore ipotecario "+price/2 + "€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.9);
            
        gc.setLineWidth(3);
        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    
}
