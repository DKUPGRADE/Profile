

package com.example.profile.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ModModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("DisplayImage")
    @Expose
    private String displayImage;
    @SerializedName("Title_for_path")
    @Expose
    private String titleForPath;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pack_type")
    @Expose
    private String pack_type;

    /**
     * No args constructor for use in serialization
     *
     */
    public ModModel() {
    }

    /**
     *
     * @param displayImage
     * @param titleForPath
     * @param description
     * @param id
     * @param title
     * @param userId
     */
    public ModModel(String id, String title, String userId, String displayImage, String titleForPath, String description, String pack_type) {
        super();
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.displayImage = displayImage;
        this.titleForPath = titleForPath;
        this.description = description;
        this.pack_type = pack_type;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public String getTitleForPath() {
        return titleForPath;
    }

    public void setTitleForPath(String titleForPath) {
        this.titleForPath = titleForPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
