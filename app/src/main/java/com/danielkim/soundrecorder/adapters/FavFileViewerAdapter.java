package com.danielkim.soundrecorder.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danielkim.soundrecorder.DBHelper;
import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.RecordingItem;
import com.danielkim.soundrecorder.fragments.PlaybackFragment;
import com.danielkim.soundrecorder.listeners.OnDatabaseChangedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sofoklis on 24/06/2017.
 */

public class FavFileViewerAdapter extends RecyclerView.Adapter<FavFileViewerAdapter.FavRecordingsViewHolder>
        implements OnDatabaseChangedListener {

    private static final String LOG_TAG = "FavFileViewerAdapter";

    private DBHelper mDatabase;

    RecordingItem item;
    Context mContext;
    LinearLayoutManager llm;

    public FavFileViewerAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super();
        mContext = context;
        mDatabase = new DBHelper(mContext);
        //mDatabase.setOnDatabaseChangedListener(this);
        llm = linearLayoutManager;
    }

    public static class FavRecordingsViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vLength;
        protected TextView vDateAdded;
        protected View cardView;

        public FavRecordingsViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.file_name_text);
            vLength = (TextView) v.findViewById(R.id.file_length_text);
            vDateAdded = (TextView) v.findViewById(R.id.file_date_added_text);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    @Override
    public FavRecordingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

                mContext = parent.getContext();

        FavRecordingsViewHolder holder = new FavRecordingsViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FavRecordingsViewHolder holder, int position) {
        item = getItem(position);

        if (item.getFavourite() == 1) {
            long itemDuration = item.getLength();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            long seconds = TimeUnit.MICROSECONDS.toSeconds(itemDuration)
                    - TimeUnit.MINUTES.toSeconds(minutes);

            holder.vName.setText(item.getName());
            holder.vLength.setText(String.format("%02d:%02d", minutes, seconds));
            holder.vDateAdded.setText(
                    DateUtils.formatDateTime(
                            mContext,
                            item.getTime(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
                    )
            );

            holder.cardView.setOnClickListener(new  View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        PlaybackFragment playbackFragment =
                                new PlaybackFragment().newInstance(getItem(holder.getPosition()));

                        FragmentTransaction transaction = ((FragmentActivity) mContext)
                                .getSupportFragmentManager()
                                .beginTransaction();

                        playbackFragment.show(transaction, "dialog_playback");

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "exception", e);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatabase.getCount();
    }

    public RecordingItem getItem(int position) {
        return mDatabase.getItemAt(position);
    }

    @Override
    public void onNewDatabaseEntryAdded() {
        notifyItemInserted(getItemCount() - 1);
        llm.scrollToPosition(getItemCount() - 1);
    }

    @Override
    public void onDatabaseEntryRenamed() {
        System.out.println("onDatabaseEntryRenamed");
    }

    public void removeOutOfApp(String filePath) {
    }
}