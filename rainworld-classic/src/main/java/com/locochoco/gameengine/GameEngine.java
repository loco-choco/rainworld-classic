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
    CreateTestLevel();// TODO REMOVE
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

  private void CreateTestLevel() {
    // Physics Settings
    physics.setGravity(new Vector2d(0, 100));
    // Level

    level = new Level();

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
    CharacterController controller = new CharacterController(go_test, inputs);
    controller.setMaxSpeed(20).setAcceleration(80).setJumpVelocity(40);
    go_test.addComponent(controller);
    level.AddGameObject(go_test);

    GameObject go_test2 = new GameObject();
    BoxRenderer box_renderer2 = new BoxRenderer(go_test2);
    box_renderer2.SetWidth(100)
        .SetHeight(5)
        .SetColor(Color.CYAN)
        .SetCenter(new Point2d(50, 2.5));
    try {
      go_test2.addRenderer(box_renderer2);
      go_test2.getTransform().setPosition(new Point2d(50, 45));
      go_test2.addCollider()
          .setShape(new Point2d(0, 0), new Point2d(100, 5))
          .setCenter(new Point2d(50, 2.5))
          .setElasticity(0);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test2);
    /*
     * GameObject go_test3 = new GameObject();
     * BoxRenderer box_renderer3 = new BoxRenderer(go_test3);
     * box_renderer3.SetWidth(5)
     * .SetHeight(5)
     * .SetColor(Color.CYAN)
     * .SetCenter(new Point2d(75 / 2.0, 2.5));
     * try {
     * go_test3.addRenderer(box_renderer3);
     * go_test3.getTransform().setPosition(new Point2d(55, 10));
     * go_test3.addCollider()
     * .setShape(new Point2d(0, 0), new Point2d(75, 5))
     * .setCenter(new Point2d(75 / 2.0, 2.5))
     * .setElasticity(0.8);
     * } catch (Exception e) {
     * System.err.println(e.getMessage());
     * }
     * level.AddGameObject(go_test3);
     */

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
          .setElasticity(0);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    level.AddGameObject(go_test4);
  }
}
