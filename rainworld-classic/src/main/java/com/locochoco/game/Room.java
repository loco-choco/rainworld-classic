package com.locochoco.game;

import com.locochoco.gameengine.*;

public class Room extends Component {
  private static Room instance;
  public String next_room;

  public void OnCreated() {
    next_room = "";
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    instance = null;
  }

  public static Room GetRoom() {
    return instance;
  }

  public void Start() {
    instance = this;
  }

  public void GoToNextRoom() {
    GameEngine.getGameEngine().LoadLevel(next_room);
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
