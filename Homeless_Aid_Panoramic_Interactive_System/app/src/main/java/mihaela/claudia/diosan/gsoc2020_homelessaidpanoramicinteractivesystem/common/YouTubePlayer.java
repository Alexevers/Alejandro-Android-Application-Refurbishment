package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;

public class YouTubePlayer extends YouTubeBaseActivity implements com.google.android.youtube.player.YouTubePlayer.OnInitializedListener {

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);

      fullScreenActivity();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_video);
        youTubePlayerView.initialize(getString(R.string.API_KEY), this);

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void fullScreenActivity() {
        //make the activity on full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer youTubePlayer, boolean restored) {

        if (!restored) {
            youTubePlayer.cueVideo("39qrOzxxqeY");
        }


    }

    @Override
    public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, 1).show();
            Toast.makeText(getApplicationContext(), youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){
            getYoutTubePlayerProvider().initialize(getString(R.string.API_KEY), this);
        }
    }

    private com.google.android.youtube.player.YouTubePlayer.Provider getYoutTubePlayerProvider() {

        return youTubePlayerView;
    }

}
