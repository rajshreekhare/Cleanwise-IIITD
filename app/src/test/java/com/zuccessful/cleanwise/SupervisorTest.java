package com.zuccessful.cleanwise;

import com.zuccessful.cleanwise.pojo.Supervisor_MT17010;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rajshreekhare on 30/4/18.
 */
public class SupervisorTest
{
    String name  = "Sudheer";
    Supervisor_MT17010 supervisor;
    @Before
    public void initialize()
    {
        
        supervisor = new Supervisor_MT17010();
        supervisor.setId("234");
        supervisor.setEmailId("supervisor@iitd.ac.in");
        supervisor.setName(name);
    }
    @Test
    public void checkSupervisorId()
    {
        assertNotNull(supervisor.getId());
    }

    @Test
    public void checkSupervisorEmailId()
    {
        assertNotNull(supervisor.getEmailId());
    }

    @Test
    public void checkSupervisorNameNull()
    {
        assertNotNull(supervisor.getName());
    }

    @Test
    public void checkSupervisorName()
    {
        assertEquals("OK",name,supervisor.getName());
    }
}