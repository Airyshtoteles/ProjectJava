package id.ac.kampus.frs.model;

import java.time.LocalDateTime;

public class PersetujuanFRS {
    private int id;
    private int idFrs;
    private String idDosen; // NIDN
    private Status status;
    private String catatan;
    private LocalDateTime waktu;

    public enum Status { MENUNGGU, DISETUJUI, DITOLAK }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFrs() { return idFrs; }
    public void setIdFrs(int idFrs) { this.idFrs = idFrs; }

    public String getIdDosen() { return idDosen; }
    public void setIdDosen(String idDosen) { this.idDosen = idDosen; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}
