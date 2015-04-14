package com.socialparking.parking.parking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;


public class EntryScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);

        final EditText etUsermame = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etMail = (EditText) findViewById(R.id.etMail);
        Button bSignUp = (Button) findViewById(R.id.bSignUp);
        Button bLogIn = (Button) findViewById(R.id.bLogIn);

        etMail.setVisibility(View.INVISIBLE);

        final SharedPreferences pref = getSharedPreferences("userDetails", 0);

        //removeSharedPref();

        //check for username and password in sharedPreferences
        if (pref.contains("username") && pref.contains("password")) {
            Log.d("PS", "EntryScreen user has an account");
            //user has an account
            //proceed to main activity
            openMainActivity(null);
        } else {

            //user doesn't has an account saved on the phone
            Log.d("PS", "EntryScreen user DOESN'T has shared preferences");
            bSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etMail.getVisibility() == View.INVISIBLE) {
                        etMail.setVisibility(View.VISIBLE);
                    } else {
                        // ************  signUp   **********************
                        Log.d("PS", "EntryScreen signUp");
                        if(!etUsermame.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                            PsUser user = new PsUser(etUsermame.getText().toString(), etPassword.getText().toString(), etMail.getText().toString());
                            //check if requested username and password are available
                            checkUserDetails(user, "SignUp");
                        }else {
                            //username or password are missing! show alert
                            ParkingAlert.showAlert("invalidSignUp",null,EntryScreen.this);
                        }
                    }
                }
            });

            bLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ****************  logIn   ************************
                    Log.d("PS", "EntryScreen logIn");
                    PsUser user = new PsUser(etUsermame.getText().toString(), etPassword.getText().toString());
                    checkUserDetails(user, "LogIn");
                }
            });
        }
    }

    private void checkUserDetails(final PsUser user, final String action) {
        Log.d("PS", "checkUserDetails");
        final Model model = Model.getInstance(EntryScreen.this);
        final ProgressDialog pDialog = ProgressDialog.show(EntryScreen.this, "Verifying account", "please wait...", true);
        model.checkUser(user, new Model.checkUserCallback() {
            @Override
            //the result after check if user exists
            public void result(PsUser userResult) {
                Log.d("PS", "EntryScreen resultCallBack");
                //close progress dialog
                pDialog.dismiss();
                //if result is false - user doesn't exist
                //if result is true - user exists
                if ((userResult != null && action.equals("SignUp")) || (userResult == null && action.equals("LogIn"))) {
                    //entering here means UNSUCCESSFUL signUp or Login
                    //show alert
                    ParkingAlert.showAlert(action,null,EntryScreen.this);
                } else {//entering here means SUCCESSFUL signUp or Login
                    if(action.equals("SignUp")){
                        //successful signUp
                        //save new user to DB in background
                        final ProgressDialog pD = ProgressDialog.show(EntryScreen.this, "Creating account", "please wait...", true);
                        model.addUser(user, new Model.addUserClbk() {
                            @Override
                            public void addUserResult(ParseException e) {
                                pD.dismiss();
                                if(e==null/*result*/){
                                    Log.d("PS","EntryScreen addUserResult true");
                                    //successful signUp
                                    //save user data to shared preferences
                                    saveUserPref(user);
                                    openMainActivity(action);
                                }else{
                                    Log.d("PS","EntryScreen addUserResult true");
                                    //unsuccessful signUp
                                    //show alert
                                    ParkingAlert.showAlert("exception",e,EntryScreen.this);
                                }
                            }
                        });
                    }else{
                        //successful logIn
                        //save user data to shared preferences
                        saveUserPref(userResult);
                        openMainActivity(action);
                    }
                }
            }
        });
    }

    //saves user data to shared preferences after signUp/logIn
    private void saveUserPref(PsUser user) {
        Log.d("PS", "EntryScreen saveUserPref");
        final SharedPreferences pref = getSharedPreferences("userDetails", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("email", user.getEmail());
        editor.commit();
    }

    //opens the main activity
    private void openMainActivity(String action) {
        Log.d("PS", "EntryScreen openMainActivity");
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
        if (action != null) {
            Toast.makeText(getApplicationContext(), action + " Successful", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void removeSharedPref() {
        Log.d("PS", "EntryScreen removeSharedPref");
        final SharedPreferences pref = getSharedPreferences("userDetails", 0);

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("email");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_screen, menu);
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
