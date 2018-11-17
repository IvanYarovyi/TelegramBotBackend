package com.eone.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetWebhookInfo;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetWebhookInfoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.eone.bot.web.JettyWebServerStarter.WEB_HOOK_PATH;

public class TelegramUtils {

    private static final Logger LOG = LogManager.getLogger(TelegramUtils.class);

    public static void setupWebHook(String webHook, String certPath, TelegramBot telegramBot) {
        if (webHook != null && certPath != null) {
            File certificateFile = new File(certPath);
            if (!certificateFile.canRead()) {
                LOG.warn("Can't read certificate :" + certPath);
            }
            SetWebhook request = new SetWebhook()
                    .url(webHook + WEB_HOOK_PATH)
                    .certificate(certificateFile);
            BaseResponse setWebHookResponse = telegramBot.execute(request);
            LOG.info("Set WebHook Response :" + setWebHookResponse);
        } else {
            LOG.info("Certificate path | Webhook url are not specified. Set webhook stage is skipped.");
        }

        GetWebhookInfoResponse webHookInfoResponse = telegramBot.execute(new GetWebhookInfo());
        LOG.info("Check Telegram status. {}", webHookInfoResponse);
    }

}
