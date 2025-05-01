package com.locochoco.app;

import javax.swing.SwingUtilities;

import com.locochoco.gameengine.GameEngine;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    GameEngine game = new GameEngine();
    while (true) {
      game.Run();
    }
  }
}
