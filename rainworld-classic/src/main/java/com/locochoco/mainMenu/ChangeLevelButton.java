package com.locochoco.mainMenu;

import com.locochoco.gameengine.*;

public class ChangeLevelButton extends Button {
  public String level_file_path;

  public void OnButtonPressed() {
    GameEngine.getGameEngine().LoadLevel(level_file_path);
  }

}
