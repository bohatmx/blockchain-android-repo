package com.aftarobot.wallet.sdk;

import com.aftarobot.wallet.data.Account;
import com.aftarobot.wallet.data.assets.Assets;
import com.aftarobot.wallet.data.effects.EffectsResponse;
import com.aftarobot.wallet.data.payments.PaymentsResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface StellarInterface {
    @GET("accounts/{account}")
    Call<Account> getAccount(@Path("account") String accountID);

    /*
    GET https://horizon-testnet.stellar.org/assets?order=desc&limit=200
    GET https://horizon-testnet.stellar.org/effects?limit=2&order=desc
    GET https://horizon-testnet.stellar.org/accounts/GDG52NAT4NG7ULJK6WALKEU3UBSKGUL533M5NQPIPHTTQGFHRFYUF2MT/effects
     */
    @GET("assets")
    Call<Assets> getAssets(@QueryMap HashMap<String,String> map);

    @GET("effects")
    Call<EffectsResponse> getEffects(@QueryMap HashMap<String,String> map);

    //GET /accounts/{account}/effects{?cursor,limit,order}
    @GET("accounts/{account}/effects/")
    Call<EffectsResponse> getAccountEffects(@Path("account") String accountID,
                                            @QueryMap HashMap<String,String> map);

    //GET https://horizon-testnet.stellar.org/payments?limit=5&order=desc
    @GET("payments")
    Call<PaymentsResponse> getPayments(@QueryMap HashMap<String,String> map);


    //GET https://horizon-testnet.stellar.org/accounts/
    // GDG52NAT4NG7ULJK6WALKEU3UBSKGUL533M5NQPIPHTTQGFHRFYUF2MT/payments?limit=33&order=desc

    @GET("accounts/{account}/payments/")
    Call<PaymentsResponse> getAccountPayments(@Path("account")  String accountID,
                                              @QueryMap HashMap<String,String> map);
}
