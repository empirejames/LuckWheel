package com.james.luckwheel;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    public Context context;
    public Dialog d;
    public Button yes;
    public TextView txt_String;
    public String result = "";
    String TAG = CustomDialogClass.class.getSimpleName();

    public CustomDialogClass(Context context) {
        super(context);
        this.context = context;
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg + " 中獎");
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void setStr(String present) {
        Log.e(TAG, present);
        this.result = present;
    }
}
