package com.eone.bot.webapp;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetWebhookInfo;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetWebhookInfoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.eone.bot.webapp.JettyWebServerStarter.WEB_HOOK_PATH;

public class TelegramWebhookUtils {

    private static final Logger LOG = LogManager.getLogger(TelegramWebhookUtils.class);

    public static void setupWebHook(String publicIp, String certPath, TelegramBot telegramBot) {
        if (publicIp != null && certPath != null) {
            File certificateFile = new File(certPath);
            if (!certificateFile.canRead()) {
                LOG.warn("Can't read certificate :" + certPath);
            }
            SetWebhook request = new SetWebhook()
                    .url(publicIp + WEB_HOOK_PATH)
                    .certificate(certificateFile);
            BaseResponse setWebHookResponse = telegramBot.execute(request);
            LOG.info("Set WebHook Response :" + setWebHookResponse);
        } else {
            LOG.info("Certificate path | Webhook url are not specified. Set webhook stage is skipped.");
        }

        GetWebhookInfoResponse webHookInfoResponse = telegramBot.execute(new GetWebhookInfo());
        LOG.info("\n\n\n Check Telegram status. {} \n\n", webHookInfoResponse);
    }

}
