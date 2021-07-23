package com.rosemellow.alternatecreditscore.HardwareSofftware;

public class HardSoftModel {

    int serialno;

    String specification;
    String result;

    public HardSoftModel() {
    }

    public HardSoftModel(int serialno, String specification, String result) {
        this.serialno = serialno;
        this.specification = specification;
        this.result = result;
    }


    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
