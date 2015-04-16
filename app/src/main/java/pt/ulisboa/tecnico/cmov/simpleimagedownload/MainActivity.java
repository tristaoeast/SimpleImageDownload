package pt.ulisboa.tecnico.cmov.simpleimagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;


public class MainActivity extends ActionBarActivity implements Handler.Callback {

    private Handler handler = new Handler((Handler.Callback) this);

    private Runnable imageDownloader = new Runnable() {

        public void run() {
            try {
                URL imageUrl = new URL("http", "android.com", "/images/froyo.png");
                Bitmap image = BitmapFactory.decodeStream(imageUrl.openStream());
                if (image != null) {
//                    Log.i("DL", "Successfully retrieved file!");
                    sendMsg("Successfully retrieved file!");
                } else {
                    sendMsg("Failed decoding file from stream");
//                    Log.i("DL", "Failed decoding file from stream");
                }
            } catch (Exception e) {
                sendMsg("Failed downloading file!");
//                Log.i("DL", "Failed downloading file!");
                e.printStackTrace();
            }
        }
    };

    private void sendMsg(String what) {
        Bundle bundle = new Bundle();
        bundle.putString("status", what);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void showMessage(String msg) {
        TextView tv = (TextView) findViewById(R.id.status);
        tv.setText(msg);
    }

    public void startDownload(View source) {
//        new Thread(imageDownloader, "Download thread").start();
        new DownloadTask().execute("http", "android.com", "/images/froyo.png");
        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText("Download started...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean handleMessage(Message msg) {
        String text = msg.getData().getString("status");
        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText(text);
        return true;
    }

    public void showImage(Bitmap image) {

        ImageView iv = (ImageView) findViewById(R.id.iv_froyo);
        iv.setImageBitmap(image);

//        ImageView iv = new ImageView(this);
//        iv.setId(5);
//        iv.setImageBitmap(image);
//        LinearLayout linlay = (LinearLayout) findViewById(R.id.ll_01);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.BELOW, R.id.ButtonRecalculate);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        linlay.addView(iv, lp);
    }

    public void iv_onClick(View view) {
        ImageView iv = (ImageView) findViewById(R.id.iv_froyo);
        iv.setImageBitmap(null);
    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap image = null;
            try {

                URL imageUrl = new URL(urls[0], urls[1], urls[2]);
                imageUrl = new URL("http://upload.wikimedia.org/wikipedia/commons/2/20/Big_Ben_IJA.PNG");
                Log.d("URL CREATED","");
                image = BitmapFactory.decodeStream(imageUrl.openStream());
                if (image != null) {
                    Log.i("DL", "Successfully retrieved file!");
                    return image;
                } else {
                    Log.i("DL", "Failed decoding file from stream");
                }
            } catch (Exception e) {
                Log.i("DL", "Failed downloading file!");
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Bitmap image) {
            showImage(image);
        }
    }
}