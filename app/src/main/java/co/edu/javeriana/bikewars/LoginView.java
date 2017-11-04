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

import java.util.regex.Pattern;

public class LoginView extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    //GUI
    private EditText user, pass;
    //Regex
    private Pattern emailRegex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        user = findViewById(R.id.loginUserTxt);
        pass = findViewById(R.id.loginPassTxt);
        emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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
        String userTxt = user.getText().toString().trim(),
        passTxt = pass.getText().toString().trim();
        if(!userTxt.isEmpty() && !passTxt.isEmpty()){
            Boolean validMail = emailRegex.matcher(userTxt).matches();
            if(validMail){
                mAuth.signInWithEmailAndPassword(userTxt, passTxt).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pass.setText("");
                        Toast.makeText(LoginView.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            Toast.makeText(this, "Ingrese los datos completos", Toast.LENGTH_SHORT).show();
        }
    }

    public void newUserLaunch(View view){
        startActivity(new Intent(getBaseContext(), NewUserView.class));
    }
}
