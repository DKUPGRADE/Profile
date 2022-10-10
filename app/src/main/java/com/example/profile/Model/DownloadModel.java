package com.example.profile.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownloadModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pack_type")
    @Expose
    private String packType;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("mod_id")
    @Expose
    private String modId;
    @SerializedName("size")
    @Expose
    private String size;

    /**
     * No args constructor for use in serialization
     */
    public DownloadModel() {
    }

    /**
     * @param path
     * @param size
     * @param packType
     * @param id
     * @param modId
     */
    public DownloadModel(String id, String packType, String path, String modId, String size) {
        super();
        this.id = id;
        this.packType = packType;
        this.path = path;
        this.modId = modId;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
