package pt.ulisboa.tecnico.cmov.simpleimagedownload;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements Handler.Callback {

    private Handler handler = new Handler((Handler.Callback) this);

    private Runnable imageDownloader = new Runnable() {

        public void run() {
            try {
                URL imageUrl = new URL("http", "android.com", "/images/froyo.png");
                Bitmap image = BitmapFactory.decodeStream(imageUrl.openStream());
                if (image != null) {
//                    Log.i("DL", "Successfully retrieved file!");
                    sendMessage("Successfully retrieved file!");
                } else {
                    sendMessage("Failed decoding file from stream");
//                    Log.i("DL", "Failed decoding file from stream");
                }
            } catch (Exception e) {
                sendMessage("Failed downloading file!");
//                Log.i("DL", "Failed downloading file!");
                e.printStackTrace();
            }
        }
    };

    private void sendMessage(String what) {
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
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.beerbottle);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RelativeLayout01);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, R.id.ButtonRecalculate);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl.addView(iv, lp);
    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap image = null;
            try {
                URL imageUrl = new URL(urls[0], urls[1], urls[2]);
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