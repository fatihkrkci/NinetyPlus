package com.fatihkurekci.ninetyplus.client;

import com.fatihkurekci.ninetyplus.data.response.MatchesListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("v4/matches")
    Call<MatchesListApiResponse> getMatchList();
}