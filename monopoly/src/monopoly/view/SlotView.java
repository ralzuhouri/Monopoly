/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ramy
 */
public abstract class SlotView {
    public Rectangle rect;
    public abstract void draw(GraphicsContext gc);
}
