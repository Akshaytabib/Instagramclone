package com.example.pushnotification.model;

public class Like {
   String LikeID,LikeName;

   public Like(){}

    public Like(String likeID, String likeName) {
        LikeID = likeID;
        LikeName = likeName;
    }

    public String getLikeID() {
        return LikeID;
    }

    public void setLikeID(String likeID) {
        LikeID = likeID;
    }

    public String getLikeName() {
        return LikeName;
    }

    public void setLikeName(String likeName) {
        LikeName = likeName;
    }
}
