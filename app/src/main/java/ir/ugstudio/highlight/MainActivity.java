package ir.ugstudio.highlight;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import life.knowledge4.videotrimmer.utils.TrimVideoUtils;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button save;
    private Button getPosition;
    private VideoView videoView;
    private ImageView leftArrow;
    private ImageView rightArrow;

    private TennisSession currentSession;
    private MyPreferenceManager preferenceManager = null;

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSharedPreferencesAndActionBar();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 77);

        findViews();
        configureViews();
    }

    private void initSharedPreferencesAndActionBar() {
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        preferenceManager = MyPreferenceManager.getInstance(this);
    }

    private void findViews() {
        button = (Button) findViewById(R.id.button);
        getPosition = (Button) findViewById(R.id.get_position);
        save = (Button) findViewById(R.id.save_button);
        leftArrow = (ImageView) findViewById(R.id.left_arrow);
        rightArrow = (ImageView) findViewById(R.id.right_arrow);
        videoView = (VideoView) findViewById(R.id.videoview);
    }

    private void configureViews() {
        leftArrow.setOnClickListener(view -> videoView.seekTo(videoView.getCurrentPosition() - 5000));
        rightArrow.setOnClickListener(view -> videoView.seekTo(videoView.getCurrentPosition() + 5000));

        save.setOnClickListener(view -> {
            try {
                File file = new File(currentSession.getVideoURI().getPath().substring(
                        currentSession.getVideoURI().getPath().indexOf(":") + 1
                ));
                File folder = Environment.getExternalStorageDirectory();
                String finalPath = folder.getPath() + File.separator;
                File file2 = new File(finalPath);

                Log.d("TAG", "abcd file path " + file.getPath());
                Log.d("TAG", "abcd file222 path " + file2.getPath());

                TrimVideoUtils.startTrim(
                        file,
                        file.getPath(), 1000, 2000, new OnTrimVideoListener() {
                            @Override
                            public void getResult(Uri uri) {
                                Log.d("TAG", "result " + uri);
                            }

                            @Override
                            public void cancelAction() {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        getPosition.setOnClickListener(view -> {
            currentSession.addVideoPart(new VideoPart(videoView.getCurrentPosition(),
                    videoView.getCurrentPosition()));
            preferenceManager.putTennisSession(currentSession);
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_FROM_GALLERY) {
            Uri videoURI = data.getData();
            currentSession = preferenceManager.getTennisSession(videoURI);
            videoView.setVideoURI(videoURI);
            videoView.seekTo(currentSession.getLastPosition());
            videoView.start();
        }
    }
}