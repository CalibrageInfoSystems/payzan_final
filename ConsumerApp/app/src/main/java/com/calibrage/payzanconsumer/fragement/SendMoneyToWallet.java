package com.calibrage.payzanconsumer.fragement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.DrawableClickListener;
import com.calibrage.payzanconsumer.framework.model.SendMoneyModel;
import com.calibrage.payzanconsumer.framework.model.SendMoneyResponseModel;
import com.calibrage.payzanconsumer.framework.model.UserWalletHistory;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Calibrage11 on 10/20/2017.
 */

public class SendMoneyToWallet extends BaseFragment {
    public static final String TAG = SendMoneyToWallet.class.getSimpleName();
    private View rootView;
    private Context context;
    private NCBTextInputLayout mobileNumberTXT, amountTXT, commentTXT;
    private CommonEditText mobileEdt, amount, commentEdt;
    static final int PICK_CONTACT = 1;
    private CommonButton submitBtn;
    private Subscription sendMoneySubscription;
    private String mobileStr, amountStr, commentStr;
    private Subscription mGetTransactions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_send_money, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();
        setViews();
        initViews();
        Gson gson = new Gson();
        String json = gson.toJson(new WalletResponse());
        Log.d(TAG, "onCreateView: " + json);

        return rootView;
    }

    private void initViews() {


        mobileEdt.setDrawableClickListener(new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);

                        break;

                    default:
                        break;
                }
            }

        });

        mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mobileNumberTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               /* if (!editable.toString().startsWith("+94")) {
                    mobileEdt.setText("+94");
                    Selection.setSelection(mobileEdt.getText(), mobileEdt
                            .getText().length());

                }*/
            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    amountTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateUi()) {
                    // Toast.makeText(context, "details", Toast.LENGTH_SHORT).show();
                    showDialog(getActivity(), "Please Wait Loading ");
                    sendMoneyRequest();
                }
            }
        });


    }

    private boolean validateUi() {
        mobileStr = mobileEdt.getText().toString().trim();
        amountStr = amount.getText().toString().trim();
        commentStr = commentEdt.getText().toString().trim();
        String s = amount.getText().toString().trim();

        if (TextUtils.isEmpty(mobileStr)) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_mobile_no));
            return false;
        } else if (!isValidPhone()) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        } else if (TextUtils.isEmpty(amountStr)) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_enter_amount));
            return false;
        } else if (!CommonUtil.isValidAmout(s)) {

            Toast.makeText(getApplicationContext(), R.string.toast_amount_is_not_valid, Toast.LENGTH_SHORT).show();
            return false;
           /* Double d = Double.parseDouble(s);
            try {

                if (s.length() < 1 || s.length() <= 15) {
                    Toast.makeText(getApplicationContext(), " amount is valid", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Toast.makeText(getApplicationContext(), " amount is not valid", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                amount.setText(" ");
                Toast.makeText(getApplicationContext(), "Bad format for number!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "field must not be empty or null", Toast.LENGTH_SHORT).show();
            return false;*/
        }


        return true;
    }

    private boolean isValidPhone() {
        String target = mobileEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void sendMoneyRequest() {


        hideDialog();
        JsonObject object = postJsonObject();

        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        sendMoneySubscription = service.sendMoneyRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendMoneyResponseModel>() {
                    @Override
                    public void onCompleted() {
                        hideDialog();
                        //    Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();

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



                    }

                    @Override
                    public void onNext(SendMoneyResponseModel sendMoneyResponseModel) {
                        hideDialog();
                        Toast.makeText(getActivity(), sendMoneyResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();


                        mobileEdt.setText("");
                        amount.setText("");

                        int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                        getTransactions(values);
                    }
                });
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
                         TransactionMainFragment.walletBalanceTxt.setText(("Wallet Balance: " + transactionResponseModel.getResult().getBalance()));
                        Long val= Long.valueOf(transactionResponseModel.getResult().getBalance());
                        SharedPrefsData.getInstance(getActivity()).saveWalletIdMoney(context, val);

                        // Toast.makeText(context, "Wallet Balance: "+transactionResponseModel.getResult().getBalance(), Toast.LENGTH_SHORT).show();


                    }
                });
    }
    
    private JsonObject postJsonObject() {
        SendMoneyModel sendMoney = new SendMoneyModel();
        sendMoney.setRecieverUserName(mobileEdt.getText().toString());
        UserWalletHistory userWalletHistory = new UserWalletHistory();
        userWalletHistory.setAmount(Double.valueOf(amount.getText().toString()));
        // userWalletHistory.setCreated("");
        // userWalletHistory.setCreatedBy(SharedPrefsData.getInstance(context).getUserId(context));
        userWalletHistory.setId(0);
        // userWalletHistory.setModified("");
        // userWalletHistory.setModifiedBy(SharedPrefsData.getInstance(context).getUserId(context));
        userWalletHistory.setReasonTypeId(20);
        userWalletHistory.setTransactionTypeId(18);
        userWalletHistory.setIsActive(true);
        userWalletHistory.setWalletId(SharedPrefsData.getInstance(context).getWalletId(context));
        sendMoney.setUserWalletHistory(userWalletHistory);
        return new Gson().toJsonTree(sendMoney)
                .getAsJsonObject();
    }

    private void setViews() {

        mobileNumberTXT = (NCBTextInputLayout) rootView.findViewById(R.id.mobileNumberTXT);
        amountTXT = (NCBTextInputLayout) rootView.findViewById(R.id.amountTXT);
        commentTXT = (NCBTextInputLayout) rootView.findViewById(R.id.commentTXT);
        mobileEdt = (CommonEditText) rootView.findViewById(R.id.mobileEdt);
        amount = (CommonEditText) rootView.findViewById(R.id.amount);
        commentEdt = (CommonEditText) rootView.findViewById(R.id.commentEdt);
        submitBtn = (CommonButton) rootView.findViewById(R.id.submit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = context.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mobileEdt.setText(hasPhone.replaceAll("\\s", ""));
                    }

                }
                break;
        }
    }
}
