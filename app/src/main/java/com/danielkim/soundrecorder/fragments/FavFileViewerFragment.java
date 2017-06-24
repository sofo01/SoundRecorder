package com.danielkim.soundrecorder.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.adapters.FavFileViewerAdapter;

import java.util.Observer;

/**
 * Created by Sofoklis on 24/06/2017.
 */

public class FavFileViewerFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "FavFileViewerFragment";

    private int position;
    private FavFileViewerAdapter mFavFileViewerAdapter;

    public static FavFileViewerFragment newInstance(int position) {
        FavFileViewerFragment f = new FavFileViewerFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        observer.startWatching();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_viewer, container, false);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFavFileViewerAdapter = new FavFileViewerAdapter(getActivity(), llm);
        mRecyclerView.setAdapter(mFavFileViewerAdapter);

        return v;
    }

    // This Observer seems redundant
    FileObserver observer =
            new FileObserver(Environment.getExternalStorageDirectory().toString()
                    + "/SoundRecorder") {
                @Override
                public void onEvent(int event, String file) {
                    if (event == FileObserver.DELETE) {

                        String filePath = Environment.getExternalStorageDirectory().toString()
                                + "/SoundRecorder" + file + "]";

                        Log.d(LOG_TAG, "File deleteed ["
                            + Environment.getExternalStorageDirectory().toString()
                            + "/SoundRecorder" + file + "]");

                        mFavFileViewerAdapter.removeOutOfApp(filePath);
                    }
                }
            };
}
