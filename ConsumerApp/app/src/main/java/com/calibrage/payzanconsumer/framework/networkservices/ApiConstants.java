package com.calibrage.payzanconsumer.framework.networkservices;

public interface ApiConstants {



    String BASE_URL = "http://192.168.1.160/PayZanAPI/";

  /*  String BASE_URL = "http://103.211.39.50/PayZan/PayZanAPI/";*/
    String REGISTER = BASE_URL+"api/Register/Register";
    String LOGIN = BASE_URL+"api/Register/Login";
    String STATES =BASE_URL+"api/States/GetStates/";
    String DISTRICTS =BASE_URL+"api/Districts/GetDistricts/";
    String MANDALS =BASE_URL+"api/Mandals/GetMandals/";
    String VILLAGE =BASE_URL+"api/Villages/GetVillages/";
    String WALLET =BASE_URL+"api/UserWallet/AddMoneyToUserWallet";
    String MOBILE_SERVICES = BASE_URL+"api/ServiceProvider/GetServiceProvidersByServiceType/";
    String AGENT_REQUEST = BASE_URL+"api/AgentRequestInfo/AddAgentRequestInfo";
    String SEND_MONEY_WALLET =BASE_URL+ "api/UserWallet/SendMoneyToUserWallet";
    String PASSBOOK =BASE_URL+ "api/UserWallet/GetPassbookDetails/";
    String CHANGE_PASSWORD = BASE_URL+ "api/Register/ChangePassword";
    /*String AGENT_CATAGORYSPLITBY_ID = "http://103.211.39.50/PayZan/PayZanAPI/api/AgentCategorySplit/GetAgentCategorySplitByAgentId/a41a0f68-9c3f-45e1-a070-d8bc6837abf7";*/
    String AGENT_CATAGORYSPLITBY_ID = "http://192.168.1.160/PayZanAPI/api/AgentCategorySplit/GetAgentCategorySplitByAgentId/a41a0f68-9c3f-45e1-a070-d8bc6837abf7";
    String BalenceBYID ="api/UserWallet/Get/";


  //  String LOGIN = "API
}
