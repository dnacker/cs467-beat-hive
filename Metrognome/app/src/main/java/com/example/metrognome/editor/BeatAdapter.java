package com.example.metrognome.editor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.metrognome.R;
import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Rhythm;

public class BeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = BeatAdapter.class.getSimpleName();
    private final Rhythm rhythm;
    private final LayoutInflater inflater;
    private final boolean isEditable;

    public BeatAdapter(Context context, Rhythm rhythm, boolean isEditable) {
        this.inflater = LayoutInflater.from(context);
        this.rhythm = rhythm;
        this.isEditable = isEditable;
    }

    @Override
    public BeatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.beat, parent, false);
        BeatViewHolder holder = new BeatViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BeatViewHolder) {
            BeatViewHolder viewHolder = (BeatViewHolder) holder;
            final Beat beat = rhythm.getBeatAt(position);
            viewHolder.beat = beat;
            viewHolder.measure = rhythm.getMeasureForBeatAt(position);
            viewHolder.rhythm = rhythm;
            viewHolder.isEditable = this.isEditable;
            viewHolder.init();
        }
    }

    @Override
    public int getItemCount() {
        return rhythm.getBeatCount();
    }
}
