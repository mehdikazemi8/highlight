package ir.ugstudio.highlight;

import java.util.ArrayList;
import java.util.List;

public class TennisSession {
    private String videoURI = null;
    private List<VideoPart> videoParts = new ArrayList<>();

    public TennisSession(String videoURI) {
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

    public String getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(String videoURI) {
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
