package co.edu.javeriana.bikewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.FriendAdapter;
import co.edu.javeriana.bikewars.Interfaces.FriendListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.UserData;

public class FriendsLobby extends AppCompatActivity implements FriendListener{

    private ListView list, groups;
    private List<dbObservable> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_lobby);
        list = (ListView) findViewById(R.id.friendsViewList);
        groups = (ListView) findViewById(R.id.friendsViewGroups);
        UserData.getInstance().addFriendListener(this);
        populateList(UserData.getInstance().getUser().getFriends());
    }

    private void populateList(final List<String> friends){
        listData = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(UserData.observablesRoot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbObservable friend = snapshot.getValue(dbObservable.class);
                    if(friends.contains(friend.getUserID())){
                        listData.add(friend);
                    }
                }
                list.setAdapter(new FriendAdapter(getBaseContext(), R.layout.friend_layout, listData));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        groups.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, UserData.getInstance().getUser().getGroups()));
    }

    public void newGroup(View view){
        startActivity(new Intent(this, NewGroupView.class));
    }

    public void searchFriends(View view){
        startActivity(new Intent(this, SearchFriendsView.class));
    }

    @Override
    public void UpdateFriends(List<String> friends) {
        populateList(friends);
    }
}
