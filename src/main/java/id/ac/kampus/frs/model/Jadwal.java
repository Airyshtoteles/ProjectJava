package id.ac.kampus.frs.model;

import java.time.LocalTime;

public class Jadwal {
    private int idJadwal;
    private String kodeMk;
    private String hari; // SENIN..SABTU
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String ruang;

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }

    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public LocalTime getJamMulai() { return jamMulai; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }

    public LocalTime getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(LocalTime jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getRuang() { return ruang; }
    public void setRuang(String ruang) { this.ruang = ruang; }
}
