/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ramy
 */
public class PropertySlotView extends SlotView {
    public String name;
    public Color color;
    public int revenue[];
    
    
    public PropertySlotView(String name, Color color,int revenue[]) {
        this.name = name;
        this.color = color;
        this.revenue = revenue;
    }
    
    public void draw(GraphicsContext gc) {
        // Draw the upper part
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.5);
        gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight() / 5.0);
        gc.strokeLine(rect.getX(), rect.getY() + rect.getHeight() / 5.0, rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() / 5.0);
        
        double basicFontSize = Math.min(rect.getWidth(), rect.getHeight()) / 10.0;
        
        // Name
        Font font = Font.font("Arial", FontWeight.BOLD, basicFontSize);
        gc.setFill(Color.BLACK);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(name, rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.32);
        
        //revenue
        Font font1 = Font.font("Arial", FontWeight.THIN,rect.getHeight()/25);
        Font font2 = Font.font("Arial", FontWeight.BOLD,rect.getHeight()/25);
        gc.setFont(font2);       
        //Font font2 = Font.font("Arial", FontWeight.LIGHT,rect.getHeight()/rect.getHeight()/20);
        
        gc.fillText("RENDITA solo terreno "+revenue[0]+"€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.45);
        
        gc.setFont(font1);
        for(int i=1;i<=4;i++){
        
            gc.fillText("Con "+i+" case:    "+revenue[i]+"€", rect.getX() + rect.getWidth() / 2.0, (rect.getY() + rect.getHeight() * 0.5)+ i * rect.getHeight() * 0.06);                   
        }
        
        gc.fillText("Con albergo:    "+revenue[5]+"€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.80);
        gc.setFont(font2);
        gc.fillText("Valore ipotecario "+revenue[6]+"€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.90);
        gc.setFont(font1);
        gc.fillText("Costo di ogni casa "+revenue[7]+"€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.95);
        //gc.fillText("Costo albergo + 4 case "+revenue[7]+"€", rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() * 0.95);
        
        gc.setLineWidth(3);
        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
}
