package com.locochoco.game;

import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

class PipedObject {
  private double time_in_pipe;
  private double time_per_pipe;
  private Pipe origin_pipe;
  private Pipeable object;

  public PipedObject(Pipeable object) {
    this.time_per_pipe = object.time_per_pipe;
    this.object = object;
    this.origin_pipe = null;
    this.time_in_pipe = 0;
  }

  public Pipe GetOriginPipe() {
    return origin_pipe;
  }

  public void SetOriginPipe(Pipe pipe) {
    origin_pipe = pipe;
  }

  public void ResetTimeInPipe() {
    time_in_pipe = 0;
  }

  public void UpdateTimeInPipe(double delta_time) {
    time_in_pipe += delta_time;
  }

  public boolean WasTimeInPipeServed() {
    return time_in_pipe >= time_per_pipe;
  }

  public Pipeable Release(Point2d release_position) {
    object.ExitPipe(release_position);
    return object;
  }

  public void Enter() {
    object.EnterPipe();
  }
}

public abstract class Pipe extends Component {

  private HashSet<Pipe> connections;
  private ArrayList<PipedObject> piped_objects;

  public void OnCreated() {
    connections = new HashSet<>();
    piped_objects = new ArrayList<>();
  }

  public void OnDestroyed() {
    for (PipedObject object : piped_objects)
      object.Release(getGameObject().getTransform().getGlobalPosition());
    piped_objects.clear();
    ReciprocalDisconnectAll();
  }

  public void Update(double delta_time) {
    ArrayList<PipedObject> objects_to_pass = new ArrayList<>();
    for (PipedObject object : piped_objects) {
      object.UpdateTimeInPipe(delta_time);
      if (object.WasTimeInPipeServed())
        objects_to_pass.add(object);
    }
    for (PipedObject object : objects_to_pass)
      PassToNextPipe(object);

    objects_to_pass.clear();
  }

  public ArrayList<PipedObject> GetObjectsBeingPiped() {
    return piped_objects;
  }

  public void ReceiveFromPipe(PipedObject object) {
    piped_objects.add(object);
    object.ResetTimeInPipe();
  }

  public void PassToNextPipe(PipedObject object) {
    piped_objects.remove(object);
    Pipe last_pipe = object.GetOriginPipe();
    for (Pipe pipe : connections) {
      if (pipe != last_pipe) {
        object.SetOriginPipe(this);
        pipe.ReceiveFromPipe(object);
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
