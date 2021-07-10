package com.aakash.videoopener;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextUrl;
    String url;
    String clipBoardText;
    Toast mToast = null;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        mContext = getApplicationContext();
//        mToast = Toast.makeText(mContext, null, Toast.LENGTH_SHORT);
        editTextUrl = findViewById(R.id.editTextUrl);

        FloatingActionButton fab_clear = findViewById(R.id.fab_clear);
        FloatingActionButton fab_paste = findViewById(R.id.fab_paste);
        FloatingActionButton fab_open = findViewById(R.id.fab_open);
        Button button_clear = findViewById(R.id.button_clear);

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        fab_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("abc", "");
                clipboardManager.setPrimaryClip(clip);
                showToast(mContext,"Clipboard Cleared");
            }
        });

        fab_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this if statement is required because when clipboard is empty, getPrimaryClip()
                // returns null, and in such case getPrimaryClip().getItem() throws
                // null pointer exception
                if(clipboardManager.getPrimaryClip() == null) {
                    showToast(mContext,"Clipboard is Empty");
                    return;
                }
                clipBoardText = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString().trim();
                if(clipBoardText.length() < 1) {
                    showToast(mContext,"Clipboard is Empty");
                }else{
                    editTextUrl.setText(clipBoardText);
                    showToast(mContext,"Pasted from Clipboard");
                }
            }
        });

        fab_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = String.valueOf(editTextUrl.getText()).trim();
                if(url.isEmpty() || url.length() < 5) {
                    showToast(mContext,"Please Enter a valid URL");
                    return;
                }else if (!url.startsWith("https://") && !url.startsWith("http://")) {
                   url = "https://" + url;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUrl.setText("");
            }
        });
    }

    void showToast(Context context,String text){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    boolean pressedBackOnce = false;
    @Override
    public void onBackPressed() {
        if (pressedBackOnce) {
            super.onBackPressed();
        } else {
            pressedBackOnce = true;
            showToast(getApplicationContext(), "Press BACK Again to EXIT");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedBackOnce = false;
                }
            }, 2000);
        }
    }
}