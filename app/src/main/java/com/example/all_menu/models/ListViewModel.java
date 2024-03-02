package com.example.all_menu.models;

public class ListViewModel {

    private String id;
    private String label;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ListViewModel(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
