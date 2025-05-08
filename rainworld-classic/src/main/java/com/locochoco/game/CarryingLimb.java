package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class CarryingLimb extends Component {

  private GameObject item_socket;
  private GameObject visuals;
  private Item item;
  private GameObject item_go;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    GameObject go = getGameObject();

    item_socket = go.findFirstChild("item_socket");
    visuals = go.findFirstChild("visual");
    visuals.setEnabled(false);
    item = null;
    item_go = null;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public boolean IsCarryingItem() {
    return item != null;
  }

  public Item GetHeldItem() {
    return item;
  }

  public Item ReleaseItem() throws Exception {
    Item item = this.item;
    item_go.setParent(null);
    item_go.getTransform().setGlobalPosition(item_socket.getTransform().getGlobalPosition());

    visuals.setEnabled(false);
    item.GetReleased();
    this.item = null;
    item_go = null;
    return item;
  }

  public boolean GrabItem(Item item) throws Exception {
    this.item = item;
    item_go = item.getGameObject();
    if (!item.TryToBeGrabbed())
      return false;
    visuals.setEnabled(true);
    item_go.setParent(item_socket);
    item_go.getTransform().setPosition(new Point2d(0, 0));
    return true;
  }

}
