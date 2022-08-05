package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection;

import android.os.Handler;
import android.os.Looper;

public class LGCommand {

    public static final short NON_CRITICAL_MESSAGE = 0;
    public static final short CRITICAL_MESSAGE = 1;

    private final String command;
    private final short priorityType;
    private final Listener listener;

    /** Callback interface for delivering parsed responses. */
    public interface Listener {
        /** Called when a response is received. */
        void onResponse(String response);
    }

    public LGCommand(String command, short priorityType, Listener listener) {
        this.command = command;
        this.priorityType = priorityType;
        this.listener = listener;
    }

    String getCommand() {
        return command;
    }

    short getPriorityType() {
        return priorityType;
    }

    void doAction(String response) {
        if(listener != null)
            new Handler(Looper.getMainLooper()).post(() -> listener.onResponse(response));
    }
}
