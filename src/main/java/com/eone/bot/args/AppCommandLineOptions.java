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
            formatter.printHelp(100,
                    " bot-backend-all.jar OPTIONS...",
                    "only option -c is mandatory",
                    options,
                    "example: java -jar bot-backend-all.jar -t=123456789:AAA-sssss_D8oXaqBBEoaaaafQhdsddsdql -p=8082");

            System.exit(1);
        }
    }

    private void initOptions() {
        Option opt1 = new Option("t", OPTION.TOKEN.toString(), true, "Telegram bot token");
        opt1.setRequired(true);
        options.addOption(opt1);

        Option opt2 = new Option("c", OPTION.CERTIFICATE.toString(), true, "Path to certificate. Certificate can either be verified or self-signed");
        opt2.setRequired(false);
        options.addOption(opt2);

        Option opt3 = new Option("h", OPTION.WEB_HOOK.toString(), true, "Webhook url for Telegram (public IP)");
        opt3.setRequired(false);
        options.addOption(opt3);

        Option opt4 = new Option("p", OPTION.SERVER_PORT.toString(), true, "Web server port for this application");
        opt4.setRequired(false);
        options.addOption(opt4);
    }

    public String getOption(OPTION option) throws Exception {
        return cmd.getOptionValue(option.toString());
    }

}
