package com.galaxy.orbitsatellitevisualizer.connection;

import android.util.Log;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionBuildCommandUtility;


import java.util.Properties;

/**
 * This class is in charge of the liquid galaxy connection to send a file
 */
public class LGConnectionSendFile implements Runnable {

    private static final String TAG_DEBUG = "LGConnectionSendFile";

    private static LGConnectionSendFile instance = null;
    private String user;
    private String password;
    private String hostname;
    private int port;
    private String filePath;


    /**
     * Enforce private constructor and add the default values
     */
    private LGConnectionSendFile() {
        user = "lg";
        password = "1234";
        this.hostname = "192.168.20.28";
        this.port = 22;
    }

    public static LGConnectionSendFile getInstance() {
        if (instance == null) {
            instance = new LGConnectionSendFile();
        }
        return instance;
    }

    public void setData(String user, String password, String hostname, int port) {
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    public void startConnection() {
        new Thread(instance).start();
    }

    @Override
    public void run() {
        if(filePath != null) sendFile();
        Log.w(TAG_DEBUG, "Filepath in LGConn: " + filePath);
    }

    private void sendFile(){
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, hostname, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect(Integer.MAX_VALUE);
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.put(filePath, ActionBuildCommandUtility.RESOURCES_FOLDER_PATH);
            Log.w(TAG_DEBUG, "Sent to the Galaxy");
        } catch (JSchException | SftpException e) {
            Log.w(TAG_DEBUG, "ERROR:" + e.getMessage());
        }
    }

    public void addPath(String path) {
        filePath = path;
    }

}
