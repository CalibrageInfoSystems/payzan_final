package com.calibrage.payzanconsumer.fragement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.AboutUSActivity;
import com.calibrage.payzanconsumer.activity.BarcodeReader_Activity;
import com.calibrage.payzanconsumer.activity.ChangePasswordActivity;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.activity.Language;
import com.calibrage.payzanconsumer.activity.OrderHistoryActivity;
import com.calibrage.payzanconsumer.activity.ProfileActivity1;
import com.calibrage.payzanconsumer.activity.SaveCardActivity;
import com.calibrage.payzanconsumer.activity.TermsofServiceActivity;
import com.calibrage.payzanconsumer.fragement.agent.AgentLoginFragment;
import com.calibrage.payzanconsumer.framework.adapters.SingleLineDropDownlanguageAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.updateResources;


public class UserProfileHome extends BaseFragment {
    public static final String TAG = UserProfileHome.class.getSimpleName();
    private Button btn_logOut, btn_Share,addMoneyBtn,btn_QRscanner,btn_lang;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private LinearLayout saveCardLyt,orderHistoryLyt, changePsdLyt,aboutuslyt,tofslyt,langLyt;
    private Context context;
    private ImageView profileImage,editProfile;
    private TextView walletBalanceTxt,userMail,userName;
    private View v;
    private Spinner lst_spinner;
    int usertype=0;
    private Subscription mGetTransactions;
    private SwipeRefreshLayout swiprefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_user_profile_home, container, false);
      /*  v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

        context = this.getActivity();
        usertype = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("usertype");
        setViews();
        initViews();
      //  IntiateSpinner();


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
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
        swiprefresh=(SwipeRefreshLayout)v.findViewById(R.id.swiprefresh);
        swiprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Toast.makeText(context, "OnRefreshListener", Toast.LENGTH_SHORT).show();
                int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                if(checkUserLoginStatusWallet(TAG)){
                    getTransactions(values);
                }else {
                    swiprefresh.setRefreshing(false);
                }


            }


        });
        return v;
    }

    private void setViews() {

        btn_logOut = (Button) v.findViewById(R.id.btn_sign_out);
        btn_Share = (Button) v.findViewById(R.id.btn_share);
        addMoneyBtn = (Button) v.findViewById(R.id.addMoneyBtn);
        changePsdLyt = (LinearLayout) v.findViewById(R.id.changePsdLyt);
        aboutuslyt=(LinearLayout)v.findViewById(R.id.aboutuslyt);
        walletBalanceTxt = (TextView) v.findViewById(R.id.walletBalanceTxt);
        userName = (TextView) v.findViewById(R.id.userName);
        userMail = (TextView) v.findViewById(R.id.userMail);
        editProfile = (ImageView) v.findViewById(R.id.editProfile);
        saveCardLyt=(LinearLayout)v.findViewById(R.id.saveCardLyt);
        orderHistoryLyt=(LinearLayout)v.findViewById(R.id.orderHistoryLyt);
        tofslyt=(LinearLayout)v.findViewById(R.id.tofslyt);
        langLyt=(LinearLayout)v.findViewById(R.id.langlyt);
        btn_QRscanner =v.findViewById(R.id.btn_QRscanner);
        lst_spinner=v.findViewById(R.id.spinner);
    }

    private void initViews() {

        LoginResponseModel loginResponseModel =     new Gson().fromJson(SharedPrefsData.getInstance(context).getUserDetails(context), LoginResponseModel.class);
        if(loginResponseModel!=null){
            walletBalanceTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletIdMoney(context)));
            userName.setText(SharedPrefsData.getInstance(context).getUserName(context));
            userMail.setText(loginResponseModel.getdata().getUser().getEmail());
        }



        saveCardLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SaveCardActivity.class);
                context.startActivity(intent);


            }
        });
        orderHistoryLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, OrderHistoryActivity.class);
               context.startActivity(intent);
            }
        });
        changePsdLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChangePasswordActivity.class);
                context.startActivity(intent);
            }
        });
        aboutuslyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, AboutUSActivity.class);
               context.startActivity(intent);
            }
        });
        tofslyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TermsofServiceActivity.class);
                context.startActivity(intent);
            }
        });
        langLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replaceFragment(getActivity(), MAIN_CONTAINER, new TermsofServicesFragment(), TAG, TermsofServicesFragment.TAG);
                //startActivity(new Intent(context, Language.class));

                selectLanguage();
            }
        });






        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(getActivity(), MAIN_CONTAINER, new TransactionMainFragment(), TAG, TransactionMainFragment.TAG);
            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsData.getInstance(getActivity()).ClearData(getActivity());

                // Toast.makeText(getActivity(), "DATA Cleared", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getContext(), HomeActivity.class);
//                startActivity(i);
                // getActivity().finish();

                closeTab();

            }
        });
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ishare = new Intent(Intent.ACTION_SEND);
                Ishare.setType("text/plain");
                Ishare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                Ishare.putExtra(Intent.EXTRA_SUBJECT, "PayZan Android");
                Ishare.putExtra(Intent.EXTRA_TEXT, "http://calibrage.in/");

                startActivity(Intent.createChooser(Ishare, "Share link!"));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, ProfileActivity1.class);
               context.startActivity(intent);


            }
        });
        btn_QRscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), BarcodeReader_Activity.class);
                context.startActivity(i);
            }
        });

    }

    private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("profileHomeTag");
        mActivityListener.messageFromChildFragmentToActivity("signout");

        if (fragment != null) {


            if (usertype == CommonConstants.ISAGENT)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AgentLoginFragment()).commit();
                HomeActivity.toolbar.setNavigationIcon(null);
                HomeActivity.toolbar.setTitle("");
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            }
            else
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
                HomeActivity.toolbar.setNavigationIcon(null);
                HomeActivity.toolbar.setTitle("");
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            }


        }

    }

    private void selectLanguage(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setTitle("Choose Language...");
//        dialog.setTitle("Choose Language...");
      //  dialog.setT

//        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//        TextView tv = (TextView) dialog.findViewById(textViewId);
//        tv.setTextColor(context.getResources().getColor(R.color.new_accent));
//        tv.setText("Choose Language");
//        dialog.setTitle(textViewId);
        // set the custom dialog components - text, image and button
        RadioButton rbEng = dialog.findViewById(R.id.rbEng);
        RadioButton rbSim = dialog.findViewById(R.id.rbSim);
        RadioButton rbTl = dialog.findViewById(R.id.rbTl);

        rbEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(context, "rdo_english", Toast.LENGTH_SHORT).show();

                updateResources(context, "en-US");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 1);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);

                dialog.dismiss();
            }
        });
        rbSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context, "rdo_english", Toast.LENGTH_SHORT).show();

                updateResources(context, "si");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 2);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);


                dialog.dismiss();
            }
        });
        rbTl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Toast.makeText(context, "rdo_english", Toast.LENGTH_SHORT).show();

                updateResources(context, "ta");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 3);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);

                dialog.dismiss();
            }
        });

        dialog.show();
    }




public  void IntiateSpinner()
{
    final ArrayList<String> data=new ArrayList<>();
    data.add("Telugu");
    data.add("Suresh");
    data.add("naeresh");
    data.add("Rakesh");
    data.add("Anil");
    data.add("mahesh");
    data.add("Suresh");
    data.add("naeresh");
    data.add("Rakesh");
    data.add("Anil");
    SingleLineDropDownlanguageAdapter adapter=new SingleLineDropDownlanguageAdapter(getActivity(), android.R.layout.simple_spinner_item,data);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  //  @SuppressLint("ResourceType") ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,R.array.days_of_week);
   // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    lst_spinner.setAdapter(adapter);

    lst_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


               data.get(i);

            Toast.makeText(context, "Selected :"+data.get(i), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }
    });
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if Activity implements listener
        if (context instanceof OnChildFragmentToActivityInteractionListener) {
            mActivityListener = (OnChildFragmentToActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChildFragmentToActivityInteractionListener");
        }
    }
    public  void getTransactions(int walletId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        String url = ApiConstants.BASE_URL+ApiConstants.BalenceBYID + walletId;
        mGetTransactions = service.GetUserBalanceByID(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletBalanceResponce>() {
                    @Override
                    public void onCompleted() {
                        hideDialog();
                        // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
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
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();

                        walletBalanceTxt.setText((" " + transactionResponseModel.getResult().getBalance()));
                        Long val= Long.valueOf(transactionResponseModel.getResult().getBalance());
                        SharedPrefsData.getInstance(getActivity()).saveWalletIdMoney(context, val);

                        // Toast.makeText(context, "Wallet Balance: "+transactionResponseModel.getResult().getBalance(), Toast.LENGTH_SHORT).show();
                        swiprefresh.setRefreshing(false);

                    }
                });
    }




}
