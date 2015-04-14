package com.socialparking.parking.parking;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Evyatar.m on 06/02/2015.
 */
public class Model {

    private static Model instance;
    private String applicationID = "7vihNeuDIU6tOTxHxLxEbp9OIiV3ye7BB6cgS644";
    private String clientKey = "Rbs8OfqHyOVMu3I19KtRubYXVQp3qjoeQuKGIluz";
    private List<ParkingSpot> allParkingSpots = new ArrayList<ParkingSpot>();
    PsUser user= PsUser.getInstance();

    private Model(Context context) {
        Parse.initialize(context, applicationID, clientKey);
        ParsePush.subscribeInBackground("Adam", new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }

    public static Model getInstance(Context context) {
        Log.d("PS", "Model getInstance");
        if (instance == null) {
            instance = new Model(context);
        }

        return instance;
    }

    interface addUserClbk {
        public void addUserResult(ParseException e);
//        public void addUserResult(PsUser user, boolean result);
    }

    //add user to parse
    public void addUser(addUserClbk aUClbk) {
        Log.d("PS", "Model addUser");
        final addUserClbk myClbk = aUClbk;
        ParseUser parseU = userToJson(user);
        //saving to parse
        parseU.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                myClbk.addUserResult(e);
//                if (e == null) {
//                    //successful signUp
//                    myClbk.addUserResult(user, true);
//                } else {
//                    //unsuccessful signUp
//                    Log.d("PS",e.toString());
//                    e.printStackTrace();
//                    myClbk.addUserResult(user, false);
//                }
            }
        });
    }

    private ParseUser userToJson(PsUser user) {
        Log.d("PS", "Model userToJson");
        ParseUser u = new ParseUser();
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        return u;
    }

    //add parking spot to parse
    public boolean addParkingSpot(ParkingSpot parkingSpot) {
        Log.d("PS", "Model addParkingSpot");
        ParseObject parsePS = parkingSpotToJson(parkingSpot);
        try {
            //saving to parse
            parsePS.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //add parking spot to parse
    public boolean addSearcherSpot(SearcherSpot searcherSpot) {
        Log.d("PS", "Model addSearcherSpot");
        ParseObject parsePS = searcherSpotToJson(searcherSpot);
        try {
            //saving to parse
            parsePS.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private SearcherSpot jsonToSearcherSpot(ParseObject o) {
        Log.d("PS", "Model jsonToSearcherSpot");
        SearcherSpot ss = new SearcherSpot(o.getDouble("latitude"), o.getDouble("longitude"), o.getString("username"));
        return ss;
    }

    private ParseObject searcherSpotToJson(SearcherSpot ss) {
        Log.d("PS", "Model searcherSpotToJson");
        ParseObject s = new ParseObject("SearcherSpot");
        s.put("latitude", ss.getLat());
        s.put("longitude", ss.getLong());
        s.put("username", ss.getUsername());
        return s;
    }

    private ParkingSpot jsonToParkingSpot(ParseObject o) {
        Log.d("PS", "Model jsonToParkingSpot");
        ParkingSpot ps = new ParkingSpot(o.getDouble("latitude"), o.getDouble("longitude"), o.getString("username"), o.getString("name"));
        return ps;
    }

    private ParseObject parkingSpotToJson(ParkingSpot ps) {
        Log.d("PS", "Model parkingSpotToJson");
        ParseObject p = new ParseObject("ParkingSpot");
        p.put("latitude", ps.getLat());
        p.put("longitude", ps.getLong());
        p.put("username", ps.getUsername());
        p.put("name", ps.getName());
        return p;
    }

    interface GetAllParkingSpotsClbck {
        public void done(List<ParkingSpot> parkingSpots);
    }

    public void getAllParkingSpots(GetAllParkingSpotsClbck clbck, final ParkingMap map) {
        Log.d("PS", "Model getAllParkingSpots");
        final GetAllParkingSpotsClbck getAllListener = clbck;

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ParkingSpot");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<ParkingSpot> list = new ArrayList<ParkingSpot>();
                for (ParseObject o : objects) {
                    ParkingSpot ps = jsonToParkingSpot(o);
                    list.add(ps);
                    //************************************************************************************
                    //the next line is here because we didn't managed to use it from the ParkingMap class
                    //************************************************************************************
                    map.setParkingSpotMarker(ps);
                }
                getAllListener.done(list);
            }
        });
    }

    interface GetAllSearchersClbck {
        public void done(List<SearcherSpot> searcherSpots);
    }

    public void getAllSearcherSpots(GetAllSearchersClbck clbck, final ParkingMap map) {
        Log.d("PS", "Model getAllSearchers");
        final GetAllSearchersClbck getAllListener = clbck;

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SearcherSpot");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<SearcherSpot> list = new ArrayList<SearcherSpot>();
                SearcherSpot ss;
                for (ParseObject o : objects) {
                    ss = jsonToSearcherSpot(o);
                    list.add(ss);
                    //************************************************************************************
                    //the next line is here because we didn't managed to use it from the ParkingMap class
                    //************************************************************************************
                    map.setSearcherSpotMarker(ss);
                }
                getAllListener.done(list);
            }
        });
    }

    interface checkUserCallback {
        public void result(boolean userResult);
    }

    public void checkUser(checkUserCallback cUClbk) {
        Log.d("PS", "Model checkUser");

        final checkUserCallback myClbk = cUClbk;
        ParseUser.logInInBackground(user.getUsername(), user.getPassword(), new com.parse.LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    //successful logIn
                    Log.d("PS", "Model checkUser not null");
                    user.setEmail(parseUser.getEmail());
                    myClbk.result(true);
                } else {
                    //unsuccessful logIn
                    Log.d("PS", e.toString());
                    myClbk.result(false);
                }

            }
        });
    }

    public void editSearcherSpot(final Location location) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SearcherSpot");
        query.whereEqualTo("username", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> parseObjects, ParseException e) {
                                       if (parseObjects.size() > 0) {
                                           ParseObject searcherSpotToEdit = parseObjects.get(0);
                                           searcherSpotToEdit.put("latitude", location.getLatitude());
                                           searcherSpotToEdit.put("longitude", location.getLongitude());
                                           searcherSpotToEdit.saveInBackground();
                                       }
                                   }
                               }
        );
    }


}
