package com.store.bookStore.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.store.bookStore.dto.MediaCoverageDto;
import com.store.bookStore.entity.Book;
import com.store.bookStore.repository.BookRepository;
import com.store.bookStore.service.exception.BadRequestException;
import com.store.bookStore.service.exception.NotFoundException;

@SpringBootTest
public class BookStoreServiceImplTest {
	
	private final Long isbnNumber = 1234L;
	private String title = "Wings of fire";
	private String author = "Abdul kalam";
	private float price = 1299.00f;
	private int totalCount = 8;

	@InjectMocks
	private BookStoreServiceImpl bookStoreServiceImpl;
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Test
	public void testAddNewBook() {
		Book book = mock(Book.class);
		when(book.getIsbnNumber()).thenReturn(isbnNumber);
		when(bookRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		bookStoreServiceImpl.addNewBook(book);
		verify(bookRepository, atLeastOnce()).save(book);
	}

	@Test
	public void testAddNewBook_Given_IsbnNumberIsPresent_Then_ThrowsBadRequestException() {
		
		String expectedMessage = "Book with same isbn number present. " + "Try to update the book count by using update method";
		Book book = mock(Book.class);
		when(book.getIsbnNumber()).thenReturn(isbnNumber);
		when(bookRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(book));
		BadRequestException BadRequestException = assertThrows(BadRequestException.class, () -> {
			bookStoreServiceImpl.addNewBook(book);
		});
		assertEquals(expectedMessage, BadRequestException.getMessage());
	}
	
	@Test
	public void testGetBookByIsbnNumber() {
		Book book = mock(Book.class);
		List<Book> bookList = new ArrayList<Book>();
		bookList.add(book);		
		when(bookRepository.findAll()).thenReturn(bookList);
		List<Book> actualBooks = bookStoreServiceImpl.getBooks();
		assertEquals(bookList, actualBooks);
	}
	
	@Test
	public void testGetBooksByTitleOrAuthor(){
		Book book = mock(Book.class);
		List<Book> bookList = new ArrayList<Book>();
		bookList.add(book);		
		when(bookRepository.findAllBookByTitleOrAuthor(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(bookList));
		List<Book> actualBooks = bookStoreServiceImpl.getBooksByTitleOrAuthor(title, author);
		assertEquals(bookList, actualBooks);
	}
	
	@Test
	public void testGetBooksByTitleOrAuthor_GivenTitleAndAuthorNotPresent_Then_ThrowsBookNotFoundException() {
		Book book = mock(Book.class);
		List<Book> bookList = new ArrayList<Book>();
		bookList.add(book);
		when(bookRepository.findAllBookByTitleOrAuthor(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, ()->bookStoreServiceImpl.getBooksByTitleOrAuthor(title, author));
	}
	
	@Test
	public void testBuyBooks() {
		Book book = new Book(isbnNumber, title, author, price, totalCount);
		when(bookRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(book));
		bookStoreServiceImpl.buyBooks(isbnNumber, totalCount);
		verify(bookRepository, atLeastOnce()).save(book);
	}
	
	@Test
	public void testBuyBooks_GivenIsbnNumberNotPresent_Then_ThrowsBookNotFoundException() {
		String expectedMessage = "Book with id: 121 is not found.";
		when(bookRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		NotFoundException bookNotFoundException = assertThrows(NotFoundException.class, () -> {
			bookStoreServiceImpl.buyBooks(121L, totalCount);
		});
		assertEquals(expectedMessage, bookNotFoundException.getMessage());
	}
	
	@Test
	public void testBuyBooks_Given_HigherRangeBook_Then_ThrowsBadRequestException() {
		String expectedMessage = "Not enough books available in the store to sell.";
		Book book = new Book(isbnNumber, title, author, price, totalCount);
		when(bookRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(book));
		BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
			bookStoreServiceImpl.buyBooks(isbnNumber, 10);
		});
		assertEquals(expectedMessage, badRequestException.getMessage());
	}
	
	@Test
	public void testMediaCoverage() {
		MediaCoverageDto mediaCoverageDto = new MediaCoverageDto(1,1,"sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
				"quia et suscipisuscipit recusandae autem sunt rem eveniet architecto");
		when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any())).thenReturn(mediaCoverageDto);
		String result = bookStoreServiceImpl.getMediaCoverage(1);
		assertEquals(mediaCoverageDto.getTitle(), result);
	}
	
	@Test
	public void testMediaCoverage_ThrowsException() {
		String expectedMessage = "Error in establishing connection";
		when(restTemplate.getForObject(Mockito.any(String.class), Mockito.any()))
				.thenThrow(new RestClientException(""));
		RestClientException restClientException = assertThrows(RestClientException.class,
				() -> bookStoreServiceImpl.getMediaCoverage(1));
		assertEquals(expectedMessage, restClientException.getMessage());
	}
}
