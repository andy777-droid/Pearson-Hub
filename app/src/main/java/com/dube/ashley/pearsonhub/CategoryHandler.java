package com.dube.ashley.pearsonhub;

public class CategoryHandler
{
    private String title;
    private String price;
    private String category;
    private String sellerNumber;
    private String sellerName;
    private String author;
    private String condition;
    private String ISBN;
    private String thumbnail;
    String bookID;

    public CategoryHandler()
    {

    }

    public CategoryHandler (String title,String price, String category, String author, String condition, String ISBN, String sellerNumber,String sellerName, String thumbnail,String bookID)
    {
        this.title = title;
        this.category = category;
        this.sellerNumber = sellerNumber;
        this.sellerName = sellerName;
        this.thumbnail = thumbnail;
        this.price = price;
        this.author = author;
        this.condition = condition;
        this.ISBN = ISBN;
        this.bookID = bookID;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
}
