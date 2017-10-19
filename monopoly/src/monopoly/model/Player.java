/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ramy
 */
public class Player implements Serializable {
    
    public static final long serialVersionUID = 454764672476422L;
            
    public final String name;
    public int position = 0;
    public int money = 1500;
    public ArrayList<Property> properties = new ArrayList<>();
    public boolean isInGame = true;

    public Player() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public ArrayList<LandProperty> getLandProperties(String color)
    {
        ArrayList<LandProperty> landProperties = new ArrayList<>(); 
        for(int i=0; i<properties.size(); i++) {
            Property p = properties.get(i);
            if (p instanceof LandProperty) {
                LandProperty lp = (LandProperty)p;
                if(lp.color.equals(color)) {
                    landProperties.add(lp);
                }
            }
        }
        return landProperties;
    }
    
    public void addProperty(Property p)
    {
        p.owner = this;
        properties.add(p);
    }
    
    public boolean inPrison;
    public int prisonCards=0;
    public Property buyableProperty = null;
    public int doubleCounter = 0;
    public int prisonTurnsCounter = 0;
    
    public Player(String name) {
        this.name = name;
    }
    
    public int numberOfSocieties() {
        int n =0;
        for(int i=0; i<properties.size();i++) {
            Property p= properties.get(i);
            if(p instanceof Society) {
                n++;
            }
        }
        return n;
    }
    
    public int numberOfStations() {
        int n =0;
        for(int i=0; i<properties.size();i++) {
            Property p= properties.get(i);
            if(p instanceof Station) {
                n++;
            }
        }
        return n;
    }
    
    public boolean canBuyProperty()
    {
        return buyableProperty != null && buyableProperty.value <= money;
    }
    
    public String buyProperty() 
    {
        if(canBuyProperty())
        {
            money -= buyableProperty.value;
            addProperty(buyableProperty);
            String text = "Properietà comprata: " + buyableProperty.name + " (" + buyableProperty.value + "€).";
            buyableProperty = null;
            return text;
        }
        return "Non puoi comprare questa proprietà!";
    }
    
    public boolean canMortgageProperty(Property p)
    {
        return properties.contains(p) && p.mortgaged == false;
    }
    
    public boolean mortgageProperty(Property p)
    {
        if(canMortgageProperty(p))
        {
            p.mortgaged = true;
            money += p.value / 2;
            return true;
        }
        return false;
    }
    
    public boolean canUnmortgageProperty(Property p)
    {
        int neededMoney = (int) (p.value * 1.1);
        neededMoney /= 2;
        return properties.contains(p) && p.mortgaged == true && neededMoney <= money;
    }
    
    public boolean unmortgageProperty(Property p)
    {
        if(canUnmortgageProperty(p))
        {
            int neededMoney = (int) (p.value * 1.1);
            neededMoney /= 2;
            money -= neededMoney;
            p.mortgaged = false;
            return true;
        }
        return false;
    }
    
    public boolean canExitFromPrison() {
        return inPrison;
    }
    
    public boolean exitFromPrison() 
    {
        if(canExitFromPrison()) 
        {
            if(prisonCards > 0) 
            {
                prisonCards--;
                prisonTurnsCounter = 0;
                inPrison = false;
                return true;
            } else if(money >= 50) 
            {
                money -= 50;
                prisonTurnsCounter = 0;
                inPrison = false;
                return true;
            }
        }
        return false;
    }
    
    public boolean canBuild(String color) 
    {
        ArrayList<LandProperty> landProperties = getLandProperties(color);
        int count = 0;
        int totalPrice = 0;
        for(int i=0; i<landProperties.size();i++) {
            LandProperty property = landProperties.get(i);
            if(property.mortgaged == false && property.getNumberOfHouses() < 5) {
                count++;
                totalPrice += property.housePrice;
            }
        }
        
        boolean result;
        if("PURPLE".equals(color) || "BROWN".equals(color))
        {
            result = count == 2 && money >= totalPrice;
        }
        else
        {
            result = count == 3 && money >= totalPrice;
        }
        return result;
    }
    
    public boolean build(String color)
    {
        if(canBuild(color))
        {
            ArrayList<LandProperty> landProperties = getLandProperties(color);
            int totalPrice = 0;
            for(int i=0; i<landProperties.size();i++) {
                LandProperty property = landProperties.get(i);
                totalPrice += property.housePrice;
                property.setNumberOfHouses(property.getNumberOfHouses() + 1);
            }
            money -= totalPrice;
            return true;
        }
        return false;
    }
    
    public boolean canDeconstruct(String color)
    {
        ArrayList<LandProperty> landProperties = getLandProperties(color);
        int count = 0;
        for(int i=0; i<landProperties.size();i++) {
            LandProperty property = landProperties.get(i);
            if(property.getNumberOfHouses() > 0) {
                count++;
            }
        }
        boolean result;
        if("PURPLE".equals(color) || "BROWN".equals(color))
        {
            result = count == 2;
        }
        else
        {
            result = count == 3;
        }
        return result;
    }
    
    public boolean deconstruct(String color)
    {
        if(canDeconstruct(color))
        {
            ArrayList<LandProperty> landProperties = getLandProperties(color);
            int moneyToAdd = 0;
            for(int i=0; i<landProperties.size();i++) {
                LandProperty property = landProperties.get(i);
                moneyToAdd += property.housePrice / 2;
                property.setNumberOfHouses(property.getNumberOfHouses() - 1);
            }
            money += moneyToAdd;
            return true;
        }
        return false;
    }
}
