package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.bikewars.Adapters.RaceCompetitorAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCompetitor;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.Entities.dbRace;

public class ExpectView extends AppCompatActivity implements OnMapReadyCallback {

    private ListView list;
    private TextView banner;
    private MapFragment mapFragment;
    private GoogleMap map;
    private dbRace race;
    private List<dbCompetitor> competitors;
    private Map<String, dbObservable> raceObservables;
    private Query ref;
    private ValueEventListener listener;
    private RaceCompetitorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expect_view);
        banner = findViewById(R.id.expectBanner);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.expectMap);
        list = findViewById(R.id.expectTopList);
        competitors = new ArrayList<>();
        adapter = new RaceCompetitorAdapter(this, competitors);
        list.setAdapter(adapter);
        race = (dbRace) getIntent().getSerializableExtra("dbRace");
        mapFragment.getMapAsync(this);
        banner.setText(race.getName());
        ref = FirebaseDatabase.getInstance().getReference(Constants.racesRoot + race.getId()).child("competitors").orderByChild("milsToTop");
        listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                competitors = new ArrayList<>();
                boolean first = true;
                long topMillis = 0;
                for(DataSnapshot competitorSnap: dataSnapshot.getChildren()){
                    dbCompetitor competitor = competitorSnap.getValue(dbCompetitor.class);
                    if(first){
                        topMillis = competitor.getMilsToTop();
                        competitor.setMilsToTop(0);
                        first = false;
                        if (map != null) {
                            dbObservable comObs = raceObservables.get(competitor.getUserID());
                            if (comObs != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(comObs.getLatitude(), comObs.getLongitude()), Constants.streetLevel));
                            }
                        }
                    }else{
                        competitor.setMilsToTop(competitor.getMilsToTop()-topMillis);
                    }
                    competitors.add(competitor);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 20/11/2017 Manejar error
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.bogotaLocation, 15));
        map.addMarker(new MarkerOptions()
                .position(Constants.bogotaLocation)
                .title("Marker"));
        map.setIndoorEnabled(false);
        raceObservables = new HashMap<>();
        for(dbCompetitor competitor: race.getCompetitors()){
            Marker competitorMarker = map.addMarker(new MarkerOptions().position(Constants.bogotaLocation).visible(false));
            dbObservable competitorObservable = new dbObservable(competitor.getUserID(), competitorMarker);
            raceObservables.put(competitor.getUserID(), competitorObservable);
        }
    }
}
