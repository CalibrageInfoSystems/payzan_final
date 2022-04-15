package com.calibrage.payzanconsumer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.BuildConfig;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.AgentModel;
import com.calibrage.payzanconsumer.framework.model.AgentResponseModel;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

;import static com.calibrage.payzanconsumer.framework.util.CommonConstants.AGENT_REQUEST_CATEGORY_ID;


/**
 * Created by Calibrage11 on 10/5/2017.
 */

public class RequestForAgent extends AppCompatActivity {

    private NCBTextInputLayout stateTIl, districtTIl, mandalTIl, villageTIl, firstNameTIl, middleNameTIL, lastNameTIL, mobileTIL, emailTIL, address1TIL, address2TIL, landmarkTIL, commentTIL;
    private CommonEditText commentsEdt, landmarkEdt, address2Edt, address1Edt, emailEdt, mobileEdt, lastNameEdt, middleNameEdt, firstNameEdt;
    private Spinner villageSpn, mandalSpn, districtSpn, stateSpn;
    private Subscription mGetStatesSubscription, mGetDistrictSubscription, getmGetDistrictSubscription;
    private MandalModel mandalModellist;
    private StatesModel provinanceModellist;
    private VillageModel villageModellist;
    private DistrictModel districtModellist;
    private Button btn_submit;
    private Subscription mRegisterSubscription;
    private ArrayList<String> provinanceList, districtList, mandalList, villageList;
    private ArrayAdapter provinanceArrayAdapter, mandalArrayAdapter, districtArrayAdapter, villageArrayAdapter;

    private String firstNameStr, middleNameStr, lastNameStr, mobileStr, emailStr, stateStr, districtStr, mandalStr, villageStr, address1Str,
            address2Str, landmarkStr, commentsStr;

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile
            ("[a-zA-Z0-9+._%-+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_as_agent);
        provinanceList = new ArrayList<>();
        districtList = new ArrayList<>();

        villageList = new ArrayList<>();
        setViews();
        initViews();
        if(CommonUtil.isNetworkAvailable(this)){
            getProvinance();
        }

        //   PostAgentRequest();
      /*  getDistricts();*/


    }

    private void setViews() {
        stateTIl = (NCBTextInputLayout) findViewById(R.id.stateTIl);
        districtTIl = (NCBTextInputLayout) findViewById(R.id.districtTIl);
        villageTIl = (NCBTextInputLayout) findViewById(R.id.villageTIl);
        firstNameTIl = (NCBTextInputLayout) findViewById(R.id.firstNameTIl);
        middleNameTIL = (NCBTextInputLayout) findViewById(R.id.middleNameTIL);
        lastNameTIL = (NCBTextInputLayout) findViewById(R.id.lastNameTIL);
        mobileTIL = (NCBTextInputLayout) findViewById(R.id.mobileTIL);
        emailTIL = (NCBTextInputLayout) findViewById(R.id.emailTIL);
        address1TIL = (NCBTextInputLayout) findViewById(R.id.address1TIL);
        address2TIL = (NCBTextInputLayout) findViewById(R.id.address2TIL);
        landmarkTIL = (NCBTextInputLayout) findViewById(R.id.landmarkTIL);
        commentTIL = (NCBTextInputLayout) findViewById(R.id.commentTIL);
        commentTIL = (NCBTextInputLayout) findViewById(R.id.commentTIL);
        commentsEdt = (CommonEditText) findViewById(R.id.commentsEdt);
        landmarkEdt = (CommonEditText) findViewById(R.id.landmarkEdt);
        address2Edt = (CommonEditText) findViewById(R.id.address2Edt);
        address1Edt = (CommonEditText) findViewById(R.id.address1Edt);
        emailEdt = (CommonEditText) findViewById(R.id.emailEdt);
        mobileEdt = (CommonEditText) findViewById(R.id.mobileEdt);
        lastNameEdt = (CommonEditText) findViewById(R.id.lastNameEdt);
        middleNameEdt = (CommonEditText) findViewById(R.id.middleNameEdt);
        firstNameEdt = (CommonEditText) findViewById(R.id.firstNameEdt);
        villageSpn = (Spinner) findViewById(R.id.villageSpn);
        mandalSpn = (Spinner) findViewById(R.id.mandalSpn);
        districtSpn = (Spinner) findViewById(R.id.districtSpn);
        stateSpn = (Spinner) findViewById(R.id.stateSpn);
        btn_submit = (Button) findViewById(R.id.submit);
        mandalTIl = (NCBTextInputLayout) findViewById(R.id.mandalTIl);

//        stateSpn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//             //   stateSpn.showDropDown();
//                getStates();
//                return false;
//            }
//        });
//        districtSpn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//               // districtSpn.showDropDown();
//
//                return false;
//            }
//        });
//        mandalSpn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//               // mandalSpn.showDropDown();
//
//                return false;
//            }
//        });
//        villageSpn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//             //   villageSpn.showDropDown();
//
//                return false;
//            }
//        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void getProvinance() {


        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);

