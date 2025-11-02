package id.ac.kampus.frs.service;

import id.ac.kampus.frs.dao.FRSDAO;
import id.ac.kampus.frs.dao.JadwalDAO;
import id.ac.kampus.frs.dao.MataKuliahDAO;
import id.ac.kampus.frs.model.FRS;
import id.ac.kampus.frs.model.MataKuliah;
import id.ac.kampus.frs.util.ActivityLogger;
import id.ac.kampus.frs.dao.SettingsDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class FRSService {
    private final FRSDAO frsDAO = new FRSDAO();
    private final MataKuliahDAO mkDAO = new MataKuliahDAO();
    private final JadwalDAO jadwalDAO = new JadwalDAO();
    private final SettingsDAO settingsDAO = new SettingsDAO();

    private final int MIN_SKS = 12;
    private final int MAX_SKS = 24;

    public FRS getOrCreateDraft(String nim, int semester) throws SQLException {
        return frsDAO.getOrCreateDraft(nim, semester);
    }

    public List<MataKuliah> listMkSemester(int semester) throws SQLException {
        return mkDAO.listBySemester(semester);
    }

    public void saveDraft(FRS frs, List<String> selectedKodeMk, int idUser) throws Exception {
        validate(frs.getNim(), selectedKodeMk);
        int total = hitungTotalSks(selectedKodeMk);
        frsDAO.replaceDetails(frs.getIdFrs(), selectedKodeMk);
        frsDAO.updateSummary(frs.getIdFrs(), total, FRS.Status.DRAFT, null);
        ActivityLogger.log(idUser, "SIMPAN_DRAFT_FRS:" + frs.getIdFrs());
    }

    public void submit(FRS frs, List<String> selectedKodeMk, int idUser) throws Exception {
        // Enforce settings: only allow submit when FRS period is active
        if (!settingsDAO.isWithinWindow()) {
            throw new IllegalStateException("Periode FRS tidak aktif saat ini");
        }
        validate(frs.getNim(), selectedKodeMk);
        int total = hitungTotalSks(selectedKodeMk);
        frsDAO.replaceDetails(frs.getIdFrs(), selectedKodeMk);
        frsDAO.updateSummary(frs.getIdFrs(), total, FRS.Status.MENUNGGU, LocalDateTime.now());
        ActivityLogger.log(idUser, "SUBMIT_FRS:" + frs.getIdFrs());
    }

    private int hitungTotalSks(List<String> kodeMks) throws SQLException {
        int total = 0;
        for (String kode : kodeMks) {
            MataKuliah mk = mkDAO.findByKode(kode);
            if (mk != null) total += mk.getSks();
        }
        return total;
    }

    private void validate(String nim, List<String> kodeMks) throws Exception {
        // SKS limit
        int total = hitungTotalSks(kodeMks);
        if (total < MIN_SKS) throw new IllegalArgumentException("Total SKS minimal " + MIN_SKS);
        if (total > MAX_SKS) throw new IllegalArgumentException("Total SKS maksimal " + MAX_SKS);

        // Jadwal konflik check pairwise
        for (int i = 0; i < kodeMks.size(); i++) {
            for (int j = i + 1; j < kodeMks.size(); j++) {
                if (jadwalDAO.isBentrok(kodeMks.get(i), kodeMks.get(j))) {
                    throw new IllegalArgumentException("Jadwal bentrok: " + kodeMks.get(i) + " dan " + kodeMks.get(j));
                }
            }
        }

        // Prasyarat sederhana: if mk has prasyarat, must include or assumed lulus - here we require it to be either selected or already taken (omitted for demo)
        for (String kode : kodeMks) {
            MataKuliah mk = mkDAO.findByKode(kode);
            if (mk != null && mk.getPrasyarat() != null) {
                if (!kodeMks.contains(mk.getPrasyarat())) {
                    // In real system, check riwayat kelulusan. For demo we demand it selected concurrently.
                    throw new IllegalArgumentException("Prasyarat belum terpenuhi untuk " + kode + ": " + mk.getPrasyarat());
                }
            }
        }
    }
}
