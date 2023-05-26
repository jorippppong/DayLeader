package com.example.dayleader;

public class Quote {
    private String engs;
    private String kors;
    private String author;

    public Quote(String engs, String kors, String author) {
        this.engs = engs;
        this.kors = kors;
        this.author = author;
    }//constructor 생성

    public String getEngs() {
        return engs;
    }//getter 생성

    public String getKors() {
        return kors;
    }//getter 생성

    public String getAuthor() {
        return author;
    }//gether 생성

    public void setEngs(String engs) {
        this.engs = engs;
    }//setter 생성

    public void setKors(String kors) {
        this.kors = kors;
    }//setter 생성

    public void setAuthor(String author) {
        this.author = author;
    }//setter 생성

    @Override
    public String toString() {
        return engs;
    }

    public String toString(int a) {
        return kors;
    }

    public String toString(String b) {
        return author;
    }
}
