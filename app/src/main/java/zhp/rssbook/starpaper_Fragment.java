package zhp.rssbook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

public class starpaper_Fragment extends Fragment {

    private MainActivity activity;
    private View rootview;

    private ListView mlistview;
    private List<Star_model> mlist;
    private Star_Adapter mAdapter;
    private http_deal mhttp;

    private RelativeLayout loading;
    private LinearLayout tab03_view;

    public static final int REQUSET = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.mtab03, container, false);
        activity = (MainActivity)getActivity();
        initView();
        fillData();
        initEvents();
        return rootview;
    }

    private void fillData() {
        new AsyncTask<Void,Void,List<Star_model>>(){

            @Override
            protected void onPreExecute() {
                loading.setVisibility(View.VISIBLE);
                tab03_view.setVisibility(View.INVISIBLE);
                super.onPreExecute();
            }

            @Override
            protected List<Star_model> doInBackground(Void... params) {
                initData();
                return mlist;
            }

            @Override
            protected void onPostExecute(List<Star_model> result){
                loading.setVisibility(View.INVISIBLE);
                tab03_view.setVisibility(View.VISIBLE);
                mAdapter = new Star_Adapter(activity,mlist);
                mlistview.setAdapter(mAdapter);
                super.onPostExecute(result);
            }
        }.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET)
            fillData();
    }

    private void initEvents() {
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent2 = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putString("xsrf", activity.getxsrf());
                bundle2.putString("cookie", activity.getcookie());
                Star_Adapter adapter = (Star_Adapter) parent.getAdapter();
                Star_model feed_id = (Star_model) adapter.getItem(position);
                bundle2.putString("title",feed_id.getText());
                bundle2.putString("feed_id",feed_id.getfeed_id());
                intent2.putExtras(bundle2);
                intent2.setClass(activity,Reading_Activity.class);
                startActivityForResult(intent2,REQUSET);
            }
        });
    }

    private void initData() {
        String url = "https://www.enjoyrss.com/api/posts";
        mhttp = new http_deal(url);
        mhttp.set_xsrf(activity.getxsrf());
        mhttp.set_cookie(activity.getcookie());
        String star_url = "https://www.enjoyrss.com/api/posts?" + "type=mark&feed_id=%40feed_id&page=0&per_page=99999";
        mhttp.get_star_paper(star_url,mhttp.get_xsrf(),mhttp.get_Cookie());
        mlist = mhttp.get_content_list3();
    }

    private void initView() {
        mlistview = (ListView) rootview.findViewById(R.id.star_list);
        loading = (RelativeLayout) rootview.findViewById(R.id.loading);
        tab03_view = (LinearLayout)rootview.findViewById(R.id.tab3_view);
    }
}
