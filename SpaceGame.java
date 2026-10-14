

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceGame extends JPanel implements ActionListener, KeyListener {

    Timer timer;
    int plX = 350;
    int Lives = 3;
    int score = 0;
    int lvl = 1;
    int Dire = 2;
    boolean gameOver = false;

    ArrayList<Rectangle> Foes = new ArrayList<>();
    ArrayList<Rectangle> bullets = new ArrayList<>();

    public SpaceGame() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        again();

        timer = new Timer(10, this);
        timer.start();
    }

    void again() {
        Foes.clear();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 6; col++) {
                Foes.add(new Rectangle(100 + col * 60, 50 + row * 40, 40, 20));
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", 280, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 350, 340);
            return;
        }

        
        g.setColor(Color.GREEN);
        g.fillRect(plX, 550, 50, 20);

        
        g.setColor(Color.RED);
        for (Rectangle Foe : Foes) {
            g.fillRect(Foe.x, Foe.y, Foe.width, Foe.height);
        }

        
        g.setColor(Color.YELLOW);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + Lives, 700, 20);
        g.drawString("Level: " + lvl, 350, 20);
    }

    public void actionPerformed(ActionEvent e) {
        boolean chDire = false;
        for (Rectangle Foe : Foes) {
            Foe.x += Dire ;
            if (Foe.x <= 0 || Foe.x >= getWidth() - Foe.width) {
                chDire = true;
            }
        }
        if (chDire) {
            Dire = -Dire;
            for (Rectangle Foe : Foes) {
                Foe.y += 20; 
                if (Foe.y >= 550) {
                    Lives--; 
                    if (Lives == 0) {
                        gameOver = true;
                    }
                    break;
                }
            }
        }

        
        ArrayList<Rectangle> rmBullet = new ArrayList<>();
        ArrayList<Rectangle> rmFoe = new ArrayList<>();
        for (Rectangle bullet : bullets) {
            bullet.y -= 5;
            if (bullet.y < 0) {
                rmBullet.add(bullet);
            } else {
                for (Rectangle Foe : Foes) {
                    if (bullet.intersects(Foe)) {
                        rmBullet.add(bullet);
                        rmFoe.add(Foe);
                        score += 10; 
                        break;
                    }
                }
            }
        }
        bullets.removeAll(rmBullet);
        Foes.removeAll(rmFoe);

        
        if (Foes.isEmpty()) {
            lvl++;
            again(); 
        }

        if (!gameOver) {
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && plX > 0) {
            plX -= 10;
        } else if (key == KeyEvent.VK_RIGHT && plX < getWidth() - 50) {
            plX += 10;
        } else if (key == KeyEvent.VK_SPACE) {
            bullets.add(new Rectangle(plX + 50 / 2 - 2, 550 - 10, 5, 10));
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        SpaceGame game = new SpaceGame();

        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}