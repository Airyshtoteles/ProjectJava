package id.ac.kampus.frs.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FRS {
    private int idFrs;
    private String nim;
    private int semester;
    private int totalSks;
    private Status status;
    private LocalDateTime tanggalPengajuan;
    private boolean lockedByAdmin;
    private List<DetailFRS> details = new ArrayList<>();

    public enum Status { DRAFT, MENUNGGU, DISETUJUI, DITOLAK, TERKUNCI }

    public int getIdFrs() { return idFrs; }
    public void setIdFrs(int idFrs) { this.idFrs = idFrs; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getTotalSks() { return totalSks; }
    public void setTotalSks(int totalSks) { this.totalSks = totalSks; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getTanggalPengajuan() { return tanggalPengajuan; }
    public void setTanggalPengajuan(LocalDateTime tanggalPengajuan) { this.tanggalPengajuan = tanggalPengajuan; }

    public boolean isLockedByAdmin() { return lockedByAdmin; }
    public void setLockedByAdmin(boolean lockedByAdmin) { this.lockedByAdmin = lockedByAdmin; }

    public List<DetailFRS> getDetails() { return details; }
}
