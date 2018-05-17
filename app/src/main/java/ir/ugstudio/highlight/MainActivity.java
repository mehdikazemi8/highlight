package ir.ugstudio.highlight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button getPosition;
    private VideoView videoView;

    private TennisSession currentSession;
    private MyPreferenceManager preferenceManager = null;

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        preferenceManager = MyPreferenceManager.getInstance(this);

        button = (Button) findViewById(R.id.button);
        getPosition = (Button) findViewById(R.id.get_position);
        videoView = (VideoView) findViewById(R.id.videoview);

        getPosition.setOnClickListener(view -> {
            Log.d("TAG", "abcd current " + videoView.getCurrentPosition());

            currentSession.addVideoPart(new VideoPart(videoView.getCurrentPosition(),
                    videoView.getCurrentPosition()));
            preferenceManager.putTennisSession(currentSession);
        });

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();

                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_FROM_GALLERY) {
            Uri videoURI = data.getData();

            currentSession = preferenceManager.getTennisSession(videoURI.toString());
            videoView.setVideoURI(videoURI);
            videoView.seekTo(currentSession.getLastPosition());
            videoView.start();
        }

    }
}