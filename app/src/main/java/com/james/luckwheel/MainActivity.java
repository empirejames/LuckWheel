package com.james.luckwheel;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.james.luckwheel.view.EnvironmentLayout;
import com.james.luckwheel.view.LuckPanLayout;
import com.james.luckwheel.view.RotatePan;

public class MainActivity extends AppCompatActivity implements RotatePan.AnimationEndListener{
    private EnvironmentLayout layout;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private TextView hit_items;
    private ImageView goBtn;
    private ImageView yunIv;
    private String[] item;
    private String[] strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hit_items = (TextView) findViewById(R.id.hit_user_tv);
        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);
        yunIv = (ImageView) findViewById(R.id.yun);
        hit_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;

                int MinValue = Math.min(width, height);
                MinValue -= Util.dip2px(MainActivity.this, 10) * 2;
                backHeight = MinValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;
                luckPanLayout.setLayoutParams(lp);

                MinValue -= Util.dip2px(MainActivity.this, 28) * 2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = MinValue;
                lp.width = MinValue;

                rotatePan.setLayoutParams(lp);


                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();
            }
        });
    }

    public void rotation(View view) {
        rotatePan.startRotate(-1);
        luckPanLayout.setDelayTime(100);
        goBtn.setEnabled(false);
    }

    @Override
    public void endAnimation(int position) {
        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(500);
        Toast.makeText(this, "Position = " + position + "," + strs[position], Toast.LENGTH_SHORT).show();
    }

    public void showDialog() {
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(this);
        FrameLayout frameLayout = new FrameLayout(optionDialog.getContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        optionDialog.setView(frameLayout);
        final AlertDialog alert = optionDialog.create();
        View myView = inflater.inflate(R.layout.activity_dialog, frameLayout);
        alert.show();
    }
}
