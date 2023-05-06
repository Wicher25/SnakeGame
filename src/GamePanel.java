import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{

    static final int screen_width = 1300;
    static final int screen_height = 750;
    static final int unit_size = 50;
    static final int amount_of_game_units = (screen_height*screen_width) / unit_size;

    static final int delay = 150;

    final int x[] = new int[amount_of_game_units];
    final int y[] = new int[amount_of_game_units];

    int body_parts = 6;
    int apple_x;
    int apple_y;
    int apples_eaten;


    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
            super.paintComponent(g);
            draw(g);

    }

    public void draw(Graphics g)
    {
        if(running)
        {
            g.setColor(Color.red);
            g.fillOval(apple_x, apple_y, unit_size, unit_size);


            for (int i = 0; i < body_parts; i++) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], unit_size, unit_size);

            }

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + apples_eaten, (screen_width - metrics.stringWidth("Score: " + apples_eaten)) / 2, g.getFont().getSize());
        }
        else
        {
            GameOver(g);
        }
    }

    public void newApple()
    {
        apple_x = random.nextInt((int)(screen_width / unit_size)) * unit_size;
        apple_y = random.nextInt((int)(screen_height / unit_size)) * unit_size;
    }

    public void move()
    {
        for(int i = body_parts; i>0 ; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction)
        {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;

        }
    }

    public void checkApple()
    {
        if((x[0] == apple_x) && (y[0] == apple_y))
        {
            body_parts++;
            apples_eaten++;
            newApple();
        }

    }

    public void checkCollision()
    {
        for(int i = body_parts; i > 0; i--)
        {
            if((x[0] == x[i]) && (y[0] == y[i]))
            {
                running = false;
                break;
            }
        }

        if(x[0] < 0)
            running = false;
        if(y[0] < 0)
            running = false;
        if(x[0] > screen_width)
            running = false;
        if(y[0] > screen_height)
            running = false;

        if(!running)
        {
            timer.stop();
        }



    }

    public void GameOver(Graphics g)
    {
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score " + apples_eaten, (screen_width - metrics1.stringWidth("Score: "+apples_eaten))/2, 100);

        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over"))/2, screen_height/2);

        JButton play_again = new JButton();
        play_again.setBounds((screen_width/2)-100,screen_height-100,200, 100);
        play_again.addActionListener(e -> restart_game());
        play_again.setText(("Play again"));
        play_again.setFocusable(false);
        play_again.setBackground(Color.black);
        this.add(play_again);


    }

    public void restart_game()
    {
        new GameFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            checkApple();
            checkCollision();

        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter
    {
            @Override
            public void keyPressed(KeyEvent e)
            {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        if(direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if(direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }
        }



}
