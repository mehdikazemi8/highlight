package ir.ugstudio.highlight;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class TennisSession {
    private Uri videoURI = null;
    private List<VideoPart> videoParts = new ArrayList<>();

    public TennisSession(Uri videoURI) {
        this.videoURI = videoURI;
    }

    public TennisSession(List<VideoPart> videoParts) {
        this.videoParts = videoParts;
    }

    public int getLastPosition() {
        int lastPosition = 0;
        for (VideoPart videoPart : videoParts) {
            lastPosition = Math.max(lastPosition, videoPart.getEnd());
        }
        return lastPosition;
    }

    public Uri getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(Uri videoURI) {
        this.videoURI = videoURI;
    }

    public void addVideoPart(VideoPart part) {
        videoParts.add(part);
    }

    public List<VideoPart> getVideoParts() {
        return videoParts;
    }

    public void setVideoParts(List<VideoPart> videoParts) {
        this.videoParts = videoParts;
    }
}
