package com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddClass {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("from_date")
    @Expose
    private String from_date;

    @SerializedName("to_date")
    @Expose
    private String to_date;

    @SerializedName("category_id")
    @Expose
    private String  category_id;

    @SerializedName("age_group")
    @Expose
    private String age_group;


    @SerializedName("max_views")
    @Expose
    private String max_views;
    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("ad_url")
    @Expose
    private String ad_url;

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("thumb")
    @Expose
    private String thumb;

    @SerializedName("Authorization")
    @Expose
    private String token;

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("HTTP_response_code")
    @Expose
    private int HTTP_response_code;

    public AddClass() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public String getMax_views() {
        return max_views;
    }

    public void setMax_views(String max_views) {
        this.max_views = max_views;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHTTP_response_code() {
        return HTTP_response_code;
    }

    public void setHTTP_response_code(int HTTP_response_code) {
        this.HTTP_response_code = HTTP_response_code;
    }
}
