package com.locochoco.gameengine;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import java.lang.Exception;

/**
 * Representation of a Object in game
 */
public final class Collider extends Component {
  private boolean physical;
  private Point2d center;
  private Point2d corner_a;
  private Point2d corner_b;

  private Transform transform;

  public Collider(GameObject owner) throws Exception {
    super(owner);
    transform = owner.getTransform();
    center = new Point2d(0, 0);
    corner_a = new Point2d(0, 0);
    corner_b = new Point2d(0, 0);
    physical = true;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public Collider setCenter(Point2d center) {
    this.center = center;
    return this;
  }

  public Collider setShape(Point2d corner_a, Point2d corner_b) throws Exception {
    if (corner_a.getX() > corner_b.getX() || corner_a.getY() > corner_b.getY())
      throw new Exception("The collider needs to be a convex shape!");
    this.corner_a = corner_a;
    this.corner_b = corner_b;
    return this;
  }

  public Collider setPhysical(boolean physical) {
    this.physical = physical;
    return this;
  }

  public Point2d getCenter() {
    return center;
  }

  public Point2d getCornerA() {
    return corner_a;
  }

  public Point2d getCornerB() {
    return corner_b;
  }

  public boolean getPhysical() {
    return physical;
  }

  public CollisionData CheckCollision(Collider other) {
    CollisionData data = new CollisionData();
    Point2d our_pos = transform.getPosition();
    Point2d other_pos = other.getGameObject().getTransform().getPosition();

    // Calculating our center and corners relative to the world
    Point2d our_center_world = new Point2d(our_pos);
    our_center_world.sub(center);
    Point2d our_corner_a_world = new Point2d(our_center_world);
    our_corner_a_world.add(corner_a);
    Point2d our_corner_b_world = new Point2d(our_center_world);
    our_corner_b_world.add(corner_b);
    // Calculating the other center and corners relative to the world
    Point2d other_center_world = new Point2d(other_pos);
    other_center_world.sub(other.getCenter());
    Point2d other_corner_a_world = new Point2d(other_center_world);
    other_corner_a_world.add(other.getCornerA());
    Point2d other_corner_b_world = new Point2d(other_center_world);
    other_corner_b_world.add(other.getCornerB());

    // See https://aalopes.com/blog/ for Box collision
    // This shows the direction/magnitude to move the OTHER box to avoid collision,
    // so we will negate when passing the collision vector
    double x_overlap = Math.min(our_corner_b_world.getX(), other_corner_b_world.getX())
        - Math.max(our_corner_a_world.getX(), other_corner_a_world.getX());
    double y_overlap = Math.min(our_corner_b_world.getY(), other_corner_b_world.getY())
        - Math.max(our_corner_a_world.getY(), other_corner_a_world.getY());
    // If they are at both at 0, means the two boxes are touching, but there is no
    // actual need to move (rare case but makes floor detection always work)
    System.out.println(x_overlap + " " + y_overlap);
    System.out.println(our_corner_a_world + " " + our_corner_b_world + " " + our_pos);
    if (x_overlap >= 0 && y_overlap >= 0)
      data.setCollision(true);
    else
      data.setCollision(false);
    if (x_overlap >= y_overlap)
      data.setCollisionVector(new Vector2d(x_overlap, 0));
    else
      data.setCollisionVector(new Vector2d(0, y_overlap)); // TODO FIX
    return data;
  }

}
