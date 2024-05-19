package com.fatihkurekci.ninetyplus.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.client.ApiClient;
import com.fatihkurekci.ninetyplus.client.ApiInterface;
import com.fatihkurekci.ninetyplus.data.adapter.RVLeagueListAdapter;
import com.fatihkurekci.ninetyplus.data.adapter.RVMatchListAdapter;
import com.fatihkurekci.ninetyplus.data.model.Match;
import com.fatihkurekci.ninetyplus.data.response.MatchesListApiResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView rv_matcheslist, rv_leagues;
    RVMatchListAdapter rvMatchListAdapter;
    RVLeagueListAdapter leagueListAdapter;
    ArrayList<Match> matchesLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        matchesLists = new ArrayList<>();
        rv_matcheslist = rootView.findViewById(R.id.rv_matcheslist);
        rv_matcheslist.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvMatchListAdapter = new RVMatchListAdapter(getContext(), matchesLists);
        rv_matcheslist.setAdapter(rvMatchListAdapter);

        rv_leagues = rootView.findViewById(R.id.rv_leagues);
        rv_leagues.setLayoutManager(new LinearLayoutManager(getActivity()));
        leagueListAdapter = new RVLeagueListAdapter(getContext(), new ArrayList<>(), leagueName -> {
            // Lig adına tıklandığında yapılacak işlemler burada olacak
            // Örneğin, tıklanan ligin maçlarını göstermek için bir fonksiyon çağrılabilir
        });
        rv_leagues.setAdapter(leagueListAdapter);

        populateServices();

        return rootView;
    }

    public void populateServices() {
        ApiClient.getClient().create(ApiInterface.class).getMatchList("2024-05-17", "2024-05-17").enqueue(new Callback<MatchesListApiResponse>() {
            @Override
            public void onResponse(Call<MatchesListApiResponse> call, Response<MatchesListApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Match> matches = response.body().getMatches();
                    if (matches != null && !matches.isEmpty()) {
                        matchesLists.addAll(matches);
                        rvMatchListAdapter.notifyDataSetChanged();

                        ArrayList<String> leagueNames = extractUniqueLeagueNames(matches);
                        leagueListAdapter.updateData(leagueNames);
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

    private ArrayList<String> extractUniqueLeagueNames(ArrayList<Match> matches) {
        Set<String> uniqueLeagueNames = new HashSet<>();
        for (Match match : matches) {
            uniqueLeagueNames.add(match.getCompetition().getName());
        }
        return new ArrayList<>(uniqueLeagueNames);
    }
}
