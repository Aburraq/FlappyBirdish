package flappybird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 1200, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public int ticks;
    public int yMotions;
    public int score = 0;
    public ArrayList<Rectangle> columns;
    public Random random = new Random();
    public boolean gameOver;
    public boolean started;

    public FlappyBird(){

        JFrame jFrame = new JFrame();
        renderer = new Renderer();
        Timer timer = new Timer(20, this);

        jFrame.add(renderer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Flappy Birdish");
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        bird = new Rectangle(WIDTH/2-10, HEIGHT/2 -10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();

    }

    public void addColumn(boolean start){


        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start){

        columns.add(new Rectangle(
                WIDTH + width + columns.size() * 300, HEIGHT- height - 120, width, height));
        columns.add(new Rectangle(
                WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));

        } else {
            columns.add(new Rectangle(
                    columns.get(columns.size() - 1).x + 600, HEIGHT- height - 120, width, height));
            columns.add(new Rectangle(
                    columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));

        }

    }

    public void paintColumn(Graphics g, Rectangle column){

        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);

    }

    public void repaint(Graphics g) {

        g.setColor(Color.cyan);
        g.fillRect(0,0, WIDTH, HEIGHT);
        g.setColor(Color.orange.darker());
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 150, WIDTH, 25);
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle w : columns){

            paintColumn(g, w);

        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 100));

        if (!started) g.drawString("Click to start!", WIDTH/2- 300, HEIGHT/2);

        if (gameOver) g.drawString("Game Over :(", WIDTH/2- 300, HEIGHT/2);

        if (!gameOver && started){

            g.drawString(String.valueOf(score), WIDTH / 2 - 25 , 100);

        }

    }

    public void jump(){

        if (gameOver){

            gameOver = false;
            bird = new Rectangle(WIDTH/2-10, HEIGHT/2 -10, 20, 20);
            columns.clear();
            yMotions = 0;
            score = 0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

        }

        if (!started){

            started = true;

        } else if (!gameOver){

            if (yMotions > 0){

                yMotions = 0;

            }

            yMotions -= 10;

        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {


        int speed = 10;
        ticks++;

        if (started){

            for (int i = 0; i < columns.size(); i++){

                Rectangle column = columns.get(i);
                column.x -= speed;

            }

            if (ticks % 2 == 0 && yMotions < 15) yMotions += 2;

            for (int i = 0; i < columns.size(); i++){

                Rectangle column = columns.get(i);
                column.x -= speed;

                if (column.x + column.width < 0){

                    columns.remove(column);

                    if (column.y == 0) addColumn(false);

                }

            }

            bird.y += yMotions;

            for (Rectangle w : columns){

                if (bird.x >= w.x + w.width) {
                    score ++;
                }

                if (w.intersects(bird)) {
                    gameOver = true;
                    bird.x = w.x - bird.width;
                }

            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) gameOver = true;

            if (bird.y + yMotions >= HEIGHT -120) bird.y = HEIGHT - 120 -bird.height;

        }


        renderer.repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        jump();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) jump();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
