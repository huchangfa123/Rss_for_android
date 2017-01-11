package zhp.rssbook;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by huchangfa on 2016/10/31.
 */
public class http_deal {

    private String url;
    private String img_url;
    private String use_name;
    private String xsrf;
    private String cookie;
    private Bitmap mimg;
    private List<dingyueyuan_ContentModel> mlist;
    private List<paper_ContentModel> mlist2;
    private List<Star_model> mlist3;
    private List<Hot_model> mlist4;
    private List<Unread_Model> mlist5;
    private dingyueyuan_specific_content_model message;
    private String content;
    private String author;
    private String search_feedid;
    private String pre;
    private String next;
    private String error;
    private Boolean mark = false;
    private Boolean love = false;
    private User_model muser;

    private Boolean judge = true;

    public http_deal(String url){
        this.url = url;
        this.xsrf = "";
        this.cookie = "";
        this.img_url="";
        this.use_name = "";
        this.content = "";
        this.author = "";
        this.search_feedid = "";
        this.mimg = null;
        this.mlist = new ArrayList<dingyueyuan_ContentModel>();
        this.mlist2 = new ArrayList<paper_ContentModel>();
        this.mlist3 = new ArrayList<Star_model>();
        this.mlist4 = new ArrayList<Hot_model>();
        this.mlist5 = new ArrayList<Unread_Model>();
        this.message = null;
    }

