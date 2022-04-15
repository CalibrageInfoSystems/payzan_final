package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.RegisterModel;
import com.calibrage.payzanconsumer.framework.model.ResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.PayZanEnums;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.isValidEmail;


/**
 * Created by Calibrage19 on 20-10-2017.
 */

public class  SignupFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static final String TAG = SignupFragment.class.getSimpleName();
    private CommonEditText reg_mobile, reg_email, reg_password, reg_confirm_password;
    private LoginButton loginButton;
    private Button fbBtn, btnRegister;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton button;
    private static final int RC_SIGN_IN = 007;
    private AlertDialog alertDialog;
    // private Subscription mRegisterSubscription;
    private NCBTextInputLayout reg_mobile_til,reg_email_til,reg_password_til,reg_confirm_password_til;
    private Subscription mRegisterSubscription;
    private View rootView;
    private Context context;
    public static Toolbar toolbar;
    private TextView terms_comditions,linkToLogin;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private String reg_mobileStr, reg_emailStr, reg_passwordStr, reg_confirm_passwordStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_signup, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = getActivity();

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login);
        fbBtn = (Button) rootView.findViewById(R.id.fbBtn);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        button = (SignInButton) rootView.findViewById(R.id.btn_sign_in);
        reg_mobile = (CommonEditText) rootView.findViewById(R.id.reg_mobile);
        reg_email = (CommonEditText) rootView.findViewById(R.id.reg_email);
        reg_password = (CommonEditText) rootView.findViewById(R.id.reg_password);
        reg_confirm_password = (CommonEditText) rootView.findViewById(R.id.reg_confirm_password);
        reg_mobile_til = (NCBTextInputLayout) rootView.findViewById(R.id.reg_mobile_til);
        reg_email_til=(NCBTextInputLayout)rootView.findViewById(R.id.reg_email_til);
        reg_password_til=(NCBTextInputLayout)rootView.findViewById(R.id.reg_password_til);
        reg_confirm_password_til=(NCBTextInputLayout)rootView.findViewById(R.id.reg_confirm_password_til);
        terms_comditions = (TextView) rootView.findViewById(R.id.terms_comditions);
        linkToLogin = (TextView) rootView.findViewById(R.id.linkToLogin);
        IntiateGoogleApi();
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.sign_up));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));

        /* mahesh commented 17-11-2017*/
        /*HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });*/

        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        SpannableString ssToLogin = new SpannableString(getResources().getString(R.string.already_have_account));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
              //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
              //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpanToLogin = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
              //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        terms_comditions.setText(ss);
        terms_comditions.setMovementMethod(LinkMovementMethod.getInstance());
        terms_comditions.setHighlightColor(Color.TRANSPARENT);
        ssToLogin.setSpan(clickableSpanToLogin,22,27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkToLogin.setText(ssToLogin);
        linkToLogin.setMovementMethod(LinkMovementMethod.getInstance());
        linkToLogin.setHighlightColor(Color.TRANSPARENT);


        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        reg_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reg_mobile_til.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    reg_mobile_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        reg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    reg_email_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reg_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    reg_password_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reg_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    reg_confirm_password_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateUi()) {
                    showDialog(getActivity(),"Please Wait Loading");
                    registerUser();
                }

            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        /*rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
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
        });*/
        return rootView;
    }

    private void closeTab() {
        // Toast.makeText(context, "ON BACK KEY PRESED", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new HomeFragment())
                .commit();
        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
        mActivityListener.messageFromChildFragmentToActivity("handleBottomNavigation");
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("SignupTag");
//
//
//        if (fragment != null)
//            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//
//        HomeActivity.toolbar.setNavigationIcon(null);
//        HomeActivity.toolbar.setTitle("");
    }

    private void registerUser() {
        JsonObject object = getRegisterObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);

        mRegisterSubscription = service.userRegister(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), R.string.toast_user_already_signup, Toast.LENGTH_SHORT).show();
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
                      //  Log.d(TAG, "Register onError: "+ e.toString());
                        hideDialog();
                        Toast.makeText(getActivity(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseModel registerResponseModel) {

hideDialog();
                        if(registerResponseModel.getIsSuccess() == true)
                        {
                            Toast.makeText(getActivity(), getString(R.string.toast_please_login), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);

                            // close this activity

                        }

                        // finish();
                    }
                });


    }

    private void IntiateGoogleApi() {
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text", messageText);
//                Toast.makeText(getActivity(), "Message: " + messageText, Toast.LENGTH_LONG).show();
//            }
//        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        loginButton.setReadPermissions("email");
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder((FragmentActivity) context)
                        .enableAutoManage((FragmentActivity) context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                loginButton.setReadPermissions("email");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private JsonObject getRegisterObject() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setMobileNumber(reg_mobile.getText().toString().trim());
        registerModel.setPassword(reg_password.getText().toString().trim());
        registerModel.setEmail(reg_email.getText().toString().trim());
        registerModel.setConfirmPassword(reg_confirm_password.getText().toString().trim());
        registerModel.setRoleId(PayZanEnums.RoleIdEnum.Consumer.RID());
        return new Gson().toJsonTree(registerModel)
                .getAsJsonObject();
    }

    private boolean isValidateUi() {

        reg_mobileStr=reg_mobile.getText().toString().trim();
        reg_emailStr=reg_email.getText().toString().trim();
        reg_passwordStr=reg_password.getText().toString().trim();
        reg_confirm_passwordStr=reg_confirm_password.getText().toString().trim();
        String password=reg_password.getText().toString().trim();
        String confirmPassword=reg_confirm_password.getText().toString().trim();


        if (TextUtils.isEmpty(reg_mobileStr)) {

              reg_mobile_til.setErrorEnabled(true);
               reg_mobile_til.setError(getString(R.string.err_please_enter_mobile_no));
            return false;
        }else if (!isValidPhone())
        {
            reg_mobile_til.setErrorEnabled(true);
            reg_mobile_til.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        }
         else if (!isValidEmail(reg_emailStr)) {
            reg_email_til.setErrorEnabled(true);
            reg_email_til.setError(getString(R.string.err_enter_valid_email_id));

            return false;
        } else if (TextUtils.isEmpty(reg_passwordStr)) {
           reg_password_til.setErrorEnabled(true);
           reg_password_til.setError(getString(R.string.err_enter_password));
            return false;
        } else if (TextUtils.isEmpty(reg_confirm_passwordStr)) {
            reg_confirm_password_til.setErrorEnabled(true);
          reg_confirm_password_til.setError(getString(R.string.err_enter_confirm_password));
            return false;
        }
        else if (!(password.length() > 4 && confirmPassword.length() > 4
                && password.length() <= 20 && confirmPassword.length() <=20)) {
            reg_password_til.setErrorEnabled(true);
            reg_password_til.setError(getString(R.string.err_please_enter_character));
            return false;
        }
        return true;
    }
    private boolean isValidPhone()
    {
        String target=reg_mobile.getText().toString().trim();
        if (target.length()!=10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
//        try {
//            dummy.setText(jsonObject.getString("email") + "\n" + jsonObject.getString("gender") + "\n" + jsonObject.getString("name"));

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
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

        // check if parent Fragment implements listener
//        if (getActivity().getSupportFragmentManager().findFragmentByTag("walletTag") instanceof OnChildFragmentInteractionListener) {
//
//            mParentListener = (OnChildFragmentInteractionListener) getParentFragment();
//        } else {
//            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
//        }
    }

    private void signIn() {
        SignupFragment fragment = (SignupFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag("SignupTag");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(

                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(false);
                        Toast.makeText(getActivity(), getString(R.string.toast_signout), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            SignupFragment fragment = (SignupFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag("SignupTag");
            fragment.onActivityResult(requestCode, resultCode, data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(" ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(" ", "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = null;
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }

            String email = acct.getEmail();

            Log.e(" ", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
