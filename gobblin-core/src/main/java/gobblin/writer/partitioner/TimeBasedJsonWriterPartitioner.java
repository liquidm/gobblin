/*
 * Copyright (C) 2014-2016 LiquidM. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.writer.partitioner;

import com.google.gson.Gson;

import gobblin.configuration.State;

import java.nio.charset.StandardCharsets;

public class TimeBasedJsonWriterPartitioner extends TimeBasedWriterPartitioner<byte[]> {
  private static final Gson gson = new Gson();

  public TimeBasedJsonWriterPartitioner(State state) {
    this(state, 1, 0);
  }

  public TimeBasedJsonWriterPartitioner(State state, int numBranches, int branchId) {
    super(state, numBranches, branchId);
  }

  @Override
  public long getRecordTimestamp(byte[] row) {
    try {
      String payload = (row == null) ? "" : new String(row, StandardCharsets.UTF_8);
      TimestampedObject event = gson.fromJson(payload, TimestampedObject.class);
      return (long) event.timestamp * 1000;
    } catch (Exception e) {
      return 0;
    }
  }
}
