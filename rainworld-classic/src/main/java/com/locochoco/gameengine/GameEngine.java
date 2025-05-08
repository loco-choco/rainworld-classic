package com.locochoco.gameengine;

import java.awt.Color;
import java.io.FileReader;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.game.*;

/**
 * Unifies all the loops and logic in here
 */
public class GameEngine {
  // Singleton instance
  private static GameEngine instance;
  // Update speed parameters
  private double frame_rate; // seconds
  private double physics_rate; // seconds
  private long last_physics_time; // ms
  private long last_graphics_time; // ms
  private long last_logic_time; // ms
  // Logic
  private Physics physics;
  private Graphics graphics;
  private InputAPI inputs;
  private Level level;

  public GameEngine() {
    physics = new Physics(this);
    graphics = new Graphics(this);
    level = null;
    frame_rate = 0.033;
    physics_rate = frame_rate / 4;
    last_physics_time = System.currentTimeMillis();
    last_graphics_time = last_physics_time;
    last_logic_time = last_physics_time;

    SwingGraphics swing_graphics = new SwingGraphics("Rain World Classic");
    graphics.SetGraphicsAPI(swing_graphics);
    inputs = swing_graphics;

    // Game Specific Settings
    swing_graphics.SetWindowSize(480, 272);
    swing_graphics.SetPixelToTransformScale(5);

    instance = this;
    LoadGameSettings("game.json");
  }

  public static GameEngine getGameEngine() {
    return instance;
  }

  public InputAPI getInputs() {
    return inputs;
  }

  public Level getLevel() {
    return level;
  }

  public boolean LoadLevel(String name) {
    try {
      level = Level.ReadLevelFromJson(name);
      return true;
    } catch (Exception e) {
      System.err.println(String.format("Error while reading %s level: %s", name, e.getMessage()));
    }
    return false;
  }

  public void Run() {
    long current_time = System.currentTimeMillis(); // ms
    double logic_delta_time = (current_time - last_logic_time) / 1000.0; // seconds
    double graphics_delta_time = (current_time - last_graphics_time) / 1000.0; // seconds
    double physics_delta_time = (current_time - last_physics_time) / 1000.0; // seconds

    level.StartObjects();

    if (physics_delta_time >= physics_rate) {
      physics.Update(physics_delta_time);
      level.PhysicsUpdate(physics_delta_time);
      last_physics_time = current_time;
    }

    // Graphics and Physics update happen at different times
    level.Update(logic_delta_time);
    if (graphics_delta_time >= frame_rate) {
      level.GraphicsUpdate(graphics_delta_time);
      graphics.Update();
      last_graphics_time = current_time;
    }

    level.LateUpdate(logic_delta_time);
    last_logic_time = current_time;

    // Object Cleanup
    level.DestroyMarkedGameObjects();
  }

  private ObjectMapper mapper;

  public void LoadGameSettings(String filename) {
    if (mapper == null)
      mapper = new ObjectMapper();

    try {
      FileReader json_file = new FileReader(filename);
      JsonNode root = mapper.readTree(json_file);

      JsonNode physics_json = root.get("physics");
      physics.ReadSettingsFromJson(physics_json, mapper);

      JsonNode graphics_json = root.get("graphics");
      graphics.ReadSettingsFromJson(graphics_json, mapper);

      JsonNode first_level_json = root.get("first_level");
      LoadLevel(first_level_json.textValue());

    } catch (Exception e) {
      System.err.println("Error while reading game settings: " + e.getMessage());
    }
  }
}
