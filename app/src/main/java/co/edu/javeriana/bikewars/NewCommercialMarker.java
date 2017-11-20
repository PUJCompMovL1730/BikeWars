package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.Logic.Entities.dbCommercialMarker;

public class NewCommercialMarker extends AppCompatActivity {

    private Bitmap photoBitMap;
    private ImageView img;
    private dbCommercialMarker newMarker;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commercial_marker);
        img = findViewById(R.id.newCommercialPhoto);
        newMarker = (dbCommercialMarker) getIntent().getSerializableExtra("newMarker");
        name = findViewById(R.id.newCommercialName);
        name.setText(newMarker.getTitle());
    }

    public void requireCamera(View v){
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 2);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("Se necesita acceso a la camara")
                .setPermissions(android.Manifest.permission.CAMERA)
                .check();
    }

    public void requireGallery(View v){
        TedPermission.with(getBaseContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent pickImage = new Intent(Intent.ACTION_PICK);
                        pickImage.setType("image/*");
                        startActivityForResult(pickImage, 1);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("Se necesita acceso a la camara")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if(resultCode==RESULT_OK){
                    try{
                        final InputStream imageStream = getContentResolver().openInputStream(data.getData());
                        photoBitMap = BitmapFactory.decodeStream(imageStream);
                        img.setImageBitmap(photoBitMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case 2:{
                if(resultCode==RESULT_OK){
                    Bundle extras = data.getExtras();
                    photoBitMap = (Bitmap) extras.get("data");
                    img.setImageBitmap(photoBitMap);
                }
                break;
            }
        }
    }

    public void Accept(View view){
        StorageReference photoRef = FirebaseStorage.getInstance().getReference("images/"+newMarker.getOwnerID()+"/"+newMarker.getTitle()+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask upload = photoRef.putBytes(data);
        upload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Ocurrio un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                newMarker.setPhoto("images/"+newMarker.getOwnerID()+"/"+newMarker.getTitle()+".jpg");
                Intent result = new Intent();
                result.putExtra("newMarker", newMarker);
                setResult(Constants.OK, result);
                finish();
            }
        });
    }
}
