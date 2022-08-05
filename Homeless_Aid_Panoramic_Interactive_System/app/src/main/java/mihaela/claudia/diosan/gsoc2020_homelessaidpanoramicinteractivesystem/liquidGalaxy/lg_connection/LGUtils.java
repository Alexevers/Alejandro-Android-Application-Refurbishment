package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LGUtils {

    private static Session session = null;

    public static Session getSession(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String user = prefs.getString("SSH-USER", "lg");
        String password = prefs.getString("SSH-PASSWORD", "lqgalaxy");
        String hostname = prefs.getString("SSH-IP", "192.168.1.76");
        int port = Integer.parseInt(prefs.getString("SSH-PORT", "22"));

        JSch jsch = new JSch();

        try {
            if (session == null || !session.isConnected()) {
                session = jsch.getSession(user, hostname, port);
                session.setPassword(password);

                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect(Integer.MAX_VALUE);
            } else {
                session.sendKeepAliveMsg();
                return session;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    public static String setConnectionWithLiquidGalaxy(Session session, String command, Activity activity) throws JSchException, IOException {

        if (session == null || !session.isConnected()) {
            session = getSession(activity);
        }

        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        channelssh.setOutputStream(baos);

        channelssh.setCommand(command);
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }


    public static void closeSession(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }


}
