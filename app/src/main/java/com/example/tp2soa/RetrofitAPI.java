package com.example.tp2soa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitAPI {

    @POST("api/api/register")
    Call<RegistroResponse> post(@Body DatoRegistro dr);

}
