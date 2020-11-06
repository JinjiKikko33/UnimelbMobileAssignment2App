package com.example.mobileassignment2;

public class UserRank {
    int rank;
    int points;
    String name;


    public int getRank() {
        return rank;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }


    public UserRank(String name, int rank, int points) {
        this.rank = rank;
        this.name = name;
        this.points = points;
    }

}
