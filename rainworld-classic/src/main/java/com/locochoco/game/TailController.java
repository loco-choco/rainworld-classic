package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class TailController extends Component {

  private RigidBody rigidBody;
  private SpriteRenderer tail;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    GameObject go = getGameObject();
    rigidBody = go.getParent().getRigidBody();

    Renderer r = go.getRenderer();
    if (r instanceof SpriteRenderer tail)
      this.tail = tail;
    else
      System.err.println("This component was attached to a non SpriteRenderer object! Check if this is intended!");
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {

  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
