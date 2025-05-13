package com.locochoco.game;

import javax.vecmath.Point2d;

import com.locochoco.gameengine.Component;

public abstract class Pipeable extends Component {

  public double time_per_pipe;

  public double GetTimePerPipe() {
    return time_per_pipe;
  }

  public abstract void EnterPipe();

  public abstract void ExitPipe(Point2d exit_position);

}
