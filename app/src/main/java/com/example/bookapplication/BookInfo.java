/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

public class BookInfo {
    private String bookTitle;
    private String bookAuthorName;
    private String bookCategory;
    private String bookPageCount;
    private String previewLink;
    private String thumbnailLink;
    private int position;

    public BookInfo() {}

    public BookInfo(String bookTitle, String bookName, String bookCategory, String bookPageCount, String previewLink, String thumbnailLink) {
        this.bookTitle = bookTitle;
        this.bookAuthorName = bookName;
        this.bookCategory = bookCategory;
        this.bookPageCount = bookPageCount;
        this.previewLink = previewLink;
        this.thumbnailLink = thumbnailLink;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthorName() {
        return bookAuthorName;
    }

    public void setBookAuthorName(String bookName) {
        this.bookAuthorName = bookName;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookPageCount() {
        return bookPageCount;
    }

    public void setBookPageCount(String bookPageCount) {
        this.bookPageCount = bookPageCount;
    }

    public String getPreviewLink() {
        return this.previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getThumbnailLink() {
        return this.thumbnailLink;
    }
}
