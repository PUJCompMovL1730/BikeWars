package co.edu.javeriana.bikewars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.bikewars.Adapters.AddFriendAdapter;
import co.edu.javeriana.bikewars.Interfaces.FriendListener;
import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;
import co.edu.javeriana.bikewars.Logic.UserData;

public class SearchFriendsView extends AppCompatActivity implements FriendListener{

    private EditText serachTxt;
    private ListView list;
    private List<dbObservable> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends_view);
        list = findViewById(R.id.searchFriendsList);
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
                    if(!friends.contains(friend.getUserID()) && !friend.getUserID().equals(UserData.getInstance().getUser().getUserID())){
                        listData.add(friend);
                    }
                }
                list.setAdapter(new AddFriendAdapter(getBaseContext(), R.layout.add_friend_layout, listData));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void UpdateFriends(List<String> friends) {
        populateList(friends);
    }
}
