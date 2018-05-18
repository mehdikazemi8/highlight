package ir.ugstudio.highlight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context context;
    private List<String> items;
    private OnNameSelectedListener onNameSelectedListener;

    public VideoAdapter(List<String> items, OnNameSelectedListener listener) {
        this.items = items;
        this.onNameSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.template_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position);

        holder.name.setText(name.substring(name.lastIndexOf("/") + 1));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ConstraintLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.root_view);

            rootView.setOnClickListener(view -> onNameSelectedListener.onNameSelected(items.get(getAdapterPosition())));
        }
    }
}
