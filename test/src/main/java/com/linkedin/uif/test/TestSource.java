package com.linkedin.uif.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.linkedin.uif.configuration.SourceState;
import com.linkedin.uif.configuration.WorkUnitState;
import com.linkedin.uif.source.Source;
import com.linkedin.uif.source.extractor.Extractor;
import com.linkedin.uif.source.workunit.Extract;
import com.linkedin.uif.source.workunit.Extract.TableType;
import com.linkedin.uif.source.workunit.WorkUnit;

/**
 * An implementation of {@link Source} for integration test.
 *
 * @author ynli
 */
public class TestSource implements Source<String, String> {

    private static final String SOURCE_FILE_LIST_KEY = "source.files";
    private static final String SOURCE_FILE_KEY = "source.file";

    private static final Splitter SPLITTER = Splitter.on(",")
            .omitEmptyStrings()
            .trimResults();

    @Override
    public List<WorkUnit> getWorkunits(SourceState state) {
        Extract extract1 = new Extract(state, TableType.SNAPSHOT_ONLY, "com.linkedin.uif.test", "TestTable1", "1");
        Extract extract2 = new Extract(state, TableType.SNAPSHOT_ONLY, "com.linkedin.uif.test", "TestTable2", "2");
        
        String sourceFileList = state.getProp(SOURCE_FILE_LIST_KEY);
        List<WorkUnit> workUnits = Lists.newArrayList();
        
        List<String> list = (List<String>) SPLITTER.splitToList(sourceFileList);
        
        for (int i = 0; i < list.size(); i++) {
            WorkUnit workUnit;
            if (i % 2 == 0) {
                workUnit = new WorkUnit(state, extract1);
            } else {
                workUnit = new WorkUnit(state, extract2);
            }
            workUnit.setProp(SOURCE_FILE_KEY, list.get(i));
            workUnits.add(workUnit);
        }
        return workUnits;
    }

    @Override
    public Extractor<String, String> getExtractor(WorkUnitState state) {
        return new TestExtractor(state);
    }

    @Override
    public void shutdown(SourceState state)
    {
        // Do nothing
    }
}