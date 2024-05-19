package com.fatihkurekci.ninetyplus.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements RVLeagueListAdapter.LeagueClickListener {

    RecyclerView rv_matcheslist, rv_leagues;
    RVMatchListAdapter rvMatchListAdapter;
    RVLeagueListAdapter leagueListAdapter;
    ArrayList<Match> allMatchesList = new ArrayList<>();
    ArrayList<Match> filteredMatchesList = new ArrayList<>();
    Button startDateButton, endDateButton, filterButton, resetButton;
    Calendar startDateCalendar, endDateCalendar;
    ProgressBar loadingSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        rv_matcheslist = rootView.findViewById(R.id.rv_matcheslist);
        rv_matcheslist.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvMatchListAdapter = new RVMatchListAdapter(getContext(), filteredMatchesList);
        rv_matcheslist.setAdapter(rvMatchListAdapter);

        rv_leagues = rootView.findViewById(R.id.rv_leagues);
        rv_leagues.setLayoutManager(new LinearLayoutManager(getActivity()));
        leagueListAdapter = new RVLeagueListAdapter(getContext(), new ArrayList<>(), this);
        rv_leagues.setAdapter(leagueListAdapter);

        startDateButton = rootView.findViewById(R.id.start_date_button);
        endDateButton = rootView.findViewById(R.id.end_date_button);
        filterButton = rootView.findViewById(R.id.filter_button);
        resetButton = rootView.findViewById(R.id.reset_button);
        loadingSpinner = rootView.findViewById(R.id.loading_spinner);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateCalendar, startDateButton);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateCalendar, endDateButton);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDateCalendar.getTime());
                String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDateCalendar.getTime());
                populateServices(startDate, endDate);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
                populateServices(today, today);
                startDateButton.setText(today);
                endDateButton.setText(today);
            }
        });

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        populateServices(today, today);

        return rootView;
    }

    private void showDatePickerDialog(final Calendar calendar, final Button button) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                button.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showLoadingSpinner() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner() {
        loadingSpinner.setVisibility(View.GONE);
    }

    public void populateServices(String startDate, String endDate) {
        showLoadingSpinner();

        ApiClient.getClient().create(ApiInterface.class).getMatchList(startDate, endDate).enqueue(new Callback<MatchesListApiResponse>() {
            @Override
            public void onResponse(Call<MatchesListApiResponse> call, Response<MatchesListApiResponse> response) {
                hideLoadingSpinner();

                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Match> matches = response.body().getMatches();
                    if (matches != null && !matches.isEmpty()) {
                        allMatchesList.clear();
                        allMatchesList.addAll(matches);
                        filterMatchesByLeague("");
                        ArrayList<String> leagueNames = extractUniqueLeagueNames(matches);
                        leagueListAdapter.updateData(leagueNames);
                    }
                } else {
                    Log.e("HomeFragment", "API call was not successful. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MatchesListApiResponse> call, Throwable throwable) {
                hideLoadingSpinner();
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

    @Override
    public void onLeagueClick(String leagueName) {
        filterMatchesByLeague(leagueName);
    }

    private void filterMatchesByLeague(String leagueName) {
        filteredMatchesList.clear();
        if (leagueName.isEmpty()) {
            filteredMatchesList.addAll(allMatchesList);
        } else {
            for (Match match : allMatchesList) {
                if (match.getCompetition().getName().equals(leagueName)) {
                    filteredMatchesList.add(match);
                }
            }
        }
        rvMatchListAdapter.notifyDataSetChanged();
    }
}

