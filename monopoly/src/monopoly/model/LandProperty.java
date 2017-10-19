/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.util.ArrayList;

/**
 *
 * @author ramy
 */
public class LandProperty extends Property {
    public final String color;
    public static final String BROWN = "BROWN";
    public static final String BLUE = "BLUE";
    public static final String PINK = "PINK";
    public static final String ORANGE = "ORANGE";
    public static final String RED = "RED";
    public static final String YELLOW = "YELLOW";
    public static final String GREEN = "GREEN";
    public static final String PURPLE = "PURPLE";
    public static final ArrayList<String> getAllColors() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add(BROWN);
        colors.add(BLUE);
        colors.add(PINK);
        colors.add(ORANGE);
        colors.add(RED);
        colors.add(YELLOW);
        colors.add(GREEN);
        colors.add(PURPLE);
        return colors;
    }
    // Revenues for a given number of houses (0-5)
    public final int housePrice;
    public final int[] revenues;
    public int getRevenue(int numberOfHouses) {
        return revenues[numberOfHouses];
    }
    
    // Houses
    private int numberOfHouses = 0;
    public int getNumberOfHouses() {
        return numberOfHouses;
    }
    public void setNumberOfHouses(int numberOfHouses) throws IllegalArgumentException {
        if(numberOfHouses >= 0 && numberOfHouses <= 5) {
            this.numberOfHouses = numberOfHouses;
        } else {
            String msg = "'numberOfHouses' argument must be inside the interval [0,5]";
            throw new IllegalArgumentException(msg);  
        }
    }

    public LandProperty(String name, int value, String color, int[] revenues, int housePrice) throws IllegalArgumentException {
        super(name, value);
        this.housePrice = housePrice;
        this.color = color;
        if(revenues.length != 6) {
            String msg = "'revenues' argument must be an array of 6 values";
            throw new IllegalArgumentException(msg);
        }
        this.revenues = revenues.clone();
    }
    
    @Override
    public String land(Player player, Game game) {
        String price = null;
        if(owner != null) {
            if(owner != player) {
                int revenue = getRevenue(getNumberOfHouses());
                player.money -= revenue;
                owner.money += revenue;
                price = " pedaggio: " + revenue + "€";
            }
        }
        else
        {
            player.buyableProperty = this;
        }
        
        String desc = "Sei atterrato su " + name;
        if(price != null) {
            desc += price;
        }
        desc += ".";
        
        return desc;
    }
    
}
