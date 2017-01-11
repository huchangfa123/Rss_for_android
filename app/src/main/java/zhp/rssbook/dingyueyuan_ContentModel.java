package zhp.rssbook;

import android.graphics.Bitmap;

/**
 * Created by huchangfa on 2016/11/17.
 */
public class dingyueyuan_ContentModel {
    private Bitmap imageView;
    private String text;
    private String text2;
    private String text3;


    public dingyueyuan_ContentModel(Bitmap imageView,String text,String text2,String text3){
        super();
        this.imageView = imageView;
        this.text = text;
        this.text2 = text2;
        this.text3 = text3;
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
}
