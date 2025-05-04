package com.locochoco.gameengine;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles all the graphic logic and calculations
 */
public class Graphics {
  private GameEngine game;
  private GraphicsAPI graphics_api;

  private HashMap<String, Integer> rendering_order;

  public Graphics(GameEngine game) {
    this.game = game;
    this.rendering_order = new HashMap<>();
  }

  public void SetGraphicsAPI(GraphicsAPI graphics_api) {
    this.graphics_api = graphics_api;
  }

  public void AddLayer(String layer) {
    rendering_order.put(layer, rendering_order.size());
  }

  public void Update() {
    graphics_api.CreateBuffer();
    ArrayList<GameObject> gos = new ArrayList<>(game.getLevel().getGameObjects());
    gos.sort((go1, go2) -> {
      Renderer rend1 = go1.getRenderer();
      Renderer rend2 = go2.getRenderer();
      if (rend1 == null)
        return 1;
      if (rend2 == null)
        return -1;
      int layer_rend1 = rendering_order.getOrDefault(rend1.layer, 0);
      int layer_rend2 = rendering_order.getOrDefault(rend2.layer, 0);
      if (layer_rend1 > layer_rend2)
        return -1;
      if (layer_rend1 < layer_rend2)
        return 1;
      return 0;
    });
    for (GameObject g : gos) {
      Renderer r = g.getRenderer();
      if (r != null && g.isEnabled()) {
        r.RenderObject(graphics_api);
      }
    }
    graphics_api.FlushBuffer();
  }

  public void ReadSettingsFromJson(JsonNode json, ObjectMapper mapper) {
    // Rendering Order
    JsonNode rendering_order = json.get("rendering_order");
    for (JsonNode layer : rendering_order) {
      AddLayer(layer.asText());
    }
  }
}
