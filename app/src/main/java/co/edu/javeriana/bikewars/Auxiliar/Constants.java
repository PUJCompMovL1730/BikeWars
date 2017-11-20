package co.edu.javeriana.bikewars.Auxiliar;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Todesser on 03/11/2017.
 */

public class Constants {
    //Firebase paths
    public static final String observablesRoot = "db/observables/";
    public static final String usersRoot = "db/users/";
    public static final String racesRoot = "db/races/";
    public static final String routesRoot = "db/routes/";
    public static final String groupsRoot = "db/groupes/";
    public static final String markersRoot = "db/markers/";
    public static final String mailBoxRoot = "db/mailBox/";
    //Implementation constants
    public static final int MAXBYTES = 10485760;
    public static final int OK = 1;
    public static final int ERROR = 0;
    //Map constants
    public static int cityLevel = 10;
    public static int streetLevel = 15;
    //Bogota's Location
    public static LatLng bogotaLocation = new LatLng(4.6275604, -74.0640883);
    //Bogota's Boundaries, obtained thank's to http://jsfiddle.net/petrabarus/5XQWx/
    public static double lowerLeftLatitude = 4.388435804754723;
    public static double lowerLeftLongitude= -74.38453087744142;
    public static double upperRightLatitude= 4.935932549646699;
    public static double upperRigthLongitude= -73.76654992041017;
    //Image Bank
    public static Map<String, Bitmap> photoBank = new HashMap<>();
}
