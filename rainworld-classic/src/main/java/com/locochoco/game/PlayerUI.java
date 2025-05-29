package com.locochoco.game;

import java.util.ArrayList;

import com.locochoco.gameengine.*;

public class PlayerUI extends Component {
  private ArrayList<Renderer> pips;

  public void OnCreated() {
    pips = new ArrayList<>();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    for (GameObject c : getGameObject().getChildren())
      pips.add(c.getRenderer());
  }

  public void OnDestroyed() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    // Makes UI that shows the amount of pips ate
    for (Renderer renderer : pips)
      renderer.setEnabled(false);
    for (int i = 0; (i < PlayerData.GetFoodAte()) && i < pips.size(); i++)
      pips.get(i).setEnabled(true);
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
