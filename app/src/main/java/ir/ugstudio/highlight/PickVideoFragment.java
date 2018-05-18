package ir.ugstudio.highlight;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PickVideoFragment extends Fragment {

    private OnNameSelectedListener onNameSelectedListener;

    public static PickVideoFragment newInstance(OnNameSelectedListener listener) {
        PickVideoFragment fragment = new PickVideoFragment();
        fragment.onNameSelectedListener = listener;
        return fragment;
    }

    private RecyclerView videos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        VideoAdapter adapter = new VideoAdapter(getAllVideoPath(getActivity()), onNameSelectedListener);
        videos.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        videos.setAdapter(adapter);
    }

    private void findViews(View view) {
        videos = view.findViewById(R.id.videos);
    }

    private List<String> getAllVideoPath(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        List<String> pathArrList = new ArrayList<String>();
        //int vidsCount = 0;
        if (cursor != null) {
            //vidsCount = cursor.getCount();
            //Log.d(TAG, "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                if (name.contains("/Camera/")) pathArrList.add(name);
                //Log.d(TAG, cursor.getString(0));
            }
            cursor.close();
        }

        return pathArrList;
    }
}
