package com.socialparking.parking.parking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    PsUser user=PsUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create this user instance
        createCurrentUser();
        // Associate the device with a user
        Model.getInstance(this);//in order to initialize parse
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.put("myUser", user.getUsername());
        installation.saveInBackground();

        Button bSearch=(Button) findViewById(R.id.bSearch);
        Button bRelease=(Button) findViewById(R.id.bRelease);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PS", "MainActivity search onClickListener");
                //beginning search process
                //open search activity
                Intent iSearch=new Intent(getApplicationContext(),Search.class);
                startActivity(iSearch);
            }
        });

        bRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PS", "MainActivity release onClickListener");
                //beginning of release process
                //open release activity
                Intent iRelease=new Intent(getApplicationContext(),Release.class);
                startActivity(iRelease);
            }
        });
    }

    private void createCurrentUser(){
        Log.d("PS", "MainActivity createCurrentUser");
        SharedPreferences pref =getSharedPreferences("userDetails", 0);
        user.setUsername(pref.getString("username","error"));
        user.setPassword(pref.getString("password", "error"));
        user.setEmail(pref.getString("email","error"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
