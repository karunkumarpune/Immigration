package com.immigration.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.immigration.R;


public class CustomProgressBar extends AlertDialog {

   private boolean clickable;
   public CustomProgressBar(Context context) {
       super(context, R.style.CustomDialogTheme);
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.progress_layout);

   }

   @Override
   public void setTitle(CharSequence title) {
       super.setTitle(title);
   }

   @Override
   public void setMessage(CharSequence message) {
       super.setMessage(message);
   }

   @Override
   public boolean isShowing() {
       return super.isShowing();
   }

   @Override
   public void show() {
       super.show();
   }

   @Override
   public void hide() {
       super.hide();
   }

   @Override
   public void dismiss() {
       super.dismiss();
   }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }
}