package com.example.ktpm_goclone_customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ktpm_goclone_customer.listeners.ItemListener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements ItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    ArrayList<ListHistoryLocation> locations ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        shopping_badge = view.findViewById(R.id.shopping_badge);
//        shopping_badge.setNumber(3);

        // This adapter for LOCATIONS
        locations = new ArrayList<>();
        recyclerViewList = view.findViewById(R.id.historyLocation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        locations.add(new ListHistoryLocation("29 Nguyen Huy Luong St.", "29 Nguyễn Huy Lượng, P.14, Q.Bình Thạnh, Hồ Chí Minh", "8 Feb 16:54", "4"));
        locations.add(new ListHistoryLocation("62 An Diem St.", "62 An Điểm, P.10, Q.5, Hồ Chí Minh, 7000, Vietnam", "31 Jan 22:04", "4"));
        locations.add(new ListHistoryLocation("63 Mac Dinh Chi St.", "63 Mạc Đỉnh Chi, P.Đa Kao, Q.1, Hồ Chí Minh, 7000, Vietnam", "31 Jan 20:35", "7"));
        locations.add(new ListHistoryLocation("227 Nguyen Van Cu St.", "227 Nguyen Van Cu, Q.5, Hồ Chí Minh, 7000, Vietnam", "10 Feb 20:35", "7"));

        adapter = new HistoryLocationAdapter(getContext(), locations,  this);
        recyclerViewList.setAdapter(adapter);


    }

    public void OnItemPosition(int position) {
        Intent intent = new Intent(getContext(), HistoryLocationDetailActivity.class);
        intent.putExtra("location",  locations.get(position));
        startActivity(intent);
    }
    @Override
    public void onStop() {
        super.onStop();

    }
}