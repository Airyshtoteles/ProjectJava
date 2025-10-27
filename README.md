# Aplikasi FRS Online (Java Swing + MySQL)

Aplikasi desktop untuk pengisian Formulir Rencana Studi (FRS) dengan peran Mahasiswa, Dosen PA, dan Admin Prodi.

## Fitur awal yang tersedia
- Login role-based (Mahasiswa/Dosen/Admin)
- Dashboard Mahasiswa: melihat profil, daftar mata kuliah satu semester, memilih MK, menghitung total SKS, simpan draft, ajukan FRS
- Kerangka Dashboard Dosen/Admin untuk pengembangan lanjutan
- Validasi backend: batas SKS (min 12, max 24), cek bentrok jadwal sederhana, cek prasyarat sederhana
- Logging aktivitas (login, simpan, ajukan)

## Persyaratan
- JDK 17 (atau kompatibel)
- Maven 3.9+
- MySQL Server 8+

## Setup Database
1. Buat database dan tabel menggunakan skrip berikut:
   - `src/main/resources/sql/schema.sql`
   - `src/main/resources/sql/seed.sql`

   Contoh eksekusi via MySQL client (sesuaikan user/password):
   ```powershell
   # Buka PowerShell di folder proyek
   cd "c:\Users\Hype\Documents\Pemrograman Desktop\Project\AplikasiFRSOnline"
   # Jalankan schema dan seed (ganti root & password sesuai instalasi)
   mysql -u root -p < .\src\main\resources\sql\schema.sql
   mysql -u root -p < .\src\main\resources\sql\seed.sql
   ```

2. Atur koneksi database di `src/main/resources/config/db.properties`:
   - `db.host` bisa diisi IP LAN untuk mode jaringan lokal (mis. `192.168.1.10`)
   - `db.port` default `3306`
   - `db.name` default `frs_db`
   - `db.user` dan `db.password` sesuai MySQL Anda

## Build & Run
1. Build aplikasi:
   ```powershell
   cd "c:\Users\Hype\Documents\Pemrograman Desktop\Project\AplikasiFRSOnline"
   mvn -DskipTests package
   ```
   Maven akan menghasilkan file JAR dan menyalin semua dependency ke `target/lib`.

2. Jalankan aplikasi:
   ```powershell
   java -jar .\target\aplikasi-frs-online-0.1.0-SNAPSHOT.jar
   ```

## Akun Demo (dari seed.sql)
- Admin: `admin` / `password`
- Dosen: `dosen1` / `password` (NIDN: `D001`)
- Mahasiswa: `2201001` / `password`

Catatan: Password tersimpan menggunakan SHA-256 untuk demo.

## Struktur Proyek (ringkas)
- `id.ac.kampus.frs.Main` — Entry point GUI
- `util/DBConnection` — Util koneksi JDBC (MySQL)
- `service/AuthService` — Autentikasi & routing role
- `service/FRSService` — Logika validasi FRS (SKS, bentrok, prasyarat)
- `dao/*` — Akses data (User, Mahasiswa, MataKuliah, Jadwal, FRS, Persetujuan)
- `ui/LoginFrame` — Layar login
- `ui/MahasiswaDashboardFrame` — Pengisian FRS mahasiswa
- `ui/DosenDashboardFrame` — Kerangka dashboard dosen
- `ui/AdminDashboardFrame` — Kerangka dashboard admin

## Roadmap Pengembangan
- Dosen: daftar pengajuan FRS bimbingan, setujui/tolak + catatan, riwayat per mahasiswa
- Admin: CRUD master data (mahasiswa/dosen/mata_kuliah), set semester aktif & periode FRS, kunci FRS, reset password
- Laporan: PDF (PDFBox) dan Excel (Apache POI)
- Notifikasi sederhana (mis. status FRS berubah)
- Penguatan validasi prasyarat berdasarkan riwayat kelulusan MK

## Keamanan
- Gunakan user/password MySQL yang tidak default di produksi
- Ganti password demo di seed.sql
- Pertimbangkan enkripsi `.properties` atau penyimpanan terpisah untuk kredensial di produksi

## Lisensi
Proyek edukasi kampus. Silakan modifikasi sesuai kebutuhan internal.
