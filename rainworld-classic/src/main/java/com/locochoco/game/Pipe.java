package com.locochoco.game;

import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

class PipedObject {
  public double time_entered_pipe;
  public Pipe last_pipe;
  public Pipeable object;

  public void Release(Point2d release_position) {
    object.Release(release_position);
  }

  public void Enter() {
    object.Enter();
  }
}
// TODO IMPLEMENT Pipeable INTERFACE AND ADD THAT TO CREATURES AND ITEMS

public class Pipe extends Component {

  private HashSet<Pipe> connections;
  private ArrayList<PipedObject> piped_objects;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
  }

  public void Start() {
    connections = new HashSet<>();
    piped_objects = new ArrayList<>();
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public void ReceiveFromPipe(Pipe passer, PipedObject object) {
    piped_objects.add(object);
    piped_objects.last_pipe = passer;
  }

  public void PassToNextPipe(PipedObject object) {
    Pipe last_pipe = object.last_pipe;
    for (Pipe pipe : connections) {
      if (pipe != last_pipe) {
        pipe.ReceiveFromPipe(this, object);
        return;
      }
    }
    System.err.println("No pipe to pass to! Releasing the object early");
    object.Release(getGameObject().getTransform().getGlobalPosition());
  }

  public void ReciprocalConnection(Pipe pipe) {
    pipe.Connect(this);
    Connect(pipe);
  }

  public void Connect(Pipe pipe) {
    connections.add(pipe);
  }

  public void ReciprocalDisconnection(Pipe pipe) {
    pipe.Disconnect(this);
    Disconnect(pipe);
  }

  public void Disconnect(Pipe pipe) {
    connections.remove(pipe);
  }

  public void DisconnectAll() {
    connections.clear();
  }

  public void ReciprocalDisconnectAll() {
    for (Pipe pipe : connections)
      pipe.Disconnect(this);
    connections.clear();
  }

  public HashSet<Pipe> GetConnections() {
    return new HashSet<>(connections);
  }

}
