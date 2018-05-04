package com.eder.rodriguez.minersup.Model;

/**
 * Created by ederr on 4/28/2018.
 */

public class NewsfeedPost {
    private String Post_Image;
    private String Post_Title;
    private String Post_Description;

    public NewsfeedPost() {}

    public NewsfeedPost(String post_Image, String post_Title, String post_Description) {
        this.Post_Image = post_Image;
        this.Post_Title = post_Title;
        this.Post_Description = post_Description;
    }

    public String getImage() {
        return Post_Image;
    }

    public String getTitle() {
        return Post_Title;
    }

    public String getDescription() {
        return Post_Description;
    }

    public void setPost_Image(String post_Image) {
        Post_Image = post_Image;
    }

    public void setPost_Title(String post_Title) {
        Post_Title = post_Title;
    }

    public void setPost_Description(String post_Description) {
        Post_Description = post_Description;
    }
}
