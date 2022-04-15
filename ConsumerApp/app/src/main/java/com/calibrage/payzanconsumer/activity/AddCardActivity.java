package com.calibrage.payzanconsumer.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.calibrage.payzanconsumer.fragement.AddCardFragment.setCurrentMonthSpinner;

public class AddCardActivity extends AppCompatActivity {

    private CommonButton submit;
    private NCBTextInputLayout cardNumberTXT, cardHolderNameTXT, cardLabelTXT, monthtxt, yeartxt;
    private CommonEditText cardLabelEdt, cardHolderNameEdt, cardNumberEdt;
    private Spinner monthSp, yearSp;
    private RadioButton saverdb;
    private String cardLabelStr, cardHolderNameStr, cardNumberStr, monthStr, yearStr;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_card);
        setView();
    }

    private void setView()
    {
        cardNumberEdt=(CommonEditText)findViewById(R.id.cardNumberEdt);
        cardLabelEdt=(CommonEditText)findViewById(R.id.cardLabelEdt);
        cardHolderNameEdt=(CommonEditText)findViewById(R.id.cardHolderNameEdt);
        cardNumberTXT=(NCBTextInputLayout)findViewById(R.id.cardNumberTXT);
        cardHolderNameTXT=(NCBTextInputLayout)findViewById(R.id.cardHolderNameTXT);
        cardLabelTXT=(NCBTextInputLayout)findViewById(R.id.cardLabelTXT);
        submit = (CommonButton) findViewById(R.id.submit);
        monthSp = (Spinner) findViewById(R.id.monthSp);
        yearSp = (Spinner) findViewById(R.id.yearSp);
        saverdb = (RadioButton) findViewById(R.id.saverdb);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.addcard));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String[] monthlist = getResources().getStringArray(R.array.month_arrays);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, R.layout.month_layout, R.id.montxt, monthlist);
        monthAdapter.setDropDownViewResource(R.layout.month_layout);
        monthSp.setAdapter(monthAdapter);
        monthSp.setSelection(setCurrentMonthSpinner(this));


        // String[] yearlist = getResources().getStringArray(R.array.year_arrays);
        ArrayList<String> years = new ArrayList<String>();
        int calendar = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = calendar; i <= 2550; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
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
