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

  private double elasticity;

  private Transform transform;

  public void OnCreated() {
    transform = getGameObject().getTransform();
    center = new Point2d(0, 0);
    corner_a = new Point2d(0, 0);
    corner_b = new Point2d(0, 0);
    physical = true;
    elasticity = 0;
  }

  public void Start() {
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

  public Collider setElasticity(double elasticity) {
    this.elasticity = elasticity;
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

  public double getElasticity() {
    return elasticity;
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

    Vector2d overlap_vector_our_a_other_b = new Vector2d(other_corner_b_world);
    overlap_vector_our_a_other_b.sub(our_corner_a_world);

    Vector2d overlap_vector_our_b_other_a = new Vector2d(other_corner_a_world);
    overlap_vector_our_b_other_a.sub(our_corner_b_world);

    if (overlap_vector_our_a_other_b.getX() >= 0 && overlap_vector_our_a_other_b.getY() >= 0
        && overlap_vector_our_b_other_a.getX() <= 0 && overlap_vector_our_b_other_a.getY() <= 0)
      data.setCollision(true);
    else
      data.setCollision(false);

    Vector2d collision_vector = new Vector2d(0, 0);
    // Getting the smalled step to handle the collision from the 2 vectors
    Vector2d collision_handling_vector = new Vector2d();
    if (Math.abs(overlap_vector_our_a_other_b.getX()) <= Math.abs(overlap_vector_our_b_other_a.getX()))
      collision_handling_vector.setX(overlap_vector_our_a_other_b.getX());
    else
      collision_handling_vector.setX(overlap_vector_our_b_other_a.getX());

    if (Math.abs(overlap_vector_our_a_other_b.getY()) <= Math.abs(overlap_vector_our_b_other_a.getY()))
      collision_handling_vector.setY(overlap_vector_our_a_other_b.getY());
    else
      collision_handling_vector.setY(overlap_vector_our_b_other_a.getY());

    // Get the smallest X,Y or XY change to handle the collision
    if (Math.abs(collision_handling_vector.getX()) <= Math.abs(collision_handling_vector.getY()))
      collision_vector.setX(collision_handling_vector.getX());
    if (Math.abs(collision_handling_vector.getY()) <= Math.abs(collision_handling_vector.getX()))
      collision_vector.setY(collision_handling_vector.getY());

    data.setCollisionVector(collision_vector);

    return data;
  }

}
