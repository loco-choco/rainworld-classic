package com.locochoco.game;

public class Food extends Item {
  public int food_pips;

  public void OnCreated() {
    food_pips = 1;
  }

  public void Start() {
    super.Start();
  }

  public int AmountOfFoodPips() {
    return food_pips;
  }
}
