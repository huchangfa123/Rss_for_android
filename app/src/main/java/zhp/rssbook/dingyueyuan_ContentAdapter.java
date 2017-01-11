package zhp.rssbook;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huchangfa on 2016/11/20.
 */
public class dingyueyuan_ContentAdapter extends BaseAdapter {

    private Context context;
    private List<dingyueyuan_ContentModel> list;

    public dingyueyuan_ContentAdapter(Context context, List<dingyueyuan_ContentModel> list) {
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
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView =LayoutInflater.from(context).inflate(
                    R.layout.dingyueyuan_item, null);
            convertView.setTag(hold);
        }else {
            hold=(ViewHold) convertView.getTag();
        }

        hold.imageView=(CircleImageView) convertView.findViewById(R.id.dingyueyuan_icon);
        hold.imageView.setImageBitmap(list.get(position).getImageView());

        hold.textView1 = (TextView) convertView.findViewById(R.id.dingyueyuan_content);
        hold.textView2 = (TextView) convertView.findViewById(R.id.unread);

        hold.textView1.setText(list.get(position).getText());
        hold.textView2.setText(list.get(position).getText2()+ "篇未读");

        return convertView;
    }

    static class ViewHold {
        public CircleImageView imageView;
        public TextView textView1;
        public TextView textView2;
    }
}

