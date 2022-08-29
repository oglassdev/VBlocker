package com.vblocker.Server;

import com.vblocker.Messages;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Set;

public class TerminalThread extends Thread {
    private final Server server;
    public TerminalThread(Server server) {
        this.server = server;
    }
    @Override
    public void run() {
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            DefaultParser parser = new DefaultParser();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(parser)
                    .build();

            while (true) {
                try {
                    String line = reader.readLine("> ");
                    switch (line.split(" ")[0].toLowerCase(Locale.ENGLISH)) {
                        case "stop":
                            Messages.log("Stopping server...");
                            terminal.close();
                            server.stop();
                            return;
                        case "list": {
                            Set<Socket> connections = server.getConnections();
                            if (connections.size() == 0) {
                                Messages.log("There are no active connections.");
                                break;
                            }
                            Messages.log("Current connections:");
                            connections.forEach(socket -> Messages.log(" " + socket.getInetAddress() + ":" + socket.getPort()));
                            break; }
                        case "send": {
                            DataOutputStream dataOut = new DataOutputStream(soc.getOutputStream());
                            dataOut.writeUTF("");
                            break; }
                        default:
                            Messages.log("Unknown command " + line.split(" ")[0] + ".");
                            break;
                    }
                } catch (UserInterruptException e) {
                    terminal.close();
                    server.stop();
                    return;
                } catch (EndOfFileException e) {
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
