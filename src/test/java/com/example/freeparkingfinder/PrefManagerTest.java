package com.example.freeparkingfinder;

import org.hamcrest.collection.IsMapContaining;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class PrefManagerTest {

    @Test
    public void getFavourites()
    {
        //assert getFavourites returns type Map<String, ?>
        Map<String, String> result = new HashMap<>();
        //random values
        result.put("name1", "lat/lng: (2.11232353256,-4.12323523467)");
        result.put("name2", "lat/lng: (5.112389745,0.5684578343)");

        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("name1", "lat/lng: (2.11232353256,-4.12323523467)");
        expectedResult.put("name2", "lat/lng: (5.112389745,0.5684578343)");


        //test equality
        assertThat(result, is(expectedResult));


        //test map entry

        //assert that map contains String values
        assertThat(result, IsMapContaining.hasEntry(is("name1"), is("lat/lng: (2.11232353256,-4.12323523467)")));

        //assert map does not contain int or char values
        assertThat(result, not(IsMapContaining.hasEntry(is(1234), is('A'))));
    }
}