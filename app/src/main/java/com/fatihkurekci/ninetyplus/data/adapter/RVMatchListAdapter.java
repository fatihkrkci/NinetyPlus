package com.fatihkurekci.ninetyplus.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.data.model.Match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RVMatchListAdapter extends RecyclerView.Adapter<RVMatchListAdapter.ViewHolder> {

    Context context;
    ArrayList<Match> matchesLists;

    public RVMatchListAdapter(Context context, ArrayList<Match> arrayList) {
        this.context = context;
        this.matchesLists = arrayList;
    }

    @NonNull
    @Override
    public RVMatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_matcheslistlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVMatchListAdapter.ViewHolder holder, int position) {
        holder.bind(matchesLists.get(position));
    }

    @Override
    public int getItemCount() {
        return matchesLists.size();
    }

    public void updateData(ArrayList<Match> updatedList) {
        matchesLists.clear();
        matchesLists.addAll(updatedList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView leagueNameTextView, homeTeamNameTextView, homeTeamScoreTextView, awayTeamScoreTextView, awayTeamNameTextView, matchTimeTextView, matchStatusTextView;
        private ImageView homeTeamLogoImageView, awayTeamLogoImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(R.id.leagueNameTextView);
            homeTeamNameTextView = itemView.findViewById(R.id.homeTeamNameTextView);
            homeTeamScoreTextView = itemView.findViewById(R.id.homeTeamScoreTextView);
            awayTeamScoreTextView = itemView.findViewById(R.id.awayTeamScoreTextView);
            awayTeamNameTextView = itemView.findViewById(R.id.awayTeamNameTextView);
            matchTimeTextView = itemView.findViewById(R.id.matchTimeTextView);
            matchStatusTextView = itemView.findViewById(R.id.matchStatusTextView);

            homeTeamLogoImageView = itemView.findViewById(R.id.homeTeamLogoImageView);
            awayTeamLogoImageView = itemView.findViewById(R.id.awayTeamLogoImageView);
        }

        public void bind(Match match) {
            leagueNameTextView.setText(match.getCompetition().getName());
            homeTeamNameTextView.setText(match.getHomeTeam().getName());
            homeTeamScoreTextView.setText(match.getScore().getFullTime().getHome() != null ? String.valueOf(match.getScore().getFullTime().getHome()) : "?");
            awayTeamScoreTextView.setText(match.getScore().getFullTime().getAway() != null ? String.valueOf(match.getScore().getFullTime().getAway()) : "?");
            awayTeamNameTextView.setText(match.getAwayTeam().getName());

            Glide.with(context).load(match.getHomeTeam().getCrest()).into(homeTeamLogoImageView);
            Glide.with(context).load(match.getAwayTeam().getCrest()).into(awayTeamLogoImageView);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

            try {
                Date date = inputFormat.parse(match.getUtcDate());
                matchTimeTextView.setText(outputFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
                matchTimeTextView.setText(match.getUtcDate());
            }

            // Maç durumu
            if (match.getScore().getWinner() == null) {
                // Maç devam ediyor
                if (match.getScore().getHalfTime().getHome() == null) {
                    // İlk yarı başlamadı
                    matchStatusTextView.setText(R.string.match_has_not_started);
                } else {
                    // İlk yarı başladı, maç devam ediyor
                    matchStatusTextView.setText(R.string.match_continues);
                }
            } else {
                // Maç sona erdi
                matchStatusTextView.setText(R.string.match_over);
            }
        }
    }
}
