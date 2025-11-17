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
package org.apache.commons.lang3.builder;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Phase IV coverage tests for Builder classes
 * Focus on improving test coverage and fixing builder patterns
 */
public class PhaseIVBuilderCoverageTest {

    private TestObject testObject;
    
    @Before
    public void setUp() {
        testObject = new TestObject("test", 42, 3.14);
    }

    /**
     * Test CompareToBuilder with various types
     */
    @Test
    public void testCompareToBuilderBasic() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        TestObject obj3 = new TestObject("test", 43, 3.14);
        
        CompareToBuilder cb = new CompareToBuilder();
        cb.append(obj1.getValue(), obj2.getValue());
        assertEquals(0, cb.toComparison());
        
        cb = new CompareToBuilder();
        cb.append(obj1.getValue(), obj3.getValue());
        assertTrue(cb.toComparison() < 0);
    }

    /**
     * Test EqualsBuilder with various types
     */
    @Test
    public void testEqualsBuilderBasic() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        TestObject obj3 = new TestObject("test", 43, 3.14);
        
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(obj1.getValue(), obj2.getValue());
        assertTrue(eb.isEquals());
        
        eb = new EqualsBuilder();
        eb.append(obj1.getValue(), obj3.getValue());
        assertFalse(eb.isEquals());
    }

    /**
     * Test HashCodeBuilder
     */
    @Test
    public void testHashCodeBuilderBasic() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        
        int hash1 = new HashCodeBuilder(17, 31)
            .append(obj1.getName())
            .append(obj1.getValue())
            .append(obj1.getPrice())
            .toHashCode();
            
        int hash2 = new HashCodeBuilder(17, 31)
            .append(obj2.getName())
            .append(obj2.getValue())
            .append(obj2.getPrice())
            .toHashCode();
            
        assertEquals(hash1, hash2);
    }

    /**
     * Test ToStringBuilder with various styles
     */
    @Test
    public void testToStringBuilderBasic() {
        String str = new ToStringBuilder(testObject)
            .append("name", testObject.getName())
            .append("value", testObject.getValue())
            .append("price", testObject.getPrice())
            .toString();
            
        assertNotNull(str);
        assertTrue(str.contains("name=test"));
        assertTrue(str.contains("value=42"));
    }

    /**
     * Test ToStringBuilder with different styles
     */
    @Test
    public void testToStringBuilderStyles() {
        String defaultStyle = new ToStringBuilder(testObject, ToStringStyle.DEFAULT_STYLE)
            .append("name", testObject.getName())
            .toString();
        assertNotNull(defaultStyle);
        
        String multiLine = new ToStringBuilder(testObject, ToStringStyle.MULTI_LINE_STYLE)
            .append("name", testObject.getName())
            .toString();
        assertNotNull(multiLine);
        
        String simple = new ToStringBuilder(testObject, ToStringStyle.SIMPLE_STYLE)
            .append("name", testObject.getName())
            .toString();
        assertNotNull(simple);
    }

    /**
     * Test EqualsBuilder with nulls
     */
    @Test
    public void testEqualsBuilderWithNulls() {
        EqualsBuilder eb = new EqualsBuilder();
        eb.append((Object)null, (Object)null);
        assertTrue(eb.isEquals());
        
        eb = new EqualsBuilder();
        eb.append("test", (Object)null);
        assertFalse(eb.isEquals());
        
        eb = new EqualsBuilder();
        eb.append((Object)null, "test");
        assertFalse(eb.isEquals());
    }

    /**
     * Test HashCodeBuilder with arrays
     */
    @Test
    public void testHashCodeBuilderWithArrays() {
        int hash1 = new HashCodeBuilder(17, 31)
            .append(new int[]{1, 2, 3})
            .toHashCode();
        
        int hash2 = new HashCodeBuilder(17, 31)
            .append(new int[]{1, 2, 3})
            .toHashCode();
            
        assertEquals(hash1, hash2);
        
        int hash3 = new HashCodeBuilder(17, 31)
            .append(new int[]{1, 2, 4})
            .toHashCode();
            
        assertTrue(hash1 != hash3);
    }

    /**
     * Test CompareToBuilder with arrays
     */
    @Test
    public void testCompareToBuilderWithArrays() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 3};
        int[] arr3 = new int[]{1, 2, 4};
        
        CompareToBuilder cb = new CompareToBuilder();
        cb.append(arr1, arr2);
        assertEquals(0, cb.toComparison());
        
        cb = new CompareToBuilder();
        cb.append(arr1, arr3);
        assertTrue(cb.toComparison() < 0);
    }

    /**
     * Test ReflectionToStringBuilder
     */
    @Test
    public void testReflectionToStringBuilder() {
        String str = ReflectionToStringBuilder.toString(testObject);
        assertNotNull(str);
        assertTrue(str.contains("TestObject"));
    }

    /**
     * Test EqualsBuilder Reflection methods
     */
    @Test
    public void testEqualsBuilderReflection() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        TestObject obj3 = new TestObject("test", 43, 3.14);
        
        assertTrue(EqualsBuilder.reflectionEquals(obj1, obj2));
        assertFalse(EqualsBuilder.reflectionEquals(obj1, obj3));
    }

    /**
     * Test HashCodeBuilder Reflection methods
     */
    @Test
    public void testHashCodeBuilderReflection() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        
        int hash1 = HashCodeBuilder.reflectionHashCode(obj1);
        int hash2 = HashCodeBuilder.reflectionHashCode(obj2);
        
        assertEquals(hash1, hash2);
    }

    /**
     * Test CompareToBuilder Reflection methods
     */
    @Test
    public void testCompareToBuilderReflection() {
        TestObject obj1 = new TestObject("test", 42, 3.14);
        TestObject obj2 = new TestObject("test", 42, 3.14);
        TestObject obj3 = new TestObject("test", 43, 3.14);
        
        assertEquals(0, CompareToBuilder.reflectionCompare(obj1, obj2));
        assertTrue(CompareToBuilder.reflectionCompare(obj1, obj3) < 0);
    }

    /**
     * Test object for builder tests
     */
    static class TestObject implements Comparable<TestObject> {
        private String name;
        private int value;
        private double price;

        public TestObject(String name, int value, double price) {
            this.name = name;
            this.value = value;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public int compareTo(TestObject o) {
            return new CompareToBuilder()
                .append(this.name, o.name)
                .append(this.value, o.value)
                .append(this.price, o.price)
                .toComparison();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestObject)) {
                return false;
            }
            return new EqualsBuilder()
                .append(this.name, ((TestObject)obj).name)
                .append(this.value, ((TestObject)obj).value)
                .append(this.price, ((TestObject)obj).price)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31)
                .append(this.name)
                .append(this.value)
                .append(this.price)
                .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("name", this.name)
                .append("value", this.value)
                .append("price", this.price)
                .toString();
        }
    }
}
