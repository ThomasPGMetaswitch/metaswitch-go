package com.example.metaswitchgo.metaswitchgo;

/**
 * Created by tcpg on 10/08/2016.
 */
public class MetadexEntry {

    String mInitials;
    String mName;
    String mPhoto;
    String mLocation;
    String mTeam;

    public MetadexEntry(String initials)
    {
        setmInitials(initials);
    }

    public String getmName() {
        return mName;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public String getmInitials() {
        return mInitials;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmTeam() {
        return mTeam;
    }

    public void setmInitials(String mInitials) {
        this.mInitials = mInitials;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmTeam(String mBuildling) {
        this.mTeam = mBuildling;
    }
}
