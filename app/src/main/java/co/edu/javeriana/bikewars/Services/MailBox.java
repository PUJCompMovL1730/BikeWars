package co.edu.javeriana.bikewars.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.javeriana.bikewars.Auxiliar.Constants;
import co.edu.javeriana.bikewars.ChatView;
import co.edu.javeriana.bikewars.Logic.Entities.dbMail;
import co.edu.javeriana.bikewars.Logic.Entities.dbUser;

/**
 * Created by jindrax on 19/11/17.
 */

public class MailBox extends Service {

    private DatabaseReference mailRef;
    private ChildEventListener mailListener;
    private int notificationID;
    private NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationID = 0;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String userID = intent.getStringExtra("userID");
            final int icon = intent.getIntExtra("icon", 0);
            mailRef = FirebaseDatabase.getInstance().getReference(Constants.mailBoxRoot + userID);
            mailListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mailRef.child(dataSnapshot.getKey()).orderByChild("date").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnap: dataSnapshot.getChildren()) {
                                final dbMail message = messageSnap.getValue(dbMail.class);
                                if (!message.isProcessed()) {
                                    FirebaseDatabase.getInstance().getReference(Constants.usersRoot + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dbUser user = dataSnapshot.getValue(dbUser.class);
                                            NotificationCompat.Builder nb = new NotificationCompat.Builder(getBaseContext())
                                                    .setSmallIcon(icon)
                                                    .setContentTitle("Mensaje nuevo")
                                                    .setContentText(user.getDisplayName() + " le ha enviado un mensaje.")
                                                    .setAutoCancel(true);
                                            Intent notificationIntent = new Intent(getBaseContext(), ChatView.class);
                                            notificationIntent.putExtra("name", user.getDisplayName());
                                            notificationIntent.putExtra("photo", user.getPhoto());
                                            notificationIntent.putExtra("userID", user.getUserID());
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                                            nb.setContentIntent(pendingIntent);
                                            Log.i("Mailbox", "Lanzada desde Added");
                                            manager.notify(notificationID, nb.build());
                                            notificationID++;
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // TODO: 19/11/2017 Manejar caso de error
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // TODO: 19/11/2017 Manejar caso de error
                        }
                    });
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mailRef.child(dataSnapshot.getKey()).orderByChild("date").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnap: dataSnapshot.getChildren()) {
                                final dbMail message = messageSnap.getValue(dbMail.class);
                                if (!message.isProcessed()) {
                                    FirebaseDatabase.getInstance().getReference(Constants.usersRoot + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dbUser user = dataSnapshot.getValue(dbUser.class);
                                            NotificationCompat.Builder nb = new NotificationCompat.Builder(getBaseContext())
                                                    .setSmallIcon(icon)
                                                    .setContentTitle("Mensaje nuevo")
                                                    .setContentText(user.getDisplayName() + " le ha enviado un mensaje.")
                                                    .setAutoCancel(true);
                                            Intent notificationIntent = new Intent(getBaseContext(), ChatView.class);
                                            notificationIntent.putExtra("name", user.getDisplayName());
                                            notificationIntent.putExtra("photo", user.getPhoto());
                                            notificationIntent.putExtra("userID", user.getUserID());
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                                            nb.setContentIntent(pendingIntent);
                                            Log.i("Mailbox", "Lanzada desde Added");
                                            manager.notify(notificationID, nb.build());
                                            notificationID++;
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // TODO: 19/11/2017 Manejar caso de error
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // TODO: 19/11/2017 Manejar caso de error
                        }
                    });
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mailRef.addChildEventListener(mailListener);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mailRef.removeEventListener(mailListener);
        super.onDestroy();
    }
}
