package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AddCardFragment extends BaseFragment {
    public static final String TAG = AddCardFragment.class.getSimpleName();
    private View view;
    private CommonButton submit;
    private NCBTextInputLayout cardNumberTXT, cardHolderNameTXT, cardLabelTXT, monthtxt, yeartxt;
    private CommonEditText cardLabelEdt, cardHolderNameEdt, cardNumberEdt;
    private Spinner monthSp, yearSp;
    private RadioButton saverdb;
    private String cardLabelStr, cardHolderNameStr, cardNumberStr, monthStr, yearStr;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.addcard));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });


        setView();
        initView();
        return view;
    }
    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new UserProfileHome(), TAG, AddCardFragment.TAG);

    }

    private void setView() {
        cardNumberEdt = (CommonEditText) view.findViewById(R.id.cardNumberEdt);
        cardHolderNameEdt = (CommonEditText) view.findViewById(R.id.cardHolderNameEdt);
        cardLabelEdt = (CommonEditText) view.findViewById(R.id.cardLabelEdt);
        cardNumberTXT = (NCBTextInputLayout) view.findViewById(R.id.cardNumberTXT);
        cardHolderNameTXT = (NCBTextInputLayout) view.findViewById(R.id.cardHolderNameTXT);
        cardLabelTXT = (NCBTextInputLayout) view.findViewById(R.id.cardLabelTXT);
        submit = (CommonButton) view.findViewById(R.id.submit);
        monthSp = (Spinner) view.findViewById(R.id.monthSp);
        yearSp = (Spinner) view.findViewById(R.id.yearSp);
//     monthtxt=(NCBTextInputLayout)view.findViewById(R.id.monthtxt);
//     yeartxt=(NCBTextInputLayout)view.findViewById(R.id.yeartxt);
        saverdb = (RadioButton) view.findViewById(R.id.saverdb);


        String[] monthlist = getResources().getStringArray(R.array.month_arrays);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.month_layout, R.id.montxt, monthlist);
        monthAdapter.setDropDownViewResource(R.layout.month_layout);
        monthSp.setAdapter(monthAdapter);
        monthSp.setSelection(setCurrentMonthSpinner(getActivity()));


       // String[] yearlist = getResources().getStringArray(R.array.year_arrays);
        ArrayList<String> years = new ArrayList<String>();
        int calendar = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = calendar; i <= 2550; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
      //  yearAdapter.setDropDownViewResource(R.layout.year_layout);
        yearSp.setAdapter(yearAdapter);

    }


    public static int setCurrentMonthSpinner(Context context) {
        String[] months = context.getResources().getStringArray(R.array.month_arrays);
        for (int i = 0; i < months.length; i++) {
            String mnth = getCurrentMonth();
            if (months[i].equals(mnth))
                return i;
        }
        return 0;
    }


    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.US);

        return month_date.format(cal.getTime());
    }

    private void initView() {

        cardNumberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cardNumberTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cardHolderNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cardHolderNameTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        saverdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        monthSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUi();
            }
        });
    }

    private boolean validateUi() {
        cardNumberStr = cardNumberEdt.getText().toString().trim();
        cardHolderNameStr = cardHolderNameEdt.getText().toString().trim();
        cardLabelStr = cardLabelEdt.getText().toString().trim();
        monthStr = (String) monthSp.getSelectedItem();
        yearStr = (String) yearSp.getSelectedItem();

        if (TextUtils.isEmpty(cardNumberStr)) {
            cardNumberTXT.setErrorEnabled(true);
            cardNumberTXT.setError(getString(R.string.err_enter_card_number));
            return false;
        } else if (TextUtils.isEmpty(cardHolderNameStr)) {
            cardHolderNameTXT.setError(getString(R.string.err_enter_cardholder_name));
            cardHolderNameTXT.setErrorEnabled(true);
        } else if (TextUtils.isEmpty(monthStr)) {
            monthtxt.setError(getString(R.string.err_enter_month));
            monthtxt.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(yearStr)) {
            yeartxt.setErrorEnabled(true);
            yeartxt.setError(getString(R.string.err_enter_year));
            return false;
        }

        return true;
    }

}
