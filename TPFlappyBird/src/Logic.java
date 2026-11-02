import javax.swing.*;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.InputStream;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -4;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    View view;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    Player player;
    ArrayList<Pipe> pipes;

    private boolean gameOver = false;
    private double score = 0; // Menggunakan double agar penambahan 0.5 terhitung akurat

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        gameLoop = new Timer(1000 / 60, this);
    }

    public void startGame() {
        pipesCooldown.start();
        gameLoop.start();
        playSound("/assets/bgm.wav", true);
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move() {
        if (gameOver) return;

        // Pergerakan Player
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // Deteksi Jatuh ke Bawah
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            triggerGameOver();
        }

        // Pergerakan & Cek Skor Pipa
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // LOGIKA SKOR (+1 per sepasang pipa yang terlewati)
            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score += 0.5; // Sepasang pipa (atas & bawah) = total +1.0
                if (score % 1 == 0) {
                    playSound("/assets/point.wav", false);
                }
            }

            // Tabrakan Pipa
            if (collision(player, pipe)) {
                triggerGameOver();
            }
        }
    }

    public boolean collision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    private void triggerGameOver() {
        if (!gameOver) {
            gameOver = true;
            gameLoop.stop();
            pipesCooldown.stop();
            playSound("/assets/die.wav", false);
        }
    }

    public void restartGame() {
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        pipesCooldown.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.updateScore(getScore());
            view.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                player.setVelocityY(-9);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            if (gameOver) {
                restartGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void playSound(String soundFile, boolean loop) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundFile);
            if (audioSrc != null) {
                InputStream bufferedInput = new java.io.BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        } catch (Exception e) {
            // Audio bersifat opsional
        }
    }

    public void setView(View view) { this.view = view; }
    public Player getPlayer() { return player; }
    public ArrayList<Pipe> getPipes() { return pipes; }
    public boolean isGameOver() { return gameOver; }
    public int getScore() { return (int) score; }
}