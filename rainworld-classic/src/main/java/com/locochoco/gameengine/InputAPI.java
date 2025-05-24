package com.locochoco.gameengine;

import java.io.File;

import javax.vecmath.Point2d;

/**
 * Handles all the graphic logic and calculations
 */
public interface InputAPI {

  public Point2d GetMousePos();

  public boolean GetMouseRightClick();

  public boolean GetMouseLeftClick();

  public boolean GetKeyPressed(int key_code);

  public void SubscribeToDND(DNDSubscriber subscriber);

  public void UnsubscribeToDND(DNDSubscriber subscriber);
}

interface DNDSubscriber {
  public void ReceiveDNDFile(File file);
}
