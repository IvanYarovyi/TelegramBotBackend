package com.eone.bot.web;

import com.eone.bot.update.UpdateProcessor;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class WebHookHandler extends AbstractHandler {

    private UpdateProcessor updateProcessor;

    public WebHookHandler(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String body = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Request BODY: \n" + body);
        try {
            Update update = BotUtils.parseUpdate(body);
            updateProcessor.processUpdate(update);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Request processed</h1>");
    }

}
