-- Complete MySQL Setup Script for Aplikasi FRS Online
-- Run this script in phpMyAdmin or MySQL CLI to create database and import data

-- ==========================================
-- PART 1: CREATE DATABASE AND TABLES
-- ==========================================

CREATE DATABASE IF NOT EXISTS frs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE frs_db;

-- Drop existing tables if needed (comment out for production)
DROP TABLE IF EXISTS password_change_request;
DROP TABLE IF EXISTS log_aktivitas;
DROP TABLE IF EXISTS persetujuan_frs;
DROP TABLE IF EXISTS detail_frs;
DROP TABLE IF EXISTS frs;
DROP TABLE IF EXISTS jadwal;
DROP TABLE IF EXISTS mata_kuliah;
DROP TABLE IF EXISTS mahasiswa;
DROP TABLE IF EXISTS dosen;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS user;

-- Users table
CREATE TABLE user (
  id_user INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL,
  role ENUM('ADMIN','DOSEN','MAHASISWA') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Dosen
CREATE TABLE dosen (
  nidn VARCHAR(20) PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  id_user INT NOT NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Mahasiswa
CREATE TABLE mahasiswa (
  nim VARCHAR(20) PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  jurusan VARCHAR(100) NOT NULL,
  semester INT NOT NULL DEFAULT 1,
  id_user INT NOT NULL,
  nidn_wali VARCHAR(20) NULL,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (nidn_wali) REFERENCES dosen(nidn) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Matakuliah
CREATE TABLE mata_kuliah (
  kode_mk VARCHAR(20) PRIMARY KEY,
  nama_mk VARCHAR(150) NOT NULL,
  sks INT NOT NULL,
  semester INT NOT NULL,
  prasyarat VARCHAR(20) NULL,
  CONSTRAINT fk_mk_prasyarat FOREIGN KEY (prasyarat) REFERENCES mata_kuliah(kode_mk) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Jadwal
CREATE TABLE jadwal (
  id_jadwal INT AUTO_INCREMENT PRIMARY KEY,
  kode_mk VARCHAR(20) NOT NULL,
  hari ENUM('SENIN','SELASA','RABU','KAMIS','JUMAT','SABTU') NOT NULL,
  jam_mulai TIME NOT NULL,
  jam_selesai TIME NOT NULL,
  ruang VARCHAR(30) NOT NULL,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- FRS header
CREATE TABLE frs (
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
CREATE TABLE detail_frs (
  id_detail INT AUTO_INCREMENT PRIMARY KEY,
  id_frs INT NOT NULL,
  kode_mk VARCHAR(20) NOT NULL,
  UNIQUE KEY uk_frs_mk (id_frs, kode_mk),
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode_mk) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Persetujuan FRS
CREATE TABLE persetujuan_frs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_frs INT NOT NULL,
  id_dosen VARCHAR(20) NOT NULL,
  status ENUM('MENUNGGU','DISETUJUI','DITOLAK') NOT NULL DEFAULT 'MENUNGGU',
  catatan TEXT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_frs) REFERENCES frs(id_frs) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_dosen) REFERENCES dosen(nidn) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Aktivitas log
CREATE TABLE log_aktivitas (
  id_log BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_user INT NULL,
  aktivitas VARCHAR(255) NOT NULL,
  waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Password change request
CREATE TABLE password_change_request (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  new_password_hash VARCHAR(128) NOT NULL,
  status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
  requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  approved_by INT NULL,
  approved_at TIMESTAMP NULL,
  rejected_reason TEXT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id_user) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (approved_by) REFERENCES user(id_user) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Settings
CREATE TABLE settings (
  id INT PRIMARY KEY DEFAULT 1,
  semester_aktif INT NOT NULL DEFAULT 1,
  frs_aktif TINYINT(1) NOT NULL DEFAULT 1,
  tanggal_mulai DATETIME NULL,
  tanggal_selesai DATETIME NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Indexes
CREATE INDEX idx_user_role ON user(role);
CREATE INDEX idx_mhs_semester ON mahasiswa(semester);
ALTER TABLE mahasiswa ADD UNIQUE KEY uk_mahasiswa_id_user (id_user);
ALTER TABLE dosen ADD UNIQUE KEY uk_dosen_id_user (id_user);
CREATE INDEX idx_frs_nim_semester ON frs(nim, semester);
CREATE INDEX idx_jadwal_kode_mk ON jadwal(kode_mk);

-- ==========================================
-- PART 2: IMPORT DATA FROM SQLITE
-- ==========================================

-- Users (Password: 'password' = 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8)
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES
(1, 'admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'ADMIN', '2025-11-02 05:00:17'),
(2, 'dosen1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'DOSEN', '2025-11-02 05:00:17'),
(3, '2201001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-11-02 05:00:17'),
(7, '20240810091', 'b9852cfa767cb0eb6de7c6c29e4a402e329e185ee8dd47041dd9972deef90851', 'MAHASISWA', '2025-11-02 06:08:30'),
(11, '20240810062', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-11-03 03:52:38'),
(12, '20240810061', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-02 03:10:00'),
(13, '20240810020', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-16 04:01:33'),
(23, '20240810001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-29 13:44:44'),
(24, '20240810034', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-30 04:07:47'),
(25, '20240810019', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:16:20'),
(26, '20240810047', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:17:33'),
(27, '20240810121', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:18:42');

-- Dosen
INSERT INTO dosen (nidn, nama, id_user) VALUES
('D001', 'Rio Andriyat Krisdiawan S.kom M.kom', 2);

-- Mahasiswa
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES
('20240810091', 'Arie Muhamad Syahrial', 'Teknik informatika', 3, 7, 'D001'),
('20240810062', 'Muhamad Haqil Abdilah', 'Teknik Informatika', 3, 11, 'D001'),
('20240810061', 'Ryan Ananda Putra', 'Teknik Informatika', 3, 12, 'D001'),
('20240810020', 'Yeyen Ai Nurhidayati', 'Teknik Informatika', 3, 13, 'D001'),
('20240810034', 'Muhammad Fahmi Firmansyah', 'Teknik Informatika', 3, 24, 'D001'),
('20240810019', 'Muhamad Rafly Irhandy', 'Teknik Informatika', 3, 25, 'D001'),
('20240810047', 'Fajar Halim', 'Teknik Informatika', 3, 26, 'D001'),
('20240810121', 'Mu''ammar Fadhillah', 'Teknik Informatika', 3, 27, 'D001');

-- Mata Kuliah
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES
('IF101', 'Algoritma dan Pemrograman', 3, 1, NULL),
('IF201', 'Struktur Data', 3, 2, 'IF101'),
('IF301', 'Basis Data', 3, 3, NULL),
('IF302', 'Pemrograman Berorientasi Objek', 3, 3, 'IF201'),
('IF303', 'Jaringan Komputer', 3, 3, NULL),
('IF304', 'Sistem Operasi', 3, 3, NULL),
('IF305', 'Interaksi Manusia dan Komputer', 3, 3, NULL),
('if2011', 'Pemrograman desktop', 3, 3, NULL);

-- Jadwal (only unique ones)
INSERT INTO jadwal (kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES
('IF301', 'SENIN', '08:00:00', '09:40:00', 'R301'),
('IF302', 'SENIN', '10:00:00', '11:40:00', 'R302'),
('IF303', 'RABU', '08:00:00', '09:40:00', 'R303'),
('IF304', 'RABU', '10:00:00', '11:40:00', 'R304'),
('IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');

-- FRS (dengan konversi tanggal yang benar)
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES
(2, '20240810091', 3, 12, 'DISETUJUI', '2025-11-02 06:09:27', 0),
(3, '20240810062', 3, 0, 'DRAFT', NULL, 0),
(4, '20240810061', 3, 0, 'DRAFT', NULL, 0),
(5, '20240810020', 3, 15, 'MENUNGGU', '2026-01-12 08:57:30', 0),
(6, '20240810034', 3, 12, 'MENUNGGU', '2026-01-06 02:28:16', 0),
(7, '20240810047', 3, 0, 'DRAFT', NULL, 0);

-- Detail FRS
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES
(1, 2, 'IF303'),
(2, 2, 'IF304'),
(3, 2, 'IF305'),
(4, 2, 'if2011'),
(5, 6, 'IF301'),
(6, 6, 'IF303'),
(7, 6, 'IF304'),
(8, 6, 'IF305'),
(9, 5, 'IF301'),
(10, 5, 'IF303'),
(11, 5, 'IF304'),
(12, 5, 'IF305'),
(13, 5, 'if2011');

-- Persetujuan FRS
INSERT INTO persetujuan_frs (id, id_frs, id_dosen, status, catatan, waktu) VALUES
(1, 2, 'D001', 'DISETUJUI', '', '2025-11-02 06:09:49');

-- Settings
INSERT INTO settings (id, semester_aktif, frs_aktif, tanggal_mulai, tanggal_selesai) VALUES
(1, 3, 1, '2025-12-31 17:00:00', '2026-01-29 17:00:00');

-- ==========================================
-- DONE!
-- ==========================================
SELECT 'Database frs_db created and data imported successfully!' AS Status;
