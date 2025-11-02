-- Seed data for Aplikasi FRS Online (SQLite)
PRAGMA foreign_keys = ON;

-- Admin user (password: 'password' as SHA-256 for demo)
INSERT INTO user (username, password_hash, role)
VALUES ('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'ADMIN')
ON CONFLICT(username) DO UPDATE SET role=excluded.role, password_hash=excluded.password_hash;

-- Dosen PA sample
INSERT INTO user (username, password_hash, role)
VALUES ('dosen1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'DOSEN')
ON CONFLICT(username) DO UPDATE SET role=excluded.role, password_hash=excluded.password_hash;

INSERT INTO dosen (nidn, nama, id_user)
VALUES ('D001', 'Dr. Dosen Satu', (SELECT id_user FROM user WHERE username='dosen1'))
ON CONFLICT(nidn) DO UPDATE SET nama=excluded.nama, id_user=excluded.id_user;

-- Mahasiswa sample
INSERT INTO user (username, password_hash, role)
VALUES ('2201001', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'MAHASISWA')
ON CONFLICT(username) DO UPDATE SET role=excluded.role, password_hash=excluded.password_hash;

INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali)
VALUES ('2201001', 'Budi Mahasiswa', 'Informatika', 3, (SELECT id_user FROM user WHERE username='2201001'), 'D001')
ON CONFLICT(nim) DO UPDATE SET nama=excluded.nama, jurusan=excluded.jurusan, semester=excluded.semester, id_user=excluded.id_user, nidn_wali=excluded.nidn_wali;

-- Mata kuliah sample
INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES
('IF101', 'Algoritma dan Pemrograman', 3, 1, NULL),
('IF201', 'Struktur Data', 3, 2, 'IF101'),
('IF301', 'Basis Data', 3, 3, NULL),
('IF302', 'Pemrograman Berorientasi Objek', 3, 3, 'IF201'),
('IF303', 'Jaringan Komputer', 3, 3, NULL),
('IF304', 'Sistem Operasi', 3, 3, NULL),
('IF305', 'Interaksi Manusia dan Komputer', 3, 3, NULL)
ON CONFLICT(kode_mk) DO UPDATE SET nama_mk=excluded.nama_mk, sks=excluded.sks, semester=excluded.semester, prasyarat=excluded.prasyarat;

-- Jadwal sample
INSERT INTO jadwal (kode_mk, hari, jam_mulai, jam_selesai, ruang) VALUES
('IF301', 'SENIN', '08:00:00', '09:40:00', 'R301'),
('IF302', 'SENIN', '10:00:00', '11:40:00', 'R302'),
('IF303', 'RABU', '08:00:00', '09:40:00', 'R303'),
('IF304', 'RABU', '10:00:00', '11:40:00', 'R304'),
('IF305', 'JUMAT', '08:00:00', '09:40:00', 'R305');
-- For jadwal we rely on (kode_mk, time) uniqueness not enforced; reruns may duplicate. Clean first if needed.

-- Default settings
INSERT INTO settings (id, semester_aktif, frs_aktif)
VALUES (1, 3, 1)
ON CONFLICT(id) DO UPDATE SET semester_aktif=excluded.semester_aktif, frs_aktif=excluded.frs_aktif;
