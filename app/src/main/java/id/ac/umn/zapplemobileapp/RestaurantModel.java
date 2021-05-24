package id.ac.umn.zapplemobileapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantModel {
    @SerializedName("restaurantID")
    @Expose
    private Integer restaurantID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("avg_price")
    @Expose
    private Integer avg_price;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("restaurant_photos")
    @Expose
    private String[] restaurant_photos;
    @SerializedName("restaurant_types")
    @Expose
    private Integer[] restaurant_types;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("isFavourite")
    @Expose
    private Boolean isFavourite;


    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(Integer avg_price) {
        this.avg_price = avg_price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String[] getRestaurant_photos() {
        return restaurant_photos;
    }

    public void setRestaurant_photos(String[] restaurant_photos) {
        this.restaurant_photos = restaurant_photos;
    }

    public Integer[] getRestaurant_types() {
        return restaurant_types;
    }

    public void setRestaurant_types(Integer[] restaurant_types) {
        this.restaurant_types = restaurant_types;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }


    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
