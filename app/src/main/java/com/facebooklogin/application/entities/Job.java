package com.facebooklogin.application.entities;

public class Job {
    private String position;
    private String company;
    private String period;
    public Job(String position, String company, String period)
    {
        this.position = position;
        this.company = company;
        this.period = period;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
