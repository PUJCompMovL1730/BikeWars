package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.RaceItemAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbRace;

public class RaceView extends AppCompatActivity {

    private List<dbRace> carreras;
    private RaceItemAdapter adapter;
    private ListView list;
    private DatabaseReference raceRef;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_view);
        raceRef = FirebaseDatabase.getInstance().getReference(Constants.racesRoot);
        listener = raceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carreras = new ArrayList<>();
                for (DataSnapshot raceSnap: dataSnapshot.getChildren()) {
                    carreras.add(raceSnap.getValue(dbRace.class));
                }
                adapter = new RaceItemAdapter(getBaseContext(), carreras);
                list = findViewById(R.id.raceList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 20/11/2017 Manejar error
            }
        });

    }

    public void newRace(View view){
        startActivity(new Intent(this, NewRaceView.class));
    }

    @Override
    protected void onDestroy() {
        raceRef.removeEventListener(listener);
        super.onDestroy();
    }
}
