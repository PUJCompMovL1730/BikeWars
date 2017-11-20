package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;

public class NewMarker extends AppCompatActivity {

    private TextView name, desc, url;
    private dbCommercialMarker newMarker;
    private final int COMMERCIAL = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_marker);
        name = findViewById(R.id.newMarkerName);
        desc = findViewById(R.id.newMarkerDesc);
        url = findViewById(R.id.newMarkerUrl);
        newMarker = new dbCommercialMarker();
        newMarker.setLatitude(getIntent().getDoubleExtra("lat", Constants.bogotaLocation.latitude));
        newMarker.setLongitude(getIntent().getDoubleExtra("lon", Constants.bogotaLocation.longitude));
        newMarker.setOwnerID(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void createMarker(View v){
        newMarker.setTitle(name.getText().toString());
        newMarker.setDescription(desc.getText().toString());
        newMarker.setUrl(url.getText().toString());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.markersRoot);
        ref.push().setValue(newMarker);
        Intent result = new Intent();
        result.putExtra("newMarker", newMarker);
        setResult(Constants.OK, result);
        finish();
    }

    public void commercialMarker(View v){
        if (name.getText().toString().isEmpty()) {
            newMarker.setTitle("Sin Titulo");
        }else{
            newMarker.setTitle(name.getText().toString());
        }
        Intent commercialIntent = new Intent(this, NewCommercialMarker.class);
        commercialIntent.putExtra("newMarker", newMarker);
        startActivityForResult(commercialIntent, COMMERCIAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case COMMERCIAL:
                if(resultCode == Constants.OK){
                    newMarker = (dbCommercialMarker) data.getSerializableExtra("newMarker");
                    Toast.makeText(this, "Marcador publicado comercialmente.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Ocurrio un error al publicar comercialmente el marcador.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
