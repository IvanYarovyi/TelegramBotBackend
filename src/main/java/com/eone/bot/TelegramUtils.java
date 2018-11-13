package com.eone.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetWebhookInfo;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetWebhookInfoResponse;

import java.io.File;

public class TelegramUtils {

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

    public static void setupWebHook(String webHook, String certPath, TelegramBot telegramBot) {
        if (webHook != null & certPath != null) {
            setWebHook(webHook, certPath, telegramBot);
        }
        GetWebhookInfoResponse webHookInfoResponse = telegramBot.execute(new GetWebhookInfo());
        System.out.println(webHookInfoResponse);
    }

}
