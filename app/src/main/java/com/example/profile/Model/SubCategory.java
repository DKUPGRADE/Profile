package com.example.profile.Model;

public class SubCategory {


    String sub_category;
    String keyword;



    public SubCategory(String sub_category, String keyword) {
        this.sub_category = sub_category;
        this.keyword = keyword;
    }

    public SubCategory(String keyword) {

        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getSub_category() {
        return sub_category;
    }


    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }


   }
