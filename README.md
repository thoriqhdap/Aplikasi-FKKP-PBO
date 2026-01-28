Aplikasi Pendaftaran Kewirausahaan Pemuda (PKKP)
Proyek ini adalah aplikasi berbasis Java dengan arsitektur Object-Oriented Programming (PBO) yang terintegrasi dengan database MySQL.

📁 Struktur Folder
proyek/: Berisi seluruh source code Java aplikasi.

pkkp.sql: Script database untuk mengimpor skema tabel dan data.

mysql-connector-j-9.3.0.jar: Driver penghubung antara Java dan MySQL.

🚀 Panduan Instalasi & Penggunaan
1. Konfigurasi Database
Pastikan XAMPP atau MySQL Server sudah berjalan.

Buat database baru bernama pkkp melalui phpMyAdmin atau terminal.

Import file pkkp.sql ke dalam database tersebut.

2. Pengaturan di IDE (NetBeans/IntelliJ/Eclipse)
Buka folder proyek sebagai project baru di IDE kamu.

Penting: Tambahkan file mysql-connector-j-9.3.0.jar ke dalam Libraries atau Build Path project kamu agar koneksi database tidak error.

Sesuaikan konfigurasi koneksi di kode program:

Host: localhost:3306

Database: pkkp

User: root

3. Menjalankan Aplikasi
Jalankan Main Class untuk membuka antarmuka aplikasi.

📝 Catatan
Aplikasi ini dikembangkan untuk memenuhi tugas mata kuliah Pemrograman Berorientasi Objek. Jika terjadi error terkait "Class Not Found", pastikan driver MySQL sudah terpasang dengan benar di library project.

Cara Update ke GitHub:
Agar file README.md ini muncul di halaman depan repositori kamu, jalankan perintah berikut di terminal:

👌bash=
git add README.md
git commit -m "Docs: Update panduan instalasi dengan detail library"
git push
