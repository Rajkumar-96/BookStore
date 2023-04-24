package com.store.bookStore.service;

import java.util.List;

import com.store.bookStore.entity.Book;

public interface BookStoreService {
	
	void addNewBook(Book book);
	
	List<Book> getBooks();
	
	List<Book> getBooksByTitleOrAuthor(String title, String author);
	
	String getMediaCoverage(int isbnNumber);
	
	void buyBooks(Long isbnNumber, int quantity);
}
