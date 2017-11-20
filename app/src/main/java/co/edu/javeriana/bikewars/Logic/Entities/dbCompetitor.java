package co.edu.javeriana.bikewars.Logic.Entities;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by Todesser on 19/11/2017.
 */

public class dbCompetitor implements Serializable{
    private String userID;
    private String name;
    private String photo;
    private long milsToTop;

    public dbCompetitor() {
    }

    public dbCompetitor(String userID, String name, String photo, long milsToTop) {
        this.userID = userID;
        this.name = name;
        this.photo = photo;
        this.milsToTop = milsToTop;
    }

    public dbCompetitor(FirebaseUser user){
        this.userID = user.getUid();
        this.name = user.getDisplayName();
        this.photo = user.getPhotoUrl().toString();
        this.milsToTop = 0;
    }

    public String getUserID() {
        return userID;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMilsToTop() {
        return milsToTop;
    }

    public void setMilsToTop(long milsToTop) {
        this.milsToTop = milsToTop;
    }
}
