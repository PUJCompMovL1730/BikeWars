package co.edu.javeriana.bikewars.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.UserData;
import co.edu.javeriana.bikewars.R;

/**
 * Created by Todesser on 30/10/2017.
 */

public class AddFriendAdapter extends ArrayAdapter<dbObservable>{
    public AddFriendAdapter(@NonNull Context context, int resource, @NonNull List<dbObservable> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.add_friend_layout, parent, false);
        }
        final ImageView photo = (ImageView) convertView.findViewById(R.id.addFriendPhoto);
        final Bitmap[] photoBit = new Bitmap[1];
        TextView name = (TextView) convertView.findViewById(R.id.addFriendName);
        ImageButton addFriend = (ImageButton) convertView.findViewById(R.id.addFriendAdd);
        final dbObservable model = getItem(position);
        FirebaseStorage.getInstance().getReferenceFromUrl(model.getPhoto()).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                photoBit[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photo.setImageBitmap(photoBit[0]);
            }
        });
        name.setText(model.getDisplayName());
        addFriend.setImageResource(R.drawable.add);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData.getInstance().addFriend(model.getUserID());
            }
        });
        return convertView;
    }
}
