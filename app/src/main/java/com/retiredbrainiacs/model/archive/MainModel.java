package com.retiredbrainiacs.model.archive;

import java.util.List;

public class MainModel {
    public List<ModelDetail> getModel() {
        return model;
    }

    public void setModel(List<ModelDetail> model) {
        this.model = model;
    }

    List<ModelDetail> model;
}
