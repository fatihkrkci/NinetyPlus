package com.fatihkurekci.ninetyplus.data.response;

import com.fatihkurekci.ninetyplus.data.model.Filter;
import com.fatihkurekci.ninetyplus.data.model.Match;
import com.fatihkurekci.ninetyplus.data.model.ResultSet;

import java.util.ArrayList;

public class MatchesListApiResponse {
    private Filter filters;
    private ResultSet resultSet;
    private ArrayList<Match> matches;

    public Filter getFilters() {
        return filters;
    }

    public void setFilters(Filter filters) {
        this.filters = filters;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }
}
