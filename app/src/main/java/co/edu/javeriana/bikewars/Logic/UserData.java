package co.edu.javeriana.bikewars.Logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Logic.Entities.dbRoute;
import co.edu.javeriana.bikewars.Logic.Entities.dbUser;

/**
 * Created by Todesser on 28/10/2017.
 */

public class UserData {
    private static final UserData ourInstance = new UserData();
    public static final String observablesRoot = "db/observables/";
    public static final String usersRoot = "db/users/";
    public static final String racesRoot = "db/races/";
    public static final String routesRoot = "db/routes/";
    public static final String groupsRoot = "db/groupes/";
    public static final String markersRoot = "db/markers/";
    public static final String mailBoxRoot = "db/mailBox/";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference refUser;
    private dbUser user;


    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public void initialize(){
        mUser = mAuth.getCurrentUser();
        if(mUser!=null){
            refUser = db.getReference(usersRoot+mUser.getUid());
            refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(dbUser.class);
                    //Log.i("UserData", "Usuario: " + user.getDisplayName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public FirebaseUser getmUser(){
        return mUser;
    }

    public dbUser getUser(){
        return user;
    }

    public void setNewUser(dbUser newUser){
        refUser.setValue(newUser);
    }

    public void addHistoric(dbRoute route){
        List<dbRoute> historic = user.getHistoric();
        if(historic!=null && historic.size()>5){
            historic.remove(0);
            historic.add(route);
        }else{
            if(historic==null){
                historic = new ArrayList<>();
                user.setHistoric(historic);
            }
            historic.add(route);
        }
        refUser.child("historic").setValue(historic);
    }
}
