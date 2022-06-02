package com.payment.arthpay.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.payment.arthpay.R;
import com.payment.arthpay.activity.ContactUs;
import com.payment.arthpay.activity.KYCStatus;
import com.payment.arthpay.adapter.MenuListAdapter;
import com.payment.arthpay.model.ProfileMenuModel;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    private ListView listView;
    private Context context;

    public HelpFragment() {
        context = getActivity();
        // Required empty public constructor
    }

    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_fragment, container, false);

        initialiseMenuList(view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), ContactUs.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), KYCStatus.class));
                }
            }
        });

        return view;
    }

    private void initialiseMenuList(View view) {
        List<ProfileMenuModel> dataList = new ArrayList<>();
        // dataList.add(new ProfileMenuModel(R.drawable.complain_ic, "Complain Box"));
        dataList.add(new ProfileMenuModel(R.drawable.contact_ic, "Contact Us"));
        //dataList.add(new ProfileMenuModel(R.drawable.kyc_ic, "KYC Status"));
        listView = view.findViewById(R.id.listView);
        MenuListAdapter adapter = new MenuListAdapter(getActivity(), dataList);
        listView.setAdapter(adapter);
    }

}