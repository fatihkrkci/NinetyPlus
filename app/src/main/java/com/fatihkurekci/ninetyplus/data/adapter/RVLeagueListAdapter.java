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

    private Context context;
    private ArrayList<String> leagueList;
    private LeagueClickListener listener;

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
        holder.bind(leagueList.get(position));
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public void updateData(ArrayList<String> updatedList) {
        leagueList.clear();
        leagueList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView leagueNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(R.id.leagueNameTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(String leagueName) {
            leagueNameTextView.setText(leagueName);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onLeagueClick(leagueList.get(position));
                }
            }
        }
    }

    public interface LeagueClickListener {
        void onLeagueClick(String leagueName);
    }
}