    /*
        登陆
    */
    public void doPost_login(String user_email,String user_password) {
        final String username=user_email;
        final String password=user_password;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                loginOfPost(username, password);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loginOfPost(String username, String password)
    {
        try {
            trustAllHosts();
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            String content = "email="+ username +"&password=" +password +"&json=true";
            out.write(content.getBytes());
            out.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
                judge = false;
                get_error_json(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                get_xsrf_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }

    public void get_xsrf_json(String result){
        if(result!=null){
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean s = jsonObject.getBoolean("success");
                if(s)
                {
                    JSONObject resultJsonobject = jsonObject.getJSONObject("body");
                    Log.i("xsrf",resultJsonobject.getString("xsrf"));
                    set_xsrf(resultJsonobject.getString("xsrf"));
                    set_cookie(resultJsonobject.getString("jwt"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    /*
        注册
     */
    public void doPost_signup(String user_email,String user_password) {
        final String username=user_email;
        final String password=user_password;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                signOfPost(username, password);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void signOfPost(String username, String password) {
        try {
            trustAllHosts();
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            OutputStream out = conn.getOutputStream();
            String content = "email="+ username +"&password=" +password;
            out.write(content.getBytes());
            out.close();

            if(conn.getResponseCode()!=200)
            {
                judge = false;
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                get_error_json(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }

    /*
        读取用户信息
    */
    public void doPost_get_user(String murl, String mxsrf,String mcookie){
        final String url = murl;
        final String xsrf = mxsrf;
        final String cookie = mcookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                get_user_message_post(url,xsrf,cookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
            System.out.println("after get user message img_url = "+getImg_url());
            do_get_pic_map("https://www.enjoyrss.com"+getImg_url(),xsrf,cookie);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void get_user_message_post(String murl,String mxsrf,String mcookie){
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_user_message_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public void get_user_message_json(String result){
        if(result!=null){
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean s = jsonObject.getBoolean("success");
                if(s)
                {
                    JSONObject resultJsonobject = jsonObject.getJSONObject("data");
                    System.out.println("user data in json :"+resultJsonobject.getString("avatar"));
                    setImg_url(resultJsonobject.getString("avatar"));
                    setUse_name(resultJsonobject.getString("username"));
                    int i = resultJsonobject.getInt("__v");
                    String s1 = resultJsonobject.getString("avatar");
                    String s2 = resultJsonobject.getString("createdtime");
                    String s3 = resultJsonobject.getString("email");
                    String s4 = resultJsonobject.getString("username");
                    muser = new User_model(i,s1,s2,s3,s4);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        修改用户名
     */
    public void put_username(String url,String xsrf,String cookie,String use_name){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        final String muse_name = use_name;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_put_username(murl, mxsrf, mcookie,muse_name);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_put_username(String murl, String mxsrf, String mcookie,String muse_name) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            User_model user = getMuser();
            int i = user.get__v();
            String s1 = user.getAvatar();
            String s2 = user.getCreatedtime();
            String s3 = user.getEmail();
            String s4 = muse_name;

            String jsonParam = "{\"__v\":\"" + i + "\"," +
                                "\"avatar\":\"" + s1 + "\"," +
                                "\"createdtime\":\"" + s2 + "\","+
                                "\"email\":\"" + s3 + "\","+
                                "\"username\":\"" + s4 + "\"}";
            dos.writeBytes(jsonParam);
            dos.flush();
            dos.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
                judge = false;
                get_error_json(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /*
      获取图片
     */
    public void do_get_pic_map(String img_url,String mxsrf,String mcookie){
        final String url = img_url;
        final String xsrf = mxsrf;
        final String cookie = mcookie;
        System.out.println(img_url);
        Thread mthread = new Thread(new Runnable() {
            @Override
        public void run() {
                getmap(url,xsrf,cookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void getmap(String img_url,String mxsrf,String mcookie){

                System.out.println(img_url);
                HttpGet get = new HttpGet(img_url);
                HttpClient client = new DefaultHttpClient();
                get.setHeader("x-xsrf-token",mxsrf);
                get.setHeader("authorization","Bearer "+mcookie);
                Bitmap pic = null;
                try {
                    trustAllHosts();
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();

                    pic = BitmapFactory.decodeStream(is);
                    set_Mimg(pic);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
    /*
        读取未读列表
     */
    public void get_unread_list(String url,String xsrf,String cookie) {
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_get_unread(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_get_unread(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_unread_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_unread_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONArray resultsArray = jsonObject.getJSONArray("data");
                    System.out.println("文章数目为:" + resultsArray.length());
                    for(int i = 0 ; i < resultsArray.length() ;  i++ )
                    {
                        JSONObject mobject = resultsArray.getJSONObject(i);
                        //System.out.println("第"+i+"个:" + mobject);
                        String url = "https://www.enjoyrss.com" + mobject.getString("favicon");
                        getmap(url,xsrf,cookie);
                        Bitmap pic = get_Mimg();
                        String content = mobject.getString("summary");
                        System.out.println("结果:" + content);
                        String time[] = mobject.getString("feed_time").split("T");
                        content = mobject.getString("title") + "\n" + "更新于:" + time[0] + "\n" + content.replace("\n","");
                        System.out.println(content);
                        mlist5.add(new Unread_Model(pic,mobject.getString("feed_title"),mobject.getString("unread"),content,mobject.getString("_id")));
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    /*
        读取订阅源列表
    */
    public void get_dingyueyuan_content(String url, String xsrf, String cookie) {
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_getcontent(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_getcontent(String url,String xsrf,String cookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",xsrf);
            conn.addRequestProperty("authorization","Bearer "+cookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_content_message_json(sb.toString(),xsrf,cookie);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_content_message_json(String s,String xsrf,String cookie) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONArray resultsArray = jsonObject.getJSONArray("data");

                    for(int i = 0 ; i < resultsArray.length() ;  i++ )
                    {
                        JSONObject mobject = resultsArray.getJSONObject(i);
                        String url = "https://www.enjoyrss.com"+mobject.getString("favicon");
                        getmap(url,xsrf,cookie);
                        Bitmap mbitmap = get_Mimg();
                        mlist.add(new dingyueyuan_ContentModel(mbitmap,mobject.getString("title"),mobject.getString("unread"),mobject.getString("feed_id")));
                    }
                    //System.out.println("paper data in json :"+resultJsonobject.getString("avatar"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        读取订阅源信息
    */
    public void get_specific_content(String url, String xsrf, String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_get_specific_content(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_get_specific_content(String url, String xsrf, String cookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",xsrf);
            conn.addRequestProperty("authorization","Bearer "+cookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_specific_message_json(sb.toString(),xsrf,cookie);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_specific_message_json(String s,String xsrf,String cookie) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                //System.out.println("结果1:" + jsonObject);
                boolean rs = jsonObject.getBoolean("success");
                JSONObject data = jsonObject.getJSONObject("data");
                //System.out.println("结果1:" + data.getString("feedNum"));
                String feedtime;
                if(rs)
                {
                    if(data.has("feed_time"))
                        feedtime = data.getString("feed_time");
                    else
                        feedtime = null;

                    String url = "https://www.enjoyrss.com" + data.getString("favicon");
                    getmap(url,xsrf,cookie);
                    Bitmap pic = get_Mimg();
                    message = new dingyueyuan_specific_content_model(data.getString("title"),data.getString("feedNum"),data.getString("absurl"),feedtime,pic);
                    //System.out.println("paper data in json :"+resultJsonobject.getString("avatar"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        读取文章列表
    */
    public void get_paper(String url, String xsrf, String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_get_paper(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_get_paper(String url, String xsrf, String cookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",xsrf);
            conn.addRequestProperty("authorization","Bearer "+cookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_paperlist_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_paperlist_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONArray resultsArray = jsonObject.getJSONArray("data");
                    System.out.println("文章数目为:" + resultsArray.length());
                    for(int i = 0 ; i < resultsArray.length() ;  i++ ) {
                        JSONObject mobject = resultsArray.getJSONObject(i);
                        Boolean read, star, zan;

                        if (mobject.has("read"))
                            read = mobject.getBoolean("read");
                        else
                            read = false;

                        if (mobject.has("mark"))
                            star = mobject.getBoolean("mark");
                        else
                            star = false;

                        if (mobject.has("love"))
                            zan = mobject.getBoolean("love");
                        else
                            zan = false;

                        mlist2.add(new paper_ContentModel(mobject.getString("title"), mobject.getString("pubdate"),read, mobject.getString("post_id"),star, zan));
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

   /*
       读取文章内容
   */
    public void get_paper_content(String url, String xsrf, String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_get_paper_content(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_get_paper_content(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_papercontent_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_papercontent_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONObject results= jsonObject.getJSONObject("data");
                    String content = results.getString("description");
                    String author = results.getString("author");
                    String pre;
                    String next;
                    if(results.has("pre"))
                        pre = results.getString("pre");
                    else
                        pre = null;
                    if(results.has("next"))
                        next = results.getString("next");
                    else
                        next = null;

                    if (results.has("mark"))
                        mark = results.getBoolean("mark");

                    if (results.has("love"))
                        love = results.getBoolean("love");

                    setContent(content);
                    setauthor(author);
                    setpre(pre);
                    setnext(next);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
       标记已读
     */
    public void put_data(String url,String xsrf,String cookie) {
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_put_data(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_put_data(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                String jsonParam = "{\"type\":\"read\"}";
            dos.writeBytes(jsonParam);
            dos.flush();
            dos.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /*
        订阅
     */
    public void put_dingyue(String url,String xsrf,String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_put_dingyue(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_put_dingyue(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    /*
        取消订阅
     */
    public void delete_dingyue(String url,String xsrf,String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_delete_dingyue(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_delete_dingyue(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("DELETE");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /*
        收藏
     */
    public void put_star(String url,String xsrf,String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_put_star(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_put_star(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String jsonParam = "{\"type\":\"mark\",\"revert\":true}";
            dos.writeBytes(jsonParam);
            dos.flush();
            dos.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    /*
        赞
     */
    public void put_love(String url,String xsrf,String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_put_love(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_put_love(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");//设定 请求格式 json，也可以设定xml格式的

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String jsonParam = "{\"type\":\"love\",\"revert\":true}";
            dos.writeBytes(jsonParam);
            dos.flush();
            dos.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("结果:" + sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /*
        读取收藏文章
    */
   public void get_star_paper(String url, String xsrf, String cookie){
       final String murl = url;
       final String mxsrf = xsrf;
       final String mcookie = cookie;
       Thread mthread = new Thread(new Runnable() {
           @Override
           public void run() {
               //访问网络要在子线程中实现，使用get取数据
               do_get_star(murl, mxsrf, mcookie);
           }
       });
       mthread.start();
       try {
           mthread.join();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

    private void do_get_star(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println(sb.toString());
                get_star_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_star_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONArray results= jsonObject.getJSONArray("data");
                    for(int i = 0 ; i < results.length() ;  i++ )
                    {
                        JSONObject mobject = results.getJSONObject(i);
                        //System.out.println("第"+i+"个:" + mobject);
                        String img_url = "https://www.enjoyrss.com" + mobject.getString("favicon");
                        getmap(img_url,xsrf,cookie);
                        Bitmap mbitmap = get_Mimg();
                        mlist3.add(new Star_model(mbitmap,mobject.getString("title"),mobject.getString("_id")));
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        获取热门订阅源
     */
    public void get_hot_dingyueyuan(String url, String xsrf, String cookie){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_get_hot(murl, mxsrf, mcookie);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_get_hot(String murl, String mxsrf, String mcookie) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                System.out.println("热门订阅源:" + sb.toString());
                get_hot_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_hot_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    JSONArray results= jsonObject.getJSONArray("data");
                    for(int i = 0 ; i < results.length() ;  i++ )
                    {
                        JSONObject mobject = results.getJSONObject(i);
                        //System.out.println("第"+i+"个:" + mobject);
                        String img_url = "https://www.enjoyrss.com" + mobject.getString("favicon");
                        getmap(img_url,xsrf,cookie);
                        Bitmap mbitmap = get_Mimg();
                        mlist4.add(new Hot_model(mbitmap,mobject.getString("title"),mobject.getString("_id"),mobject.getString("feedNum")));
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        搜索订阅源
     */
    public void search_dingyueyuan(String url, String xsrf, String cookie,String xml){
        final String murl = url;
        final String mxsrf = xsrf;
        final String mcookie = cookie;
        final String mxml = xml;
        Thread mthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                do_search(murl, mxsrf, mcookie,mxml);
            }
        });
        mthread.start();
        try {
            mthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void do_search(String murl, String mxsrf, String mcookie,String mxml) {
        try {
            trustAllHosts();
            URL httpurl = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("x-xsrf-token",mxsrf);
            conn.addRequestProperty("authorization","Bearer "+mcookie);
            conn.setRequestProperty("Content-Type", " application/json");

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String jsonParam = "{\"feedlink\":\""+ mxml +"\"}";
            dos.writeBytes(jsonParam);
            dos.flush();
            dos.close();

            if(conn.getResponseCode()!=200)
            {
                System.out.println(conn.getResponseCode());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str=reader1.readLine())!=null){
                    sb.append(str);
                }
                judge = false;
                get_error_json(sb.toString());
                //System.out.println(sb.toString());
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                get_search_json(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void get_search_json(String s) {
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(rs)
                {
                    String feed_id = jsonObject.getString("data");
                    setfeedid(feed_id);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
        获取错误信息
     */
    public void get_error_json(String s){
        if(s!=null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean rs = jsonObject.getBoolean("success");
                if(!rs)
                {
                   error = jsonObject.getString("message");
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }



    public void set_xsrf(String str) {this.xsrf = str;}

    public void set_cookie(String str) { this.cookie = str;}

    private void set_Mimg(Bitmap img) {this.mimg = img;}

    private void setImg_url(String str) {this.img_url = str;}

    private void setUse_name(String str) {this.use_name = str;}

    private void setContent(String str) {this.content = str;}

    private void setauthor(String str) {this.author = str;}

    private void setfeedid(String str) {this.search_feedid = str;}

    private void setpre(String str) {this.pre = str;}

    private void setnext(String str) {this.next = str;}



    public String getpre() {return this.pre;}

    public String getnext() {return this.next;}

    public String getContent() {return this.content;}

    public String getauthor(){
        return this.author;
    }

    public String get_xsrf(){
        return this.xsrf;
    }

    public List get_content_list(){return  this.mlist;}

    public List get_content_list2(){return  this.mlist2;}

    public List get_content_list3(){return  this.mlist3;}

    public List get_content_list4(){return  this.mlist4;}

    public List get_content_list5(){return  this.mlist5;}

    public String get_Cookie(){
        return this.cookie;
    }

    public Bitmap get_Mimg(){
        return this.mimg;
    }

    public String getImg_url() { return this.img_url;}

    public String getUrl() { return  this.url; }

    public String getfeedid() { return  this.search_feedid;}

    public String getUse_name() { return this.use_name;}

    public dingyueyuan_specific_content_model get_message() {return this.message;}

    public Boolean getjudge() {return this.judge;}

    public Boolean getMark() {return this.mark;}

    public Boolean getLove() {return this.love;}

    public String getError() {return this.error;}

    public User_model getMuser() {return this.muser;}







    /*
         无视证书的要求
    */
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkServerTrusted");
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
