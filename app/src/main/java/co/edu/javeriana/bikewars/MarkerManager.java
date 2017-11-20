package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.MarkerAdapter;
import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;

public class MarkerManager extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref;
    private ValueEventListener listener;
    private List<dbCommercialMarker> markersList;
    private ListView list;
    private MarkerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_manager);
        list = findViewById(R.id.MarkManList);
        ref = FirebaseDatabase.getInstance().getReference(Constants.markersRoot);
        listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                markersList = new ArrayList<>();
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    dbCommercialMarker marker = snap.getValue(dbCommercialMarker.class);
                    if(marker.getOwnerID()!=null){
                        if(marker.getOwnerID().equals(user.getUid())){
                            marker.setMarkerID(snap.getKey());
                            markersList.add(marker);
                        }
                    }
                }
                adapter = new MarkerAdapter(getBaseContext(), R.layout.marker_manager_layout, markersList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onDestroy() {
        if (ref!=null) {
            if (listener!=null) {
                ref.removeEventListener(listener);
            }
        }
        super.onDestroy();
    }
}
