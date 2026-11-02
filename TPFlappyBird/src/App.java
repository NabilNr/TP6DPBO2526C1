import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Main Menu Panel Kustom dengan Gambar Background
        JPanel menuPanel = new JPanel() {
            private Image bgImage = new ImageIcon(getClass().getResource("/assets/background.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gambar background game di menu awal
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

                // Tambahkan overlay semi-transparan agar teks dan tombol terlihat jelas
                g.setColor(new Color(0, 0, 0, 80));
                g.fillRect(0, 0, getWidth(), getHeight());

                // Menggambar Efek Bayangan pada Judul
                g.setFont(new Font("Arial", Font.BOLD, 42));
                g.setColor(Color.BLACK);
                g.drawString("FLAPPY BIRD", 43, 173); // Shadow

                g.setColor(new Color(255, 215, 0)); // Warna Emas/Kuning
                g.drawString("FLAPPY BIRD", 40, 170);

                // Petunjuk Kontrol di Bagian Bawah Menu
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.setColor(Color.WHITE);
                g.drawString("Press SPACE to Jump", 105, 520);
                g.drawString("Press R to Restart when Game Over", 60, 545);
            }
        };

        menuPanel.setLayout(null); // Gunakan Absolute Layout untuk penataan presisi

        // Tombol START GAME Kustom
        JButton startButton = createStyledButton("START GAME", new Color(46, 204, 113), new Color(39, 174, 96));
        startButton.setBounds(80, 260, 200, 50);

        // Tombol EXIT Kustom
        JButton exitButton = createStyledButton("EXIT", new Color(231, 76, 60), new Color(192, 57, 43));
        exitButton.setBounds(80, 330, 200, 50);

        menuPanel.add(startButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);
        frame.setVisible(true);

        // Action Listener Tombol Start Game
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(menuPanel); // Hapus Menu Utama

                // Inisialisasi Game Logic & View
                Logic logic = new Logic();
                View view = new View(logic);
                logic.setView(view);

                frame.add(view);
                frame.revalidate();
                frame.repaint();
                view.requestFocus(); // Mengarahkan fokus agar input keyboard terbaca
                logic.startGame(); // Jalankan Game Loop & Audio
            }
        });

        // Action Listener Tombol Exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // Helper Method untuk Membikin Tombol Berpenampilan Modern + Efek Hover
    private static JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efek Hover (Saat kursor diarahkan ke tombol)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }
}