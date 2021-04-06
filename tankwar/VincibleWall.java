package tankwar;

import CombatGame.IdleGameObject;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by ericgumba on 4/19/17.
 */
public class VincibleWall  extends TankWar implements IdleGameObject {

  Image wallImage = getImage("Resources/Wall2.png");
  int x, y, width, height ;
  boolean isVisible;

  VincibleWall(int x, int y) {
    this.x = x;
    this.y = y;
    width = wallImage.getWidth(null);
    height = wallImage.getHeight(null);
    isVisible = true;
  }

  @Override
  public void update() {
    if (isVisible) {
      for (int i = 0; i < playerOneBullets.size(); i++) {
        if (playerOneBullets.get(i).collision(x + 20, y, width - 20, height)) {
          isVisible = false;
          death.play();
        }
      }
      for (int i = 0; i < playerTwoBullets.size(); i++) {
        if (playerOneBullets.get(i).collision(x + 20, y, width - 20, height)) {
          isVisible = false;
          death.play();
        }
      }
    }
  }

  @Override
  public void draw(Graphics g, ImageObserver obs) {
    if (isVisible) {
      g.drawImage(wallImage, x, y, observer);
    }
  }

  public boolean collision(int oX, int oY, int oW, int oH) {
    if (isVisible) {
      if ((oY + oH > this.y) && (oY < this.y + height)) {
        if ((oX + oW > this.x) && (oX < this.x + width)) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
}
