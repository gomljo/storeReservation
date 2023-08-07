package com.store.reservation.store.store.util.implementation.juso.constant;

import java.util.List;

public final class JusoOpenApiConstants {

    public static final String URL ="https://api.vworld.kr/req/address";
    public static final String SEARCH_TYPE = "ROAD";
    public static final String COORDINATE_SYSTEM_TYPE = "epsg:4326";
    public static final String SERVICE_QUERY = "?service=";
    public static final String SERVICE = "address";
    public static final String REQUEST_QUERY = "&request=";
    public static final String REQUEST = "getCoord";
    public static final String FORMAT = "json";
    public static final String FORMAT_QUERY = "&format=";
    public static final String KEY_QUERY = "&key=";
    public static final String TYPE_QUERY = "&type=";
    public static final String ADDRESS_QUERY="&address=";
    public static final String COORDINATE_SYSTEM_QUERY = "&crs=";
    public static final String WHITE_SPACE = "";
    public static final List<String> parsingKeys = List.of("response","result", "point");
    public static final String COORDINATE_X = "x";
    public static final String COORDINATE_Y = "y";

    private JusoOpenApiConstants(){};


}
