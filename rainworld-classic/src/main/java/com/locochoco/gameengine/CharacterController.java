package com.locochoco.gameengine;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector2d;

/**
 * Representation of a Object in game
 */
public class CharacterController extends Component {

  private InputAPI inputs;
  private Rigidbody rigidbody;
  private double max_speed;
  private double acceleration;
  private double jump_velocity;

  private boolean jumped_was_pressed;

  public CharacterController(GameObject owner, InputAPI inputs) {
    super(owner);
    this.inputs = inputs;
    rigidbody = owner.getRigidbody();
    jumped_was_pressed = false;
  }

  public CharacterController setMaxSpeed(double max_speed) {
    this.max_speed = max_speed;
    return this;
  }

  public CharacterController setAcceleration(double acceleration) {
    this.acceleration = acceleration;
    return this;
  }

  public CharacterController setJumpVelocity(double jump_velocity) {
    this.jump_velocity = jump_velocity;
    return this;
  }

  public void PhysicsUpdate(double delta_time) {
    // Input Gathering
    double horizontal_movement = 0;
    boolean jump = !jumped_was_pressed && inputs.GetKeyPressed(KeyEvent.VK_Z);
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_LEFT) ? -1 : 0;
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_RIGHT) ? 1 : 0;
    // Movement Force
    Vector2d movement_force = new Vector2d(0, 0);
    // Horizontal Force
    double target_acceleration = Math.clamp(acceleration, 0.0,
        Math.abs(rigidbody.GetVelocity().getX() - max_speed * horizontal_movement) / delta_time);
    movement_force.setX(target_acceleration * horizontal_movement);
    // Vertical Force
    movement_force.setY(jump ? -jump_velocity / delta_time : 0);
    // Force Adition
    movement_force.scale(rigidbody.getMass());
    rigidbody.AddForce(movement_force);
    // Jump button debouncing
    jumped_was_pressed = inputs.GetKeyPressed(KeyEvent.VK_Z);
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
