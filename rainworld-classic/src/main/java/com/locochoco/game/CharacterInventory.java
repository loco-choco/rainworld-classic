package com.locochoco.game;

import java.awt.event.KeyEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class CharacterInventory extends Component implements CollisionListener {

  private InputAPI inputs;

  private Collider collider;

  private GameObject left_arm;
  private GameObject left_hand;
  private Item left_hand_item;

  private GameObject right_arm;
  private GameObject right_hand;
  private Item right_hand_item;

  private RigidBody rigidBody;
  public double throw_velocity;
  private Vector2d last_looked_direction;

  boolean pressed_use_item;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
    last_looked_direction = new Vector2d(1, 0);
    pressed_use_item = false;
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

    left_arm = go.findFirstChild("left_arm");
    left_hand = left_arm.findFirstChild("hand");
    right_arm = go.findFirstChild("right_arm");
    right_hand = right_arm.findFirstChild("hand");
    left_arm.setEnabled(false);
    right_arm.setEnabled(false);
  }

  public void PhysicsUpdate(double delta_time) {
    Vector2d vel = rigidBody.GetVelocity();
    if (vel.lengthSquared() != 0) {
      last_looked_direction = new Vector2d(vel);
      last_looked_direction.normalize();
    }
  }

  public void OnCollision(CollisionData data) {
    Item item = (Item) data.getOtherCollider().getGameObject().getComponent(Item.class);
    if (item == null) // We only care about items
      return;
    if (left_hand_item == null) { // Adding first to the left hand
      AddItemToHand(left_hand, left_arm, item);
      left_hand_item = item;
    } else if (right_hand_item == null) { // Then to the right hand
      AddItemToHand(right_hand, right_arm, item);
      right_hand_item = item;
    }
    // If not enough space, ignore
  }

  public void OnEnterCollision(Collider collider) {
  }

  public void OnExitCollision(Collider collider) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
    boolean use_item_input = inputs.GetKeyPressed(KeyEvent.VK_X);
    boolean use_item = use_item_input;
    if (use_item_input && pressed_use_item)
      use_item = false;
    pressed_use_item = use_item_input;
    boolean drop_item = inputs.GetKeyPressed(KeyEvent.VK_DOWN) && use_item;
    GameObject main_hand = null;
    GameObject main_arm = null;
    Item main_item = null;
    if (left_hand_item != null) { // Left Hand has Priority
      main_item = left_hand_item;
      main_hand = left_hand;
      main_arm = left_arm;
    } else if (right_hand_item != null) { // Then right_hand
      main_item = right_hand_item;
      main_hand = right_hand;
      main_arm = right_arm;
    }
    // If no items, pass
    if (main_item == null)
      return;

    if (drop_item) {
      ThrowItemFromHand(main_hand, main_arm, main_item, new Vector2d(0, 0));
    } else if (use_item) {
      InteractWithItem(main_hand, main_arm, main_item);
    }
  }

  private void AddItemToHand(GameObject hand, GameObject arm, Item item) {
    GameObject item_go = item.getGameObject();
    if (!item.TryToBeGrabbed())
      return;
    try {
      item_go.setParent(hand);

    } catch (Exception e) {
      System.err.printf("Issues attaching item %s to hand on arm %s\n", item_go.getName(), arm.getName());
      return;
    }
    item_go.getTransform().setPosition(new Point2d(0, 0));
    arm.setEnabled(true);
  }

  private void InteractWithItem(GameObject hand, GameObject arm, Item item) {
    // TODO ADD ITEM INTERACION BUT BETTER
    if (item instanceof Food food) {
      int food_pips = food.AmountOfFoodPips();
      System.out.printf("Ate %s pips\n", food_pips);
      // TODO Destroy Food Item
    } else {
      Vector2d vel = new Vector2d(last_looked_direction);
      vel.scale(throw_velocity);
      ThrowItemFromHand(hand, arm, item, vel);
    }
  }

  private void ThrowItemFromHand(GameObject hand, GameObject arm, Item item, Vector2d velocity) {
    GameObject item_go = item.getGameObject();
    try {
      item_go.setParent(null);

    } catch (Exception e) {
      System.err.printf("Issues detaching item %s from hand on arm %s\n", item_go.getName(), arm.getName());
      return;
    }
    item.Throw(velocity);
    arm.setEnabled(false);
  }

  public void LateUpdate(double delta_time) {
  }
}
