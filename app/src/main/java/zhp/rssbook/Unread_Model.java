package zhp.rssbook;

import android.graphics.Bitmap;

/**
 * Created by huchangfa on 2016/12/16.
 */
public class Unread_Model {
    private Bitmap imageView;
    private String text;
    private String text2;
    private String text3;
    private String feed_id;

    public Unread_Model(Bitmap imageView,String text,String text2,String text3,String feed_id){
        super();
        this.imageView = imageView;
        this.text = text;
        this.text2 = text2;
        this.text3 = text3;
        this.feed_id = feed_id;
    }

    public Bitmap getImageView(){
        return imageView;
    }

    public void setImageView(Bitmap imageView){
        this.imageView = imageView;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText2(){
        return text2;
    }

    public void setText2(String text){
        this.text2 = text2;
    }

    public String getText3(){
        return text3;
    }

    public void setText3(String text){
        this.text3 = text3;
    }

    public String getFeed_id(){
        return this.feed_id;
    }

    public void setFeed_id(String feed_id){
        this.feed_id = feed_id;
    }
}
