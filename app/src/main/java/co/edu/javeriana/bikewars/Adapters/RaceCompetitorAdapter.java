package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCompetitor;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 04/09/2017.
 */

public class RaceCompetitorAdapter extends ArrayAdapter<dbCompetitor>{


    public RaceCompetitorAdapter(Context context, List<dbCompetitor> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.race_competitor_layout, parent, false);
        }
        final ImageView photo = convertView.findViewById(R.id.competitorPhoto);
        TextView name = convertView.findViewById(R.id.competitorName);
        TextView time = convertView.findViewById(R.id.competitorTime);
        final dbCompetitor competitor = getItem(position);
        if(Constants.photoBank.get(competitor.getPhoto())!=null){
            photo.setImageBitmap(Constants.photoBank.get(competitor.getPhoto()));
        }else{
            FirebaseStorage.getInstance().getReferenceFromUrl(competitor.getPhoto()).getBytes(Constants.MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap downloadedPhoto = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    photo.setImageBitmap(downloadedPhoto);
                    Constants.photoBank.put(competitor.getPhoto(), downloadedPhoto);
                }
            });
        }
        name.setText(competitor.getName());
        long millis = competitor.getMilsToTop();
        time.setText("+" + String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        ));
        return convertView;
    }
}
