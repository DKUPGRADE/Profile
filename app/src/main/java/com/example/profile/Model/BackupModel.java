package com.example.profile.Model;

public class BackupModel {

    String ModeName,filepath,img_path,name_file_path,file_name;
    int isBackupExists;

    public BackupModel(String modeName, String filepath, String img_path, String name_file_path, String file_name, int isBackupExists) {
        ModeName = modeName;
        this.filepath = filepath;
        this.img_path = img_path;
        this.name_file_path = name_file_path;
        this.file_name = file_name;
        this.isBackupExists = isBackupExists;
    }

    public BackupModel() {
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String modeName) {
        ModeName = modeName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getName_file_path() {
        return name_file_path;
    }

    public void setName_file_path(String name_file_path) {
        this.name_file_path = name_file_path;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getIsBackupExists() {
        return isBackupExists;
    }

    public void setIsBackupExists(int isBackupExists) {
        this.isBackupExists = isBackupExists;
    }
}
