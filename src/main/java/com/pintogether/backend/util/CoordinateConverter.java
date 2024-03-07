package com.pintogether.backend.util;

import com.pintogether.backend.dto.CoordinateDto;
import org.locationtech.proj4j.*;

/**
 *
 * Coordinate Conversion EPSG:5174 to WGS84
 */

public final class CoordinateConverter {

    // CRS 객체 생성
    private static CRSFactory crsFactory = new CRSFactory();
    private static String epsg5174Name = "ESPG5174";
    private static String epsg5174Proj = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
    private static CoordinateReferenceSystem espg5174System = crsFactory.createFromParameters(epsg5174Name, epsg5174Proj);
    private static String wgs84Name = "WGS84";
    private static String wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs";
    private static CoordinateReferenceSystem wgs84System = crsFactory.createFromParameters(wgs84Name, wgs84Proj);

    public static CoordinateDto convert(double longitude, double latitude) {

        ProjCoordinate p = new ProjCoordinate();
        p.x = latitude;
        p.y = longitude;
        ProjCoordinate p2 = new ProjCoordinate();

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform coordinateTransform = ctFactory.createTransform(espg5174System, wgs84System);
        ProjCoordinate projCoordinate = coordinateTransform.transform(p, p2);

        return CoordinateDto.builder()
                .latitude(projCoordinate.y)
                .longitude(projCoordinate.x)
                .build();
    }

}