        String BaseUrl = BuildConfig.LOCAL_URL + "api/Province/GetProvinces/null";
       /* String URL = "http://192.168.1.147/PayZanAPI/api/States/GetStateInfo/1";*//*ApiConstants.STATES +"1" ;*/
        mGetStatesSubscription = service.getProvinance(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatesModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StatesModel statesModel) {
                        provinanceModellist = statesModel;
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        if (!provinanceModellist.getListResult().isEmpty()) {
                            for (int i = 0; i < provinanceModellist.getListResult().size(); i++) {
                                provinanceList.add(provinanceModellist.getListResult().get(i).getName());
                            }
                        } else {
                            provinanceList.clear();
                            districtList.clear();
                           // mandalList.clear();
                            villageList.clear();

                            districtArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, districtList);
                            districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            districtSpn.setAdapter(districtArrayAdapter);

//                            mandalArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, mandalList);
//                            mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            //Setting the ArrayAdapter data on the Spinner
//                            mandalSpn.setAdapter(mandalArrayAdapter);

                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);
                        }


                        provinanceArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, provinanceList);
                        provinanceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        stateSpn.setAdapter(provinanceArrayAdapter);

//                        SingleLineDropDownAdapter singleLineDropDownAdapter = new SingleLineDropDownAdapter(RequestForAgent.this, R.layout.adapter_single_item, (List<StatesModel.Data>) statesModel.getListResult());
//                        singleLineDropDownAdapter.setAdapterOnClick(RequestForAgent.this);
//                        stateSpn.setAdapter(singleLineDropDownAdapter);
                    }
                });
    }


    private void getDistricts(int id) {
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Province/GetDistrictsByProvinceId/" + id;
       /* String URL = "http://192.168.1.147/PayZanAPI/api/Districts/GetDistrictsInfo/" + id;  *//*ApiConstants.DISTRICTS + "1"*/
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getDistricts(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DistrictModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DistrictModel districtModel) {
                        districtModellist = districtModel;
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();



                        if (!districtModellist.getListResult().isEmpty()) {
                            for (int i = 0; i < districtModellist.getListResult().size(); i++) {
                                districtList.add(districtModellist.getListResult().get(i).getName());
                            }
                        } else {
                            districtList.clear();
                           // mandalList.clear();
                            villageList.clear();

                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);
//                            mandalArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, mandalList);
//                            mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            //Setting the ArrayAdapter data on the Spinner
//                            mandalSpn.setAdapter(mandalArrayAdapter);

                        }
                        districtArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, districtList);
                        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        districtSpn.setAdapter(districtArrayAdapter);
//                        SingleLineDropDownAdapterdistrict singleLineDropDownAdapter = new SingleLineDropDownAdapterdistrict(RequestForAgent.this, R.layout.adapter_single_item, (List<DistrictModel.Data>) districtModel.getListResult());
//                        singleLineDropDownAdapter.setAdapterDistOnClick(RequestForAgent.this);
//
//                        districtSpn.setAdapter(singleLineDropDownAdapter);
                    }
                });
    }


    private void getMandals(int Id) {
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Mandals/GetMandalByDistrict/" + Id;
       /* String Url = "http://192.168.1.147/PayZanAPI/api/Mandals/GetMandalInfo/" + Id;  *//*ApiConstants.MANDALS + "1"*/
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getMandals(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MandalModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MandalModel mandalModel) {
                        mandalModellist = mandalModel;
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        mandalList = new ArrayList<>();
                        if (!mandalModellist.getListResult().isEmpty()) {
                            for (int i = 0; i < mandalModellist.getListResult().size(); i++) {
                                mandalList.add(mandalModellist.getListResult().get(i).getName());
                            }
                        } else {
                            mandalList.clear();
                            villageList.clear();

                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);

                        }


                        mandalArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, mandalList);
                        mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        mandalSpn.setAdapter(mandalArrayAdapter);

