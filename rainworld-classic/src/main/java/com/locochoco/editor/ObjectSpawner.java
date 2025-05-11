package com.locochoco.editor;

import com.locochoco.gameengine.*;

public abstract class ObjectSpawner extends Component {
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
    GameObject obj = GameEngine.getGameEngine().getLevel().LoadGameObjectFromJson(file_name, null);
    obj.getTransform().position = getGameObject().getTransform().getGlobalPosition();
    ConfigObject(obj);
    getGameObject().MarkToDestruction();
  }

  protected abstract void ConfigObject(GameObject obj);

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }
}
