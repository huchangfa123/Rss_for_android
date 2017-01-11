package zhp.rssbook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by huchangfa on 2016/11/29.
 */
public class Paper_Adapter extends BaseAdapter {

    private Context context;
    private List<paper_ContentModel> list;

    public Paper_Adapter(Context context, List<paper_ContentModel> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView =LayoutInflater.from(context).inflate(
                    R.layout.paper_item, null);
            convertView.setTag(hold);
        }else {
            hold=(ViewHold) convertView.getTag();
        }

        hold.textView = (TextView) convertView.findViewById(R.id.paper);
        hold.point = (ImageView) convertView.findViewById(R.id.point);


        hold.textView.setText(list.get(position).getTitle());
        if(list.get(position).getread()){
            hold.textView.setTextColor(Color.rgb(128, 128, 128));
            hold.point.setVisibility(View.INVISIBLE);
        }
        else{
            hold.textView.setTextColor(Color.rgb(256, 256, 256));
            hold.point.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHold {
        public TextView textView;
        public ImageView point;
    }


}
