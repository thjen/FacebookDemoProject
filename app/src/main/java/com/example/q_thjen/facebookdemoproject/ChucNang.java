package com.example.q_thjen.facebookdemoproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChucNang extends AppCompatActivity {

    Button bt_shareUrl, bt_shareImage, bt_pickVideo, bt_shareVideo;
    EditText et_title, et_description, et_url;
    ImageView imageShared;
    VideoView videoView;

    int IMAGE_PICKED = 1;
    int VIDEO_PICKED = 2;

    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    Bitmap bitmap;

    Uri selectVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_nang);

        bt_shareUrl = findViewById(R.id.bt_shareUrl);
        bt_shareImage = findViewById(R.id.bt_shareImage);
        bt_shareVideo = findViewById(R.id.bt_shareVideo);
        bt_pickVideo = findViewById(R.id.bt_pickVideo);
        et_description = findViewById(R.id.et_description);
        et_title = findViewById(R.id.et_title);
        et_url = findViewById(R.id.et_url);
        imageShared = findViewById(R.id.iv_shared);
        videoView = findViewById(R.id.videoView);

        shareDialog = new ShareDialog(this);
        bt_shareUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ShareDialog.canShow(ShareLinkContent.class)) {
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle(et_title.getText().toString())
                            .setContentDescription(et_description.getText().toString())
                            .setContentUrl(Uri.parse(et_url.getText().toString()))
                    .build();
                }
                shareDialog.show(shareLinkContent);
            }
        });

        imageShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICKED);
            }
        });

        bt_shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap).build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialog.show(content);
            }
        });

        bt_pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, VIDEO_PICKED);

            }
        });

        bt_shareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareVideo shareVideo = null;
                shareVideo = new ShareVideo.Builder()
                            .setLocalUrl(selectVideo)
                            .build();

                ShareVideoContent content = new ShareVideoContent.Builder()
                        .setVideo(shareVideo)
                        .build();

                shareDialog.show(content);
                videoView.stopPlayback();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == IMAGE_PICKED && resultCode == RESULT_OK && data != null ) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageShared.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        if ( requestCode == VIDEO_PICKED && resultCode == RESULT_OK && data != null ) {

            selectVideo = data.getData ();
            videoView.setVideoURI(selectVideo);
            videoView.start();
        }

    }


}
