package com.icoding.warranty.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Warrenties")
public class WarrantyData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "deviceId")
    public int mId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "duration")
    public int duration;

    public WarrantyData(String name, String description, String date, int duration) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.duration = duration;
    }

    public int getId() {
        return mId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}


