
package com.franklinho.nytimessearch.models;

import org.parceler.Parcel;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
@Parcel
public class Multimedium implements Serializable {

    private Integer width;
    private String url;
    private Integer height;
    private String type;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Multimedium() {

    }
    /**
     * 
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }


}
