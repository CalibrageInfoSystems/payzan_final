package com.calibrage.payzanconsumer.framework.networkservices;


import com.calibrage.payzanconsumer.framework.adapters.AgentSplit;
import com.calibrage.payzanconsumer.framework.model.AgentResponseModel;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordResponseModel;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.OperatorModel;
import com.calibrage.payzanconsumer.framework.model.ResponseModel;
import com.calibrage.payzanconsumer.framework.model.SendMoneyResponseModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.TransactionResponseModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Calibrage11 on 9/8/2017.
 */

public interface  MyServices {


    // @Headers("Accept: application/json")
    @POST(ApiConstants.REGISTER)
    Observable<ResponseModel> userRegister(@Body JsonObject data);

    @POST(ApiConstants.LOGIN)
    Observable<LoginResponseModel> UserLogin(@Body JsonObject data);

    @POST(ApiConstants.WALLET)
    Observable<WalletResponse> UserAddWallet(@Body JsonObject data);

    @GET
    Observable<StatesModel> getProvinance(@Url String url);

    @GET
    Observable<DistrictModel> getDistricts(@Url String url);

    @GET
    Observable<MandalModel> getMandals(@Url String url);

    @GET
    Observable<VillageModel> getVillages(@Url String url);

    @GET
    Observable<OperatorModel> getOperator(@Url String url);

    @POST(ApiConstants.AGENT_REQUEST)
    Observable<AgentResponseModel> agentRequest(@Body JsonObject data);

    @POST(ApiConstants.CHANGE_PASSWORD)
    Observable<ChangePasswordResponseModel> changePassword(@Body JsonObject data);

    @POST(ApiConstants.SEND_MONEY_WALLET)
    Observable<SendMoneyResponseModel> sendMoneyRequest(@Body JsonObject data);

    @GET
    Observable<TransactionResponseModel> myTransactions(@Url String url);

    @GET
    Observable<AgentSplit> GetAgentCategorySplitByAgentId(@Url String url);

    @GET
    Observable<WalletBalanceResponce> GetUserBalanceByID(@Url String url);


}
