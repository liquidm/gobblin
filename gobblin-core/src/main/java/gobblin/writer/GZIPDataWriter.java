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
package gobblin.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import gobblin.configuration.State;

public class GZIPDataWriter extends SimpleDataWriter {
  public GZIPDataWriter(SimpleDataWriterBuilder builder, State properties) throws IOException {
    super(builder, properties);
  }

  @Override
  protected OutputStream createStagingFileOutputStream() throws IOException {
    return this.closer.register(new GZIPOutputStream(this.fs.create(this.stagingFile, this.filePermission, true, this.bufferSize,
        this.replicationFactor, this.blockSize, null)));
  }
}
