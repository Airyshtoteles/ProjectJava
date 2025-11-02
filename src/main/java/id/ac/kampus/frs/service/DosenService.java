package id.ac.kampus.frs.service;

import id.ac.kampus.frs.dao.FRSDAO;
import id.ac.kampus.frs.dao.MataKuliahDAO;
import id.ac.kampus.frs.dao.PersetujuanFRSDAO;
import id.ac.kampus.frs.model.FRS;
import id.ac.kampus.frs.model.MataKuliah;
import id.ac.kampus.frs.model.PersetujuanFRS;
import id.ac.kampus.frs.util.ActivityLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DosenService {
    private final FRSDAO frsDAO = new FRSDAO();
    private final PersetujuanFRSDAO persetujuanDAO = new PersetujuanFRSDAO();
    private final MataKuliahDAO mkDAO = new MataKuliahDAO();

    public List<FRSDAO.PengajuanRow> listPengajuan(String nidn) throws SQLException {
        return frsDAO.listPengajuanForDosen(nidn);
    }

    public List<MataKuliah> listDetailMk(int idFrs) throws SQLException {
        var details = frsDAO.listDetails(idFrs);
        List<MataKuliah> mks = new ArrayList<>();
        for (var d : details) {
            MataKuliah mk = mkDAO.findByKode(d.getKodeMk());
            if (mk != null) mks.add(mk);
        }
        return mks;
    }

    public void approve(int idFrs, String nidn, int idUser, String catatan) throws SQLException {
        persetujuanDAO.insertApproval(idFrs, nidn, PersetujuanFRS.Status.DISETUJUI, catatan);
        frsDAO.setStatus(idFrs, FRS.Status.DISETUJUI);
        ActivityLogger.log(idUser, "APPROVE_FRS:" + idFrs);
    }

    public void reject(int idFrs, String nidn, int idUser, String catatan) throws SQLException {
        persetujuanDAO.insertApproval(idFrs, nidn, PersetujuanFRS.Status.DITOLAK, catatan);
        frsDAO.setStatus(idFrs, FRS.Status.DITOLAK);
        ActivityLogger.log(idUser, "REJECT_FRS:" + idFrs);
    }
}
