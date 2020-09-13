package com.dube.ashley.pearsonhub;

public class CategoryHandler
{
    private String title;
    private float price;
    private String category;
    private String sellerNumber;
    private String sellerName;
    private String author;
    private int condition;
    private String ISBN;
    private int thumbnail;


    public CategoryHandler()
    {

    }

    public CategoryHandler (String title,float price, String category, String author, int condition, String ISBN, String sellerNumber,String sellerName, int thumbnail)
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

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        String bookPrice=String.valueOf(price);
        return bookPrice;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
