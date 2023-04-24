package com.store.bookStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.bookStore.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query(value = "Select * from book b where b.title ILIKE %?1% OR b.author ILIKE %?2%", nativeQuery = true)
	Optional<List<Book>> findAllBookByTitleOrAuthor(String title, String author);

}
