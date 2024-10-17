package com.uni.uni_erp.util.date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;

public class GsonUtil {

    public static JsonArray toJsonArray(EmpDocument document) {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(document.toJson());
        return jsonArray;
    }
}
