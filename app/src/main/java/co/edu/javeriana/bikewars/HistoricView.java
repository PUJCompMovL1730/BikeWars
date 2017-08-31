package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HistoricView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_view);
    }

    public void mainLaunch(View view){
        startActivity(new Intent(this, MainView.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}