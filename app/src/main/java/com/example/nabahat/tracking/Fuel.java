package com.example.nabahat.tracking;

/**
 * Created by msnab on 3/29/2018.
 */

public class Fuel {

    double fuel_Consumed;
    double fuel_Cost;
    long journey_Time;
    String bus_number;
    double distance_Covered;

    @Override
    public String toString() {
        return "Fuel{" +
                "fuel_Consumed=" + fuel_Consumed +
                ", fuel_Cost=" + fuel_Cost +
                ", journey_Time=" + journey_Time +
                ", bus_number='" + bus_number + '\'' +
                ", distance_Covered=" + distance_Covered +
                ", speed=" + speed +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    double speed;
    String date;
    String time;

    public double getFuel_Consumed() {
        return fuel_Consumed;
    }

    public void setFuel_Consumed(double fuel_Consumed) {
        this.fuel_Consumed = fuel_Consumed;
    }

    public double getFuel_Cost() {
        return fuel_Cost;
    }

    public void setFuel_Cost(double fuel_Cost) {
        this.fuel_Cost = fuel_Cost;
    }

    public long getJourney_Time() {
        return journey_Time;
    }

    public void setJourney_Time(long journey_Time) {
        this.journey_Time = journey_Time;
    }

    public String getBus_number() {
        return bus_number;
    }

    public void setBus_number(String bus_number) {
        this.bus_number = bus_number;
    }

    public double getDistance_Covered() {
        return distance_Covered;
    }

    public void setDistance_Covered(double distance_Covered) {
        this.distance_Covered = distance_Covered;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }







    public Fuel(){}

    public Fuel(double fuel_Consumed, double fuel_Cost, long journey_Time, String bus_number, double distance_Covered, double speed, String date,  String time) {
        this.fuel_Consumed = fuel_Consumed;
        this.fuel_Cost = fuel_Cost;
        this.journey_Time = journey_Time;
        this.bus_number = bus_number;
        this.distance_Covered = distance_Covered;
        this.speed = speed;
        this.date = date;
        this.time = time;
    }

}
