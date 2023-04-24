package com.store.bookStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.store.bookStore.entity.Book;
import com.store.bookStore.service.BookStoreService;

@RestController
@RequestMapping("/api/v1")
public class BookStoreController {

	@Autowired
	BookStoreService bookStoreService;
	@PostMapping("/add-new-book")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewBook(@RequestBody Book book) {
		bookStoreService.addNewBook(book);
	}

	@GetMapping("/books")
	public List<Book> getBooks(@RequestParam(required = false) String title,
			@RequestParam(required = false) String author) {
		if (null == title && null == author)
			return bookStoreService.getBooks();
		return bookStoreService.getBooksByTitleOrAuthor(title, author);
	}
	
	@GetMapping("/media-coverage/{isbnNumber}")
	public String getMediaCoverage(@PathVariable int isbnNumber) {		
		return bookStoreService.getMediaCoverage(isbnNumber);
	}

	@PutMapping("/buy-books/{isbnNumber}/{quantity}")
	public void buyBooks(@PathVariable Long isbnNumber, @PathVariable int quantity) {
		bookStoreService.buyBooks(isbnNumber, quantity);
	}

}
