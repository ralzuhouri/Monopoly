/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

/**
 *
 * @author ramy
 */
public class Station extends Property{
    
    // Revenues for a given number of stations (1-4)
    public final int[] revenues;
    public int getRevenue(int numberOfStations) {
        return revenues[numberOfStations-1];
    }
    
    public Station(String name, int value, int[] revenues) throws IllegalArgumentException {
        super(name, value);
        if(revenues.length != 4) {
            String msg = "'revenues' argument must be an array of 4 values";
            throw new IllegalArgumentException(msg);
        }
        this.revenues = revenues;
    }
    
    @Override
    public String land(Player player, Game game) {
        String price = null;
        if(owner != null) {
            if(owner != player) {
                int revenue = getRevenue(owner.numberOfStations());
                player.money -= revenue;
                owner.money += revenue;
                price = "pedaggio: " + revenue + "€";
            }
        }
        else
        {
            player.buyableProperty = this;
        }
        
        String desc = "Sei atterrato su " + name;
        if(price != null) {
            desc += " " + price;
        }
        desc += ".";
        return desc;
    }
}