//                        SingleLineDropDownAdapterMandals singleLineDropDownAdapter = new SingleLineDropDownAdapterMandals(RequestForAgent.this, R.layout.adapter_single_item, (List<MandalModel.data>) mandalModel.getListResult());
//                        singleLineDropDownAdapter.setAdapterMandalOnClick(RequestForAgent.this);
//
//                        mandalSpn.setAdapter(singleLineDropDownAdapter);

                    }
                });
    }

    private void getVillages(int id) {

        /*String Url = "http://192.168.1.147/PayZanAPI/api/Villages/GetVillageInfo/" + id;*/
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Villages/GetVillageByMandal/" + id;
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
       /* ApiConstants.VILLAGE + "1"*/
        mGetDistrictSubscription = service.getVillages(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VillageModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VillageModel villageModel) {
                        villageModellist = villageModel;

                        Toast.makeText(RequestForAgent.this, R.string.toast_sucess, Toast.LENGTH_SHORT).show();

                        if (!villageModellist.getListResult().isEmpty()) {
                            for (int i = 0; i < villageModellist.getListResult().size(); i++) {
                                villageList.add(villageModellist.getListResult().get(i).getName());
                            }
                        } else {
                            villageList.clear();
                        }


                        villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                        villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        villageSpn.setAdapter(villageArrayAdapter);

//                        SingleLineDropDownAdapterVillages singleLineDropDownAdapter = new SingleLineDropDownAdapterVillages(RequestForAgent.this, R.layout.adapter_single_item, (List<VillageModel.Data>) villageModel.getListResult());
//                        singleLineDropDownAdapter.setAdapterVillageOnClick(RequestForAgent.this);
//
//                        villageSpn.setAdapter(singleLineDropDownAdapter);

                    }
                });
    }


    private void initViews() {
        firstNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    firstNameTIl.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        middleNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    middleNameTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lastNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    lastNameTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    mobileTIL.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    emailTIL.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        stateSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateTIl.setErrorEnabled(false);
                if (!provinanceModellist.getListResult().isEmpty())
                    getDistricts(provinanceModellist.getListResult().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtTIl.setErrorEnabled(false);
                if (!districtModellist.getListResult().isEmpty())
                    getMandals(districtModellist.getListResult().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(RequestForAgent.this, R.string.toast_iscalling, Toast.LENGTH_SHORT).show();
            }
        });
        mandalSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mandalTIl.setErrorEnabled(false);
                if (!mandalModellist.getListResult().isEmpty())
                    getVillages(mandalModellist.getListResult().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        villageSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villageTIl.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        stateSpn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.length() > 0)
