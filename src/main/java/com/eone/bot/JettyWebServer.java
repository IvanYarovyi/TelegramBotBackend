package com.eone.bot;

import com.eone.bot.update.HelloProcessor;
import com.eone.bot.web.WebHookHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

import java.io.File;

public class JettyWebServer {

    public static void setWebHook(String webHook, String certPath, TelegramBot telegramBot) {
        File certificateFile = new File(certPath);
        if (!certificateFile.canRead()) {
            System.out.println("Can't read certificate :" + certPath);
        }
        SetWebhook request = new SetWebhook()
                .url(webHook)
                .certificate(certificateFile);
        BaseResponse setWebHookResponse = telegramBot.execute(request);
        System.out.println("Set WebHook Response :" + setWebHookResponse);
    }

    public static void start(int port, TelegramBot telegramBot) throws Exception {
        Server server = new Server(port);

        ContextHandler context = new ContextHandler();
        context.setContextPath("/webHook");
        context.setResourceBase(".");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        server.setHandler(context);

        context.setHandler(new WebHookHandler(new HelloProcessor(telegramBot)));

        server.start();
        server.join();
    }

}
