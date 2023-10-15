package com.blog.util;

import com.luciad.imageio.webp.WebPWriteParam;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static void createDirectoryIfNoExist(String directoryPath) {
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

    public static String exportFirstPdfPage(InputStream inputStream, String outputPath, float quality) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);
        String coverName = UUID.randomUUID().toString() + Constants.WEBP_EXTENSION;
        document.close();
        return writeImage(image, outputPath, quality, coverName);
    }

    public static String uploadImage(InputStream inputStream, String outputPath, float quality, String imageName) throws IOException {
        return writeImage(ImageIO.read(inputStream), outputPath, quality, imageName);
    }

    private static String writeImage(BufferedImage image, String outputPath, float quality, String imageName) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
        writeParam.setCompressionQuality(quality);

        String coverImagePath = outputPath + imageName;
        FileImageOutputStream output = new FileImageOutputStream(new File(coverImagePath));
        writer.setOutput(output);
        writer.write(null, new IIOImage(image, null, null), writeParam);
        output.close();

        return imageName;
    }

    public static byte[] resizeImage(byte[] imageBytes, Integer newWidth) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int newHeight = (int) ((double) newWidth / originalWidth * originalHeight);

            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(scaledImage, 0, 0, null);
            graphics2D.dispose();

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(resizedImage, "png", outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}