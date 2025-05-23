package com.locochoco.editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.BoxRenderer;

public abstract class PipeTile extends Tile {
  private int id;
  private String file_name;
  private HashSet<PipeTile> connections;

  public PipeTile(EditorController controller, String file_name, Point2d position, int id) {
    super(controller.RoundClosestPointDown(position), controller.RoundClosestPointUp(position), Color.RED);
    this.id = id;
    this.file_name = file_name;
    this.connections = new HashSet<>();
    BoxRenderer renderer = (BoxRenderer) (representation.getRenderer());
    renderer.layer = "foreground";
  }

  public String GetFileName() {
    return file_name;
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

  public ArrayList<Integer> GetConnectionsIds() {
    ArrayList<Integer> ids = new ArrayList<>();
    for (PipeTile pipe : connections)
      ids.add(pipe.GetId());
    return ids;
  }

  public boolean CheckConnection(PipeTile pipe) {
    return connections.contains(pipe);
  }

  public void SaveToJson(JsonGenerator generator, ObjectMapper mapper) {
  }
  public  void ReadFromJson(JsonNode node, ObjectMapper mapper){}
}
