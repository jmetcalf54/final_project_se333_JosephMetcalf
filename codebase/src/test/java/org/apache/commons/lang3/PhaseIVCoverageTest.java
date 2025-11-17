/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Phase IV coverage tests for Apache Commons Lang3
 * Focus on improving test coverage for uncovered code paths
 */
public class PhaseIVCoverageTest {

    /**
     * Test ArrayUtils edge cases for uncovered branches
     */
    @Test
    public void testArrayUtilsEdgeCases() {
        // Test empty array operations
        byte[] emptyByteArray = new byte[0];
        assertEquals(ArrayUtils.INDEX_NOT_FOUND, ArrayUtils.indexOf(emptyByteArray, (byte)1));
        assertEquals(ArrayUtils.INDEX_NOT_FOUND, ArrayUtils.lastIndexOf(emptyByteArray, (byte)1));
        
        // Test null safe operations
        int[] nullArray = null;
        assertTrue(ArrayUtils.isEmpty(nullArray));
        assertEquals(0, ArrayUtils.getLength(nullArray));
    }

    /**
     * Test StringUtils edge cases
     */
    @Test
    public void testStringUtilsEdgeCases() {
        // Test various string operations
        assertEquals("", StringUtils.defaultIfEmpty("", "default"));
        assertEquals("default", StringUtils.defaultIfBlank("  ", "default"));
        assertEquals("test", StringUtils.defaultString("test", "default"));
        
        // Test comparison operations
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "def"));
        assertTrue(StringUtils.equalsIgnoreCase("ABC", "abc"));
    }

    /**
     * Test ClassUtils reflection capabilities
     */
    @Test
    public void testClassUtilsReflection() {
        // Test primitive wrapper conversions
        assertEquals(Integer.class, ClassUtils.primitiveToWrapper(int.class));
        assertEquals(int.class, ClassUtils.wrapperToPrimitive(Integer.class));
        
        // Test class name operations
        assertEquals("java.lang.String", ClassUtils.getShortCanonicalName(String.class));
        assertTrue(ClassUtils.isPrimitiveOrWrapper(int.class));
        assertFalse(ClassUtils.isPrimitiveOrWrapper(String.class));
    }

    /**
     * Test ObjectUtils operations
     */
    @Test
    public void testObjectUtilsEdgeCases() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        // Test equality comparisons
        assertTrue(ObjectUtils.equals(obj1, obj1));
        assertFalse(ObjectUtils.equals(obj1, obj2));
        assertTrue(ObjectUtils.equals(null, null));
        
        // Test identification operations
        assertNotNull(ObjectUtils.identityToString(obj1));
    }

    /**
     * Test Range operations
     */
    @Test
    public void testRangeEdgeCases() {
        Range<Integer> range = Range.between(1, 10);
        
        assertTrue(range.contains(5));
        assertFalse(range.contains(0));
        assertFalse(range.contains(11));
        assertTrue(range.isAfter(0));
        assertTrue(range.isBefore(11));
    }

    /**
     * Test CharUtils operations
     */
    @Test
    public void testCharUtilsEdgeCases() {
        assertTrue(CharUtils.isAscii('a'));
        assertTrue(CharUtils.isAsciiPrintable('a'));
        assertTrue(CharUtils.isAsciiControl('\n'));
        
        assertNotNull(CharUtils.toCharacterObject('a'));
    }

    /**
     * Test Validate utility operations
     */
    @Test
    public void testValidateEdgeCases() {
        // Test notEmpty validation
        Validate.notEmpty("test");
        Validate.notEmpty(new String[]{"a"});
        
        // Test notNull validation  
        Validate.notNull("test");
        
        // Test isTrue validation
        Validate.isTrue(true);
        Validate.isTrue(1 > 0, "Should be greater");
    }

    /**
     * Test SerializationUtils operations
     */
    @Test
    public void testSerializationEdgeCases() {
        String original = "test string";
        byte[] serialized = SerializationUtils.serialize(original);
        assertNotNull(serialized);
        
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    /**
     * Test SystemUtils information retrieval
     */
    @Test
    public void testSystemUtilsEdgeCases() {
        assertNotNull(SystemUtils.JAVA_VERSION);
        assertNotNull(SystemUtils.OS_NAME);
        // Just verify OS detection works
        assertTrue(SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC);
    }

    /**
     * Test BooleanUtils operations
     */
    @Test
    public void testBooleanUtilsEdgeCases() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        
        assertTrue(BooleanUtils.toBoolean("true"));
        assertTrue(BooleanUtils.toBoolean("yes"));
        assertTrue(BooleanUtils.toBoolean("on"));
    }

    /**
     * Test EnumUtils operations
     */
    @Test
    public void testEnumUtilsOperations() {
        assertNotNull(EnumUtils.getEnumList(Thread.State.class));
        assertNotNull(EnumUtils.getEnumMap(Thread.State.class));
        assertTrue(EnumUtils.isValidEnum(Thread.State.class, "RUNNABLE"));
        assertFalse(EnumUtils.isValidEnum(Thread.State.class, "INVALID"));
    }

    /**
     * Test BitField operations
     */
    @Test
    public void testBitFieldEdgeCases() {
        BitField bf = new BitField(0xFF);
        assertEquals(255, bf.getRawValue(255));
        bf.setValue(255, 0x0F);
        // Test BitField operations
    }

    /**
     * Test tuple operations
     */
    @Test
    public void testTupleOperations() {
        org.apache.commons.lang3.tuple.Pair<String, Integer> pair = 
            org.apache.commons.lang3.tuple.Pair.of("test", 42);
        assertEquals("test", pair.getLeft());
        assertEquals(Integer.valueOf(42), pair.getRight());
        
        org.apache.commons.lang3.tuple.Triple<String, Integer, Boolean> triple =
            org.apache.commons.lang3.tuple.Triple.of("test", 42, true);
        assertEquals("test", triple.getLeft());
        assertEquals(Integer.valueOf(42), triple.getMiddle());
        assertEquals(Boolean.TRUE, triple.getRight());
    }
}
