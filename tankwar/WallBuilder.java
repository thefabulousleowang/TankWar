/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankwar;

import CombatGame.GameEvents;
import CombatGame.MobileGameObject;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by ericgumba on 4/19/17.
 */
public class WallBuilder extends TankWar{

  private ArrayList<InvincibleWall> invincibleWalls = new ArrayList();
  private ArrayList<VincibleWall> vincibleWalls = new ArrayList();
  private int wallWidth = 38;


  WallBuilder() {
    int checkPoint = 0;
    for (int i = 0; i < 5 ; i++) {
      invincibleWalls.add(new InvincibleWall(borderX/2, i * (borderY / 32)));
      checkPoint = i * borderY / 32;
    }
    for (int i = 0; i < 6; i++){
      invincibleWalls.add(new InvincibleWall(borderX/2 - i * wallWidth, checkPoint));
    }

    for (int i = 0; i < 5; i++){
      invincibleWalls.add(new InvincibleWall(borderX /2, borderY - wallWidth * i ));
      checkPoint = borderY - wallWidth * i;
    }

    for ( int i = 0; i < 5; i++){
      invincibleWalls.add(new InvincibleWall(borderX/2 + i * wallWidth, checkPoint));
    }
    for ( int i = 0; i < 5; i++){
      invincibleWalls.add(new InvincibleWall(wallWidth + wallWidth * i, borderY/2 - 38 * 2));
    }
    checkPoint = 300;
    for (int i = 0 ; i < 13; i++){
      vincibleWalls.add(new VincibleWall((borderX / 2), 300 + i * 38));
      vincibleWalls.add(new VincibleWall((borderX / 2 - wallWidth), checkPoint + i * wallWidth ));
      vincibleWalls.add(new VincibleWall(( borderX / 2 + 38 + wallWidth * i), checkPoint));
      vincibleWalls.add(new VincibleWall((borderX / 2 + wallWidth - wallWidth * i), checkPoint + 13 * wallWidth) );
    }

    for (int i = 0; i < borderY / 32; i++){
      invincibleWalls.add(new InvincibleWall(borderX - 20, i * (borderY / 32)));
      invincibleWalls.add(new InvincibleWall(10, i * (borderY / 32)));
    }

    for(int i = 0; i < borderX / 32; i ++){
      invincibleWalls.add(new InvincibleWall(i * borderX / 32 , 10));
      invincibleWalls.add(new InvincibleWall(i * borderX / 32, borderY-20));
    }

  }


  public void draw(Graphics g, ImageObserver obs) {


    for (int i = 0; i < vincibleWalls.size(); i++) {
      vincibleWalls.get(i).update();
      vincibleWalls.get(i).draw(g, obs);
    }
    for ( int i = 0; i < invincibleWalls.size(); i++){
      invincibleWalls.get(i).update();
      invincibleWalls.get(i).draw(g, obs);
    }
  }

  public boolean collision(int oX, int oY, int oW, int oH) {
    for (int i = 0; i < vincibleWalls.size(); i++) {
      if (vincibleWalls.get(i).collision(oX, oY, oW, oH)) {
        return true;
      }
    }
    for(int i = 0; i < invincibleWalls.size(); i++) {
      if (invincibleWalls.get(i).collision(oX, oY, oW, oH)) {
        return true;
      }
    }
    return false;
  }

}
