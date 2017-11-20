package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.ExpectView;
import co.edu.javeriana.bikewars.Logic.Entities.dbCompetitor;
import co.edu.javeriana.bikewars.Logic.Entities.dbRace;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 04/09/2017.
 */

public class RaceItemAdapter extends ArrayAdapter<dbRace>{


    public RaceItemAdapter(Context context, List<dbRace> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.race_item_layout, parent, false);
        }
        final TextView name = convertView.findViewById(R.id.race_item_name);
        TextView date = convertView.findViewById(R.id.race_item_date);
        ImageButton expect = convertView.findViewById(R.id.race_item_expect);
        ImageButton join = convertView.findViewById(R.id.race_item_join);
        final dbRace race = getItem(position);
        name.setText(race.getName());
        date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(race.getStartDate()));
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinRace(race);
            }
        });
        expect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expect(race);
            }
        });
        return convertView;
    }

    public void joinRace(dbRace race){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.racesRoot + race.getId());
        dbCompetitor competitor = new dbCompetitor(FirebaseAuth.getInstance().getCurrentUser());
        race.getCompetitors().add(competitor);
        ref.setValue(race);
    }

    public void expect(dbRace race){
        Intent intent = new Intent(getContext(), ExpectView.class);
        intent.putExtra("dbRace", race);
        getContext().startActivity(intent);
    }
}
