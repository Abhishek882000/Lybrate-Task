package com.example.lybratetask;

public class ListItem {
    private String name;
    private String cuisines;
    private String averageCost;
    private String timings;


    public ListItem(String name, String cuisines,String averageCost,String timings) {
        this.name = name;
        this.cuisines = cuisines;
        this.averageCost = averageCost;
        this.timings = timings;
    }

    public String getName() {
        return name;
    }

    public String getCuisines() {
        return cuisines;
    }

    public String getAverageCost() {
        return averageCost;
    }

    public String getTimings() {
        return timings;
    }
}
