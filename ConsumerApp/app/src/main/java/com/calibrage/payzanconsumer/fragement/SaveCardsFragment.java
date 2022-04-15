package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.adapters.SaveCardsAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;

import java.util.ArrayList;
import java.util.List;




public class SaveCardsFragment extends BaseFragment {
    public static final String TAG = SaveCardsFragment.class.getSimpleName();
    Context context;
    RecyclerView savecardrecycler;
    List<SaveCardsModel> listdata;
    private CommonButton savecardbtn;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_save_cards, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        savecardrecycler=(RecyclerView)view.findViewById(R.id.savecardrecycler);
        SaveCardsAdapter saveCardsAdapter=new SaveCardsAdapter(context,getdata());

        savecardrecycler.setAdapter(saveCardsAdapter);
        savecardrecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.save_card));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });

        setView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    closeTab();
                    // onCloseFragment();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return view;
    }

    private void setView()
    {
        savecardbtn=(CommonButton)view.findViewById(R.id.savecardbtn);
        savecardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(getActivity(), MAIN_CONTAINER, new AddCardFragment(), TAG, AddCardFragment.TAG);
            }
        });
    }

    private   List<SaveCardsModel> getdata()
    {
       List<SaveCardsModel> data=new ArrayList<>();
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));

        return  data;

    }
    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new UserProfileHome(), TAG, SaveCardsFragment.TAG);

    }

}
