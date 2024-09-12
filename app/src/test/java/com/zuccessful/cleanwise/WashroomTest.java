package com.zuccessful.cleanwise;

import com.zuccessful.cleanwise.pojo.Washroom_MT17010;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rajshreekhare on 30/4/18.
 */
public class WashroomTest
{
    Washroom_MT17010 washroom;
    @Before
    public void initialize()
    {
        washroom = new Washroom_MT17010();
        washroom.setId("234");
        washroom.setFloor(2);
        washroom.setLocation("Boys Hostel");
    }
    @Test
    public void checkWashroomID()
    {
        assertNotNull(washroom.getId());
    }

    @Test
    public void checkWashroomFloor()
    {
        assertNotNull(washroom.getFloor());
    }

    @Test
    public void checkWashroomLocation()
    {
        assertNotNull(washroom.getLocation());
    }
}