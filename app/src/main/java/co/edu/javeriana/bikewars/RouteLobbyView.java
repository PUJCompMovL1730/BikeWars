package co.edu.javeriana.bikewars;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.MapData;
import co.edu.javeriana.bikewars.Services.MailBox;

public class RouteLobbyView extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener {

    //Static Context
    public static Context context;

    //Codex
    private final int NEW_MARKER_ID = 20;

    private MapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient apiClient;
    private FirebaseAuth mAuth;
    private Marker ubication;
    private dbObservable dbUbication;
    private FloatingActionButton endBtn;
    private Map<String, dbCommercialMarker> markers;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
        markers = new HashMap<>();
        final RouteLobbyView instance = this;
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mainMap);
        mAuth = FirebaseAuth.getInstance();
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
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
        serviceIntent = new Intent(this, MailBox.class);
        serviceIntent.putExtra("userID", mAuth.getCurrentUser().getUid());
        serviceIntent.putExtra("icon", R.drawable.ic_envelope);
        startService(serviceIntent);
    }

    public void newRouteLaunch(View view) {
        startActivity(new Intent(this, NewRouteView.class));
    }

    public void historicLaunch(View view) {
        startActivity(new Intent(this, HistoricView.class));
    }

    public void searchRouteLaunch(View view) {
        startActivity(new Intent(this, SearchView.class));
    }

    public void friendsLaunch(View view) {
        startActivity(new Intent(this, FriendsLobby.class));
    }

    public void racesLaunch(View view) {
        startActivity(new Intent(this, RaceView.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MailBox.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapData.bogotaMark, Constants.cityLevel));
        apiClient.connect();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.markersRoot);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dbCommercialMarker marker = dataSnapshot.getValue(dbCommercialMarker.class);
                marker.setMarker(map.addMarker(new MarkerOptions().visible(false).position(new LatLng(0,0))));
                markers.put(dataSnapshot.getKey(), marker);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                dbCommercialMarker oldMarker = markers.get(key);
                dbCommercialMarker newMarker = dataSnapshot.getValue(dbCommercialMarker.class);
                oldMarker.uptdateMarker(newMarker);
                markers.put(key, newMarker);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                markers.get(key).removeMarker();
                markers.remove(key);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //No aplica en la logica de la app
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO Manejar el error en caso de que se cancele la escucha de los cambios en los marcadores comerciales
            }
        });
        map.setOnMapLongClickListener(this);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                if (marker.getTag()!=null) {
                    final dbCommercialMarker markerTag = (dbCommercialMarker) marker.getTag();
                    View v = getLayoutInflater().inflate(R.layout.marker_layout, null);
                    final ImageView image = v.findViewById(R.id.markerImage);
                    TextView title = v.findViewById(R.id.markerTitle);
                    EditText desc = v.findViewById(R.id.markerDesc);
                    if(markerTag.getPhoto()!=null){
                        Bitmap imagePhoto = Constants.photoBank.get(markerTag.getPhoto());
                        if (imagePhoto==null) {
                            FirebaseStorage.getInstance().getReference(markerTag.getPhoto()).getBytes(Constants.MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap downloadedPhoto = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    image.setImageBitmap(downloadedPhoto);
                                    Constants.photoBank.put(markerTag.getPhoto(), downloadedPhoto);
                                    marker.showInfoWindow();
                                }
                            });
                        }else{
                            image.setImageBitmap(imagePhoto);
                        }
                    }
                    title.setText(markerTag.getTitle());
                    desc.setText(markerTag.getDescription());
                    return v;
                }else{
                    return null;
                }
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTag()!=null) {
                    final dbCommercialMarker markerTag = (dbCommercialMarker) marker.getTag();
                    if (markerTag.getUrl()!=null) {
                        if (URLUtil.isValidUrl(markerTag.getUrl())) {
                            Intent browseIntent = new Intent(Intent.ACTION_VIEW);
                            browseIntent.setData(Uri.parse(markerTag.getUrl()));
                            startActivity(browseIntent);
                        }else{
                            Toast.makeText(RouteLobbyView.this, "Url invalida", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RouteLobbyView.this, "Marcador sin Url", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
            case R.id.mainMenuMarkers:
                Intent markersIntent = new Intent(this, MarkerManager.class);
                startActivity(markersIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void test(View v) {
        MapData.getInstance().endRoute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Se ha perdido el permiso de ubicacion, no se puede continuar con el uso de la app", Toast.LENGTH_SHORT).show();
            finish();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO: Manejar error en caso de que se suspenda la conexion

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //TODO: Manejar error en caso de que la conexion falle
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        if(ubication == null){
            ubication = map.addMarker(new MarkerOptions().position(latlng).visible(false));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, Constants.streetLevel));
            dbUbication = new dbObservable(mAuth.getCurrentUser(), location,ubication);
        }else{
            dbUbication.updateLocation(latlng);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent newMarkerIntent = new Intent(this, NewMarker.class);
        newMarkerIntent.putExtra("lat", latLng.latitude);
        newMarkerIntent.putExtra("lon", latLng.longitude);
        startActivityForResult(newMarkerIntent, NEW_MARKER_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case NEW_MARKER_ID:
                if(resultCode == 1){
                    dbCommercialMarker newMarker = (dbCommercialMarker) data.getSerializableExtra("newMarker");
                    newMarker.setMarker(map.addMarker(new MarkerOptions().position(Constants.bogotaLocation).visible(false)));
                    Toast.makeText(this, "Marcador creado correctamente", Toast.LENGTH_SHORT).show();
                }else if(resultCode == 0){
                    Toast.makeText(this, "No se ha creado un nuevo marcador.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
