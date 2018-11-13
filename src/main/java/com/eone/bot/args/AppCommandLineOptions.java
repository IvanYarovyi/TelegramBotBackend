package com.eone.bot.args;

import org.apache.commons.cli.*;

public class AppCommandLineOptions {

    private Options options = new Options();
    private CommandLine cmd;
    private CommandLineParser parser = new DefaultParser();
    private HelpFormatter formatter = new HelpFormatter();

    public AppCommandLineOptions(String[] args) {
        initOptions();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("telegram-bot-backend", options);
            System.exit(1);
        }
    }

    private void initOptions() {
        Option opt1 = new Option("t", OPTION.TOKEN.toString(), true, "Telegram bot token");
        opt1.setRequired(true);
        options.addOption(opt1);

        Option opt2 = new Option("c", OPTION.CERTIFICATE.toString(), true, "Path to certificate");
        opt2.setRequired(false);
        options.addOption(opt2);

        Option opt3 = new Option("h", OPTION.WEB_HOOK.toString(), true, "Web hook url");
        opt3.setRequired(false);
        options.addOption(opt3);

        Option opt4 = new Option("p", OPTION.SERVER_PORT.toString(), true, "Web server port");
        opt4.setRequired(false);
        options.addOption(opt4);
    }

    public String getOption(OPTION option) throws Exception {
        return cmd.getOptionValue(option.toString());
    }

}
