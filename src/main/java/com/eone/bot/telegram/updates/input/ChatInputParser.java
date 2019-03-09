package com.eone.bot.telegram.updates.input;

public final class ChatInputParser {

    public static boolean isStartCommand(String userQuery) {
        return "/start".equals(userQuery);
    }

    public static boolean valid(String[] words){
        if (words.length == 2 || words.length == 3){
            return true;
        }
        return false;
    }

    public static String[] getWordsFromText(String userText) {
        String[] words = userText.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("^\\w", "");
        }
        return words;
    }

    public static String[] convertToFullTextSearch( String[] queryParamStrings) {
        String[] param3 = new String[3];
        param3[0] = "%";
        param3[1] = "%";
        param3[2] = "%";
        for (int i = 0; i < queryParamStrings.length; i++) {
            param3[i] = queryParamStrings[i]+"%";
        }
        return param3;
    }
}
