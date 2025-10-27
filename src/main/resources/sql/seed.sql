-- Seed data for Aplikasi FRS Online
USE frs_db;

-- Passwords below are SHA-256 of 'password' for demo only
-- Admin user
INSERT INTO user (username, password_hash, role) VALUES
('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'ADMIN')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- Dosen PA sample
INSERT INTO user (username, password_hash, role) VALUES
('dosen1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'DOSEN')
ON DUPLICATE KEY UPDATE role=VALUES(role);
INSERT INTO dosen (nidn, nama, id_user) SELECT 'D001', 'Dr. Dosen Satu', id_user FROM user WHERE username='dosen1'
ON DUPLICATE KEY UPDATE nama=VALUES(nama);

-- Mahasiswa sample
INSERT INTO user (username, password_hash, role) VALUES
('2201001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA')
ON DUPLICATE KEY UPDATE role=VALUES(role);
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user) 
SELECT '2201001', 'Budi Mahasiswa', 'Informatika', 3, id_user FROM user WHERE username='2201001'
ON DUPLICATE KEY UPDATE nama=VALUES(nama), jurusan=VALUES(jurusan), semester=VALUES(semester);

-- Mata kuliah sample
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES
('IF101', 'Algoritma dan Pemrograman', 3, 1, NULL),
('IF201', 'Struktur Data', 3, 2, 'IF101'),
('IF301', 'Basis Data', 3, 3, NULL),
('IF302', 'Pemrograman Berorientasi Objek', 3, 3, 'IF201'),
('IF303', 'Jaringan Komputer', 3, 3, NULL)
ON DUPLICATE KEY UPDATE nama_mk=VALUES(nama_mk), sks=VALUES(sks), semester=VALUES(semester), prasyarat=VALUES(prasyarat);

-- Jadwal sample
INSERT INTO jadwal (kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES
('IF301', 'SENIN', '08:00:00', '09:40:00', 'R301'),
('IF302', 'SENIN', '10:00:00', '11:40:00', 'R302'),
('IF303', 'RABU', '08:00:00', '09:40:00', 'R303')
ON DUPLICATE KEY UPDATE ruang=VALUES(ruang);
