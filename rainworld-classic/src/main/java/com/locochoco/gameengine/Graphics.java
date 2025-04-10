package com.locochoco.gameengine;

import java.awt.Color;
import java.awt.Image;

import javax.vecmath.Point2i;

/**
 * Handles all the graphic logic and calculations
 */
public class Graphics {
  private GameEngine game;
  private GraphicsAPI graphics_api;

  public Graphics(GameEngine game) {
    this.game = game;
  }

  public void SetGraphicsAPI(GraphicsAPI graphics_api) {
    this.graphics_api = graphics_api;
  }

  public void Update() {
    graphics_api.CreateBuffer();
    for (GameObject g : game.getLevel().getGameObjects()) {
      // graphics_api.DrawSprite(image, position);
    }
    graphics_api.DrawRect(new Point2i(20, 20), 20, 20, Color.CYAN);
    graphics_api.FlushBuffer();
  }
}
