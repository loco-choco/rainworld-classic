package com.locochoco.game;

import com.locochoco.gameengine.*;

public class EntrancePipe extends Pipe implements CollisionListener {

  Collider entrance;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    super.OnDestroyed();
    entrance.removeCollisionListener(this);
  }

  public void Start() {
    super.Start();

    entrance = getGameObject().findFirstChild("entrance").getCollider();
    entrance.addCollisionListener(this);
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public void ReceiveFromPipe(PipedObject object) {
    object.Release(getGameObject().getTransform().getGlobalPosition());
  }

  public void OnCollision(CollisionData data) {
    GameObject creature = data.getOtherCollider().getGameObject();
    Pipeable object = (Pipeable) creature.getComponent(Pipeable.class);
    if (object == null)
      return;
    PipedObject pipedObject = new PipedObject(object, object.GetTimePerPipe());
    pipedObject.SetOriginPipe(this);
    PassToNextPipe(pipedObject);
  }

  public void OnEnterCollision(Collider collider) {
  }

  public void OnExitCollision(Collider collider) {
  }

}
