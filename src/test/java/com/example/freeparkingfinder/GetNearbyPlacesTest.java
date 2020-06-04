package com.example.freeparkingfinder;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetNearbyPlacesTest {

    @Test
    public void doInBackground()
    {

        String urlExample =    "html_attributions" + ": [],"  + "next_page_token" + ":" + "CqQCHAEAAC-KYQptGNr7vo3yFOCCXcw17YGkAHfcptZSL_6o_EGNz0bDScVAeCyq4pt1N6uTNir4mmfOx5Y8ihLCCV54NKB_KlKR" +
        "SAqcHFrZqmLE7Ndg_osFrL8s67TOCA1ahioEK5611cZyJz-_AaL89P41KV0kuq0fJoltMzeRTvpso76K7Wvhtq7Y6IEKzcU_ROfn-5QEbantSqcDhdL5IycsIslquQIgUCgjIkEqSSx5pHtGBE2DhQCB_Cvci" +
        "ZGeodHbXTwVKnq7m7KsHx-qJhKOlsAlmE4FCrJp6IOAjGfx_9kRE_h9c19s0qZBh7qwPJKrX_rxT_luvOBowDihBtXnDapc2sDLPqGFlBio_mJafOHqG_dpLTMlw-cM06yoOVm3FhIQq_Wn_hqj5zK-iaYSlnP" +
        "GKhoUVkJ03Bh3Ac-OTddJIGz_d-2U3T0." +   "results" + ": [      {"   +      "geometry"  + ": {"     +        "location" + ": {"    +           "lat" + ": 52.952231," +
        "lng" + ": -1.145174" +           "}," +            "viewport" + ": {" +              "northeast" + ": {"+                  "lat" + ": 52.95354023029149," +"lng" +":"+
        "-1.143819869708498               },               " + "southwest" + ": {" +                  "lat" + ": 52.9508422697085," +                  "lng" +": -1.146517830291502"+
    "}            }         }," +         "icon" +":"  + "https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png," +         "id" +":" +"0e85caf88eaae"+
            "02bb9e78d2323aff6446df02769," +        "name" + ": " + "Lace Market Car Park," +         "opening_hours" + ": {" +           "open_now"+ ": true" +         "}," +
            "photos" + ": [            {" +              "height" + ": 4032," +              "html_attributions" + ": [" +                 "https://maps.google."+
        "com/maps/contrib/108849926879872794130\"\u003eAbdullah Abdulhameed Bagasi\u003c/a\u003e" +               "]," +              "photo_reference" +":"+ "CmRaAAA" +
        "A2FARV5FbBvt8XpuaddF7ApRZy_S2f0OWaBUDB9YfA8GETbItWwi6HkTLxyxS839zUcspoP3_0WXFoAK0YQrGOncJDDfXfADuj51bqmcwd39dpIU6Zf5uTtQWIz8Q6_8XEhByCX3XR1BUdlEGApP" +
        "aalWNGhRbHjgf9nnS0bmhO_-06MfrPxyZEA," +               "width" + ": 3024"  +          "}         ],"  +       "place_id" + ":" + "ChIJy5qupNXDeUgR8a7b8FM1xkg";


        assertTrue((urlExample.contains("geometry")) && (urlExample.contains("name"))
                && (urlExample.contains("place_id")));
    }
}
