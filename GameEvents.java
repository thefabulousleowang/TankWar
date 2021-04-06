package CombatGame;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author Moaan
 */
public class GameEvents extends Observable {

    public int eventType;
    public Object event;

    public void setValue(KeyEvent k, int keyEventType) {
        try {
            eventType = keyEventType;
            event = k;
            setChanged();
            notifyObservers(this);
        } catch(Exception e){

        }
    }

}
