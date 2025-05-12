package com.locochoco.editor;

import java.awt.Color;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.*;

public abstract class Tile {

  protected GameObject representation;

  public Tile(Point2d start, Point2d end, Color tile_color) {
    this.representation = CreateTileObject(start, end, tile_color);
  }

  public GameObject GetGameObject() {
    return representation;
  }

  protected GameObject CreateTileObject(Point2d start, Point2d end, Color tile_color) {
    GameObject representation = new GameObject();
    GameEngine.getGameEngine().getLevel().AddGameObject(representation);
    representation.setName("tile");
    Point2d center = new Point2d(start);
    center.add(end);
    center.scale(0.5);
    try {
      BoxCollider collider = (BoxCollider) representation.addComponent(new BoxCollider());
      collider.layer = "terrain";
      collider.setShape(start, end);
      collider.setCenter(center);
      BoxRenderer renderer = (BoxRenderer) representation.addComponent(new BoxRenderer());
      Point2d diff = new Point2d(end);
      diff.sub(start);
      renderer.layer = "background";
      int width, height;
      width = (int) diff.getX();
      height = (int) diff.getY();
      renderer.SetCenter(new Point2d(width / 2, height / 2));
      renderer.SetWidth(width);
      renderer.SetHeight(height);
      renderer.SetColor(tile_color);
    } catch (Exception e) {
      System.err.println("Somethind went wrong creating tile: " + e.getMessage());
    }
    representation.getTransform().setPosition(center);
    return representation;
  }

  public void Destroy() {
    representation.MarkToDestruction();
  }

  public abstract void SaveToJson(JsonGenerator generator, ObjectMapper mapper);

}
