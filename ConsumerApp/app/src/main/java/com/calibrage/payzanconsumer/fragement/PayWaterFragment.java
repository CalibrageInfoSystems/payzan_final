package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.OperatorModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Calibrage11 on 10/23/2017.
 */

public class PayWaterFragment extends BaseFragment {
    public static final String TAG = PayWaterFragment.class.getSimpleName();
    private View rootView;
    private Context context;
    private Spinner boardSpn;
    private CommonEditText consumerNEdt, amountEdt;
    private NCBTextInputLayout consNoTXT, boardTXT, amountTXT;
    private ArrayList<OperatorModel.ListResult> listResults;
    private Subscription operatorSubscription;
    private Button submit;
    private String boardStr,consumerStr,amountStr;
    private ArrayList<String> boardArrayList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pay_water, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setViews();
        initViews();


        return rootView;
    }

    private void setViews() {

        setHasOptionsMenu(true);
        listResults = new ArrayList<OperatorModel.ListResult>();
        /*((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
      /*  HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);*/
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.water_sname));
   /*     HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));*/
       /* HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });*/
        consumerNEdt = (CommonEditText) rootView.findViewById(R.id.consumerNEdt);
        amountEdt = (CommonEditText) rootView.findViewById(R.id.amountEdt);
        boardSpn = (Spinner) rootView.findViewById(R.id.boardSpn);
        boardTXT = (NCBTextInputLayout) rootView.findViewById(R.id.boardTXT);
        consNoTXT = (NCBTextInputLayout) rootView.findViewById(R.id.consNoTXT);
        amountTXT = (NCBTextInputLayout) rootView.findViewById(R.id.amountTXT);
        submit=(Button)rootView.findViewById(R.id.submit);

        getOperator(CommonConstants.SERVICE_PROVIDER_ID_WATER);

    }

    private void initViews() {
        boardSpn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
              //  boardSpn.showDropDown();
                return false;
            }
        });
        consumerNEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0)
                    consNoTXT.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    amountTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserLoginStatus(TAG)){
                   isValidateUi();
                }
            }
        });
    }

    private void getOperator(String providerType) {

        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getOperator(ApiConstants.MOBILE_SERVICES + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OperatorModel>() {
                    @Override
                    public void onCompleted() {
                       // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(OperatorModel operatorModel) {

                        listResults = (ArrayList<OperatorModel.ListResult>) operatorModel.getListResult();
                        for (int i = 0; i <listResults.size() ; i++) {
                            boardArrayList.add(listResults.get(i).getName());
                        }
//                        ArrayAdapter<OperatorModel.ListResult> listResultArrayAdapter = new ArrayAdapter<OperatorModel.ListResult>(context,android.R.layout.simple_dropdown_item_1line,listResults);
//                        currentOperator.setAdapter(listResultArrayAdapter);
                        ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,boardArrayList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        boardSpn.setAdapter(aa);
//
//                        GenericAdapter genericAdapter = new GenericAdapter(context, operatorModel.getListResult(), R.layout.adapter_single_item);
//                        genericAdapter.setAdapterOnClick(PayWaterFragment.this);
//                        boardSpn.setAdapter(genericAdapter);
                    }
                });
    }

    private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("waterTag");


        if (fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");
            CommonUtil.hideSoftKeyboard((AppCompatActivity)getActivity());
        }
    }

    private  boolean isValidateUi()
    {
        boardStr=(String) boardSpn.getSelectedItem();
        consumerStr=consumerNEdt.getText().toString().trim();
        amountStr=amountEdt.getText().toString().trim();
        if (TextUtils.isEmpty(boardStr))
        {
            boardTXT.setErrorEnabled(true);
            boardTXT.setError(getString(R.string.err_select_board));
            return  false;
        }
        else if (TextUtils.isEmpty(consumerStr))
        {
            consNoTXT.setErrorEnabled(true);
            consNoTXT.setError(getString(R.string.err_enter_consumer_no));
            return  false;
        }
        else if (TextUtils.isEmpty(amountStr))
        {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_enter_amount));
            return false;
        }

        return true;
    }
//    @Override
//    public void adapterOnClick(int position) {
//        boardSpn.setText(listResults.get(position).getName());
//    }


}
