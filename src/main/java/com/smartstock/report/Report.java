package com.smartstock.report;

public abstract class Report {

    private final String title;

    protected Report(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public abstract String generate();
}