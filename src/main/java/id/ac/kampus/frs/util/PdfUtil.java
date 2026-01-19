package id.ac.kampus.frs.util;

import id.ac.kampus.frs.model.MataKuliah;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfUtil {
    public static void exportFrs(File file, String title, String nim, String nama, int semester, List<MataKuliah> mks, int totalSks, String status) throws Exception {
        // 1. Load Template
        InputStream in = PdfUtil.class.getResourceAsStream("/reports/frs_template.jrxml");
        if (in == null) {
            throw new RuntimeException("Template laporan tidak ditemukan: /reports/frs_template.jrxml. Pastikan file ada di src/main/resources/reports/");
        }
        
        // 2. Compile Report (in production you might want to pre-compile to .jasper)
        JasperDesign jasperDesign = JRXmlLoader.load(in);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // 3. Parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("nim", nim);
        parameters.put("nama", nama);
        parameters.put("semester", semester);
        parameters.put("status", status);
        parameters.put("totalSks", totalSks);

        // 4. Data Source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mks);

        // 5. Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 6. Export to PDF
        // Ensure parent dirs exist
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        
        try (FileOutputStream out = new FileOutputStream(file)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        }
    }
}
