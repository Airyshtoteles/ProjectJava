# Aplikasi FRS Online (Java Swing + MySQL)

Aplikasi desktop untuk pengisian Formulir Rencana Studi (FRS) dengan peran Mahasiswa, Dosen PA, dan Admin Prodi.

## Fitur yang tersedia
- Login role-based (Mahasiswa/Dosen/Admin)
- Dashboard Mahasiswa:
   - Profil
   - Isi FRS: pilih MK semester aktif, total SKS dinamis, Simpan Draft, Ajukan
   - Status: lihat status FRS dan riwayat persetujuan, Cetak FRS Final (PDF) saat DISETUJUI
- Dashboard Dosen:
   - Daftar pengajuan FRS bimbingan (MENUNGGU)
   - Filter cepat (NIM/Nama)
   - Export Excel dan Print daftar
   - Setujui/Tolak dengan catatan
- Dashboard Admin:
   - Pengaturan semester aktif dan periode FRS (enforce pada saat Ajukan)
   - Master Mahasiswa/Dosen/Mata Kuliah (CRUD) dengan picker User & Dosen Wali
- Validasi backend: batas SKS (min 12, max 24), cek bentrok jadwal sederhana, cek prasyarat sederhana
- Logging aktivitas (login, simpan, ajukan, approve/reject)

## Persyaratan
- JDK 17 (atau kompatibel)
- Maven 3.9+
- Salah satu database:
   - MySQL Server 8+ (default lama), atau
   - SQLite (tanpa server) + sqlite3 CLI untuk inisialisasi (opsional)

## Setup Database
Anda bisa memilih MySQL atau SQLite. Secara default konfigurasi sekarang menggunakan SQLite agar lebih simpel.

### Opsi A: SQLite (disarankan untuk lokal)
1. Set konfigurasi ke SQLite di `src/main/resources/config/db.properties`:
   ```properties
   db.type=sqlite
   db.sqlite.path=frs.db   # lokasi file DB (boleh absolute path)
   ```
2. Inisialisasi skema dan data contoh (butuh sqlite3 CLI):
   ```powershell
   cd "c:\Users\Hype\Documents\Pemrograman Desktop\Project\AplikasiFRSOnline"
   # Jika sqlite3 ada di PATH
   sqlite3 .\frs.db ".read .\src\main\resources\sql\sqlite-schema.sql"
   sqlite3 .\frs.db ".read .\src\main\resources\sql\sqlite-seed.sql"
   # Jika sqlite3 tidak di PATH, gunakan path penuh, misal:
   "&C:\\Program Files\\SQLite\\sqlite3.exe" .\frs.db ".read .\src\main\resources\sql\sqlite-schema.sql"
   "&C:\\Program Files\\SQLite\\sqlite3.exe" .\frs.db ".read .\src\main\resources\sql\sqlite-seed.sql"
   ```

### Opsi B: MySQL
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

   Catatan: `schema.sql` menambahkan UNIQUE KEY untuk memastikan satu akun `user` hanya terkait ke satu Mahasiswa/Dosen. Jika menjalankan ulang `schema.sql` pada database yang sudah ada, dan constraint sudah ada, perintah `ALTER TABLE` bisa gagal. Jalankan secara manual hanya sekali atau sesuaikan sesuai kondisi database Anda.

2. Atur koneksi database di `src/main/resources/config/db.properties`:
   - Untuk SQLite: `db.type=sqlite`, `db.sqlite.path=frs.db`
   - Untuk MySQL: `db.type=mysql` dan set `db.host`, `db.port`, `db.name`, `db.user`, `db.password`

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

## Roadmap Lanjutan
- Dosen: pencarian lanjutan (semester/status), export ke CSV
- Admin: kunci FRS per mahasiswa/angkatan, reset password
- Laporan tambahan dan desain template PDF lebih rapi
- Notifikasi sederhana (mis. saat status FRS berubah)
- Validasi prasyarat berbasis riwayat kelulusan MK

## Keamanan
- Gunakan user/password MySQL yang tidak default di produksi
- Ganti password demo di seed.sql
- Pertimbangkan enkripsi `.properties` atau penyimpanan terpisah untuk kredensial di produksi

## Lisensi
Proyek edukasi kampus. Silakan modifikasi sesuai kebutuhan internal.
