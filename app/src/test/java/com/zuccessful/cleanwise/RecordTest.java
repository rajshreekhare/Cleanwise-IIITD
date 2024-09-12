package com.zuccessful.cleanwise;

import com.zuccessful.cleanwise.pojo.Record_MT17010;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rajshreekhare on 30/4/18.
 */
public class RecordTest
{
    
    Record_MT17010 record;
    String id = "2131";
    String superId = "121";


    @Before
    public void initialize()
    {
        record = new Record_MT17010();
        record.setJobId(id);
        record.setSupervisorId(superId);
    }
    @Test
    public void checkSupervisorIdNull()
    {
        assertNotNull(record.getSupervisorId());
    }

    @Test
    public void checkJobIdNull()
    {
        assertNotNull(record.getJobId());
    }

    @Test
    public void checkSupervisorId()
    {
        assertEquals("OK",record.getSupervisorId(),superId);
    }

    @Test
    public void checkJobId()
    {
        assertEquals("OK",record.getJobId(),id);
    }
}