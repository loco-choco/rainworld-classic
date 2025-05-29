package com.locochoco.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import com.locochoco.gameengine.*;

public class ThingsDNDSpawner extends Component implements DNDSubscriber {

  private InputAPI input;

  public void OnCreated() {
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    input = GameEngine.getGameEngine().getInputs();
    input.SubscribeToDND(this);
  }

  public void OnDestroyed() {
    input.UnsubscribeToDND(this);
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

  public void ReceiveDNDFile(File file) {
    System.out.printf("Creating %s\n", file.getAbsolutePath());
    GameObject go = GameEngine.getGameEngine().getLevel().LoadGameObjectFromJson(file.getAbsolutePath(), null);
    go.getTransform().setGlobalPosition(input.GetMousePos());
    /*
     * try {
     * ZipInputStream out = new ZipInputStream(new FileInputStream(file));
     * ZipEntry entry = out.getNextEntry();
     * 
     * } catch (IOException ioe) {
     * System.err.printf("Issue reading %s file: %s\n", file.getName(),
     * ioe.getMessage());
     * }
     */
  }
}
