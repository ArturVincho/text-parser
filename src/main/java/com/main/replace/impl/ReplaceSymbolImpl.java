package com.main.replace.impl;

import com.main.replace.ReplaceSymbol;
import org.springframework.stereotype.Component;

@Component
public class ReplaceSymbolImpl implements ReplaceSymbol {
    private static final String EXCUSES = "(?<!\\S)(?:это|как|так|и|в|над|к|до|не|на|но|за|то|с|ли|а|во|от|со|для|о|же|ну|вы|бы|что|кто|он|она)(?!\\S)";
    private static final String PRONOUNS = "(?<!\\S)(?:я|мы|ты|вы|он|она|оно|они|мой|моя|мое|мои|наш|наша|наше|наши|твой|твоя|твое|твои|ваш|ваша|ваше|ваши|его|её|их|" + "кто|что|какой|каков|чей|который|сколько|где|когда|куда|откуда|зачем|столько|этот|тот|такой|таков|тут|здесь|сюда|туда|оттуда|отсюда|тогда|поэтому|затем|" + "весь|всякий|все|сам|самый|каждый|любой|другой|иной|всяческий|всюду|везде|всегда|никто|ничто|некого|нечего|никакой|ничей|некто|нечто|некий|некоторый|" + "несколько|кое-кто|кое-что|кое-куда|какой-либо|сколько-нибудь|куда-нибудь|зачем-нибудь|чей-либо)(?!\\S)";


    @Override
    public String replaceSymbol(String content) {
        return content.replaceAll("\\n", "").replaceAll("\\r", " ")
                .replaceAll(" {2}", " ").replaceAll(EXCUSES, "").replaceAll(PRONOUNS, "").replaceAll("(?<!\\S)(?:null| null)(?!\\S)", "");
    }
}
