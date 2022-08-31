package com.galaxy.gcmobile;
import org.tensorflow.lite.Interpreter;
import android.os.Bundle;
import java.util.ArrayList;
import java.lang.Integer;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.media.AudioManager;
import android.content.res.AssetFileDescriptor;
import android.content.Context;
import org.tensorflow.lite.Interpreter;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {

  private AudioManager audioManager;


  private static final String CHANNEL = "gcmobile";

  protected Interpreter tflite;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //GeneratedPluginRegistrant.registerWith(this);
    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    try{
      tflite = new Interpreter(loadModelFile());
    } catch (Exception e){
      System.out.println(e);
    }

    new MethodChannel( getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
      new MethodCallHandler() {
        @Override
        public void onMethodCall(MethodCall call, Result result) {
          int pose;
          if (call.method.equals("identifyPose")) {
            ArrayList<Double> args  = new ArrayList<>();
            args = call.argument("arg");
            pose = comparePose(args);
            result.success(pose);
          } else {
            result.notImplemented();
          }
        }
      }
    );
  }

  Integer comparePose(ArrayList<Double> input_data){
    float max = 0;
    int result = 0;
    float floatArray[] = new float[input_data.size()];
    for(int i = 0;i<input_data.size();i++)
      floatArray[i] = (float) input_data.get(i).floatValue();

    // int i =0;
    // for(Float e : input_data){
    //     floatArray[0][i] = e;
    //     i++;
    // }
    // System.out.println(floatArray);
    float [][] output_datas= new float[1][9];
    tflite.run(floatArray, output_datas);

    for(int i = 0; i < output_datas[0].length;i++){
      if (output_datas[0][i] > max){
        max = output_datas[0][i];
        result = i;
      }
    }
    return result;
  }

  private MappedByteBuffer loadModelFile() throws IOException {
    String MODEL_ASSETS_PATH = "model.tflite";
    AssetFileDescriptor assetFileDescriptor = getAssets().openFd(MODEL_ASSETS_PATH) ;
    FileInputStream fileInputStream = new FileInputStream( assetFileDescriptor.getFileDescriptor() ) ;
    FileChannel fileChannel = fileInputStream.getChannel() ;
    long startoffset = assetFileDescriptor.getStartOffset() ;
    long declaredLength = assetFileDescriptor.getDeclaredLength() ;
    return fileChannel.map( FileChannel.MapMode.READ_ONLY , startoffset , declaredLength ) ;
  }
}
