package com.locochoco.editor;

import java.util.ArrayList;
import java.util.HashMap;

import com.locochoco.game.Pipe;
import com.locochoco.gameengine.*;

public class PipeGenerator extends Component {
  public PipeInfo pipes[];
  private HashMap<Integer, Pipe> pipes_objs;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    pipes_objs = new HashMap<>();
    for (PipeInfo info : pipes)
      GeneratePipe(info);
    for (PipeInfo info : pipes) {
      for (int id : info.connections)
        pipes_objs.get(info.id).Connect(pipes_objs.get(id));
    }

    getGameObject().MarkToDestruction();
    this.setEnabled(false);
  }

  private void GeneratePipe(PipeInfo info) {
    GameObject obj = GameEngine.getGameEngine().getLevel().LoadGameObjectFromJson(info.file_name,
        getGameObject().getParent());
    obj.getTransform().setGlobalPosition(info.position);
    pipes_objs.put(info.id, (Pipe) obj.getComponent(Pipe.class));
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
