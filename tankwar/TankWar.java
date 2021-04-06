package tankwar;

import CombatGame.GameControl;
import CombatGame.GameEvents;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.AudioClip;
/**
 *
 * @author Moaan
 */
public class TankWar extends JApplet {
    static WallBuilder wallE;
    static HashMap<Integer, String> controls = new HashMap<>();
    static int borderX = 1475, borderY = 1155; //objects need to know the border
    private static int screenWidth = 840, screenHeight = 580;
    ImageObserver observer;
    private BufferedImage bufferedImg, bufferedImg2, bulletBall;
    private Thread coreThread;
    static AudioClip death, fire;

    //Game Objects
    DesertBackground theBackground;
    static UserTank tankL, tankR;
    static UserTank[] Enemy = new UserTank[3];
    static Image[] explosionFrames;
    static ArrayList<Bullet> playerOneBullets, playerTwoBullets; // why an array list of bullets?

    public static void main(String[] args) {
        final TankWar preAlpha = new TankWar();
        preAlpha.init();
        JFrame f = new JFrame("Pre-Alpha TankWar Game");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", preAlpha);
        f.pack();
        f.setSize(new Dimension(screenWidth, screenHeight));
        f.setVisible(true);
        f.setResizable(false);
        preAlpha.start();

    }

    public void init() {
        controls.put(KeyEvent.VK_LEFT, "left2");
        controls.put(KeyEvent.VK_UP, "up2");
        controls.put(KeyEvent.VK_DOWN, "down2");
        controls.put(KeyEvent.VK_RIGHT, "right2");
        controls.put(KeyEvent.VK_ENTER, "shoot2");
        controls.put(KeyEvent.VK_A, "left1");
        controls.put(KeyEvent.VK_W, "up1");
        controls.put(KeyEvent.VK_S, "down1");
        controls.put(KeyEvent.VK_D, "right1");
        controls.put(KeyEvent.VK_SPACE, "shoot1");

        try {
          death = getAudioClip(TankWar.class.getResource("Resources/explosion3.wav"));
          fire = getAudioClip(TankWar.class.getResource("resources/explosion2.wav"));
        } catch ( Exception e ){
          System.out.print("unable to get you know, the sound files.");
        }

        String bul = "Resources/Ball_strip9.png";
        try {
          bulletBall = getBufferedImage(bul);
        } catch( Exception e){
          System.out.print( e );
        }

        Image b = (Image) bulletBall.getSubimage(30,0,32,29);

        Image bullet = b;

        explosionFrames = new Image[] {
            getImage("Resources/explosion2_1.png"),
            getImage("Resources/explosion2_2.png"),
            getImage("Resources/explosion2_3.png"),
            getImage("Resources/explosion2_4.png"),
            getImage("Resources/explosion2_5.png"),
            getImage("Resources/explosion2_6.png"),
            getImage("Resources/explosion2_7.png")}; // CHECK POINT

        //Game Objects:
        theBackground = new DesertBackground();

        String tankLImages = "Resources/Tank_blue_light_strip60.png";
        String tankRImages = "Resources/Tank_red_light_strip60.png";

        playerOneBullets = new ArrayList();
        playerTwoBullets = new ArrayList();

        wallE = new WallBuilder();
        tankL = new UserTank(tankLImages, playerTwoBullets, playerOneBullets, bullet, 1);
        tankR = new UserTank(tankRImages, playerOneBullets, playerTwoBullets, bullet, 2);

        // index 0 won't be used.
        Enemy[1] = tankR;
        Enemy[2] = tankL;

        setBackground(Color.black);
        this.setFocusable(true);
        observer = this;
        GameEvents gE = new GameEvents();
        gE.addObserver(tankL);
        gE.addObserver(tankR);

        GameControl gC = new GameControl(gE);
        addKeyListener(gC);


    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(borderX, borderY);
        Graphics2D g3 = createOuterGraphics2D(borderX, borderY);
        updateAndDisplay(borderX, borderY, g2);
        g2.dispose();

            int displayX1 = tankL.x + 30 - screenWidth / 4;
            if (displayX1 < 0) {
                displayX1 = 0;
            } else if (displayX1 + screenWidth / 2 > borderX) {
                displayX1 = borderX - screenWidth / 2;
            }
            int displayY1 = tankL.y + 30 - screenHeight / 2;
            if (displayY1 < 0) {
                displayY1 = 0;
            } else if (displayY1 + screenHeight > borderY) {
                displayY1 = borderY - screenHeight;
            }

            //getting tankR's viewing window
            int displayX2 = tankR.x + 30 - screenWidth / 4;
            if (displayX2 < 0) {
                displayX2 = 0;
            } else if (displayX2 + screenWidth / 2 > borderX) {
                displayX2 = borderX - screenWidth / 2;
            }
            int displayY2 = tankR.y + 30 - screenHeight / 2;
            if (displayY2 < 0) {
                displayY2 = 0;
            } else if (displayY2 + screenHeight > borderY) {
                displayY2 = borderY - screenHeight;
            }

            g3.drawImage(bufferedImg.getSubimage(displayX1, displayY1, screenWidth / 2, screenHeight), 0, 0, this);
            g3.drawImage(bufferedImg.getSubimage(displayX2, displayY2, screenWidth / 2, screenHeight), screenWidth / 2, 0, this);
            g3.drawLine(d.width / 2 + 2, 0, d.width / 2 + 2, d.height);
            g3.drawRect(d.width / 2 - (d.width / 5) / 2 - 1, d.height * 3 / 4 - 1, d.width / 5 + 1, d.height / 5 + 1);
            g3.drawImage(bufferedImg.getScaledInstance(d.width / 5, d.height / 5, 1), d.width / 2 - (d.width / 5) / 2, d.height * 3 / 4, this);
            g3.dispose();
            g.drawImage(bufferedImg2, 0, 0, this);
        }


    private Graphics2D createOuterGraphics2D(int w, int h) {
        if (bufferedImg2 == null || bufferedImg2.getWidth() != w || bufferedImg2.getHeight() != h) {
            bufferedImg2 = (BufferedImage) createImage(w, h);
        }
        Graphics2D g2 = bufferedImg2.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    private Graphics2D createGraphics2D(int w, int h) { 
        if (bufferedImg == null || bufferedImg.getWidth() != w || bufferedImg.getHeight() != h) {
            bufferedImg = (BufferedImage) createImage(w, h);
        }
        Graphics2D g2 = bufferedImg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    private void updateAndDisplay(int w, int h, Graphics2D g2) {

      theBackground.draw(g2, this);
      tankL.move();
      tankL.draw(g2, this);

      tankR.move();
      tankR.draw(g2, this);
      wallE.draw(g2, this);
      for (int i = 0; i < playerOneBullets.size(); i++) {
        if (playerOneBullets.get(i).move()) {
          playerOneBullets.remove(i);
        } else {
          playerOneBullets.get(i).draw(g2, this);
        }
      }

      for (int i = 0; i < playerTwoBullets.size(); i++) {
        if (playerTwoBullets.get(i).move()) {
          playerTwoBullets.remove(i);
        } else {
          playerTwoBullets.get(i).draw(g2, this);
        }
      }
    }

    public Image getImage(String name) {
        URL url = TankWar.class.getResource(name);
        Image image = getToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println(e);
        }
        return image;
    }

    public BufferedImage getBufferedImage(String name) throws IOException {
        URL url = TankWar.class.getResource(name);
        BufferedImage bufferedImage = ImageIO.read(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(bufferedImage, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println( "Unable to get the buffered image" );
        }
        return bufferedImage;
    }
}
