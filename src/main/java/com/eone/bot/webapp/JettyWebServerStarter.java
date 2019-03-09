package com.eone.bot.webapp;

import com.eone.bot.telegram.UpdateProcessor;
import com.pengrad.telegrambot.TelegramBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class JettyWebServerStarter {
    private static final Logger LOG = LogManager.getLogger(JettyWebServerStarter.class);
    public static final String WEB_HOOK_PATH = "/botUpdatesWebHook/";
    public static final int DEFAULT_PORT = 8080;

    public static int getWebServerPort(String portStr) {
        if (portStr != null) {
            try {
                return Integer.parseInt(portStr);
            } catch (Exception e) {
                LOG.warn("Could not parse port: " + portStr);
            }
        }
        return DEFAULT_PORT;
    }

    public static void start(int port, TelegramBot telegramBot, UpdateProcessor  updateProcessor) throws Exception {
        Server server = new Server(port);

        ContextHandler context = new ContextHandler();
        context.setContextPath(WEB_HOOK_PATH);
        context.setResourceBase(".");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        server.setHandler(context);

        context.setHandler(new WebHookHandler(updateProcessor));

        server.start();
        server.join();
    }

}
