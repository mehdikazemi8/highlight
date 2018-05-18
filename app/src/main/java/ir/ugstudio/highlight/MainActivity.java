package ir.ugstudio.highlight;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import life.knowledge4.videotrimmer.utils.TrimVideoUtils;

public class MainActivity extends AppCompatActivity {

    private Button farzadSave;
    private Button mehdiSave;
    private Button masoudSave;
    private Button samanSave;

    private Button button;
    private Button setStart;
    private VideoView videoView;
    private ImageView leftArrow;
    private ImageView rightArrow;

    private TextView startPosition;

    private int startPositionMili;
    private int endPositionMili;

    private TennisSession currentSession;
    private MyPreferenceManager preferenceManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSharedPreferencesAndActionBar();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 77);
        findViews();
        configureViews();
        openSelectVideoFragment();
    }

    private void openSelectVideoFragment() {
        PickVideoFragment fragment = PickVideoFragment.newInstance(onNameSelectedListener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    private OnNameSelectedListener onNameSelectedListener = name -> {
        getSupportFragmentManager().popBackStack();

        currentSession = preferenceManager.getTennisSession(name);
        videoView.setVideoURI(Uri.parse(name));
        videoView.seekTo(currentSession.getLastPosition());
        videoView.start();
    };

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
        setStart = (Button) findViewById(R.id.set_start);
        leftArrow = (ImageView) findViewById(R.id.left_arrow);
        rightArrow = (ImageView) findViewById(R.id.right_arrow);
        videoView = (VideoView) findViewById(R.id.videoview);

        startPosition = findViewById(R.id.start_position);

        farzadSave = findViewById(R.id.farzad_save);
        mehdiSave = findViewById(R.id.mehdi_save);
        masoudSave = findViewById(R.id.masoud_save);
        samanSave = findViewById(R.id.saman_save);
    }

    private void configureViews() {
        leftArrow.setOnClickListener(view -> videoView.seekTo(videoView.getCurrentPosition() - 5000));
        rightArrow.setOnClickListener(view -> videoView.seekTo(videoView.getCurrentPosition() + 5000));

        farzadSave.setOnClickListener(view -> saveVideo(((Button) view).getText().toString()));
        mehdiSave.setOnClickListener(view -> saveVideo(((Button) view).getText().toString()));
        masoudSave.setOnClickListener(view -> saveVideo(((Button) view).getText().toString()));
        samanSave.setOnClickListener(view -> saveVideo(((Button) view).getText().toString()));

        setStart.setOnClickListener(view -> {
//            currentSession.addVideoPart(new VideoPart(videoView.getCurrentPosition(),
//                    videoView.getCurrentPosition()));
//            preferenceManager.putTennisSession(currentSession);

            startPositionMili = videoView.getCurrentPosition();
            startPosition.setText(getTimeInMinute(startPositionMili) + "  -  " + startPositionMili);
        });

        button.setOnClickListener(view -> openSelectVideoFragment());
    }

    private String getTimeInMinute(int currentTime) {
        int mili = currentTime % 1000;
        currentTime = currentTime / 1000;

        int second = currentTime % 60;
        int minute = currentTime / 60;
        int hour = currentTime / 3600;

        return getString(R.string.template_time,
                (hour < 10 ? "0" : hour),
                (minute < 10 ? "0" : minute),
                (second < 10 ? "0" : second),
                "" + mili
        );
    }

    private void saveVideo(String playerName) {
        endPositionMili = videoView.getCurrentPosition();

        currentSession.addVideoPart(new VideoPart(startPositionMili, endPositionMili));
        preferenceManager.putTennisSession(currentSession);

        try {
            File file = new File(currentSession.getVideoPath());

            File playerDir = new File(Environment.getExternalStorageDirectory() +
                    getString(R.string.template_folder_name, currentSession.getVideoDate(), playerName));

            if (!playerDir.exists())
                playerDir.mkdirs();
            else
                Log.d("error", "dir. already exists");

            File file2 = new File(playerDir.getPath() + File.separator + "/" + playerName + "_");

            Log.d("TAG", "abcd file path " + file.getPath());
            Log.d("TAG", "abcd file222 path " + file2.getPath());

            TrimVideoUtils.startTrim(
                    file,
                    file2.getPath(), startPositionMili, endPositionMili, new OnTrimVideoListener() {
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
    }
}