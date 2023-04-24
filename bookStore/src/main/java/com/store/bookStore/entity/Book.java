package com.store.bookStore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Book {

	@Id
	private Long isbnNumber;

	@NotNull
	private String title;

	private String author;

	@Min(value = 0)
	private float price;

	@Min(value = 0)
	private int totalCount;

	public Book() {
	}

	public Book(Long isbnNumber, @NotNull String title, String author, @Min(0) float price, @Min(0) int totalCount) {
		super();
		this.isbnNumber = isbnNumber;
		this.title = title;
		this.author = author;
		this.price = price;
		this.totalCount = totalCount;
	}

	public Long getIsbnNumber() {
		return isbnNumber;
	}

	public void setIsbnNumber(Long isbnNumber) {
		this.isbnNumber = isbnNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "Book [isbnNumber=" + isbnNumber + ", title=" + title + ", author=" + author + ", price=" + price
				+ ", totalCount=" + totalCount + "]";
	}

}
