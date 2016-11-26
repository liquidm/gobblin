/*
 * Copyright (C) 2014-2016 LinkedIn Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Charsets;


public class StreamCopierTest {

  @Test
  public void testSimpleCopy() throws Exception {
    String testString = "This is a string";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes(Charsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    new StreamCopier(inputStream, outputStream).copy();

    Assert.assertEquals(testString, new String(outputStream.toByteArray(), Charsets.UTF_8));
  }

  @Test
  public void testLongCopy() throws Exception {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      builder.append("testString");
    }
    String testString = builder.toString();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes(Charsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    new StreamCopier(inputStream, outputStream).withBufferSize(100).copy();

    Assert.assertEquals(testString, new String(outputStream.toByteArray(), Charsets.UTF_8));
  }

  @Test
  public void testCopyMeter() throws Exception {
    String testString = "This is a string";
    Meter meter = new MetricRegistry().meter("my.meter");
    ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes(Charsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    new StreamCopier(inputStream, outputStream).withCopySpeedMeter(meter).copy();

    Assert.assertEquals(testString, new String(outputStream.toByteArray(), Charsets.UTF_8));
    Assert.assertEquals(meter.getCount(), testString.length());
  }

}
