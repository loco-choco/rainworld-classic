package com.locochoco.editor;

import java.awt.Color;
import java.util.HashSet;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public abstract class PipeTile extends Tile {
  private int id;
  private HashSet<PipeTile> connections;

  public PipeTile(EditorController controller, Point2d position, int id) {
    super(controller.RoundClosestPointDown(position), controller.RoundClosestPointUp(position));
    this.id = id;
    this.connections = new HashSet<>();
    try {
      // spawner = representation.addComponent(new CreatureSpawner());
      // spawner.file_name = "objects/creatures/slugcat.json";
      // spawner.setEnabled(false);
    } catch (Exception e) {
      System.err.println("Couldn't add component");
    }
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.layer = "foreground";
    renderer.SetColor(Color.RED);
  }

  public int GetId() {
    return id;
  }

  public void ReciprocalConnection(PipeTile pipe) {
    pipe.Connect(this);
    Connect(pipe);
  }

  public void Connect(PipeTile pipe) {
    connections.add(pipe);
  }

  public void ReciprocalDisconnection(PipeTile pipe) {
    pipe.Disconnect(this);
    Disconnect(pipe);
  }

  public void Disconnect(PipeTile pipe) {
    connections.remove(pipe);
  }

  public void DisconnectAll() {
    connections.clear();
  }

  public void ReciprocalDisconnectAll() {
    for (PipeTile pipe : connections)
      pipe.Disconnect(this);
    connections.clear();
  }

  public HashSet<PipeTile> GetConnections() {
    return new HashSet<>(connections);
  }

  public boolean CheckConnection(PipeTile pipe) {
    return connections.contains(pipe);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
    // spawner.setEnabled(true);
    representation.Deserialize(generator, mapper);
    // spawner.setEnabled(false);
  }
}
