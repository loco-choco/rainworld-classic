package com.locochoco.editor;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point2d;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locochoco.gameengine.CollisionMath;
import com.locochoco.gameengine.InputAPI;

public abstract class EditorMode<M extends Enum<M>> {
  protected EditorController controller;
  protected ArrayList<Tile> tiles;
  protected InputAPI inputs;

  private M current_submode;
  private M default_submode;
  private HashMap<M, EditorSubmode> submodes;

  public EditorMode(EditorController controller, InputAPI inputs, M default_submode) {
    this.controller = controller;
    this.inputs = inputs;
    tiles = new ArrayList<>();
    submodes = new HashMap<>();
    this.default_submode = default_submode;
    current_submode = default_submode;
  }

  public boolean IsAtDefaultSubmode() {
    return default_submode == current_submode;
  }

  public void AddSubmode(M id, EditorSubmode submode) {
    submodes.put(id, submode);
  }

  public M GetCurrentSubmode() {
    return current_submode;
  }

  public void AddTile(Tile tile) {
    tiles.add(tile);
  }

  public void RemoveTile(Tile tile) {
    tiles.remove(tile);
    tile.Destroy();
  }

  public void ClearAllTiles() {
    for (Tile tile : tiles)
      tile.Destroy();
    tiles.clear();
  }

  public Tile GetTileUnderCursor() {
    Point2d cursor = inputs.GetMousePos();
    for (Tile tile : tiles) {
      if (CollisionMath.CheckPointCollision(tile.GetGameObject().getCollider(), cursor))
        return tile;
    }
    return null;
  }

  public void SerializeTiles(JsonGenerator generator, ObjectMapper mapper) {
    for (Tile tile : tiles)
      tile.SaveToJson(generator, mapper);
  }

  public abstract void OnEnterMode();

  public abstract void OnExitMode();

  public String GetSubmode() {
    if (submodes.containsKey(current_submode))
      return current_submode.name();
    return "";
  }

  public String GetSubmodeStatus() {
    if (submodes.containsKey(current_submode))
      return submodes.get(current_submode).GetStatus();
    return "";
  }

  public void OnLoopMode() {
    if (submodes.containsKey(current_submode))
      submodes.get(current_submode).OnLoopSubmode();
  }

  public void SwitchMode(M id) {
    if (id != current_submode) {
      if (submodes.containsKey(current_submode))
        submodes.get(current_submode).OnExitSubmode();
      current_submode = id;
      if (submodes.containsKey(current_submode))
        submodes.get(current_submode).OnEnterSubmode();
      if (current_submode == default_submode)
        System.out.println("\tNo submode");
    }
  }

}
