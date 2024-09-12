package com.zuccessful.cleanwise;

import com.zuccessful.cleanwise.pojo.Job_MT17010;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rajshreekhare on 30/4/18.
 */
public class JobTest
{
    
    Job_MT17010 job;
    String id = "2131";
    String superId = "121";
    String washroomId = "B123";
    int slot = 2;


    @Before
    public void initialize()
    {
        
        job = new Job_MT17010();
        job.setId(id);
        job.setSupervisorId(superId);
        job.setWashroomId(washroomId);
        job.setSlot(slot);

    }

    @Test
    public void checkSupervisorIdNull()
    {
        assertNotNull(job.getSupervisorId());
    }

    @Test
    public void checkIdNull()
    {
        assertNotNull(job.getId());
    }


    @Test
    public void checkWashroomIdNull()
    {
        assertNotNull(job.getWashroomId());
    }


    @Test
    public void checkSlotNull()
    {
        assertNotNull(job.getSlot());
    }

    @Test
    public void checkSupervisorId()
    {
        assertEquals("OK",job.getSupervisorId(),superId);
    }

    @Test
    public void checkId()
    {
        assertEquals("OK",job.getId(),id);
    }

    @Test
    public void checkWashroomId()
    {
        assertEquals("OK",job.getWashroomId(),washroomId);
    }    
    
    @Test
    public void checkSlot()
    {
        assertEquals("OK",job.getSlot(),slot);
    }




}