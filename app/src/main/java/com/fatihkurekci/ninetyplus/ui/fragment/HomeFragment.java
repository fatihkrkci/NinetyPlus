package com.fatihkurekci.ninetyplus.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.client.ApiClient;
import com.fatihkurekci.ninetyplus.client.ApiInterface;
import com.fatihkurekci.ninetyplus.data.adapter.RVMatchListAdapter;
import com.fatihkurekci.ninetyplus.data.model.Match;
import com.fatihkurekci.ninetyplus.data.response.MatchesListApiResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView rv_matcheslist;
    RVMatchListAdapter rvMatchListAdapter;
    ArrayList<Match> matchesLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootView'ü inflate et
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        matchesLists = new ArrayList<>();
        rv_matcheslist = rootView.findViewById(R.id.rv_matcheslist);
        rv_matcheslist.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvMatchListAdapter = new RVMatchListAdapter(getContext(),matchesLists);
        rv_matcheslist.setAdapter(rvMatchListAdapter);
        populateServices();

        // Düzeltme: rootView'ü döndür
        return rootView;
    }

    public void populateServices() {
        ApiClient.getClient().create(ApiInterface.class).getMatchList().enqueue(new Callback<MatchesListApiResponse>() {
            @Override
            public void onResponse(Call<MatchesListApiResponse> call, Response<MatchesListApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Log API yanıtını
// API yanıtını JSON formatına dönüştürme
                        String responseBodyJson = new Gson().toJson(response.body());

// JSON formatındaki API yanıtını loglama
                        Log.d("HomeFragment", "API response JSON: " + responseBodyJson);
                        if (response.body().getMatches() != null) {
                            // Log maç verilerini
                            Log.d("HomeFragment", "Match data: " + response.body().getMatches().toString());

                            matchesLists.addAll(response.body().getMatches());
                            rvMatchListAdapter.notifyDataSetChanged();
                            Log.d("HomeFragment", "API response received and data added to the list");
                        } else {
                            Log.e("HomeFragment", "Response data is null");
                        }
                    } else {
                        Log.e("HomeFragment", "Response body is null");
                    }
                } else {
                    Log.e("HomeFragment", "API call was not successful. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MatchesListApiResponse> call, Throwable throwable) {
                Log.e("HomeFragment", "API call failed", throwable);
            }
        });
    }

}
