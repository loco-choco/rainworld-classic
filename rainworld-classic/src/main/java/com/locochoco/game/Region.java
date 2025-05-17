package com.locochoco.game;

import java.util.HashMap;

import com.locochoco.editor.RoomInfo;
import com.locochoco.gameengine.*;

public class Region extends Component {
  public RoomInfo[] rooms_info;

  private HashMap<String, Room> rooms;

  public void OnCreated() {
    rooms = new HashMap<>();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    for (RoomInfo info : rooms_info) {
      GameObject room_go = new GameObject();
      try {
        Room room = (Room) room_go.addComponent(new Room());
        room.SetFileName(info.file_name);
        rooms.put(info.file_name, room);
        room_go.getTransform().setPosition(info.position);
      } catch (Exception e) {
        System.err.printf("Issue adding components to room_go (%s): %s\n", info.file_name, e.getMessage());
        continue;
      }
      GameEngine.getGameEngine().getLevel().AddGameObject(room_go);
    }
  }

  public Room GetRoom(String file_name) {
    return rooms.get(file_name);
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
