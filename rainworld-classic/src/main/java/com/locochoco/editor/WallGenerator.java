package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.*;

public class WallGenerator extends ObjectGenerator {
  public int width;
  public int height;

  protected void ConfigObject(GameObject obj) {
    super.ConfigObject(obj);
    try {
      obj.setParent(getGameObject().getParent());
    } catch (Exception e) {
      System.err.println("Failed to set wall parent to the generator parent");
    }
    BoxCollider collider = (BoxCollider) obj.getCollider();
    try {
      collider.setShape(width, height);
    } catch (Exception e) {
      System.err.println("Wrong shape format to wall collider: " + e.getMessage());
    }
    collider.setCenter(null);
    BoxRenderer renderer = (BoxRenderer) obj.getRenderer();
    renderer.SetWidth(width).SetHeight(height);
    renderer.SetCenter(null);
  }

}
