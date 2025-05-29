package com.locochoco.game;

import com.locochoco.gameengine.*;

public class RoomExitPipe extends Pipe {
  public int pips_to_next_room;

  public void OnCreated() {
    super.OnCreated();
    pips_to_next_room = 0;
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    super.OnDestroyed();
  }

  public void Start() {
  }

  public void ReceiveFromPipe(PipedObject object) {
    CharacterController char_control = (CharacterController) object.GetPipeableObject().getGameObject()
        .getComponent(CharacterController.class);
    // If player entered this pipe, go to next level
    if (char_control != null && PlayerData.GetFoodAte() >= pips_to_next_room) {
      System.out.println("################ GOING TO NEXT ROOM ################");
      Room.GetRoom().GoToNextRoom();
      return;
    }
    super.ReceiveFromPipe(object);
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
