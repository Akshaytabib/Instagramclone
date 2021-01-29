package com.example.pushnotification.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Save implements Parcelable {

    String SaveID,saveName,saveProfile,saveFeedimge,saveLike,saveuserName,saveText;

    public Save() {
    }

    public Save(String saveID, String saveName, String saveProfile, String saveFeedimge, String saveLike, String saveuserName, String saveText) {
        SaveID = saveID;
        this.saveName = saveName;
        this.saveProfile = saveProfile;
        this.saveFeedimge = saveFeedimge;
        this.saveLike = saveLike;
        this.saveuserName = saveuserName;
        this.saveText = saveText;
    }

    protected Save(Parcel in) {
        SaveID = in.readString();
        saveName = in.readString();
        saveProfile = in.readString();
        saveFeedimge = in.readString();
        saveLike = in.readString();
        saveuserName = in.readString();
        saveText = in.readString();
    }

    public static final Creator<Save> CREATOR = new Creator<Save>() {
        @Override
        public Save createFromParcel(Parcel in) {
            return new Save(in);
        }

        @Override
        public Save[] newArray(int size) {
            return new Save[size];
        }
    };

    public String getSaveID() {
        return SaveID;
    }

    public void setSaveID(String saveID) {
        SaveID = saveID;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getSaveProfile() {
        return saveProfile;
    }

    public void setSaveProfile(String saveProfile) {
        this.saveProfile = saveProfile;
    }

    public String getSaveFeedimge() {
        return saveFeedimge;
    }

    public void setSaveFeedimge(String saveFeedimge) {
        this.saveFeedimge = saveFeedimge;
    }

    public String getSaveLike() {
        return saveLike;
    }

    public void setSaveLike(String saveLike) {
        this.saveLike = saveLike;
    }

    public String getSaveuserName() {
        return saveuserName;
    }

    public void setSaveuserName(String saveuserName) {
        this.saveuserName = saveuserName;
    }

    public String getSaveText() {
        return saveText;
    }

    public void setSaveText(String saveText) {
        this.saveText = saveText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(SaveID);
        parcel.writeString(saveName);
        parcel.writeString(saveProfile);
        parcel.writeString(saveFeedimge);
        parcel.writeString(saveLike);
        parcel.writeString(saveuserName);
        parcel.writeString(saveText);
    }
}
