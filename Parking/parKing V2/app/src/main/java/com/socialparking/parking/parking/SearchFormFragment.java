package com.socialparking.parking.parking;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFormFragment extends Fragment {

    searchCallBack sClBk=null;
    SearchView svSearcherLocation;

    public SearchFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sClBk=(searchCallBack)activity;
    }

    public interface searchCallBack {
        public void onSearchCallBack(SearcherSpot searcherSpot);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PS", "SearchFormFragment onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("PS", "SearchFormFragment onCreateView");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_form, container, false);

        SearchView svSearcherLocation = (SearchView) view.findViewById(R.id.svSearcherLocation);
        svSearcherLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //add data to the point after locating the coordinates of requested search
                SearcherSpot ss = new SearcherSpot(MainActivity.currentUser.getUsername());
                ss = (SearcherSpot) ss.convertAddressToPoint(getActivity().getApplicationContext(), query);
                if (ss != null) {
                    //returns to release to show current location
                    sClBk.onSearchCallBack(ss);
                } else {
                    //address wasn't found
                    ParkingAlert.showAlert("addressError",null,getActivity());
                }
                //hides the softKeyboard when done typing
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

//    private void showAlert() {
//        Log.d("PS", "SearchFormFragment showAlert");
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Address wasn't found..").setNeutralButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog aD = builder.create();
//        aD.show();
//    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFormFragment newInstance(String param1, String param2) {
        SearchFormFragment fragment = new SearchFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
