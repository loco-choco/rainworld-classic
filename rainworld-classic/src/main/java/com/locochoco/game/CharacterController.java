package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class CharacterController extends Component implements CollisionListener {

  private InputAPI inputs;
  private RigidBody rigidbody;
  private Collider collider;
  public double max_speed;
  public double acceleration;
  public double air_acceleration;
  public double jump_velocity;
  public double wall_jump_height_contribution;
  public double wall_jump_horizontal_contribution;
  public double jump_coyote_time;
  public double jump_antispam_timeout;

  private boolean jumped_was_pressed;
  private boolean is_grounded;
  private Vector2d ground_normal;
  private double time_ungrounded;
  private double time_since_last_jump;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
    jumped_was_pressed = false;
    is_grounded = false;
    ground_normal = new Vector2d();
    time_ungrounded = 0;
    time_since_last_jump = 0;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    rigidbody = getGameObject().getRigidBody();
    collider = getGameObject().getCollider();
    collider.addCollisionListener(this);
    CameraFollower.SetFollower(this.getGameObject().getTransform());
  }

  public void OnDestroyed() {
    collider.removeCollisionListener(this);
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
    boolean jump = (is_grounded || (time_ungrounded < jump_coyote_time) && (time_since_last_jump == 0))
        && !jumped_was_pressed && inputs.GetKeyPressed(KeyEvent.VK_Z);
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_LEFT) ? -1 : 0;
    horizontal_movement += inputs.GetKeyPressed(KeyEvent.VK_RIGHT) ? 1 : 0;
    // Movement Force
    Vector2d movement_force = new Vector2d(0, 0);
    Vector2d jump_force = new Vector2d(0, -1);
    jump_force.scaleAdd(wall_jump_height_contribution, ground_normal);
    if (jump_force.length() != 0) {
      jump_force.normalize();
    }
    jump_force.setX(jump_force.getX() * wall_jump_horizontal_contribution);
    // Horizontal Force
    double horizontal_velocity = rigidbody.GetVelocity().getX();
    double acceleration = is_grounded ? this.acceleration : this.air_acceleration;
    double target_acceleration = Math.min(Math.max(acceleration, 0.0),
        Math.abs(Math.min(Math.abs(horizontal_velocity), max_speed) * Math.signum(horizontal_velocity)
            - max_speed * horizontal_movement) / delta_time);
    movement_force.setX(target_acceleration * horizontal_movement);
    // Jump Force
    jump_force.scale(jump ? jump_velocity / delta_time : 0);
    movement_force.add(jump_force);
    // Force Adition
    movement_force.scale(rigidbody.getMass());
    rigidbody.AddForce(movement_force);
    // Jump button debouncing
    jumped_was_pressed = inputs.GetKeyPressed(KeyEvent.VK_Z);
    // Coyote and Jump Antispam Time time Passing
    time_since_last_jump -= delta_time;
    if (time_since_last_jump < 0)
      time_since_last_jump = 0;
    if (!is_grounded)
      time_ungrounded += delta_time;
    if (jump) {
      time_ungrounded = jump_coyote_time;
      time_since_last_jump = jump_antispam_timeout;
    }
  }

  public void OnCollision(CollisionData data) {
    Vector2d ground_normal = new Vector2d(data.getCollisionVector());
    if (ground_normal.lengthSquared() != 0) {
      ground_normal.normalize();
      ground_normal.scale(-1);
      this.ground_normal = ground_normal;
      is_grounded = true;
    }
  }

  public void OnEnterCollision(Collider collider) {
  }

  public void OnExitCollision(Collider collider) {
    time_ungrounded = 0;
    is_grounded = false;
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
