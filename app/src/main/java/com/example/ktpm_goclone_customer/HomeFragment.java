package com.example.ktpm_goclone_customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NotificationBadge shopping_badge;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;

    private RecyclerView.Adapter adapter2;
    private RecyclerView recyclerViewList2;
    private TextView locationTextView;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        if (mapsFragment == null){
//            mapsFragment = new MapsFragment();
//        }
//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, mapsFragment)
//                .commit();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        shopping_badge = view.findViewById(R.id.shopping_badge);
//        shopping_badge.setNumber(3);

        // This adapter for LOCATIONS

        recyclerViewList = view.findViewById(R.id.view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        ArrayList<ListLocation> locations = new ArrayList<>();
        locations.add(new ListLocation("29 Nguyen Huy Luong St.", "29 Nguyễn Huy Lượng, P.14, Q.Bình Thạnh, Hồ Chí Minh"));
        locations.add(new ListLocation("62 An Diem St.", "62 An Điểm, P.10, Q.5, Hồ Chí Minh, 7000, Vietnam"));
        locations.add(new ListLocation("63 Mac Dinh Chi St.", "63 Mạc Đỉnh Chi, P.Đa Kao, Q.1, Hồ Chí Minh, 7000, Vietnam"));

        adapter = new LocationAdapter(locations);
        recyclerViewList.setAdapter(adapter);


        // This adapter for SAVE PLACES
        recyclerViewList2 = view.findViewById(R.id.view2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewList2.setLayoutManager(linearLayoutManager2);
        ArrayList<ListLocation> marks = new ArrayList<>();
        marks.add(new ListLocation("THPT N....", ""));
        marks.add(new ListLocation("THPT N...", ""));
        marks.add(new ListLocation("29 Nguy...", ""));
        marks.add(new ListLocation("Trường ...", ""));
        marks.add(new ListLocation("Home...", ""));

        adapter2 = new MarkAdapter(marks);
        recyclerViewList2.setAdapter(adapter2);


        locationTextView= view.findViewById(R.id.locationTextView);
        locationTextView.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), MapsActivity.class));
        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}