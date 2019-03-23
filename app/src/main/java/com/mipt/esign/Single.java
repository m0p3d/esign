package com.mipt.esign;


import java.util.ArrayList;
import java.util.List;

class Single {
    static final Single instance = new Single();

    private List<float[]> coords;
    private List<Curve> slice = null;

    private Single() {
        coords = new ArrayList();
    }

    List getCoords() {
        return coords;
    }

    void flushCoords() {
        coords = new ArrayList<>();
    }

    public List<Curve> getSlice() {
        return slice;
    }

    public void setSlice(List<Curve> slice) {
        this.slice = slice;
    }
}
