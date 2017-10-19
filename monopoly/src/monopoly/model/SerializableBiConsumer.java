/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 *
 * @author ramy
 */
public interface SerializableBiConsumer<T,U> extends BiConsumer<T,U>, Serializable {
    public static final long serialVersionUID = 4547411230563123L;
}
