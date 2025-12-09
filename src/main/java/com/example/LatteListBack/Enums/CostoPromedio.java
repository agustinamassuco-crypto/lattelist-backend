package com.example.LatteListBack.Enums;


public enum CostoPromedio {
    BARATO("$"),
    MEDIO("$$"),
    CARO("$$$");

    private final String label;

    CostoPromedio(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
