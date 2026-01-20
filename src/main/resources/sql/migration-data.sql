-- Data migration from SQLite to MySQL
-- Generated automatically
USE frs_db;
SET FOREIGN_KEY_CHECKS = 0;

-- Table: user
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (1, 'admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'ADMIN', '2025-11-02 05:00:17');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (2, 'dosen1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'DOSEN', '2025-11-02 05:00:17');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (3, '2201001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-11-02 05:00:17');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (7, '20240810091', 'b9852cfa767cb0eb6de7c6c29e4a402e329e185ee8dd47041dd9972deef90851', 'MAHASISWA', '2025-11-02 06:08:30');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (11, '20240810062', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-11-03 03:52:38');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (12, '20240810061', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-02 03:10:00');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (13, '20240810020', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-16 04:01:33');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (23, '20240810001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-29 13:44:44');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (24, '20240810034', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2025-12-30 04:07:47');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (25, '20240810019', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:16:20');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (26, '20240810047', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:17:33');
INSERT INTO user (id_user, username, password_hash, role, created_at) VALUES (27, '20240810121', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA', '2026-01-06 02:18:42');

-- Table: dosen
INSERT INTO dosen (nidn, nama, id_user) VALUES ('D001', 'Rio Andriyat Krisdiawan S.kom M.kom', 2);

-- Table: mahasiswa
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810091', 'Arie Muhamad Syahrial', 'Teknik informatika', 3, 7, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810062', 'Muhamad Haqil Abdilah', 'Teknik Informatika', 3, 11, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810061', 'Ryan Ananda Putra', 'Teknik Informatika', 3, 12, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810020', 'Yeyen Ai Nurhidayati', 'Teknik Informatika', 3, 13, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810034', 'Muhammad Fahmi Firmansyah', 'Teknik Informatika', 3, 24, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810019', 'Muhamad Rafly Irhandy', 'Teknik Informatika', 3, 25, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810047', 'Fajar Halim', 'Teknik Informatika', 3, 26, 'D001');
INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES ('20240810121', 'Mu''ammar Fadhillah', 'Teknik Informatika', 3, 27, 'D001');

-- Table: mata_kuliah
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF101', 'Algoritma dan Pemrograman', 3, 1, NULL);
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF201', 'Struktur Data', 3, 2, 'IF101');
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF301', 'Basis Data', 3, 3, NULL);
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF302', 'Pemrograman Berorientasi Objek', 3, 3, 'IF201');
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF303', 'Jaringan Komputer', 3, 3, NULL);
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF304', 'Sistem Operasi', 3, 3, NULL);
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('IF305', 'Interaksi Manusia dan Komputer', 3, 3, NULL);
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES ('if2011', 'Pemrograman desktop', 3, 3, NULL);

-- Table: jadwal
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (1, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (2, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (3, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (4, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (5, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (6, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (7, 'IF304', 'RABU', '10:00:00', '11:40:00', 'R304');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (8, 'IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (9, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (10, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (11, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (12, 'IF304', 'RABU', '10:00:00', '11:40:00', 'R304');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (13, 'IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (14, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (15, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (16, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (17, 'IF304', 'RABU', '10:00:00', '11:40:00', 'R304');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (18, 'IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (19, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (20, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (21, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (22, 'IF304', 'RABU', '10:00:00', '11:40:00', 'R304');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (23, 'IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (24, 'IF301', 'SENIN', '08:00:00', '09:40:00', 'R301');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (25, 'IF302', 'SENIN', '10:00:00', '11:40:00', 'R302');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (26, 'IF303', 'RABU', '08:00:00', '09:40:00', 'R303');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (27, 'IF304', 'RABU', '10:00:00', '11:40:00', 'R304');
INSERT INTO jadwal (id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES (28, 'IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');

-- Table: frs
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (2, '20240810091', 3, 12, 'DISETUJUI', 1762063767620, 0);
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (3, '20240810062', 3, 0, 'DRAFT', NULL, 0);
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (4, '20240810061', 3, 0, 'DRAFT', NULL, 0);
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (5, '20240810020', 3, 15, 'MENUNGGU', 1768208250153, 0);
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (6, '20240810034', 3, 12, 'MENUNGGU', 1767666496389, 0);
INSERT INTO frs (id_frs, nim, semester, total_sks, status, tanggal_pengajuan, locked_by_admin) VALUES (7, '20240810047', 3, 0, 'DRAFT', NULL, 0);

-- Table: detail_frs
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (1, 2, 'IF303');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (2, 2, 'IF304');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (3, 2, 'IF305');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (4, 2, 'if2011');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (5, 6, 'IF301');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (6, 6, 'IF303');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (7, 6, 'IF304');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (8, 6, 'IF305');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (9, 5, 'IF301');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (10, 5, 'IF303');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (11, 5, 'IF304');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (12, 5, 'IF305');
INSERT INTO detail_frs (id_detail, id_frs, kode_mk) VALUES (13, 5, 'if2011');

-- Table: persetujuan_frs
INSERT INTO persetujuan_frs (id, id_frs, id_dosen, status, catatan, waktu) VALUES (1, 2, 'D001', 'DISETUJUI', '', '2025-11-02 06:09:49');

-- Table: log_aktivitas
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (1, 2, 'LOGIN', '2025-11-02 05:02:19');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (2, 3, 'LOGIN', '2025-11-02 05:15:52');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (3, 3, 'LOGIN', '2025-11-02 05:30:32');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (4, 3, 'LOGIN', '2025-11-02 05:39:41');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (5, 2, 'LOGIN', '2025-11-02 05:41:16');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (6, 1, 'LOGIN', '2025-11-02 05:41:39');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (7, 1, 'LOGIN', '2025-11-02 06:07:11');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (8, 7, 'LOGIN', '2025-11-02 06:09:17');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (9, 7, 'SUBMIT_FRS:2', '2025-11-02 06:09:27');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (10, 2, 'LOGIN', '2025-11-02 06:09:42');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (11, 2, 'APPROVE_FRS:2', '2025-11-02 06:09:49');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (12, 1, 'LOGIN', '2025-11-02 06:10:04');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (13, 1, 'LOGIN', '2025-11-02 07:04:21');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (14, 7, 'LOGIN', '2025-11-02 23:36:07');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (15, 7, 'LOGIN', '2025-11-02 23:38:55');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (16, 7, 'LOGIN', '2025-11-02 23:49:26');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (17, 7, 'LOGIN', '2025-11-03 00:02:03');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (18, 1, 'LOGIN', '2025-11-03 00:02:23');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (19, 2, 'LOGIN', '2025-11-03 00:02:47');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (20, 7, 'LOGIN', '2025-11-03 00:03:03');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (21, 7, 'LOGIN', '2025-11-03 01:14:48');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (22, 1, 'LOGIN', '2025-11-03 01:15:04');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (23, 2, 'LOGIN', '2025-11-03 01:15:39');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (24, 1, 'LOGIN', '2025-11-03 03:51:48');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (25, 11, 'LOGIN', '2025-11-03 03:52:59');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (26, 1, 'LOGIN', '2025-12-02 03:09:09');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (27, 12, 'LOGIN', '2025-12-02 03:10:29');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (28, 1, 'LOGIN', '2025-12-02 03:11:07');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (29, 7, 'LOGIN', '2025-12-08 06:43:12');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (30, 7, 'LOGIN', '2025-12-09 03:51:13');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (31, 1, 'LOGIN', '2025-12-09 03:52:15');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (32, 1, 'LOGIN', '2025-12-10 07:16:17');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (33, 7, 'LOGIN', '2025-12-10 07:17:13');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (34, 7, 'LOGIN', '2025-12-16 03:06:52');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (35, 1, 'LOGIN', '2025-12-16 03:07:28');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (36, 7, 'LOGIN', '2025-12-16 03:10:25');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (37, 1, 'LOGIN', '2025-12-16 04:00:38');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (38, 13, 'LOGIN', '2025-12-16 04:01:48');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (39, 7, 'LOGIN', '2025-12-17 04:39:09');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (40, 1, 'LOGIN', '2025-12-17 04:45:27');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (41, 1, 'APPROVE_PWD_REQ:1', '2025-12-17 04:45:34');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (42, 7, 'LOGIN', '2025-12-17 04:45:46');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (43, 7, 'LOGIN', '2025-12-17 06:36:51');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (44, 1, 'LOGIN', '2025-12-17 06:37:05');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (45, 1, 'LOGIN', '2025-12-17 06:37:13');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (46, 7, 'LOGIN', '2025-12-22 10:44:58');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (47, 2, 'LOGIN', '2025-12-22 10:45:36');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (48, 1, 'LOGIN', '2025-12-22 10:45:51');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (49, 7, 'LOGIN', '2025-12-24 06:01:16');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (50, 2, 'LOGIN', '2025-12-24 06:02:18');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (51, 1, 'LOGIN', '2025-12-24 06:02:51');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (52, 7, 'LOGIN', '2025-12-24 06:17:01');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (53, 2, 'LOGIN', '2025-12-24 06:17:39');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (54, 1, 'LOGIN', '2025-12-24 06:18:39');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (55, 7, 'LOGIN', '2025-12-29 03:51:50');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (56, 7, 'LOGIN', '2025-12-29 13:43:50');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (57, 1, 'LOGIN', '2025-12-29 13:44:12');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (58, 1, 'LOGIN', '2025-12-30 04:07:15');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (59, 24, 'LOGIN', '2025-12-30 04:08:01');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (60, 7, 'LOGIN', '2025-12-30 04:08:23');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (61, 7, 'LOGIN', '2026-01-05 23:57:07');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (62, 2, 'LOGIN', '2026-01-05 23:57:25');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (63, 1, 'LOGIN', '2026-01-05 23:57:38');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (64, 1, 'LOGIN', '2026-01-06 00:04:55');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (65, 1, 'LOGIN', '2026-01-06 00:04:59');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (66, 2, 'LOGIN', '2026-01-06 00:05:19');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (67, 13, 'LOGIN', '2026-01-06 00:05:41');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (68, 7, 'LOGIN', '2026-01-06 00:07:20');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (69, 1, 'LOGIN', '2026-01-06 00:07:52');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (70, 7, 'LOGIN', '2026-01-06 00:23:17');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (71, 1, 'LOGIN', '2026-01-06 00:28:01');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (72, 24, 'LOGIN', '2026-01-06 02:13:29');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (73, 7, 'LOGIN', '2026-01-06 02:13:51');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (74, 1, 'LOGIN', '2026-01-06 02:14:10');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (75, 2, 'LOGIN', '2026-01-06 02:14:30');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (76, 1, 'LOGIN', '2026-01-06 02:14:51');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (77, 1, 'LOGIN', '2026-01-06 02:15:06');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (78, 2, 'LOGIN', '2026-01-06 02:19:21');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (79, 1, 'LOGIN', '2026-01-06 02:19:32');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (80, 24, 'LOGIN', '2026-01-06 02:20:02');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (81, 1, 'LOGIN', '2026-01-06 02:24:30');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (82, 1, 'LOGIN', '2026-01-06 02:26:17');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (83, 24, 'LOGIN', '2026-01-06 02:27:52');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (84, 24, 'SUBMIT_FRS:6', '2026-01-06 02:28:16');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (85, 2, 'LOGIN', '2026-01-06 02:28:39');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (86, 24, 'LOGIN', '2026-01-06 02:36:04');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (87, 2, 'LOGIN', '2026-01-06 02:36:18');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (88, 7, 'LOGIN', '2026-01-06 02:37:07');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (89, 2, 'LOGIN', '2026-01-06 02:37:25');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (90, 2, 'LOGIN', '2026-01-06 02:43:08');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (91, 7, 'LOGIN', '2026-01-09 09:12:06');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (92, 1, 'LOGIN', '2026-01-09 09:12:22');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (93, 7, 'LOGIN', '2026-01-12 02:39:48');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (94, 1, 'LOGIN', '2026-01-12 02:40:01');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (95, 1, 'LOGIN', '2026-01-12 08:42:03');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (96, 13, 'LOGIN', '2026-01-12 08:56:42');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (97, 13, 'SUBMIT_FRS:5', '2026-01-12 08:57:30');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (98, 2, 'LOGIN', '2026-01-12 08:57:42');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (99, 1, 'LOGIN', '2026-01-15 08:06:27');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (100, 26, 'LOGIN', '2026-01-15 08:06:48');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (101, 7, 'LOGIN', '2026-01-19 07:58:28');
INSERT INTO log_aktivitas (id_log, id_user, aktivitas, waktu) VALUES (102, 2, 'LOGIN', '2026-01-19 07:58:44');

-- Table: password_change_request
INSERT INTO password_change_request (id, user_id, new_password_hash, status, requested_at, approved_by, approved_at, rejected_reason) VALUES (1, 7, 'b9852cfa767cb0eb6de7c6c29e4a402e329e185ee8dd47041dd9972deef90851', 'APPROVED', '2025-12-17 04:45:12', 1, '2025-12-17 04:45:34', NULL);

-- Table: settings
INSERT INTO settings (id, semester_aktif, frs_aktif, tanggal_mulai, tanggal_selesai, updated_at) VALUES (1, 3, 1, 1767204000000, 1769709600000, '2025-11-02 05:00:17');


SET FOREIGN_KEY_CHECKS = 1;
-- Migration complete
