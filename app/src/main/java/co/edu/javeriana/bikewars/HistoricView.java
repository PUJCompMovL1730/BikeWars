package co.edu.javeriana.bikewars;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.RouteAdapter;
import co.edu.javeriana.bikewars.Logic.Entities.dbRoute;
import co.edu.javeriana.bikewars.Logic.Route;
import co.edu.javeriana.bikewars.Logic.UserData;

public class HistoricView extends AppCompatActivity {

    private ListView list;
    private DatabaseReference ref;
    private View selecView;
    private dbRoute seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_view);
        list = (ListView) findViewById(R.id.historicList);
        List<dbRoute> listData = UserData.getInstance().getUser().getHistoric();
        if(listData==null){
            listData = new ArrayList<>();
        }
        list.setAdapter(new RouteAdapter(getBaseContext(), R.layout.route_layout, listData));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selecView==null){
                    selecView = list.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbRoute) list.getItemAtPosition(position);
                }else{
                    selecView.setBackgroundColor(Color.WHITE);
                    selecView = list.getChildAt(position);
                    selecView.setBackgroundColor(getResources().getColor(R.color.colorSelection));
                    seleccionado = (dbRoute) list.getItemAtPosition(position);
                }
            }
        });
    }

    public void mainLaunch(View view){
        if(seleccionado!=null){
            new Route(seleccionado);
            finish();
        }else{
            Toast.makeText(this, "Seleccion invalida, intente de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
}
