package com.karthik.imager.APIModels;



/**
 * Created by karthikrk on 27/02/16.
 */
public class DummyObject{
    private String dummyTitle;

    public String getDummyDesc() {
        return dummyDesc;
    }

    public void setDummyDesc(String dummyDesc) {
        this.dummyDesc = dummyDesc;
    }

    public String getDummyTitle() {
        return dummyTitle;
    }

    public void setDummyTitle(String dummyTitle) {
        this.dummyTitle = dummyTitle;
    }

    private String dummyDesc;

    public DummyObject(String dummyTitle,String dummyDesc){
        this.dummyTitle = dummyTitle;
        this.dummyDesc = dummyDesc;
    }
    public DummyObject(){

    }
}
