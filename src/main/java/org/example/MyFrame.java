package org.example;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyFrame extends JFrame implements MouseListener {
    private JLayeredPane layeredPane;
    private ImageIcon backgroundIcon;
    private JLabel backgroundLabel;
    private ImageIcon mamm0nIcon;
    private JLabel mamm0nLabel;
    private JLabel mortadelaLabel;
    private ImageIcon mortadelaIcon;
    private JLabel healthLabel;
    private ImageIcon healthIcon;
    private int health = 10;
    private Timer healthDecreaseTimer;
    private Timer healthIncreaseTimer;
    private JLabel hungerLabel;
    private ImageIcon hungerIcon;
    private int hunger = 3;
    private Timer hungerDecreaseTimer;
    private JLabel funLabel;
    private ImageIcon funIcon;
    private int fun = 3;
    private Timer funDecreaseTimer;
    private JLabel lifeLabel;
    private int life = 0;
    private Timer lifeTimer;
    private JLabel restartLabel;
    private DateTimeFormatter formatter;
    private String closeAppDate;
    private LocalDateTime now;
    private static final String FILE_NAME = "Tamagotchi.txt";
    private File file = new File(FILE_NAME);
    private Clip mortadelaClip;
    private Clip funClip;
    private Clip endClip;
    MyFrame() {

        readDataFromFile();

        this.setTitle("Tamagotchi");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                closeAppDate = now.format(formatter);
                saveDataToFile(health, hunger, fun, life, closeAppDate);
            }
        });

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,600,600);

        backgroundIcon = new ImageIcon(getClass().getResource("/background.jpg"));
        backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0,0,backgroundIcon.getIconWidth(),backgroundIcon.getIconHeight());

        mamm0nIcon = new ImageIcon(getClass().getResource("/Mamm0n.png"));
        mamm0nLabel = new JLabel(mamm0nIcon);
        mamm0nLabel.setBounds(80, 180, mamm0nIcon.getIconWidth(), mamm0nIcon.getIconWidth());
        mamm0nLabel.addMouseListener(this);
        funLoadSound("/fun.wav");

        mortadelaIcon = new ImageIcon(getClass().getResource("/mortadela.png"));
        mortadelaLabel = new JLabel(mortadelaIcon);
        mortadelaLabel.setBounds(450,25,mortadelaIcon.getIconWidth(),mortadelaIcon.getIconHeight());
        mortadelaLabel.addMouseListener(this);
        mortadelaLoadSound("/mortadela.wav");

        healthIcon = new ImageIcon(getClass().getResource("/health.png"));
        healthLabel = new JLabel(healthIcon);
        healthLabel.setText(" = " + health);
        healthLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        healthLabel.setVerticalTextPosition(SwingConstants.CENTER);
        healthLabel.setBounds(0, 20, healthIcon.getIconWidth() + 50, healthIcon.getIconHeight());

        healthDecreaseTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(health>0) {
                        if(hunger==0||fun==0) {
                        health--;
                        healthLabel.setText(" = " + health);
                    }
                } else {
                    endPlaySound("/end.wav");
                    mamm0nLabel.setIcon(new ImageIcon(getClass().getResource("/Mamm0n_dead.png")));
                    mamm0nLabel.setBounds(80, 200, mamm0nIcon.getIconWidth(), mamm0nIcon.getIconWidth());
                    lifeLabel.setText("The END! Mamm0n was alive: "+life+" seconds");
                    lifeLabel.setVisible(true);
                    lifeTimer.stop();
                    restartLabel.setVisible(true);
                }
            }
        });
        healthDecreaseTimer.start();

        healthIncreaseTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(health<100) {
                    if(hunger==100&&fun==100) {
                        health++;
                        healthLabel.setText(" = " + health);
                    }
                }
            }
        });
        healthIncreaseTimer.start();

        hungerIcon = new ImageIcon(getClass().getResource("/hunger.png"));
        hungerLabel = new JLabel( hungerIcon);
        hungerLabel.setText(" = " + hunger);
        hungerLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        hungerLabel.setVerticalTextPosition(SwingConstants.CENTER);
        hungerLabel.setBounds(0, 50, hungerIcon.getIconWidth() + 50, hungerIcon.getIconHeight());

        hungerDecreaseTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hunger>0) {
                    hunger--;
                    hungerLabel.setText(" = " + hunger);
                }
            }
        });
        hungerDecreaseTimer.start();

        funIcon = new ImageIcon(getClass().getResource("/fun.png"));
        funLabel = new JLabel(funIcon);
        funLabel.setText(" = " + fun);
        funLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        funLabel.setVerticalTextPosition(SwingConstants.CENTER);
        funLabel.setBounds(0, 80, funIcon.getIconWidth() + 50, funIcon.getIconHeight());

        funDecreaseTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fun>0) {
                    fun--;
                    funLabel.setText(" = " + fun);
                }
            }
        });
        funDecreaseTimer.start();
        lifeLabel = new JLabel();
        lifeLabel.setBounds(75,150,450,30);
        lifeLabel.setOpaque(true);
        lifeLabel.setBackground(Color.RED);
        lifeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        lifeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lifeLabel.setVisible(false);

        lifeTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                life++;
            }
        });
        lifeTimer.start();

        restartLabel = new JLabel("Restart");
        restartLabel.setBounds(200, 500, 200,30);
        restartLabel.setOpaque(true);
        restartLabel.setBackground(Color.RED);
        restartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        restartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        restartLabel.setVisible(false);
        restartLabel.addMouseListener(this);

        layeredPane.add(backgroundLabel, Integer.valueOf(1));
        layeredPane.add(mamm0nLabel, Integer.valueOf(2));
        layeredPane.add(mortadelaLabel, Integer.valueOf(2));
        layeredPane.add(healthLabel, Integer.valueOf(2));
        layeredPane.add(hungerLabel, Integer.valueOf(2));
        layeredPane.add(funLabel, Integer.valueOf(2));
        layeredPane.add(lifeLabel, Integer.valueOf(2));
        layeredPane.add(restartLabel, Integer.valueOf(3));

        this.setIconImage(mamm0nIcon.getImage());
        this.add(layeredPane);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(health>0) {
            if(e.getComponent() == mamm0nLabel) {
                mamm0nLabel.setIcon(new ImageIcon(getClass().getResource("/Mamm0n_fun.png")));
                playSound(funClip);
                if(fun<100)
                    fun++;
                funLabel.setText(" = " + fun);
            } else if (e.getComponent() == mortadelaLabel) {
                mamm0nLabel.setIcon(new ImageIcon(getClass().getResource("/Mamm0n_open_mouth.png")));
                playSound(mortadelaClip);
                if(hunger<100)
                    hunger++;
                hungerLabel.setText(" = " + hunger);
            }
        } else if (e.getComponent() == restartLabel) {
            dispose();
            endClip.close();
            if(file.exists()) {
                file.delete();
            }
            new MyFrame();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(health>0) {
            if(e.getComponent() == mamm0nLabel) {
                mamm0nLabel.setIcon(new ImageIcon(getClass().getResource("/Mamm0n.png")));
            }  else if (e.getComponent() == mortadelaLabel) {
                mamm0nLabel.setIcon(new ImageIcon(getClass().getResource("/Mamm0n.png")));
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void saveDataToFile(int health, int hunger, int fun, int life, String closeAppDate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(health + "\n" + hunger + "\n" + fun + "\n" + life + "\n" + closeAppDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromFile() {
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int tempHealth;
        if(file.exists() && file.length() != 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

                health = Integer.parseInt(reader.readLine());
                hunger = Integer.parseInt(reader.readLine());
                fun = Integer.parseInt(reader.readLine());
                life = Integer.parseInt(reader.readLine());
                String closeAppDate = reader.readLine();

                LocalDateTime savedTime = LocalDateTime.parse(closeAppDate, FORMATTER);
                LocalDateTime currentTime = LocalDateTime.now();

                Duration duration = Duration.between(savedTime, currentTime);
                int secondsElapsed = (int) duration.toSeconds();

                if((hunger-secondsElapsed/5)>0 && (fun-secondsElapsed/5)>0) {
                    hunger = hunger - secondsElapsed/5;
                    fun = fun - secondsElapsed/5;
                    life = life + secondsElapsed;
                } else if ((hunger-secondsElapsed/5)<=0 && ((hunger-secondsElapsed/5)<=(fun-secondsElapsed/5))) {
                    tempHealth = health;
                    if((health - secondsElapsed + hunger*5)>0) {
                        health = health - secondsElapsed + hunger*5;
                        life = life + secondsElapsed;
                        if((fun - secondsElapsed/5)>0) {
                            fun = fun - secondsElapsed/5;
                        } else {
                            fun = 0;
                        }
                    } else {
                        health = 0;
                        life = life + tempHealth + hunger*5;
                        fun = 0;
                    }
                    hunger = 0;
                } else if ((fun-secondsElapsed/5)<=0 && ((hunger-secondsElapsed/5)>=(fun-secondsElapsed/5))) {
                    tempHealth = health;
                    if((health - secondsElapsed + fun*5)>0) {
                        health = health - secondsElapsed + fun*5;
                        life = life + secondsElapsed;
                        if((hunger - secondsElapsed/5)>0) {
                            hunger = hunger - secondsElapsed/5;
                        } else {
                            hunger = 0;
                        }
                    } else {
                        health = 0;
                        life = life + tempHealth + fun*5;
                        hunger = 0;
                    }
                    fun = 0;
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void mortadelaLoadSound(String soundResourcePath) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundResourcePath);
            if (audioSrc == null) {
                System.err.println("Nie znaleziono pliku: " + soundResourcePath);
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            mortadelaClip = AudioSystem.getClip();
            mortadelaClip.open(audioInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void funLoadSound(String soundResourcePath) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundResourcePath);
            if (audioSrc == null) {
                System.err.println("Nie znaleziono pliku: " + soundResourcePath);
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            funClip = AudioSystem.getClip();
            funClip.open(audioInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void endPlaySound(String soundResourcePath) {
        try {
            if (endClip != null && endClip.isRunning()) {
                return;
            }

            if (endClip == null) {
                InputStream audioSrc = getClass().getResourceAsStream(soundResourcePath);
                if (audioSrc == null) {
                    System.err.println("Nie znaleziono pliku: " + soundResourcePath);
                    return;
                }

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

                endClip = AudioSystem.getClip();
                endClip.open(audioInputStream);
            }

            endClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
