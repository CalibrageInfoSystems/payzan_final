package com.calibrage.payzanconsumer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.MyOrderFragment;
import com.calibrage.payzanconsumer.framework.adapters.PaymentMethodsAdapter;
import com.calibrage.payzanconsumer.framework.interfaces.PaymentMethodClickListiner;

import java.util.ArrayList;


public class CompletePayment extends AppCompatActivity implements PaymentMethodClickListiner {

    private FrameLayout content_frame;
    private FragmentManager fragmentManager;
    private ArrayList<Pair<Integer, String>> menuPairList = new ArrayList<>();
    private RecyclerView lst_payment;
    private LinearLayoutManager mLayoutManager;
    PaymentMethodClickListiner methodClickListiner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_payment);
        fragmentManager = getSupportFragmentManager();

        mLayoutManager = new LinearLayoutManager(this);
        menuPairList.add(Pair.create(R.drawable.ic_mobile, "Saved Cards"));
        menuPairList.add(Pair.create(R.drawable.ic_electricity, "Credit Card"));
        menuPairList.add(Pair.create(R.drawable.ic_event, "Debit Card"));
        menuPairList.add(Pair.create(R.drawable.ic_sport, "Net Banking"));

        PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(menuPairList, getBaseContext());
        paymentMethodsAdapter.setOnAdapterListener(this);
        lst_payment = (RecyclerView) findViewById(R.id.Lst_PaymentMethods);
        lst_payment.setLayoutManager(new GridLayoutManager(this, 4));
        lst_payment.setAdapter(paymentMethodsAdapter);


    }


    @Override
    public void onPaymentMethodClickListiner(int pos) {
        MyOrderFragment Mobile=new MyOrderFragment();
        ReplcaFragment(Mobile);

    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
    }


}
