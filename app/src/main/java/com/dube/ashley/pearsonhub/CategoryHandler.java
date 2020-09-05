package com.dube.ashley.pearsonhub;

public class CategoryHandler
{
    private String title;
    private float price;
    private String category;
    private String description;
    private int thumbnail;

    public CategoryHandler()
    {

    }

    public CategoryHandler(String title,float price, String category, String description, int thumbnail)
    //public CategoryHandler(String title, String category, String description, int thumbnail)
    {
        this.title = title;
        this.category = category;
        this.description = description;
        this.thumbnail = thumbnail;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
