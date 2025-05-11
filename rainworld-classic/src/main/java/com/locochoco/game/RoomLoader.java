package com.locochoco.game;

import com.locochoco.gameengine.*;

public class RoomLoader extends Component {
  public String file_name;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    GameEngine.getGameEngine().getLevel().AdditiveLevelLoad(file_name, getGameObject());
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
