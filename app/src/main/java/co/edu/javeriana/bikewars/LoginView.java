package co.edu.javeriana.bikewars;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class LoginView extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    //GUI
    private EditText userTxt, passTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        userTxt = (EditText) findViewById(R.id.loginUserTxt);
        passTxt = (EditText) findViewById(R.id.loginPassTxt);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    TedPermission.with(getBaseContext())
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    startActivity(new Intent(getBaseContext(), RouteLobbyView.class));
                                    finish();
                                }

                                @Override
                                public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                                }
                            })
                            .setDeniedMessage("La aplicacion necesita permisos de ubicacion")
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                }
            }
        });
    }

    public void login(View context){
        if(!userTxt.getText().toString().isEmpty() || !passTxt.getText().toString().isEmpty()){
            TedPermission.with(getBaseContext())
                    .setPermissionListener(new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    mAuth.signInWithEmailAndPassword(userTxt.getText().toString(), passTxt.getText().toString());
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                }
            })
                    .setDeniedMessage("La aplicacion necesita permisos de ubicacion")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        }else{
            Toast.makeText(this, "Ingrese los datos completos", Toast.LENGTH_SHORT).show();
        }
    }

    public void newUserLaunch(View view){
        startActivity(new Intent(getBaseContext(), NewUserView.class));
    }
}
