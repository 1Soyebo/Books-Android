package com.example.books;

public class Book {
    public String id;
    public String title;
    public String subtitle;
    public String[] authors;
    public String publisher;
    public String publishedDate;

    public Book(String id, String title, String subtitle, String[] authors, String publisher, String publishedDate) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }
}
