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
public class TurnOutcome 
{   
    private final int diceResult;
    public int getDiceResult() {
        return diceResult;
    }
    
    private final boolean finished;
    public boolean isFinished() {
        return finished;
    }
    
    private final boolean isADouble;
    public boolean isDouble() {
        return isADouble;
    }
    
    private String description = "";
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    private final boolean passedGo;
    public boolean passedGo()
    {
        return passedGo;
    }
    
    public TurnOutcome(int diceResult, boolean isFinished, boolean isDouble, String description, boolean passedGo) 
    {
        this.diceResult = diceResult;
        this.finished = isFinished;
        this.isADouble = isDouble;
        this.description = description;
        this.passedGo = passedGo;
    }
}
