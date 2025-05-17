package com.locochoco.editor;

import javax.vecmath.Point2d;

import com.locochoco.game.RoomConnectionInfo;
import com.locochoco.gameengine.*;

public class RoomInfo {
  public String file_name;
  public Point2d position;
  public RoomConnectionInfo north_connection;
  public RoomConnectionInfo south_connection;
  public RoomConnectionInfo west_connection;
  public RoomConnectionInfo east_connection;
}
