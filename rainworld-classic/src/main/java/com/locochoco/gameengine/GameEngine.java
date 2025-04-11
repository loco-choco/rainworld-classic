package com.locochoco.gameengine;

import java.util.concurrent.TimeUnit;
import java.awt.Color;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

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
    level = null;
    frame_rate = 33 / 2;
    last_physics_time = System.currentTimeMillis();
    last_graphics_time = last_physics_time;

    SwingGraphics swing_graphics = new SwingGraphics("Rain World Classic");
    swing_graphics.SetWindowSize(480, 272);
    swing_graphics.SetPixelToTransformScale(5);
    graphics.SetGraphicsAPI(swing_graphics);

    CreateTestLevel();// TODO REMOVE
  }

  public Level getLevel() {
    return level;
  }

  public boolean LoadLevel(String name) {
    return false;
  }

  public void Run() {
    long current_time = System.currentTimeMillis(); // ms
    double physics_delta_time = (current_time - last_physics_time) / 1000.0; // seconds
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
    last_physics_time = current_time;
  }

  private void CreateTestLevel() {
    level = new Level(); // TODO Set to Null

    GameObject go_test = new GameObject();
    BoxRenderer box_renderer = new BoxRenderer(go_test);
    box_renderer.SetWidth(5)
        .SetHeight(10)
        .SetColor(Color.ORANGE)
        .SetCenter(new Point2d(2.5, 5));
    try {
      go_test.addRenderer(box_renderer);
      go_test.addRigidbody().SetVelocity(new Vector2d(8, 10));
      go_test.addCollider().setShape(new Point2d(0, 0), new Point2d(5, 10))
          .setCenter(new Point2d(2.5, 5));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test);

    GameObject go_test2 = new GameObject();
    BoxRenderer box_renderer2 = new BoxRenderer(go_test2);
    box_renderer2.SetWidth(75)
        .SetHeight(5)
        .SetColor(Color.CYAN)
        .SetCenter(new Point2d(75 / 2.0, 2.5));
    try {
      go_test2.addRenderer(box_renderer2);
      go_test2.getTransform().setPosition(new Point2d(50, 45));
      go_test2.addCollider()
          .setShape(new Point2d(0, 0), new Point2d(75, 5))
          .setCenter(new Point2d(75 / 2.0, 2.5))
          .setElasticity(1);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test2);

    GameObject go_test3 = new GameObject();
    BoxRenderer box_renderer3 = new BoxRenderer(go_test3);
    box_renderer3.SetWidth(75)
        .SetHeight(5)
        .SetColor(Color.CYAN)
        .SetCenter(new Point2d(75 / 2.0, 2.5));
    try {
      go_test3.addRenderer(box_renderer3);
      go_test3.getTransform().setPosition(new Point2d(55, 10));
      go_test3.addCollider()
          .setShape(new Point2d(0, 0), new Point2d(75, 5))
          .setCenter(new Point2d(75 / 2.0, 2.5))
          .setElasticity(1);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test3);

    GameObject go_test4 = new GameObject();
    BoxRenderer box_renderer4 = new BoxRenderer(go_test4);
    box_renderer4.SetWidth(5)
        .SetHeight(75)
        .SetColor(Color.CYAN)
        .SetCenter(new Point2d(2.5, 75 / 2.0));
    try {
      go_test4.addRenderer(box_renderer4);
      go_test4.getTransform().setPosition(new Point2d(55 + 75 / 2.0, (45 + 10) / 2.0));
      go_test4.addCollider()
          .setShape(new Point2d(0, 0), new Point2d(5, 75))
          .setCenter(new Point2d(2.5, 75 / 2.0))
          .setElasticity(1);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test4);
  }
}
