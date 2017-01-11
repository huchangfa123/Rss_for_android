package zhp.rssbook;

import android.graphics.Bitmap;

/**
 * Created by huchangfa on 2016/12/9.
 */
public class Star_model {
    private Bitmap imageView;
    private String feed_id;
    private String text;

    public Star_model(Bitmap imageView,String text,String feed_id){
        super();
        this.imageView = imageView;
        this.text = text;
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

    public String getfeed_id() { return feed_id;}

    public void setFeed_id(String feed_id) {this.feed_id = feed_id;}
}
