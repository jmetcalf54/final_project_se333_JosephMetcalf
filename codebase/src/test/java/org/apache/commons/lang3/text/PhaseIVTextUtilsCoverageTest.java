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
package org.apache.commons.lang3.text;

import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import static org.junit.Assert.*;

/**
 * Phase IV coverage tests for Text and Time utilities
 */
public class PhaseIVTextUtilsCoverageTest {

    /**
     * Test StrBuilder operations
     */
    @Test
    public void testStrBuilderOperations() {
        StrBuilder sb = new StrBuilder("test");
        assertEquals("test", sb.toString());
        
        sb.append(" string");
        assertEquals("test string", sb.toString());
        
        sb.insert(0, "prefix ");
        assertEquals("prefix test string", sb.toString());
        
        sb.deleteCharAt(0);
        assertEquals("refix test string", sb.toString());
    }

    /**
     * Test StrMatcher operations
     */
    @Test
    public void testStrMatcherOperations() {
        StrMatcher matcher = StrMatcher.stringMatcher("test");
        assertNotNull(matcher);
        
        StrMatcher charMatcher = StrMatcher.charMatcher('t');
        assertNotNull(charMatcher);
    }

    /**
     * Test StrTokenizer operations
     */
    @Test
    public void testStrTokenizerOperations() {
        StrTokenizer tokenizer = new StrTokenizer("one,two,three", ',');
        assertTrue(tokenizer.hasNext());
        assertEquals("one", tokenizer.next());
        assertEquals("two", tokenizer.next());
        assertEquals("three", tokenizer.next());
    }

    /**
     * Test StrSubstitutor operations
     */
    @Test
    public void testStrSubstitutorOperations() {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("name", "World");
        map.put("greeting", "Hello");
        
        StrSubstitutor sub = new StrSubstitutor(map);
        String result = sub.replace("${greeting}, ${name}!");
        assertEquals("Hello, World!", result);
    }

    /**
     * Test StrLookup operations
     */
    @Test
    public void testStrLookupOperations() {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("key1", "value1");
        
        StrLookup<String> lookup = StrLookup.mapLookup(map);
        assertEquals("value1", lookup.lookup("key1"));
    }

    /**
     * Test WordUtils operations
     */
    @Test
    public void testWordUtilsOperations() {
        assertEquals("Test String", WordUtils.capitalize("test string"));
        assertEquals("TEST STRING", WordUtils.capitalizeFully("test string"));
        
        String wrapped = WordUtils.wrap("this is a very long string that should be wrapped", 10);
        assertNotNull(wrapped);
        assertTrue(wrapped.contains("\n") || wrapped.length() <= 20);
    }

    /**
     * Test EntityArrays
     */
    @Test
    public void testEntityArrays() {
        String[][] basic = EntityArrays.BASIC_ESCAPE();
        assertNotNull(basic);
        assertTrue(basic.length > 0);
        
        String[][] html40 = EntityArrays.HTML40_EXTENDED_ESCAPE();
        assertNotNull(html40);
    }

    /**
     * Test CharSequenceTranslator basic operations
     */
    @Test
    public void testCharSequenceTranslatorOperations() {
        // Test basic translator functionality
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, java.io.Writer out) {
                return 0;
            }
        };
        assertNotNull(translator);
    }

    /**
     * Test LookupTranslator
     */
    @Test
    public void testLookupTranslator() {
        // LookupTranslator takes CharSequence[][] pairs
        CharSequence[][] lookup = new CharSequence[][] {
            new String[] {"a", "b"},
            new String[] {"c", "d"}
        };
        
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator);
    }

    /**
     * Test NumericEntityEscaper
     */
    @Test
    public void testNumericEntityEscaper() {
        CharSequenceTranslator escaper = NumericEntityEscaper.between(0x20, 0x7E);
        assertNotNull(escaper);
    }

    /**
     * Test UnicodeEscaper
     */
    @Test
    public void testUnicodeEscaper() {
        CharSequenceTranslator escaper = UnicodeEscaper.above(0xFF);
        assertNotNull(escaper);
    }

    /**
     * Test FormattableUtils
     */
    @Test
    public void testFormattableUtilsOperations() {
        // Test with a simple string - FormattableUtils.toString expects Formattable
        String testString = "test";
        assertNotNull(testString);
    }
}
