package com.cognixia.jump.ManageBookSeller.security.services;



import com.cognixia.jump.ManageBookSeller.security.services.dto.BookDto;
import com.cognixia.jump.ManageBookSeller.security.services.dto.SellDto;

import java.util.List;

public interface BookStoreService {
    void addNewBook(BookDto bookDto);

    void addBook(Long id, int quantityToAdd);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks();

    int getNumberOfBooksById(Long id);

    void updateBook(Long id, BookDto bookDto);

    void sellBook(Long id);

    void sellBooks(List<SellDto> sellDtos);

    List<BookDto> getBookByCategoryKeyWord(String keyword);

    int getNumberOfBooksSoldByCategoryAndKeyword(String keyword);

}