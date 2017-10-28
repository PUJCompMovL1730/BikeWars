package co.edu.javeriana.bikewars.Logic;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Interfaces.LocationUpdater;
import co.edu.javeriana.bikewars.RouteLobbyView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by jairo on 28/10/17.
 */

public class MapData{
    //Aux
    private static MapData instance = null;
    public static final LatLng bogotaMark = new LatLng(4.624335, -74.063644);
    private static ReactiveLocationProvider provider;
    private static Disposable subscription;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Attributes
    private MarkerOptions ubication;
    private List<MarkerOptions> markers;
    private List<LocationUpdater> listeners;
    private Ruta route;

    private MapData() {
        ubication = new MarkerOptions();
        markers = new ArrayList<>();
        listeners = new ArrayList<>();
        provider = new ReactiveLocationProvider(RouteLobbyView.context);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public static MapData getInstance(){
        if(instance==null){
            instance = new MapData();
        }
        return instance;
    }

    public MarkerOptions getUbication(){
        synchronized (ubication){
            return ubication;
        }
    }

    public Marker addMarker(GoogleMap map, MarkerOptions options){
        markers.add(options);
        return map.addMarker(options);
    }

    public List<MarkerOptions> getMarkers(){
        return markers;
    }

    public void addListener(LocationUpdater listener){
        synchronized (listeners){
            if(listeners.isEmpty()){
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                subscription = provider.getUpdatedLocation(mLocationRequest).subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        synchronized (ubication){
                            ubication = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mUser.getDisplayName());
                        }
                        updateListeners();
                    }
                });
                listeners.add(listener);
            }else{
                listeners.add(listener);
            }
        }
    }

    public void unSuscribe(LocationUpdater client){
        synchronized (listeners){
            listeners.remove(client);
            if(listeners.isEmpty() && subscription != null){
                subscription.dispose();
            }
        }
    }

    private void updateListeners(){
        synchronized (listeners){
                for(LocationUpdater listener: listeners){
                    synchronized (ubication){
                        listener.updateLocation(ubication, markers, route);
                    }
                }
        }
    }

    public void setRoute(Ruta route){
        this.route = route;
        updateListeners();
    }
}
