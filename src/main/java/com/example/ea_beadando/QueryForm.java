package com.example.ea_beadando;

public class QueryForm {
    private String currency;   // pl. EUR
    private String startDate;  // yyyy-MM-dd
    private String endDate;    // yyyy-MM-dd

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
