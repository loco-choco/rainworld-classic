package com.locochoco.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class CharacterInventory extends Component implements CollisionListener {

  private InputAPI inputs;

  private Collider collider;

  private ArrayList<CarryingLimb> carrying_limbs;

  private RigidBody rigidBody;
  public double throw_velocity;
  private Vector2d last_looked_direction;

  boolean pressed_use_item;

  public void OnCreated() {
    inputs = GameEngine.getGameEngine().getInputs();
    carrying_limbs = new ArrayList<>();
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

    GameObject limbs = go.findFirstChild("limbs");
    for (GameObject limb : limbs.getChildren()) {
      CarryingLimb carrying_limb = (CarryingLimb) limb.getComponent(CarryingLimb.class);
      if (carrying_limb != null)
        carrying_limbs.add(carrying_limb);
    }
  }

  public void OnDestroyed() {
    collider.removeCollisionListener(this);
    for (CarryingLimb limb : carrying_limbs) {
      if (limb.IsCarryingItem()) {
        ThrowItem(limb, new Vector2d(0, 0));
      }
    }
  }

  public void PhysicsUpdate(double delta_time) {
    // TODO DO THIS BETTER
    // Vector2d vel = rigidBody.GetVelocity();
    // if (vel.lengthSquared() != 0) {
    // last_looked_direction = new Vector2d(vel);
    // last_looked_direction.normalize();
    // }
  }

  public void OnCollision(CollisionData data) {
    Item item = (Item) data.getOtherCollider().getGameObject().getComponent(Item.class);
    if (item == null) // We only care about items
      return;
    // Try to give the item to the limbs in order
    for (CarryingLimb carrying_limb : carrying_limbs) {
      if (!carrying_limb.IsCarryingItem()) {
        try {
          carrying_limb.GrabItem(item);
          return;
        } catch (Exception e) {
          System.err.printf("Issues attaching item %s to hand on arm %s\n",
              item.getGameObject().getName(),
              carrying_limb.getGameObject().getName());
          return;
        }
      }
    }
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
    CarryingLimb main_limb = null;
    for (CarryingLimb carrying_limb : carrying_limbs) {
      if (carrying_limb.IsCarryingItem()) {
        main_limb = carrying_limb;
        break;
      }
    }
    if (main_limb == null)
      return;
    if (drop_item) {
      ThrowItem(main_limb, new Vector2d(0, 0));
    } else if (use_item) {
      InteractWithItem(main_limb);
    }
  }

  private void InteractWithItem(CarryingLimb limb) {
    Item item = limb.GetHeldItem();
    // TODO ADD ITEM INTERACION BUT BETTER
    if (item instanceof Food food) {
      int food_pips = food.AmountOfFoodPips();
      System.out.printf("Ate %s pips\n", food_pips);
      // TODO Destroy Food Item
    } else {
      Vector2d vel = new Vector2d(last_looked_direction);
      vel.scale(throw_velocity);
      ThrowItem(limb, vel);
    }
  }

  private void ThrowItem(CarryingLimb limb, Vector2d velocity) {
    Item item;
    try {
      item = limb.ReleaseItem();
    } catch (Exception e) {
      System.err.printf("Issues detaching item from limb %s\n",
          limb.getGameObject().getName());
      return;
    }
    Vector2d vel_to_set = new Vector2d(velocity);
    vel_to_set.add(rigidBody.GetVelocity());
    item.getGameObject().getRigidBody().SetVelocity(vel_to_set);
  }

  public void LateUpdate(double delta_time) {
  }
}
