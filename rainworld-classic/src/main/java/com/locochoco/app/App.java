package com.locochoco.app;

import javax.swing.SwingUtilities;

import com.locochoco.gameengine.GameEngine;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    GameEngine game = new GameEngine();
    while (true) {
      game.Run();
    }
  }
}
