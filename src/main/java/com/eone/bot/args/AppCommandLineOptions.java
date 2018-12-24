package com.eone.bot.args;

import com.eone.Main;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class AppCommandLineOptions {
    private static final Logger LOG = LogManager.getLogger(AppCommandLineOptions.class);

    private Options options = new Options();
    private CommandLine cmd;
    private CommandLineParser parser = new DefaultParser();
    private HelpFormatter formatter = new HelpFormatter();

    public AppCommandLineOptions(String[] args) {
        initOptions();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(200,
                    "bot-backend-all.jar OPTIONS...",
                    "Options -c and -h -u -s are mandatory",
                    options,
                    "example: java -jar bot-backend-all.jar " +
                            "-t=123456789:AAA-sssss_D8oXaqBBEoaaaafQhdsddsdql -p=8082 " +
                            "-h=jdbc:mariadb://localhost:3306/fop " +
                            "-u=fop " +
                            "-s=123321 ");
            System.out.println(args);
            LOG.error("Your arguments: {}", Arrays.asList(args));
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

        Option opt3 = new Option("i", OPTION.PUBLIC_IP.toString(), true, "Your public IP");
        opt3.setRequired(false);
        options.addOption(opt3);

        Option opt4 = new Option("p", OPTION.SERVER_PORT.toString(), true, "Web server port for this application");
        opt4.setRequired(false);
        options.addOption(opt4);

        Option opt5 = new Option("h", OPTION.DB_HOST.toString(), true, "Database connection url");
        opt5.setRequired(true);
        options.addOption(opt5);

        Option opt6 = new Option("u", OPTION.DB_USER.toString(), true, "Database user url");
        opt6.setRequired(true);
        options.addOption(opt6);

        Option opt7 = new Option("s", OPTION.DB_PASSWORD.toString(), true, "Database password url");
        opt7.setRequired(true);
        options.addOption(opt7);
    }

    public String getOption(OPTION option) {
        return cmd.getOptionValue(option.toString());
    }

}
