package gobblin.converter.json;

import java.util.List;
import java.nio.charset.StandardCharsets;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import gobblin.configuration.ConfigurationKeys;
import gobblin.configuration.WorkUnitState;
import gobblin.converter.Converter;
import gobblin.converter.DataConversionException;
import gobblin.converter.SchemaConversionException;


/**
 * A {@link Converter} to remove JSON attributes from JSON string.
 */
public class JsonFieldRemoverConverter extends Converter<String, String, byte[], byte[]> {
  private List<String> fields;

  private static Gson GSON = new Gson();
  private static final Splitter SPLITTER_ON_COMMA = Splitter.on(',').trimResults().omitEmptyStrings();

  @Override
  public Converter<String, String, byte[], byte[]> init(WorkUnitState workUnit) {
    Preconditions.checkArgument(workUnit.contains(ConfigurationKeys.CONVERTER_JSON_REMOVE_FIELDS),
        "Missing required property converter.json.remove.fields for the JsonFieldRemoverConverter class.");
    this.fields = SPLITTER_ON_COMMA.splitToList(workUnit.getProp(ConfigurationKeys.CONVERTER_JSON_REMOVE_FIELDS));
    return super.init(workUnit);
  }

  @Override
  public String convertSchema(String inputSchema, WorkUnitState workUnit)
      throws SchemaConversionException {
    
    return inputSchema;
  }

  @Override
  public Iterable<byte[]> convertRecord(String outputSchema, byte[] inputRecord, WorkUnitState workUnit)
      throws DataConversionException {
        String payload = (inputRecord == null) ? "" : new String(inputRecord, StandardCharsets.UTF_8);
        JsonObject record = GSON.fromJson(payload, JsonObject.class);
        for (String key : fields) {
          record.remove(key);
        }

    return Lists.newArrayList(GSON.toJson(record).getBytes(StandardCharsets.UTF_8));
  }

}
