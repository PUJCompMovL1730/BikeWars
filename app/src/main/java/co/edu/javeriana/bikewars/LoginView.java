package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginView extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    //GUI
    private EditText userTxt, passTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        userTxt = findViewById(R.id.loginUserTxt);
        passTxt = findViewById(R.id.loginPassTxt);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getBaseContext(), RouteLobbyView.class));
                    finish();
                }
            }
        });
    }

    public void login(View context){
        if(!userTxt.getText().toString().isEmpty() && !passTxt.getText().toString().isEmpty()){
            mAuth.signInWithEmailAndPassword(userTxt.getText().toString(), passTxt.getText().toString()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    passTxt.setText("");
                    Toast.makeText(LoginView.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Ingrese los datos completos", Toast.LENGTH_SHORT).show();
        }
    }

    public void newUserLaunch(View view){
        startActivity(new Intent(getBaseContext(), NewUserView.class));
    }
}
