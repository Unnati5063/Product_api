package com.cognixia.jump.ManageBookSeller.repository;

import com.cognixia.jump.ManageBookSeller.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "Select * from book b where " +
            "(b.title like %?1% OR CAST(b.id as CHAR) like %?1% OR LOWER(b.author) like %?1%) " +
            "AND b.category=?2",
            nativeQuery = true)
    List<Book> findAllBookByCategoryAndKeyword(String keyword);

    @Query(value = "Select IF(SUM(b.sold) IS NULL,0,SUM(b.sold)) from book b where " +
            "(b.title like %?1% OR CAST(b.id as CHAR) like %?1% OR LOWER(b.author) like %?1%) " +
            "AND b.category=?2 AND b.sold>0",
            nativeQuery = true)
    long countNumberOfBooksSold(String keyword);

}