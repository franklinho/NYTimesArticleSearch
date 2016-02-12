
package com.franklinho.nytimessearch.models;


import org.parceler.Parcel;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
@Parcel
public class Headline implements Serializable {

    private String main;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Headline() {

    }
    /**
     * 
     * @return
     *     The main
     */
    public String getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(String main) {
        this.main = main;
    }

//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

}
