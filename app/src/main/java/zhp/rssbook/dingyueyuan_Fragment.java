package zhp.rssbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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


import java.util.ArrayList;
import java.util.List;

public class dingyueyuan_Fragment extends Fragment {

    private View rootview;
    private MainActivity activity;

    private ListView mlistview;
    private List<dingyueyuan_ContentModel> mlist;
    private dingyueyuan_ContentAdapter mAdapter;
    private http_deal mhttp;

    private RelativeLayout loading;
    private LinearLayout tab02_view;

    private String feed_id;

    public static final int REQUSET = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mtab02, container, false);
        activity = (MainActivity)getActivity();
        //String str1 = activity.getxsrf();
        initView();
        fillData();
        initEvents();
        return rootview;
    }

    private void fillData() {
        new AsyncTask<Void,Void,List<dingyueyuan_ContentModel>>(){

            @Override
            protected void onPreExecute() {
                loading.setVisibility(View.VISIBLE);
                tab02_view.setVisibility(View.INVISIBLE);
                super.onPreExecute();
            }

            @Override
            protected List<dingyueyuan_ContentModel> doInBackground(Void... params) {
                initData();
                return mlist;
            }

            @Override
            protected void onPostExecute(List<dingyueyuan_ContentModel> result){

                loading.setVisibility(View.INVISIBLE);
                tab02_view.setVisibility(View.VISIBLE);
                mAdapter = new dingyueyuan_ContentAdapter(activity,mlist);
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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("xsrf",activity.getxsrf());
                bundle.putString("cookie",activity.getcookie());
                dingyueyuan_ContentAdapter adapter = (dingyueyuan_ContentAdapter)parent.getAdapter();
                dingyueyuan_ContentModel feed_id = (dingyueyuan_ContentModel)adapter.getItem(position);
                String str = feed_id.getText3();
                Bitmap pic = feed_id.getImageView();
                //System.out.println(str);
                bundle.putString("title",feed_id.getText());
                bundle.putString("feed_id",str);
                bundle.putParcelable("pic",pic);
                intent.putExtras(bundle);
                intent.setClass(activity, dingyueyuan_Message_Activity.class);
                startActivityForResult(intent,REQUSET);
            }
        });
    }

    private void initData() {
        mlist = new ArrayList<dingyueyuan_ContentModel>();
        mhttp = new http_deal("https://www.enjoyrss.com/api/feed");
        mhttp.get_dingyueyuan_content(mhttp.getUrl(),activity.getxsrf(),activity.getcookie());
        mlist = mhttp.get_content_list();
    }


    private void initView() {
        mlistview = (ListView)rootview.findViewById(R.id.dingyueyuan_content_list);
        tab02_view = (LinearLayout) rootview.findViewById(R.id.tab2_view);
        loading = (RelativeLayout) rootview.findViewById(R.id.loading);
    }

}
