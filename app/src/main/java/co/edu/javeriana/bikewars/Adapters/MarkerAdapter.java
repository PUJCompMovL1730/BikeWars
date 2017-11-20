package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 30/10/2017.
 */

public class MarkerAdapter extends ArrayAdapter<dbCommercialMarker>{

    private Context cache;

    public MarkerAdapter(@NonNull Context context, int resource, @NonNull List<dbCommercialMarker> objects) {
        super(context, resource, objects);
        cache = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.marker_manager_layout, parent, false);
        }
        TextView name = convertView.findViewById(R.id.markerManName);
        ImageButton edit = convertView.findViewById(R.id.markerManEdit);
        ImageButton remove = convertView.findViewById(R.id.markerManRemove);
        final dbCommercialMarker model = getItem(position);
        name.setText(model.getTitle());
        edit.setImageResource(R.drawable.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 20/11/17 Falta poder editar los marcadores. 
            }
        });
        remove.setImageResource(R.drawable.cancel);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.markersRoot+model.getMarkerID());
                ref.removeValue();
            }
        });
        return convertView;
    }
}
