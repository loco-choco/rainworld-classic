package com.locochoco.game;

import com.locochoco.gameengine.*;

public class PlayerData extends Component {

  private int food_pips_ate;

  private static PlayerData instance;

  public void OnCreated() {
    food_pips_ate = 0;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    instance = this;
  }

  public void OnDestroyed() {
    instance = null;
  }

  public static int GetFoodAte() {
    return instance.food_pips_ate;
  }

  public static void AteFood(int pips) {
    instance.food_pips_ate += pips;
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
