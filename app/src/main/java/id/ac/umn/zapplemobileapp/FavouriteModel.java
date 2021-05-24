package id.ac.umn.zapplemobileapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteModel {
    @SerializedName("isFavourite")
    @Expose
    private Boolean isFavourite;

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
