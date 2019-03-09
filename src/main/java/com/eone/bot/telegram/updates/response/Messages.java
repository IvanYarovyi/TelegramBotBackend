package com.eone.bot.telegram.updates.response;

public enum Messages {
    //"За запитом: \"%s\" в базі нічого не знайденно."
    MESSAGE_NO_DATA ("\u0417\u0430 \u0437\u0430\u043f\u0438\u0442\u043e\u043c: \" %s \" \u0432 \u0431\u0430\u0437\u0456 \u043d\u0456\u0447\u043e\u0433\u043e \u043d\u0435 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043d\u043e."),
    //"Не валідний запит. Спробуйте: _Мельник Іван Іванович_ або _Мельник Іван_ ";
    MESSAGE_QUERY_EXAMPLE("\u041d\u0435 \u0432\u0430\u043b\u0456\u0434\u043d\u0438\u0439 \u0437\u0430\u043f\u0438\u0442. \u0421\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435: _\u041c\u0435\u043b\u044c\u043d\u0438\u043a \u0406\u0432\u0430\u043d \u0406\u0432\u0430\u043d\u043e\u0432\u0438\u0447_ \u0430\u0431\u043e _\u041c\u0435\u043b\u044c\u043d\u0438\u043a \u0406\u0432\u0430\u043d_ "),
    //"За фільтром \"%s\" в базі знайденно: %d записів."
    MESSAGE_FOUND_COUNT("\u0417\u0430 \u0444\u0456\u043b\u044c\u0442\u0440\u043e\u043c \" %s \" \u0432 \u0431\u0430\u0437\u0456 \u0437\u043d\u0430\u0439\u0434\u0435\u043d\u043d\u043e: %d \u0437\u0430\u043f\u0438\u0441\u0456\u0432."),
    //"\n _В системі присутні такі 'по батькові':_ \n"
    MESSAGE_LIST("\n _\u0412 \u0441\u0438\u0441\u0442\u0435\u043c\u0456 \u043f\u0440\u0438\u0441\u0443\u0442\u043d\u0456 \u0442\u0430\u043a\u0456 '\u043f\u043e \u0431\u0430\u0442\u044c\u043a\u043e\u0432\u0456':_ \n"),
    //"Просто відправте повідомлення з Прізвищем Ім'ям та Побатькові."
    MESSAGE_ABOUT("\u041f\u0440\u043e\u0441\u0442\u043e \u0432\u0456\u0434\u043f\u0440\u0430\u0432\u0442\u0435 \u043f\u043e\u0432\u0456\u0434\u043e\u043c\u043b\u0435\u043d\u043d\u044f \u0437 \u041f\u0440\u0456\u0437\u0432\u0438\u0449\u0435\u043c \u0406\u043c'\u044f\u043c \u0442\u0430 \u041f\u043e\u0431\u0430\u0442\u044c\u043a\u043e\u0432\u0456."),
    //"Уточніть ваш запит."
    MESSAGE_CLARIFY("\u0423\u0442\u043e\u0447\u043d\u0456\u0442\u044c \u0432\u0430\u0448 \u0437\u0430\u043f\u0438\u0442."),
    MESSAGE_NONE("");


    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args){
        return String.format(getMessage(), args);
    }
}
