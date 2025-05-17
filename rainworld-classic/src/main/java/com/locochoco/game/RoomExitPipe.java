package com.locochoco.game;

import com.locochoco.gameengine.*;

public class RoomExitPipe extends Pipe {
  public RoomExitType type;
  private Room room;

  public void OnCreated() {
    super.OnCreated();
    type = RoomExitType.NORTH;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    super.OnDestroyed();
  }

  public void Start() {
    room = (Room) getGameObject().getParent().getParent().getComponent(Room.class);
    room.SetPipe(this, type);
  }

  public boolean AtemptToPassBeforeReleasing(PipedObject object) {
    Pipe exit = room.GetPipeExit(type);
    if (exit == null)
      return false;
    object.SetOriginPipe(this);
    exit.ReceiveFromPipe(object);
    return true;
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

}
