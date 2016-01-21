package com.zynga.samplemopubnativead;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.StaticNativeAd;

public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "11a17b188668469fb0412708c3d16813";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showAd(View view) {
        view.setVisibility(View.GONE);

        MoPubNative.MoPubNativeNetworkListener moPubNativeListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(final NativeAd nativeAd) {
                BaseNativeAd baseNativeAd = nativeAd.getBaseNativeAd();
                if (!(baseNativeAd instanceof StaticNativeAd)) {
                    return;
                }

                final StaticNativeAd staticNativeAd = (StaticNativeAd)baseNativeAd;

                TextView titleView = (TextView)findViewById(R.id.titleView);
                titleView.setText(staticNativeAd.getTitle());

                TextView textView = (TextView)findViewById(R.id.textView);
                textView.setText(staticNativeAd.getText());

                ImageView mainImageView = (ImageView)findViewById(R.id.mainImageView);
                NativeImageHelper.loadImageView(staticNativeAd.getMainImageUrl(), mainImageView);

                ImageView iconImageView = (ImageView)findViewById(R.id.iconImageView);
                NativeImageHelper.loadImageView(staticNativeAd.getIconImageUrl(), iconImageView);

                Button ctaView = (Button)findViewById(R.id.ctaView);
                ctaView.setVisibility(View.VISIBLE);
                ctaView.setText(staticNativeAd.getCallToAction());
                ctaView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setEnabled(false);
                        v.setBackgroundColor(Color.GRAY);
                        staticNativeAd.handleClick(null);
                    }
                });

                staticNativeAd.recordImpression(null);
            }

            @Override
            public void onNativeFail(final NativeErrorCode errorCode) { }

        };

        Activity activity = this;
        MoPubNative moPubNative = new MoPubNative(activity, AD_UNIT_ID, moPubNativeListener);
        moPubNative.registerAdRenderer(new MoPubStaticNativeAdRenderer(null));

        RequestParameters requestParameters = new RequestParameters.Builder().build();
        moPubNative.makeRequest(requestParameters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
