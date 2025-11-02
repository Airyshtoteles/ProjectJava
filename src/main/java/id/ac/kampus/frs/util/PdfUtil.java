package id.ac.kampus.frs.util;

import id.ac.kampus.frs.model.MataKuliah;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfUtil {
    public static void exportFrs(File file, String title, String nim, String nama, int semester, List<MataKuliah> mks, int totalSks, String status) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = page.getMediaBox().getHeight() - 72;
                cs.beginText();
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                cs.newLineAtOffset(72, y);
                cs.showText("FRS Final - " + title);
                cs.endText();

                y -= 24;
                text(cs, 12, 72, y, "Nama: " + nama);
                y -= 16; text(cs, 12, 72, y, "NIM: " + nim);
                y -= 16; text(cs, 12, 72, y, "Semester: " + semester);
                y -= 16; text(cs, 12, 72, y, "Status: " + status);

                y -= 24; text(cs, 12, 72, y, "Daftar Mata Kuliah:");
                y -= 16;
                for (MataKuliah mk : mks) {
                    text(cs, 12, 80, y, mk.getKodeMk() + " - " + mk.getNamaMk() + " (" + mk.getSks() + " SKS)");
                    y -= 14;
                    if (y < 100) { cs.close(); page = new PDPage(PDRectangle.A4); doc.addPage(page); y = page.getMediaBox().getHeight() - 72; }
                }
                y -= 10; text(cs, 12, 72, y, "Total SKS: " + totalSks);
            }
            doc.save(file);
        }
    }

    private static void text(PDPageContentStream cs, float size, float x, float y, String s) throws IOException {
        cs.beginText();
    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), size);
        cs.newLineAtOffset(x, y);
        cs.showText(s);
        cs.endText();
    }
}
