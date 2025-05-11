package com.locochoco.serialization;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

// From https://stackoverflow.com/questions/60678833/how-i-parse-color-java-class-to-json-with-jackson
public class ColorJsonDeserializer extends JsonDeserializer<Color> {

  @Override
  public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return new Color(p.getValueAsInt());
  }
}
