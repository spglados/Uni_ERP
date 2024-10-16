package com.uni.uni_erp.util.sort;

import org.springframework.context.annotation.Bean;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LanguageSortUtil {

    public static void koreanSortDesc(List<String> list) {
        Collator collator = Collator.getInstance(Locale.KOREAN);
        list.sort(collator::compare);
    }

}
