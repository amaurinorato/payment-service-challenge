package com.finreach.paymentservice.store;


import java.util.HashMap;
import java.util.Map;

import com.finreach.paymentservice.domain.Statistic;

public class Statistics {
	
    private Map<String, Statistic> statistics = new HashMap<>();

    public void add(Statistic statistic) {
        statistics.put(statistic.getAccountId(), statistic);
    }

    public Map<String, Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Statistic> statistics) {
        this.statistics = statistics;
    }
}
