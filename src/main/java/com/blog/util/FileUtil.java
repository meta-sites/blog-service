package com.blog.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static void createDirectoryIfNoExist(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void deleteAllFilesInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) return;
        File[] files = folder.listFiles();
        if (Objects.isNull(files)) return;

        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    public static void addToZipFile(String folderPath, ZipOutputStream zos, String saveZipPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            byte[] buffer = new byte[1024];

            for (File file : files) {
                if (file.isFile()) {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(saveZipPath.concat(file.getName())));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    fis.close();
                }
            }
        }
    }

    public static void addToTarGzFile(String folderPath, TarArchiveOutputStream tarOut, String saveTarPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    TarArchiveEntry tarEntry = new TarArchiveEntry(file, saveTarPath.concat(file.getName()));
                    tarOut.putArchiveEntry(tarEntry);

                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        tarOut.write(buffer, 0, bytesRead);
                    }

                    bis.close();
                    tarOut.closeArchiveEntry();
                }
            }
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
