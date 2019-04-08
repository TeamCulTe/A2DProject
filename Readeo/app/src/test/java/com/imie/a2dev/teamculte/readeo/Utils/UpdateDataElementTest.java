package com.imie.a2dev.teamculte.readeo.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class used to test UpdateDataElement methods.
 */
public class UpdateDataElementTest {
    /**
     * Stores the test element.
     */
    private UpdateDataElement element;

    /**
     * Defines the number of ids.
     */
    private final int idsNumber = 2;

    /**
     * Defines the first id.
     */
    private final int firstId = 1;

    /**
     * Defines the second id.
     */
    private final int secondId = 2;

    @Before
    public void setUp() throws Exception {
        int[] ids = new int[this.idsNumber];
        ids[0] = this.firstId;
        ids[1] = this.secondId;

        this.element = new UpdateDataElement(ids, new Date());
    }

    @Test
    public void testGetId() {
        assertEquals(this.element.getId(0), this.firstId);
        assertEquals(this.element.getId(1), this.secondId);
    }

    @Test
    public void testSize() {
        assertEquals(this.element.size(), this.idsNumber);
    }
}