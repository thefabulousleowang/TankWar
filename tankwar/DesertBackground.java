package tankwar;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

/**
 *
 * @author Moaan
 */
public class DesertBackground extends TankWar {

    Image map = getImage("Resources/BackgroundFullBig.png");

    public void draw(Graphics2D g, ImageObserver obs) {
        g.drawImage(map, 0, 0, obs);
    }

}
