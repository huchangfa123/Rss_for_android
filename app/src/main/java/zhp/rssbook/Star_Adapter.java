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
 * Created by huchangfa on 2016/12/9.
 */
public class Star_Adapter extends BaseAdapter {
    private Context context;
    private List<Star_model> list;

    public Star_Adapter(Context context, List<Star_model> list) {
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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.star_item, null);
            convertView.setTag(hold);
        }else {
            hold=(ViewHold) convertView.getTag();
        }

        hold.imageView=(CircleImageView) convertView.findViewById(R.id.star_imageview);
        hold.imageView.setImageBitmap(list.get(position).getImageView());

        hold.textView1 = (TextView) convertView.findViewById(R.id.star_textview);
        hold.textView1.setText(list.get(position).getText());

        return convertView;
    }

    static class ViewHold {
        public CircleImageView imageView;
        public TextView textView1;
    }
}
