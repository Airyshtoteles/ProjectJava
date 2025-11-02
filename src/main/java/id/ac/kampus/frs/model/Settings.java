package id.ac.kampus.frs.model;

import java.time.LocalDateTime;

public class Settings {
    private int semesterAktif;
    private boolean frsAktif;
    private LocalDateTime tanggalMulai;
    private LocalDateTime tanggalSelesai;

    public int getSemesterAktif() { return semesterAktif; }
    public void setSemesterAktif(int semesterAktif) { this.semesterAktif = semesterAktif; }
    public boolean isFrsAktif() { return frsAktif; }
    public void setFrsAktif(boolean frsAktif) { this.frsAktif = frsAktif; }
    public LocalDateTime getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDateTime tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public LocalDateTime getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDateTime tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
}
