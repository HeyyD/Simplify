package com.tamk.hmhat.simplify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by hmhat on 13.3.2018.
 */

public class Buffer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.buffer, container, false);

        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.buffer);
        v.findViewById(R.id.buffer).setAnimation(rotation);

        return v;
    }
}
