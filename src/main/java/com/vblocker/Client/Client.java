package com.vblocker.server;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class Server {
    public Server() throws IOException {
        Terminal terminal = TerminalBuilder.builder().build();
        DefaultParser parser = new DefaultParser();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .build();
        while (true) {
            try {
                String line = reader.readLine("> ");
                if (line.equals("close")) {
                    terminal.close();
                    return;
                }
                System.out.println(line);
            }
            catch (UserInterruptException e) {
                terminal.close();
                return;
            }
            catch (EndOfFileException e) {
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
