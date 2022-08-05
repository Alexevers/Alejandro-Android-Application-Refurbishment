package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.MainActivityLG;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POI;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation.POIController;

public class VisitPoiTask extends AsyncTask<Void, Void, String> {
    String command;
    POI currentPoi;
    boolean rotate;
    int rotationAngle = 10;
    int rotationFactor = 1;
    boolean changeVelocity = false;
    private ProgressDialog dialog;
    Activity activity;
    Context context;

    public VisitPoiTask(String command, POI currentPoi, boolean rotate, Activity activity, Context context) {
        this.command = command;
        this.currentPoi = currentPoi;
        this.rotate = rotate;
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String logos_slave, homeless_slave, local_statistics_slave, global_statistics_slave, live_overview_homeless;
        String hostname;
        SharedPreferences preferences;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        logos_slave = preferences.getString("logos_preference","");
        homeless_slave = preferences.getString("homeless_preference","");
        local_statistics_slave = preferences.getString("local_preference","");
        global_statistics_slave = preferences.getString("global_preference","");
        live_overview_homeless = preferences.getString("live_overview_homeless", "");
        hostname = preferences.getString("SSH-IP", "");




        if (dialog == null) {
            dialog = new ProgressDialog(context);
            String message = context.getResources().getString(R.string.viewing) + " " + this.currentPoi.getName() + " " + context.getResources().getString(R.string.inLG);
            dialog.setMessage(message);
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);


            //Buton positive => more speed
            //Button neutral => less speed
            if (this.rotate) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.speedx2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, we after define the onclick
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getResources().getString(R.string.speeddiv2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, we after define the onclick
                    }
                });
            }


            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    POIController.cleanKmls();
                    POIController.cleanKmlSlave(homeless_slave);
                    POIController.cleanKmlSlave(local_statistics_slave);
                    POIController.cleanKmlSlave(global_statistics_slave);
                    cancel(true);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });


            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_forward_black_36dp, 0, 0);
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_rewind_black_36dp, 0, 0);
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeVelocity = true;
                    rotationFactor = rotationFactor * 2;

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(context.getResources().getString(R.string.speedx4));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setText(context.getResources().getString(R.string.speeddiv2));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(true);
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_rewind_black_36dp, 0, 0);

                    if (rotationFactor == 4) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            });
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeVelocity = true;
                    rotationFactor = rotationFactor / 2;

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(context.getResources().getString(R.string.speedx2));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setText(context.getResources().getString(R.string.speeddiv4));
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_forward_black_36dp, 0, 0);

                    if (rotationFactor == 1) {
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(false);
                    }
                }
            });
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            Session session = LGUtils.getSession(activity);

            //We fly to the point
            LGUtils.setConnectionWithLiquidGalaxy(session, command, activity);

            //If rotation button is pressed, we start the rotation
            if (this.rotate) {

                boolean isFirst = true;

                while (!isCancelled()) {
                    session.sendKeepAliveMsg();

                    for (int i = 0; i <= (360 - this.currentPoi.getHeading()); i += (this.rotationAngle * this.rotationFactor)) {

                        String commandRotate = "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt>" +
                                "<longitude>" + this.currentPoi.getLongitude() + "</longitude>" +
                                "<latitude>" + this.currentPoi.getLatitude() + "</latitude>" +
                                "<altitude>" + this.currentPoi.getAltitude() + "</altitude>" +
                                "<heading>" + (this.currentPoi.getHeading() + i) + "</heading>" +
                                "<tilt>" + this.currentPoi.getTilt() + "</tilt>" +
                                "<range>" + this.currentPoi.getRange() + "</range>" +
                                "<gx:altitudeMode>" + this.currentPoi.getAltitudeMode() + "</gx:altitudeMode>" +
                                "</LookAt>' > /tmp/query.txt";


                        LGUtils.setConnectionWithLiquidGalaxy(session, commandRotate, activity);
                        session.sendKeepAliveMsg();

                        if (isFirst) {
                            isFirst = false;
                            Thread.sleep(7000);
                        } else {
                            Thread.sleep(4000);
                        }
                    }
                }
            }

            return "";

        } catch (JSchException e) {
            this.cancel(true);
            if (dialog != null) {
                dialog.dismiss();
            }
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, context.getResources().getString(R.string.error_galaxy), Toast.LENGTH_LONG).show();
                }
            });

            return null;
        } catch (InterruptedException e) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, context.getResources().getString(R.string.visualizationCanceled), Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        if (success != null) {
            if (dialog != null) {
                dialog.hide();
                dialog.dismiss();
            }
        }
    }
}

