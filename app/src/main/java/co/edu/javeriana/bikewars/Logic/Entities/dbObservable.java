package co.edu.javeriana.bikewars.Logic.Entities;

import android.location.Location;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.javeriana.bikewars.Interfaces.ObservableListener;
import co.edu.javeriana.bikewars.Logic.UserData;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbObservable {
    private String userID;
    private String displayName;
    private String photo;
    private double latitude;
    private double longitude;
    private DatabaseReference ref;
    private ValueEventListener listener;
    public MarkerOptions oldMarker;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public dbObservable(String userID, String displayName, double latitude, double longitude, String photo) {
        this.userID = userID;
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public dbObservable() {
    }

    public dbObservable(String userID, final ObservableListener listener){
        ref = FirebaseDatabase.getInstance().getReference(UserData.observablesRoot+userID);
        this.listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbObservable retornado = dataSnapshot.getValue(dbObservable.class);
                displayName = retornado.getDisplayName();
                latitude = retornado.getLatitude();
                longitude = retornado.getLongitude();
                photo = retornado.getPhoto();
                listener.updateObservable(retornado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public dbObservable(FirebaseUser user, final ObservableListener listener, Location location){
        ref = FirebaseDatabase.getInstance().getReference(UserData.observablesRoot+user.getUid());
        this.userID = user.getUid();
        this.displayName = user.getDisplayName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.photo = user.getPhotoUrl().toString();
        ref.setValue(this);
        this.listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbObservable retornado = dataSnapshot.getValue(dbObservable.class);
                displayName = retornado.getDisplayName();
                latitude = retornado.getLatitude();
                longitude = retornado.getLongitude();
                photo = retornado.getPhoto();
                listener.updateObservable(retornado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeObservable(){
        ref.removeEventListener(listener);
    }

    public void updateObservable(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        ref.setValue(this);
    }
}
