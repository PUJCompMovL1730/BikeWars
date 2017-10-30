package co.edu.javeriana.bikewars.Logic.Entities;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbUser {
    private String userID;
    private String displayName;
    private String photo;
    private List<String> friends;
    private List<String> routes;
    private List<String> groups;
    private List<dbRoute> historic;
    private List<String> commercialMarkers;

    public dbUser() {
    }

    public dbUser(String userID, String displayName, Uri photo) {
        this.userID = userID;
        this.displayName = displayName;
        this.photo = photo.toString();
        friends = new ArrayList<>();
        routes = new ArrayList<>();
        groups = new ArrayList<>();
        historic = new ArrayList<>();
        commercialMarkers = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<dbRoute> getHistoric() {
        return historic;
    }

    public void setHistoric(List<dbRoute> historic) {
        this.historic = historic;
    }

    public List<String> getCommercialMarkers() {
        return commercialMarkers;
    }

    public void setCommercialMarkers(List<String> commercialMarkers) {
        this.commercialMarkers = commercialMarkers;
    }
}
