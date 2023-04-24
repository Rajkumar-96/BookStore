package com.store.bookStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.store.bookStore.dto.MediaCoverageDto;
import com.store.bookStore.entity.Book;
import com.store.bookStore.repository.BookRepository;
import com.store.bookStore.service.exception.BadRequestException;
import com.store.bookStore.service.exception.NotFoundException;

@Service
public class BookStoreServiceImpl implements BookStoreService {

	@Autowired
	BookRepository bookRepository;
	
	@Value("${mediaCoverage.url}")
	private String mediaCoverageUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void addNewBook(Book book) {
		bookRepository.findById(book.getIsbnNumber()).ifPresent(e -> {
			throw new BadRequestException(
					"Book with same isbn number present. " + "Try to update the book count by using update method");
		});
		bookRepository.save(book);
	}

	@Override
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@Override
	public List<Book> getBooksByTitleOrAuthor(String title, String author) {
		Optional<List<Book>> findAllBookByTitleOrAuthor = bookRepository.findAllBookByTitleOrAuthor(title, author);
		if (!findAllBookByTitleOrAuthor.isPresent() || findAllBookByTitleOrAuthor.get().isEmpty())
			throw new NotFoundException("Book with title:" + title + "and author:" + author + " is not found.");
		return findAllBookByTitleOrAuthor.get();
	}

	@Override
	public String getMediaCoverage(int isbnNumber) {
		MediaCoverageDto result = null;
		try {
			result = restTemplate.getForObject(mediaCoverageUrl + isbnNumber, MediaCoverageDto.class);
		} catch (RestClientException e) {
			throw new RestClientException("Error in establishing connection");
		}
		return result.getTitle();
	}

	@Override
	public synchronized void buyBooks(Long isbnNumber, int quantity) {
		Book book = bookRepository.findById(isbnNumber)
				.orElseThrow(() -> new NotFoundException("Book with id: " + isbnNumber + " is not found."));
		int totalCount = book.getTotalCount() - quantity;
		if (totalCount < 0) {
			throw new BadRequestException("Not enough books available in the store to sell.");
		}
		book.setTotalCount(totalCount);
		bookRepository.save(book);
	}
}
