package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.SaveCardsAdapter;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;

import java.util.ArrayList;
import java.util.List;

public class SaveCardActivity extends AppCompatActivity {

   Toolbar toolbar;
    CommonButton savecardbtn;
    RecyclerView savecardrecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_save_cards);
        setView();
    }
    private void setView()
    {
        savecardrecycler=(RecyclerView)findViewById(R.id.savecardrecycler);
        savecardbtn=(CommonButton)findViewById(R.id.savecardbtn);
        SaveCardsAdapter saveCardsAdapter=new SaveCardsAdapter(this,getdata());
        savecardrecycler.setAdapter(saveCardsAdapter);
        savecardrecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.save_card));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        savecardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaveCardActivity.this,AddCardActivity.class);
                startActivity(intent);
            }
        });

    }


    private List<SaveCardsModel> getdata()
    {
        List<SaveCardsModel> data=new ArrayList<>();
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));

        return  data;

    }
}
