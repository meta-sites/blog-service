package com.blog.repositories;

import com.blog.enums.PdfTypeEnum;
import com.blog.models.PdfFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PdfFileRepository extends JpaRepository< PdfFile, String> {

    List<PdfFile> findByFileType(PdfTypeEnum type, Pageable pageable);

    List<PdfFile> findByName(String name, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO book_subscriber (subscriber_id, file_id) VALUES (:userId, :pdfId)", nativeQuery = true)
    int subscribedPdfFile(@Param("userId") String userId, @Param("pdfId") String pdfId);

    @Query(value = "SELECT file_id FROM book_subscriber bs " +
            " JOIN pdf_file pf ON bs.file_id = pf.id" +
            " WHERE bs.subscriber_id = :id OR pf.file_type = 'CV'", nativeQuery = true)
    List<String> findScribeByUserId(String id);

    @Query("SELECT pf FROM PdfFile pf WHERE pf.fileType = 'BOOK' ORDER BY pf.numSub DESC ")
    List<PdfFile> filterBySubscribe(Pageable pageable);

    @Query("SELECT pf FROM PdfFile pf WHERE pf.fileType = 'BOOK' AND pf.description LIKE %:txtSearch% " +
            "OR pf.author LIKE %:txtSearch% " +
            "OR pf.fileName LIKE %:txtSearch% " +
            "OR pf.name LIKE %:txtSearch% " +
            "OR pf.tags LIKE %:txtSearch%")
    List<PdfFile> filterByTxtSearch(String txtSearch, Pageable pageable);
}
