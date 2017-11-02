package co.edu.javeriana.bikewars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Interfaces.LocationListener;
import co.edu.javeriana.bikewars.Logic.MapData;
import co.edu.javeriana.bikewars.Logic.Route;
import co.edu.javeriana.bikewars.Logic.UserData;

public class RouteLobbyView extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    //Static Context
    public static Context context;

    private MapFragment mapFragment;
    private GoogleMap map;
    private FirebaseAuth mAuth;
    private Marker ubication;
    private FloatingActionButton endBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        setContentView(R.layout.activity_route_lobby_view);
        endBtn = findViewById(R.id.lobbyEndBtn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapData.getInstance().endRoute();
                endBtn.setVisibility(View.INVISIBLE);
            }
        });
        final RouteLobbyView instance = this;
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mainMap);
        mAuth = FirebaseAuth.getInstance();
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        UserData.getInstance().initialize();
                        MapData.getInstance();
                        mapFragment.getMapAsync(instance);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        mAuth.signOut();
                        startActivity(new Intent(getBaseContext(), LoginView.class));
                        finish();
                    }
                })
                .setDeniedMessage("La aplicacion necesita permisos de ubicacion")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public void newRouteLaunch(View view){
        startActivity(new Intent(this, NewRouteView.class));
    }

    public void historicLaunch(View view){
        startActivity(new Intent(this, HistoricView.class));
    }

    public void searchRouteLaunch(View view){
        startActivity(new Intent(this, SearchView.class));
    }

    public void friendsLaunch(View view){
        startActivity(new Intent(this, FriendsLobby.class));
    }

    public void racesLaunch(View view){
        startActivity(new Intent(this, RaceView.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapData.getInstance().unSuscribe(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MapData.getInstance().addListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapData.bogotaMark, 10));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuLogout:
                mAuth.signOut();
                startActivity(new Intent(getBaseContext(), LoginView.class));
                finish();
                return true;
            case R.id.mainMenuConfig:
                // TODO: 27/10/2017 Lanzamiento de configuracion.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateLocation(MarkerOptions location, List<MarkerOptions> markers, Route route, List<MarkerOptions> globals) {
        map.clear();
        if(location!=null){
            if(ubication==null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location.getPosition(), 15));
            }
            ubication = map.addMarker(location);
        }
        if(route!=null){
            endBtn.setVisibility(View.VISIBLE);
            map.addPolyline(route.getRoute());
            map.addMarker(route.getStart());
            map.addMarker(route.getEnd());
        }
        for(MarkerOptions mark: markers){
            map.addMarker(mark);
        }
        for(MarkerOptions mark: globals){
            map.addMarker(mark);
        }
    }

    public void test(View v){
        MapData.getInstance().endRoute();
    }
}
