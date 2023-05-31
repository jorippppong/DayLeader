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
        return "Quote{" +
                "engs='" + engs + '\'' +
                ", kors='" + kors + '\'' +
                ", author='" + author + '\'' +
                '}';
    }// 출력할 명언의 모양 설정하기 - 마지막에 결과 출려해보고 수정할 것
}
