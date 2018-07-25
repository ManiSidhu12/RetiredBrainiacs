package com.retiredbrainiacs.apis;

import com.retiredbrainiacs.common.GlobalConstants;

public class ApiUtils {
    private ApiUtils(){

    }

    public static Service getAPiService(){
        return RetrofitClient.getClient(GlobalConstants.API_URL1).create(Service.class);
    }
}
