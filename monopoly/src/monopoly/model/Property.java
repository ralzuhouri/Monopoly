/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;

/**
 *
 * @author ramy
 */
public abstract class Property implements Element, Serializable {
    public String name;
    public final int value;
    public boolean mortgaged = false;
    public Player owner = null;
    public static final long serialVersionUID = 4476466574563754L;
    
    public Property(String name, int value) 
    {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
}
