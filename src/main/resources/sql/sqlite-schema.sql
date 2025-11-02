-- SQLite schema for Aplikasi FRS Online
-- Run once to initialize the database file

PRAGMA foreign_keys = ON;

-- Users table: authentication + roles
CREATE TABLE IF NOT EXISTS user (
  id_user INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL CHECK (role IN ('ADMIN','DOSEN','MAHASISWA')),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Dosen
CREATE TABLE IF NOT EXISTS dosen (
  nidn TEXT PRIMARY KEY,
  nama TEXT NOT NULL,
  id_user INTEGER NOT NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Mahasiswa
CREATE TABLE IF NOT EXISTS mahasiswa (
  nim TEXT PRIMARY KEY,
  nama TEXT NOT NULL,
  jurusan TEXT NOT NULL,
  semester INTEGER NOT NULL DEFAULT 1,
  id_user INTEGER NOT NULL,
  nidn_wali TEXT NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (nidn_wali) REFERENCES dosen(nidn) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Matakuliah
CREATE TABLE IF NOT EXISTS mata_kuliah (
  kode_mk TEXT PRIMARY KEY,
  nama_mk TEXT NOT NULL,
  sks INTEGER NOT NULL,
  semester INTEGER NOT NULL,
  prasyarat TEXT NULL,
  FOREIGN KEY (prasyarat) REFERENCES mata_kuliah(kode_mk) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Jadwal
CREATE TABLE IF NOT EXISTS jadwal (
  id_jadwal INTEGER PRIMARY KEY AUTOINCREMENT,
  kode_mk TEXT NOT NULL,
  hari TEXT NOT NULL CHECK (hari IN ('SENIN','SELASA','RABU','KAMIS','JUMAT','SABTU')),
  jam_mulai TIME NOT NULL,
  jam_selesai TIME NOT NULL,
  ruang TEXT NOT NULL,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE CASCADE ON UPDATE CASCADE
);

-- FRS header
CREATE TABLE IF NOT EXISTS frs (
  id_frs INTEGER PRIMARY KEY AUTOINCREMENT,
  nim TEXT NOT NULL,
  semester INTEGER NOT NULL,
  total_sks INTEGER NOT NULL DEFAULT 0,
  status TEXT NOT NULL CHECK (status IN ('DRAFT','MENUNGGU','DISETUJUI','DITOLAK','TERKUNCI')) DEFAULT 'DRAFT',
  tanggal_pengajuan DATETIME NULL,
  locked_by_admin INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (nim) REFERENCES mahasiswa(nim) ON DELETE CASCADE ON UPDATE CASCADE
);

-- FRS detail
CREATE TABLE IF NOT EXISTS detail_frs (
  id_detail INTEGER PRIMARY KEY AUTOINCREMENT,
  id_frs INTEGER NOT NULL,
  kode_mk TEXT NOT NULL,
  UNIQUE (id_frs, kode_mk),
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Persetujuan FRS (oleh dosen wali)
CREATE TABLE IF NOT EXISTS persetujuan_frs (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_frs INTEGER NOT NULL,
  id_dosen TEXT NOT NULL, -- NIDN
  status TEXT NOT NULL CHECK (status IN ('MENUNGGU','DISETUJUI','DITOLAK')) DEFAULT 'MENUNGGU',
  catatan TEXT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_dosen) REFERENCES dosen(nidn) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Aktivitas log
CREATE TABLE IF NOT EXISTS log_aktivitas (
  id_log INTEGER PRIMARY KEY AUTOINCREMENT,
  id_user INTEGER NULL,
  aktivitas TEXT NOT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Permintaan ubah password (harus disetujui admin)
CREATE TABLE IF NOT EXISTS password_change_request (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  new_password_hash TEXT NOT NULL,
  status TEXT NOT NULL CHECK (status IN ('PENDING','APPROVED','REJECTED')) DEFAULT 'PENDING',
  requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  approved_by INTEGER NULL,
  approved_at TIMESTAMP NULL,
  rejected_reason TEXT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (approved_by) REFERENCES user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Settings (singleton row)
CREATE TABLE IF NOT EXISTS settings (
  id INTEGER PRIMARY KEY,
  semester_aktif INTEGER NOT NULL DEFAULT 1,
  frs_aktif INTEGER NOT NULL DEFAULT 1,
  tanggal_mulai DATETIME NULL,
  tanggal_selesai DATETIME NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_user_role ON user(role);
CREATE INDEX IF NOT EXISTS idx_mhs_semester ON mahasiswa(semester);
CREATE UNIQUE INDEX IF NOT EXISTS uk_mahasiswa_id_user ON mahasiswa(id_user);
CREATE UNIQUE INDEX IF NOT EXISTS uk_dosen_id_user ON dosen(id_user);
CREATE INDEX IF NOT EXISTS idx_frs_nim_semester ON frs(nim, semester);
CREATE INDEX IF NOT EXISTS idx_jadwal_kode_mk ON jadwal(kode_mk);
