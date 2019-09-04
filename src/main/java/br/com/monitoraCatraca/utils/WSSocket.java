package br.com.monitoraCatraca.utils;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.faces.bean.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/ws/{identifier}")
public class WSSocket {

    static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    //private static final Map<String, Date> sendRow = Collections.synchronizedMap(new LinkedHashMap<>());
    public WSSocket() {

    }

    // DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @OnOpen
    public void open(Session session, @PathParam("identifier") String identifier) {
        clients.add(session);
    }

    @OnClose
    public void close(Session session) {
        clients.remove(session);
    }

    // @OnMessage
    public static void send(String identifier) {
        send(identifier, "1");
    }

    public static void send(String identifier, String text) {
        for (Session sess : clients) {
            if (sess.getRequestURI().getPath().contains(identifier)) {
                try {
                    sess.getBasicRemote().sendText(text);
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
        }
    }

    @OnError
    public void error(Throwable t) throws Throwable {
        // Most likely cause is a user closing their browser. Check to see if
        // the root cause is EOF and if it is ignore it.
        // Protect against infinite loops.
        int count = 0;
        Throwable root = t;
        while (root.getCause() != null && count < 20) {
            root = root.getCause();
            count++;
        }
        if (root instanceof EOFException) {
            // Assume this is triggered by the user closing their browser and
            // ignore it.
        } else {
            throw t;
        }
    }
}
