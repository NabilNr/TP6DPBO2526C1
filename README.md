# Janji
Saya Khalifa Nabil Nur dengan NIM 2511372 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. <br>

# Desain Program
## Struktur Kelas Utama
1. <b>App.java
   - Berfungsi menginisialisasi jendela aplikasi utama (JFrame) berukuran 360 x 640 piksel.
   - Menampilkan Main Menu interaktif berbasis JPanel yang dilengkapi gambar background, text shadow retro arcade, serta tombol START GAME dan EXIT.
2. <b>Player.java 
   - Merepresentasikan karakter utama (burung).
   - Menyimpan atribut posisi (posX, posY), dimensi (width, height), gambar (image), serta kecepatan vertikal (velocityY).
3. <b> Pipe.java <b>
   - Merepresentasikan objek rintangan pipa.
   - Menyimpan atribut posisi (posX, posY), dimensi (width, height), gambar (image), serta status passed (penanda apakah pipa sudah dilewati player untuk perhitungan skor)
4. Logic.java
   - Mengatur seluruh logika permainan (behind the scenes), meliputi:
     - Game Loop: Menggunakan Timer 60FPS untuk memperbarui posisi objek secara berkala.
     - Simulasi Gravitasi: Mengubah velocityY burung secara berkala agar jatuh secara realistis.
     - Spawner Pipa: Menggunakan Timer interval 1.5 detik untuk membuat pipa secara acak (random height).
     - Deteksi Tabrakan (Collision Detection): Memeriksa apakah bounding box burung bersinggungan dengan pipa atau batas bawah frame.
     - Sistem Skor & Sound Effect: Menambah skor +1 setiap berhasil melewati sepasang pipa serta memainkan BGM dan SFX (suara poin/hit).
     - Event Handler Input: Mengimplementasikan KeyListener untuk merespons tombol SPACE (melompat) dan tombol R
5. View.java
   - Merupakan komponen JPanel yang bertindak sebagai kanvas utama permainan.
   - Menggunakan method paintComponent(Graphics g) untuk meng-render elemen grafis (latar belakang, player, pipa, pesan Game Over).
   - Memiliki komponen JLabel untuk menampilkan skor pemain di pojok atas layar secara real-time. <br>

# Penjelasan Alur
1. Inisialisasi & Main Menu (Awal)
   - Program dijalankan melalui App.java.
   - JFrame terbentuk dan menampilkan Main Menu.
   - Pemain memilih tombol START GAME untuk memulai permainan atau EXIT untuk keluar dari program.
2. Pengalihan Layar ke Arena Game
   - Saat tombol START GAME diklik, App.java menghapus menuPanel dan memasukkan instansiasi View serta Logic ke dalam JFrame.
   - Logic memulai game loop (gameLoop.start()), pembuat pipa (pipesCooldown.start()), dan memutar musik latar (BGM).
3. Looping Permainan (Game Loop)
   - Setiap tick Timer 60 FPS, method actionPerformed() di Logic.java akan dipanggil secara berulang:
     - Update Posisi Player: Posisi burung ditarik oleh gravitasi bumi (velocityY bertambah).
     - Update Posisi Pipa: Pipa bergerak secara horizontal ke kiri (pipeVelocityX).
     - Pemeriksaan Skor: Jika posisi burung melewati batas kanan pipa dan pipe.isPassed() masih false, skor bertambah +1 dan SFX poin diputar.
     - Pemeriksaan Tabrakan: Jika burung menabrak pipa atas/bawah atau menyentuh lantai/batas bawah layar, permainan langsung memicu kondisi Game Over.
     - Redraw (Repaint): View.java menggambar ulang seluruh objek grafis dan memperbarui teks pada JLabel skor.
4. Input Kontrol Pemain
   - Tombol SPACE: Mengubah kecepatan vertikal burung menjadi negatif (velocityY = -9), membuat burung terdorong ke atas (melompat).
   - Tombol R: Jika status game sedang Game Over, menekan tombol R akan mereset posisi burung, mengosongkan list pipa, mereset skor menjadi 0, dan memutar kembali game loop dari awal.  
5. Kondisi Game Over
   - Game loop dan spawner pipa dihentikan (stop()).
   - Efek suara benturan (hit.wav) diputar.
   - View.java menggambar overlay semi-transparan hitam bertuliskan "GAME OVER", skor akhir pemain, serta instruksi "Press 'R' to Restart". <br>

# Dokumentasi