//                    stateTIl.setErrorEnabled(false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        districtSpn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.length() > 0) {
//                    districtTIl.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        mandalSpn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.length() > 0) {
//                    mandalTIl.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        villageSpn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.length() > 0) {
//                  villageTIl.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        address1Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    address1TIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address2Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    address2TIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        landmarkEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    landmarkTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commentsEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    commentTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUi() && CommonUtil.isNetworkAvailable(RequestForAgent.this)) {
                    postAgentRequest();
                } /*else {
                    Toast.makeText(RequestForAgent.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }


    private boolean validateUi() {

        firstNameStr = firstNameEdt.getText().toString().trim();
        middleNameStr = middleNameEdt.getText().toString().trim();
        lastNameStr = lastNameEdt.getText().toString().trim();
        mobileStr = mobileEdt.getText().toString().trim();
        emailStr = emailEdt.getText().toString().trim();
        stateStr = (String) stateSpn.getSelectedItem();
        districtStr = (String) districtSpn.getSelectedItem();
        mandalStr = (String) mandalSpn.getSelectedItem();
        villageStr = (String) villageSpn.getSelectedItem();
        address1Str = address1Edt.getText().toString().trim();
        address2Str = address2Edt.getText().toString().trim();
        landmarkStr = landmarkEdt.getText().toString().trim();
        commentsStr = commentsEdt.getText().toString().trim();


        if (TextUtils.isEmpty(firstNameStr)) {
            firstNameTIl.setError(getString(R.string.err_enter_first_name));
            firstNameTIl.setErrorEnabled(true);
            return false;
       /* } else if (TextUtils.isEmpty(middleNameStr)) {
            middleNameTIL.setError("Enter middle name");
            middleNameTIL.setErrorEnabled(true);
            return false;*/
        } else if (TextUtils.isEmpty(lastNameStr)) {
            lastNameTIL.setError(getString(R.string.err_enter_last_name));
            lastNameTIL.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(mobileStr)) {
            mobileTIL.setErrorEnabled(true);
            mobileTIL.setError(getString(R.string.err_enter_mobile_no));
            return false;
        } else if (!isValidPhone()) {
            mobileTIL.setErrorEnabled(true);
            mobileTIL.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        } else if (TextUtils.isEmpty(emailStr)) {
            emailTIL.setError(getString(R.string.err_enter_email_id));
            emailTIL.setErrorEnabled(true);
            // return EMAIL_ADDRESS_PATTERN.matcher(emailStr).matches();
            return false;
        } else if (!checkEmail()) {
            emailTIL.setError(getString(R.string.err_enter_valid_email));
            emailTIL.setErrorEnabled(true);
            return false;
        }
        else if (TextUtils.isEmpty(stateStr)) {
            stateTIl.setErrorEnabled(true);
            stateTIl.setError(getString(R.string.err_enter_state_name));
            return false;
        } else if (TextUtils.isEmpty(districtStr)) {
            districtTIl.setErrorEnabled(true);
            districtTIl.setError(getString(R.string.err_enter_district_name));
            return false;
        }
        else if (TextUtils.isEmpty(mandalStr)) {
            mandalTIl.setError(getString(R.string.err_enter_mandal_name));
            mandalTIl.setErrorEnabled(true);
            return false;
        }
        else if (TextUtils.isEmpty(villageStr)) {
            villageTIl.setErrorEnabled(true);
            villageTIl.setError(getString(R.string.err_enter_village_name));
            return false;
        }
        else if (TextUtils.isEmpty(address1Str)) {
            address1TIL.setError(getString(R.string.err_enter_address1));
            address1TIL.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(address2Str)) {
            address2TIL.setError(getString(R.string.err_enter_address2));
            address2TIL.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(landmarkStr)) {
            landmarkTIL.setErrorEnabled(true);
            landmarkTIL.setError(getString(R.string.err_enter_landmark));
            return false;
        } else if (TextUtils.isEmpty(commentsStr)) {
            commentTIL.setErrorEnabled(true);
            commentTIL.setError(getString(R.string.err_enter_comments));
            return false;
        }
        return true;
    }

    private boolean checkEmail() {
        String email = emailEdt.getText().toString().trim();
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone() {
        String target = mobileEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }


    private void postAgentRequest() {

      /*  String BaseUrl= BuildConfig.AZURE_URL+"api/AgentRequestInfo/AddUpdateAgentRequestInfo";
        String URL = "http://192.168.1.147/PayZanAPI/api/AgentRequestInfo/AddUpdateAgentRequestInfo";*/
        JsonObject object = getAgentObject();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mRegisterSubscription = (Subscription) service.agentRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AgentResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AgentResponseModel AgentResponseModel) {

                        Toast.makeText(RequestForAgent.this, AgentResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                      /*  CommonConstants.USERID = loginResponseModel.getData().getUser().getId();
                        CommonConstants.WALLETID = String.valueOf(loginResponseModel.getData().getUserWallet().getWalletId());*/
                        finish();
                    }
                });

    }

    private JsonObject getAgentObject() {

        AgentModel agentModel = new AgentModel();
        agentModel.setId(0);
        agentModel.setAgentRequestCategoryId(Integer.parseInt(AGENT_REQUEST_CATEGORY_ID));
        agentModel.setTitleTypeId(4);
        agentModel.setFirstName(firstNameStr);
        agentModel.setMiddleName(middleNameStr);
        agentModel.setLastName(lastNameStr);
        agentModel.setMobileNumber(mobileStr);
        agentModel.setEmail(emailStr);
        agentModel.setAddressLine1(address1Str);
        agentModel.setAddressLine2(address2Str);
        agentModel.setLandmark(landmarkStr);
        agentModel.setVillageId(villageModellist.getListResult().get((int) villageSpn.getSelectedItemId()).getId());
        agentModel.setComments(commentsStr);
        agentModel.setCreated("2017-10-31T05:15:57.983Z");


        return new Gson().toJsonTree(agentModel)
                .getAsJsonObject();

    }


}


