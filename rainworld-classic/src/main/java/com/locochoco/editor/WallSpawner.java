package com.locochoco.editor;

import com.locochoco.gameengine.*;

public class WallSpawner extends ObjectSpawner {
  protected void ConfigObject(GameObject obj) {
    BoxCollider our = (BoxCollider) getGameObject().getCollider();
    BoxCollider collider = (BoxCollider) obj.getCollider();
    collider.setCenter(our.getCenter());
    try {
      collider.setShape(our.getCornerA(), our.getCornerB());
    } catch (Exception e) {
      System.err.println("Wrong shape format to wall collider: " + e.getMessage());
    }
    BoxRenderer renderer = (BoxRenderer) obj.getRenderer();
    int width = (int) (our.getCornerB().getX() - our.getCornerA().getX());
    int height = (int) (our.getCornerB().getY() - our.getCornerA().getY());
    renderer.SetWidth(width).SetHeight(height);
    renderer.SetCenter(null);
  }

}
