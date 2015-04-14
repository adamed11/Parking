package com.socialparking.parking.parking;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PendingRequestsList extends ActionBarActivity {

    public static List<String> pendingRequests=new ArrayList<String>();
    public static CustomAdapter adapter;
    private ListView lv;
    public static boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests_list);
        lv = (ListView) findViewById(R.id.lvPendingRquests);
        adapter = new CustomAdapter();
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        active=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending_requests_list, menu);
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

    class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public CustomAdapter() {
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return pendingRequests.size();
        }

        @Override
        public Object getItem(int arg0) {
            return pendingRequests.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int location, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.pendeing_request, parent,
                        false);
            }

//            Restaurant rest = restaurantsList.get(location);

            TextView request = (TextView) convertView.findViewById(R.id.tvRequestor);
//            TextView phone = (TextView) convertView.findViewById(R.id.phone);
//            ImageView image = (ImageView) convertView
//                    .findViewById(R.id.imageView1);

            request.setText("Requestor: " + pendingRequests.get(location));

            return convertView;
        }

    }
}
