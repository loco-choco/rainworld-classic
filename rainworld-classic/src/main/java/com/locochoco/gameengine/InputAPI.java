package com.locochoco.gameengine;

import javax.vecmath.Point2d;

/**
 * Handles all the graphic logic and calculations
 */
public interface InputAPI {

  public Point2d GetMousePos();

  public boolean GetMouseRightClick();

  public boolean GetKeyPressed(int key_code);
}
