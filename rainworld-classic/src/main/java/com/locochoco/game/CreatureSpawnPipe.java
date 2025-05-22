package com.locochoco.game;

import com.locochoco.gameengine.*;

public class CreatureSpawnPipe extends Pipe {

  public String creature_file_name;

  public void OnCreated() {
    super.OnCreated();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    super.OnDestroyed();
  }

  public void Start() {
    GameObject creature = GameEngine.getGameEngine().getLevel()
        .LoadGameObjectFromJson(creature_file_name, null);
    Pipeable object = (Pipeable) creature.getComponent(Pipeable.class);
    if (object == null) {
      System.out.printf("Spawned Creature %s should be pipeable! What did you DO?!?!\n", creature_file_name);
      return;
    }
    PipedObject pipedObject = new PipedObject(object);
    pipedObject.SetOriginPipe(this);
    pipedObject.Enter();
    PassToNextPipe(pipedObject);
  }
  public boolean AtemptToPassBeforeReleasing(PipedObject object) {
    Pipe og = object.GetOriginPipe();
    object.SetOriginPipe(this);
    og.ReceiveFromPipe(object);
    return true;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

}
