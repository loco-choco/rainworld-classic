package com.locochoco.gameengine;

/**
 * Unifies all the loops and logic in here
 */
public class GameEngine {
  // Update speed parameters
  private long frame_rate; // ms
  private long last_physics_time; // ms
  private long last_graphics_time; // ms
  // Logic
  private Physics physics;
  private Graphics graphics;
  private Level level;

  public GameEngine() {
    physics = new Physics(this);
    graphics = new Graphics(this);
    level = new Level(); // TODO Set to Null
    frame_rate = 33;
    last_physics_time = 0;
    last_graphics_time = 0;

    SwingGraphics swing_graphics = new SwingGraphics("Rain World Classic");
    swing_graphics.SetWindowSize(480, 272);
    graphics.SetGraphicsAPI(swing_graphics);
  }

  public Level getLevel() {
    return level;
  }

  public boolean LoadLevel(String name) {
    return false;
  }

  public void Run() {
    long current_time = System.currentTimeMillis(); // ms
    double physics_delta_time = (last_physics_time - current_time) / 1000.0; // seconds
    level.PhysicsUpdate(physics_delta_time);
    physics.Update(physics_delta_time);

    // Graphics and Physics update happen at different times
    level.Update(physics_delta_time);
    if (current_time - last_graphics_time >= frame_rate) {
      level.GraphicsUpdate(current_time - last_graphics_time);
      graphics.Update();
      last_graphics_time = current_time;
    }

    level.LateUpdate(physics_delta_time);
    physics_delta_time = current_time;
  }

}
