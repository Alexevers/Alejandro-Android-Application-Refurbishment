package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class DonateFragment extends Fragment  implements View.OnClickListener {

    /*Buttons*/
    private MaterialButton donateBtn;
    private MaterialButton videoBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        donateBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.donate_button:
                startActivity(new Intent(getActivity(), Payment.class));
                break;

            case R.id.view_video_button:
                startActivity(new Intent(getActivity(), YouTubePlayer.class));
                break;
        }

    }

    private void initViews(View view){
        donateBtn = view.findViewById(R.id.donate_button);
        videoBtn = view.findViewById(R.id.view_video_button);
    }

}
