package com.blog.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {

    public static void createDirectoryIfNoExist(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }



    public static String exportFirstPdfPage(InputStream inputStream, String outputPath) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);
        createDirectoryIfNoExist(outputPath);
        String coverName = UUID.randomUUID().toString() + Constants.PNG_EXTENSION;
        String coverImagePath = outputPath + coverName;

        File cover = new File(coverImagePath);
        ImageIO.write(image, "png", cover);

        document.close();

        return coverName;
    }
}
