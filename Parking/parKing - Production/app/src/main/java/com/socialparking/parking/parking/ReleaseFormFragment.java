package com.socialparking.parking.parking;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
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
 * Use the {@link ReleaseFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReleaseFormFragment extends Fragment {

    releaseCallBack rClBk = null;
    SearchView svReleaserLocation;
    PsUser user=PsUser.getInstance();

    public ReleaseFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        rClBk = (releaseCallBack) activity;
    }

    public interface releaseCallBack {
        public void onReleaseCallBack(ParkingSpot parkingSpot);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PS", "ReleaseFormFragment onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("PS", "ReleaseFormFragment onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release_form, container, false);

        svReleaserLocation = (SearchView) view.findViewById(R.id.svReleaserLocation);
        svReleaserLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //add data to the point after locating the coordinates of requested search
                ParkingSpot ps = new ParkingSpot(user.getUsername());
                ps = (ParkingSpot) ps.convertAddressToPoint(getActivity().getApplicationContext(), query);
                if (ps != null) {
                    //returns to release to show current location

                    rClBk.onReleaseCallBack(ps);
                } else {
                    //address wasn't found
                    showAlert();
                }
                //hides the softKeyboard when done typing
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
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

    private void showAlert() {
        Log.d("PS", "ReleaseFormFragment showAlert");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Address wasn't found..").setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog aD = builder.create();
        aD.show();
    }

//    @Override
//    public void onPause() {
//        Log.d("PS", "ReleaseFormFragment onPause");
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        Log.d("PS", "ReleaseFormFragment onStop");
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d("PS", "ReleaseFormFragment onDestroy");
//        super.onDestroy();
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
     * @return A new instance of fragment ReleaseFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReleaseFormFragment newInstance(String param1, String param2) {
        ReleaseFormFragment fragment = new ReleaseFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
