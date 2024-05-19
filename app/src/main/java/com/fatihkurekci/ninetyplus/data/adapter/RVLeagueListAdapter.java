package com.fatihkurekci.ninetyplus.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatihkurekci.ninetyplus.R;

import java.util.ArrayList;

public class RVLeagueListAdapter extends RecyclerView.Adapter<RVLeagueListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> leagueList;
    private final LeagueClickListener listener;

    public RVLeagueListAdapter(Context context, ArrayList<String> leagueList, LeagueClickListener listener) {
        this.context = context;
        this.leagueList = leagueList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_leagueitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String leagueName = leagueList.get(position);
        holder.leagueNameTextView.setText(leagueName);
        holder.itemView.setOnClickListener(v -> listener.onLeagueClick(leagueName));
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public void updateData(ArrayList<String> updatedLeagueList) {
        leagueList.clear();
        leagueList.addAll(updatedLeagueList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView leagueNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(R.id.leagueNameTextView);
        }
    }

    public interface LeagueClickListener {
        void onLeagueClick(String leagueName);
    }
}
