package com.example.dayleader;

public class Quote {
    private String quote;
    private String kor;
    private String author;


    public Quote(String quote, String kor, String author) {
        this.quote = quote;
        this.kor = kor;
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getKor() {
        return kor;
    }
}
