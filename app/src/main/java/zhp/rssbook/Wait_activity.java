package zhp.rssbook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Created by huchangfa on 2016/12/22.
 */
public class Wait_activity extends Activity{


    private View rootview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootview = getLayoutInflater().inflate(R.layout.wait, null);
        setContentView(rootview);
    }


}
