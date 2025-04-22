package com.locochoco.gameengine;

import java.awt.Color;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
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
    // TODO MOVE THIS TO APP
    try {
      level = Level.ReadLevelFromJson("levels/level0.json");
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    // TODO ADD THIS TO A GAME.json config or smth
    // Physics Settings
    physics.setGravity(new Vector2d(0, 100));
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
    return false;
  }

  public void Run() {
    long current_time = System.currentTimeMillis(); // ms
    double logic_delta_time = (current_time - last_logic_time) / 1000.0; // seconds
    double graphics_delta_time = (current_time - last_graphics_time) / 1000.0; // seconds
    double physics_delta_time = (current_time - last_physics_time) / 1000.0; // seconds

    level.StartObjects();

    if (physics_delta_time >= physics_rate) {
      level.PhysicsUpdate(physics_delta_time);
      physics.Update(physics_delta_time);
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
  }
}
