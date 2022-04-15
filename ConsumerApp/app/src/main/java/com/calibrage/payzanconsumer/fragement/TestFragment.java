package com.calibrage.payzanconsumer.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;


public class TestFragment extends BaseFragment {
    public static final String TAG = TestFragment.class.getSimpleName();

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        showToast(getContext(),"mahesh");
        return inflater.inflate(R.layout.fragment_test, container, false);
    }


}
