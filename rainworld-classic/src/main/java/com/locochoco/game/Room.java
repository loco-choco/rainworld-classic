package com.locochoco.game;

import java.util.HashMap;

import com.locochoco.gameengine.*;

public class Room extends Component {
  private Region region;
  private String file_name;

  private HashMap<RoomExitType, Pipe> pipes;
  private HashMap<RoomExitType, RoomConnectionInfo> pipes_exit;

  public void OnCreated() {

    region = null;
    file_name = "";
    pipes = new HashMap<>();
    pipes_exit = new HashMap<>();
  }

  public void SetFileName(String file_name) {
    this.file_name = file_name;
  }

  public String GetFileName() {
    return file_name;
  }

  public void SetRegion(Region region) {
    this.region = region;
  }

  public Region GetRegion() {
    return region;
  }

  public void SetPipe(Pipe pipe, RoomExitType type) {
    if (pipes.containsKey(type))
      pipes.replace(type, pipe);
    else
      pipes.put(type, pipe);
  }

  public void SetPipeExit(RoomConnectionInfo connection, RoomExitType for_pipe) {
    if (pipes_exit.containsKey(for_pipe))
      pipes_exit.replace(for_pipe, connection);
    else
      pipes_exit.put(for_pipe, connection);
  }

  public Pipe GetPipeExit(RoomExitType type) {
    return GetPipeExit(pipes_exit.get(type));
  }

  public Pipe GetPipe(RoomExitType type) {
    return pipes.get(type);
  }

  private Pipe GetPipeExit(RoomConnectionInfo info) {
    if (region == null)
      return null;
    if (info == null)
      return null;
    Room exit_region = region.GetRoom(info.other_room);
    if (exit_region == null)
      return null;
    return exit_region.GetPipe(info.type);

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
