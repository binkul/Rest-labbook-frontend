package com.lab.labbook.config;

public class ClientConfig {
    private static final String MAIN_ENDPOINT = "http://localhost:8080/v1/";

    public static final String ACTUATOR_INFO_ENDPOINT = "http://localhost:8080/actuator/info";

    public static final String LAB_ENDPOINT = MAIN_ENDPOINT + "lab";
    public static final String LAB_SLASH_ENDPOINT = MAIN_ENDPOINT + "lab/";
    public static final String LAB_USER_ENDPOINT = MAIN_ENDPOINT + "lab/user/";

    public static final String INGR_ENDPOINT = MAIN_ENDPOINT + "recipe/all/";
    public static final String INGR_PRICE_ENDPOINT = MAIN_ENDPOINT + "recipe/price/";
    public static final String INGR_VOC_ENDPOINT = MAIN_ENDPOINT + "recipe/voc/";
    public static final String INGR_MOVE_ENDPOINT = MAIN_ENDPOINT + "recipe/move";
    public static final String INGR_DELETE_ENDPOINT = MAIN_ENDPOINT + "recipe/";
    public static final String INGR_SUM_ENDPOINT = MAIN_ENDPOINT + "recipe/sum/";
    public static final String INGR_SAVE_ENDPOINT = MAIN_ENDPOINT + "recipe";

    public static final String MAT_ENDPOINT = MAIN_ENDPOINT + "material/all";
    public static final String MAT_NAME_ENDPOINT = MAIN_ENDPOINT + "material";
    public static final String MAT_SAVE_ENDPOINT = MAIN_ENDPOINT + "material";
    public static final String MAT_SLASH_ENDPOINT = MAIN_ENDPOINT + "material/";

    public static final String SUPPLIER_ENDPOINT = MAIN_ENDPOINT + "supplier/all";
    public static final String SUPPLIER_NAME_ENDPOINT = MAIN_ENDPOINT + "supplier";
    public static final String SUPPLIER_SAVE_ENDPOINT = MAIN_ENDPOINT + "supplier";
    public static final String SUPPLIER_DELETE_ENDPOINT = MAIN_ENDPOINT + "supplier/";

    public static final String CURRENCY_ENDPOINT = MAIN_ENDPOINT + "currency";

    public static final String SERIES_ENDPOINT = MAIN_ENDPOINT + "series/all";
    public static final String SERIES_TITLE_ENDPOINT = MAIN_ENDPOINT + "series";
    public static final String SERIES_SAVE_ENDPOINT = MAIN_ENDPOINT + "series";
    public static final String SERIES_DELETE_ENDPOINT = MAIN_ENDPOINT + "series/";

    public static final String USER_ENDPOINT = MAIN_ENDPOINT + "user/all";
    public static final String USER_NAME_ENDPOINT = MAIN_ENDPOINT + "user/find";
    public static final String USER_SAVE_ENDPOINT = MAIN_ENDPOINT + "user";
    public static final String USER_LOGIN_ENDPOINT = MAIN_ENDPOINT + "user";
    public static final String USER_DELETE_ENDPOINT = MAIN_ENDPOINT + "user/";

    public static final String WEATHER_ENDPOINT = MAIN_ENDPOINT + "weather";

    public static final String LOG_ENDPOINT = MAIN_ENDPOINT + "log";
}
