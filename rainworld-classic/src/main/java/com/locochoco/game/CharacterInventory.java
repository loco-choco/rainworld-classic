package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class CharacterInventory extends Component implements CollisionListener {

  private InputAPI inputs;

  private Collider collider;

  private GameObject left_hand;
  private Item left_hand_item;

  private GameObject right_hand;
  private Item right_hand_item;

  private RigidBody rigidBody;
  public double throw_velocity;
  private Vector2d last_looked_direction;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
    last_looked_direction = new Vector2d(1, 0);
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    GameObject go = getGameObject();
    collider = go.getCollider();
    collider.addCollisionListener(this);

    rigidBody = go.getRigidBody();

    left_hand = go.findFirstChild("left_hand");
    right_hand = go.findFirstChild("right_hand");
    left_hand.setEnabled(false);
    right_hand.setEnabled(false);
  }

  public void PhysicsUpdate(double delta_time) {
    Vector2d vel = rigidBody.GetVelocity();
    if (vel.lengthSquared() != 0) {
      last_looked_direction = new Vector2d(vel);
      last_looked_direction.normalize();
    }
  }

  public void OnCollision(CollisionData data, Collider collidee) {
    Item item = (Item) collidee.getGameObject().getComponent(Item.class);
    if (item == null) // We only care about items
      return;
    if (left_hand_item == null) // Adding first to the left hand
      AddItemToHand(left_hand, item);
    else if (right_hand_item == null) // Then to the right hand
      AddItemToHand(right_hand, item);
    // If not enough space, ignore
  }

  public void OnEnterCollision() {
  }

  public void OnExitCollision() {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    boolean use_item = inputs.GetKeyPressed(KeyEvent.VK_X);
    boolean drop_item = inputs.GetKeyPressed(KeyEvent.VK_DOWN) && use_item;
    GameObject main_hand = null;
    Item main_item = null;
    if (left_hand_item != null) { // Left Hand has Priority
      main_item = left_hand_item;
      main_hand = left_hand;
    } else if (right_hand_item != null) { // Then right_hand
      main_item = right_hand_item;
      main_hand = right_hand;
    }
    // If no items, pass
    if (main_item == null)
      return;

    if (drop_item) {
      ThrowItemFromHand(main_hand, main_item, new Vector2d(0, 0));
    } else if (use_item) {
      InteractWithItem(main_hand, main_item);
    }
  }

  private void AddItemToHand(GameObject hand, Item item) {
    GameObject item_go = item.getGameObject();
    try {
      item_go.setParent(hand);

    } catch (Exception e) {
      System.err.printf("Issues attaching item %s to hand %s\n", item_go.getName(), hand.getName());
      return;
    }
    item_go.getTransform().setPosition(new Point2d(0, 0));
    hand.setEnabled(true);
  }

  private void InteractWithItem(GameObject hand, Item item) {
    // TODO ADD ITEM INTERACION BUT BETTER
    if (item instanceof Food food) {
      int food_pips = food.AmountOfFoodPips();
      System.out.printf("Ate %s pips\n", food_pips);
      // TODO Destroy Food Item
    } else {
      Vector2d vel = new Vector2d(last_looked_direction);
      vel.scale(throw_velocity);
      ThrowItemFromHand(hand, item, vel);
    }
  }

  private void ThrowItemFromHand(GameObject hand, Item item, Vector2d velocity) {
    GameObject item_go = item.getGameObject();
    try {
      item_go.setParent(null);

    } catch (Exception e) {
      System.err.printf("Issues detaching item %s from hand %s\n", item_go.getName(), hand.getName());
      return;
    }
    item.Throw(velocity);
    hand.setEnabled(false);
  }

  public void LateUpdate(double delta_time) {
  }
}
