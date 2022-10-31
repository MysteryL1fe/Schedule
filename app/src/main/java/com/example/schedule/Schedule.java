package com.example.schedule;

public abstract class Schedule {
    String[] Monday_1 = new String[8];
    String[] Tuesday_1 = new String[8];
    String[] Wednesday_1 = new String[8];
    String[] Thursday_1 = new String[8];
    String[] Friday_1 = new String[8];
    String[] Saturday_1 = new String[8];
    
    String[] Monday_0 = new String[8];
    String[] Tuesday_0 = new String[8];
    String[] Wednesday_0 = new String[8];
    String[] Thursday_0 = new String[8];
    String[] Friday_0 = new String[8];
    String[] Saturday_0 = new String[8];
    
    public abstract void Init();
}
