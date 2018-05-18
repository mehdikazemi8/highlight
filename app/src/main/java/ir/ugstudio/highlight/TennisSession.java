package ir.ugstudio.highlight;

import java.util.ArrayList;
import java.util.List;

public class TennisSession {
    private String videoPath = null;
    private List<VideoPart> videoParts = new ArrayList<>();

    public TennisSession(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getLastPosition() {
        int lastPosition = 0;
        for (VideoPart videoPart : videoParts) {
            lastPosition = Math.max(lastPosition, videoPart.getEnd());
        }
        return lastPosition;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoURI) {
        this.videoPath = videoPath;
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
