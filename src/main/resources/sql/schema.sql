-- Schema for Aplikasi FRS Online (MySQL)
-- Run with a user that has CREATE privileges

CREATE DATABASE IF NOT EXISTS frs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE frs_db;

-- Users table: authentication + roles
CREATE TABLE IF NOT EXISTS user (
  id_user INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL,
  role ENUM('ADMIN','DOSEN','MAHASISWA') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Mahasiswa
CREATE TABLE IF NOT EXISTS mahasiswa (
  nim VARCHAR(20) PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  jurusan VARCHAR(100) NOT NULL,
  semester INT NOT NULL DEFAULT 1,
  id_user INT NOT NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Dosen
CREATE TABLE IF NOT EXISTS dosen (
  nidn VARCHAR(20) PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  id_user INT NOT NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Matakuliah
CREATE TABLE IF NOT EXISTS mata_kuliah (
  kode_mk VARCHAR(20) PRIMARY KEY,
  nama_mk VARCHAR(150) NOT NULL,
  sks INT NOT NULL,
  semester INT NOT NULL,
  prasyarat VARCHAR(20) NULL,
  CONSTRAINT fk_mk_prasyarat FOREIGN KEY (prasyarat) REFERENCES mata_kuliah(kode_mk) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Jadwal
CREATE TABLE IF NOT EXISTS jadwal (
  id_jadwal INT AUTO_INCREMENT PRIMARY KEY,
  kode_mk VARCHAR(20) NOT NULL,
  hari ENUM('SENIN','SELASA','RABU','KAMIS','JUMAT','SABTU') NOT NULL,
  jam_mulai TIME NOT NULL,
  jam_selesai TIME NOT NULL,
  ruang VARCHAR(30) NOT NULL,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- FRS header
CREATE TABLE IF NOT EXISTS frs (
  id_frs INT AUTO_INCREMENT PRIMARY KEY,
  nim VARCHAR(20) NOT NULL,
  semester INT NOT NULL,
  total_sks INT NOT NULL DEFAULT 0,
  status ENUM('DRAFT','MENUNGGU','DISETUJUI','DITOLAK','TERKUNCI') NOT NULL DEFAULT 'DRAFT',
  tanggal_pengajuan DATETIME NULL,
  locked_by_admin TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (nim) REFERENCES mahasiswa(nim) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- FRS detail
CREATE TABLE IF NOT EXISTS detail_frs (
  id_detail INT AUTO_INCREMENT PRIMARY KEY,
  id_frs INT NOT NULL,
  kode_mk VARCHAR(20) NOT NULL,
  UNIQUE KEY uk_frs_mk (id_frs, kode_mk),
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Persetujuan FRS (oleh dosen wali)
CREATE TABLE IF NOT EXISTS persetujuan_frs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_frs INT NOT NULL,
  id_dosen VARCHAR(20) NOT NULL, -- NIDN
  status ENUM('MENUNGGU','DISETUJUI','DITOLAK') NOT NULL DEFAULT 'MENUNGGU',
  catatan TEXT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_dosen) REFERENCES dosen(nidn) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Aktivitas log
CREATE TABLE IF NOT EXISTS log_aktivitas (
  id_log BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NULL,
  aktivitas VARCHAR(255) NOT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Indexes for performance
CREATE INDEX idx_user_role ON user(role);
CREATE INDEX idx_mhs_semester ON mahasiswa(semester);
CREATE INDEX idx_frs_nim_semester ON frs(nim, semester);
CREATE INDEX idx_jadwal_kode_mk ON jadwal(kode_mk);
