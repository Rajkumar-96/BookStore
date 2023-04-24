package com.store.bookStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.bookStore.controller.BookStoreController;
import com.store.bookStore.entity.Book;
import com.store.bookStore.service.BookStoreService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookStoreControllerTest {

	private final Long isbnNumber = 1234L;
	private String title = "Wings of fire";
	private String author = "Abdul kalam";
	private float price = 1299.00f;
	private int totalCount = 8;
	private Book bookObj;

	private MockMvc mockMvc;

	@MockBean
	private BookStoreService bookStoreService;

	@Autowired
	private BookStoreController bookStoreController;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		bookObj = new Book(isbnNumber, title, author, price, totalCount);
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(this.bookStoreController).build();
	}

	@Test
	public void testAddNewBook() throws Exception {
		doNothing().when(bookStoreService).addNewBook(bookObj);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/v1/add-new-book").content(objectMapper.writeValueAsBytes(bookObj))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void getBooks() throws Exception {
		List<Book> booksList = new ArrayList<Book>();
		booksList.add(bookObj);
		when(bookStoreService.getBooks()).thenReturn(booksList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*].isbnNumber").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbnNumber").value(isbnNumber));
	}

	@Test
	public void getBooksWithWithTitleAndAuthor() throws Exception {
		String url = "/api/v1/books?" + "title=" + title + "&author=" + author;
		List<Book> booksList = new ArrayList<Book>();
		booksList.add(bookObj);
		when(bookStoreService.getBooksByTitleOrAuthor(Mockito.anyString(), Mockito.anyString())).thenReturn(booksList);

		mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*].isbnNumber").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbnNumber").value(isbnNumber))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].author").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(author))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].totalCount").isNotEmpty());
	}

	@Test
	public void getMediaCoverage() throws Exception {
		String mediaCoverageTitle = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";
		when(bookStoreService.getMediaCoverage(Mockito.anyInt())).thenReturn(mediaCoverageTitle);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/media-coverage/" + isbnNumber).accept(MediaType.ALL))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(String.valueOf(mediaCoverageTitle)));

	}

	@Test
	public void testBuyBooks() throws Exception {
		doNothing().when(bookStoreService).buyBooks(isbnNumber, totalCount);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/buy-books/" + isbnNumber + "/" + totalCount))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
