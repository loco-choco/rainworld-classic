package com.locochoco.serialization;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

//From https://stackoverflow.com/questions/60678833/how-i-parse-color-java-class-to-json-with-jackson
public class ColorJsonSerializer extends JsonSerializer<Color> {

  @Override
  public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      gen.writeNull();
      return;
    }
    gen.writeNumber(value.getRGB());
  }
}
