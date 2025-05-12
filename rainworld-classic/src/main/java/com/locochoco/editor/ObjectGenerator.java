package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class ObjectGenerator extends Component {
  public String file_name;
  public Point2d position;

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
    ConfigObject(obj);
    getGameObject().MarkToDestruction();
  }

  protected void ConfigObject(GameObject obj) {
    obj.getTransform().position = position;
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
