package id.ac.umn.zapplemobileapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModel {
    @SerializedName("reviewID")
    @Expose
    private Integer reviewID;
    @SerializedName("restaurantID")
    @Expose
    private Integer restaurantID;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("media_link")
    @Expose
    private String media_link;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getMedia_link() {
        return media_link;
    }

    public void setMedia_link(String media_link) {
        this.media_link = media_link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

