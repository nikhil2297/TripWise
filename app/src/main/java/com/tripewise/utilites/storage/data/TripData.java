package com.tripewise.utilites.storage.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class TripData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "trip_name")
    private String tripName;

    @ColumnInfo(name = "member_name")
    private ArrayList<String> memberName;

    @ColumnInfo(name = "member_count")
    private int memberCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public ArrayList<String> getMemberName() {
        return memberName;
    }

    public void setMemberName(ArrayList<String> memberName) {
        this.memberName = memberName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
