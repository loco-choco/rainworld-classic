package com.locochoco.game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class TailController extends Component {

  private Transform transform;
  private RigidBody rigidBody;
  private boolean was_going_to_the_right;
  private SpriteRenderer tail;

  public void OnCreated() {
    was_going_to_the_right = true;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    GameObject go = getGameObject();
    rigidBody = go.getParent().getRigidBody();
    transform = go.getTransform();

    Renderer r = go.getRenderer();
    if (r instanceof SpriteRenderer tail)
      this.tail = tail;
    else
      System.err.println("This component was attached to a non SpriteRenderer object! Check if this is intended!");
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    double horizontal = rigidBody.GetVelocity().getX();
    if (horizontal == 0)
      return;
    boolean going_to_the_right = horizontal > 0;
    if (going_to_the_right && !was_going_to_the_right || !going_to_the_right && was_going_to_the_right) {
      BufferedImage image = tail.GetSprite();
      AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-image.getWidth(), 0);
      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      image = op.filter(image, null);
      tail.SetSprite(image);

      Point2d pos = new Point2d(transform.getPosition());
      pos.setX(-pos.getX());
      transform.setPosition(pos);
    }

    was_going_to_the_right = going_to_the_right;
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
