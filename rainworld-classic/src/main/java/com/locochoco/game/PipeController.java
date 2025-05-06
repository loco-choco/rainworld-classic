package com.locochoco.game;

import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.locochoco.gameengine.*;

public class PipeController extends Component implements CollisionListener {
  public double traveling_speed;

  private ArrayList<ObjectTravelingPipe> objects_traveling;
  private GameObject side_a;
  private GameObject side_a_entrance;
  private GameObject side_b;
  private GameObject side_b_entrance;

  public double repulsion_force;
  public Vector2d side_a_direction;
  public Vector2d side_b_direction;

  private double pipe_total_lenght;
  private ArrayList<BoxRenderer> pipes;

  public void OnCreated() {
    objects_traveling = new ArrayList<>();
    pipes = new ArrayList<>();
    traveling_speed = 20;
    side_a_direction = new Vector2d();
    side_b_direction = new Vector2d();
  }

  public void OnEnabled() {
  }

  public void OnDisabled() {
  }

  public void Start() {
    GameObject go = getGameObject();
    side_a = go.findFirstChild("side_a");
    side_b = go.findFirstChild("side_b");

    side_a_entrance = side_a.findFirstChild("entrance");
    side_a_entrance.getCollider().addCollisionListener(this);
    side_b_entrance = side_b.findFirstChild("entrance");
    side_b_entrance.getCollider().addCollisionListener(this);

    // Pipes Visual
    pipe_total_lenght = 0;
    Vector2d last_pipe = new Vector2d(side_a_entrance.getTransform().getGlobalPosition());

    GameObject pipes = go.findFirstChild("pipes");
    for (GameObject pipe : pipes.getChildren()) {
      Renderer pipe_flash_rend = pipe.findFirstChild("flash").getRenderer();
      if (pipe_flash_rend instanceof BoxRenderer) {
        this.pipes.add((BoxRenderer) pipe_flash_rend);
        // Find Pipe Lenght
        Point2d pipe_pos = pipe.getTransform().getGlobalPosition();
        last_pipe.sub(pipe_pos);
        pipe_total_lenght = last_pipe.length();
        last_pipe = new Vector2d(pipe_pos);
      } else {
        System.err.println("Renderer in flash should be a Box Renderer!");
      }
    }
  }

  public void OnCollision(CollisionData data, Collider collidee) {
    ObjectTravelingPipe object = null;
    GameObject entrance;
    GameObject creature;
    if (data.getFirstCollider().getGameObject() == side_a_entrance
        || data.getFirstCollider().getGameObject() == side_b_entrance) {
      entrance = data.getFirstCollider().getGameObject();
      creature = data.getSecondCollider().getGameObject();
    } else {
      entrance = data.getSecondCollider().getGameObject();
      creature = data.getFirstCollider().getGameObject();
    }
    // Side A
    if (entrance == side_a_entrance) {
      object = new ObjectTravelingPipe(creature, side_b.getTransform(),
          traveling_speed, 1, pipe_total_lenght, side_a_direction);
    }
    // Side B
    else if (entrance == side_b_entrance) {
      object = new ObjectTravelingPipe(creature, side_a.getTransform(),
          traveling_speed, -1, pipe_total_lenght, side_b_direction);
    }

    // Means that we have something that shouldn't be able to use the pipe
    // Be it an item or another entrance that accidentaly is colliding
    if (creature.getCollider().layer == entrance.getCollider().layer) {
      RigidBody creature_r = creature.getRigidBody();
      if (creature_r != null) { // If this thing has a RigidBody, push it out
        Vector2d force = new Vector2d(object.getExitDirection());
        force.scale(repulsion_force);
        creature_r.AddForce(force);
      }
      return;
    }

    if (object != null) {
      object.MakeObjectEnterPipe();
      objects_traveling.add(object);
    }
  }

  public void OnEnterCollision() {
  }

  public void OnExitCollision() {
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
    for (BoxRenderer pipe : pipes) { // Disable the color in all
      pipe.getGameObject().setEnabled(false);
    }
    for (ObjectTravelingPipe object : objects_traveling) { // Show where the objects are in the pipe
      int amount_of_pipes = pipes.size();
      int pipe_index = Math.clamp((int) (object.getPorcentage() * amount_of_pipes - 0.5), 0, amount_of_pipes);
      pipes.get(pipe_index).getGameObject().setEnabled(true);
    }
  }

  public void Update(double delta_time) {
    Iterator<ObjectTravelingPipe> objects = objects_traveling.iterator();
    while (objects.hasNext()) {
      ObjectTravelingPipe object = objects.next();
      object.Update(delta_time);
      if (object.hasTraveledPipe()) {
        object.MakeObjectExitPipe();
        objects.remove();
      }
    }
  }

  public void LateUpdate(double delta_time) {
  }
}

class ObjectTravelingPipe {
  GameObject object;
  double distance;
  double porcentage;
  double direction;
  double speed;
  Transform exit_end;
  Vector2d exit_direction;

  public ObjectTravelingPipe(GameObject object, Transform exit_end, double speed, double direction, double distance,
      Vector2d exit_direction) {
    this.object = object;
    this.direction = direction;
    this.speed = speed;
    this.exit_end = exit_end;
    this.distance = distance;
    this.exit_direction = exit_direction;
    if (direction >= 0)
      porcentage = 0;
    else
      porcentage = 1;
  }

  public void MakeObjectEnterPipe() {
    object.setEnabled(false);
  }

  public Vector2d getExitDirection() {
    return exit_direction;
  }

  public void MakeObjectExitPipe() {
    object.getTransform().setGlobalPosition(new Point2d(exit_end.getGlobalPosition()));

    RigidBody object_rb = object.getRigidBody();
    if (object_rb != null)
      object_rb.SetVelocity(new Vector2d(0, 0));
    object.setEnabled(true);
  }

  public void Update(double delta_time) {
    porcentage = Math.clamp(porcentage + delta_time * direction * speed / distance, 0, 1.0);
  }

  public double getPorcentage() {
    return porcentage;
  }

  public boolean hasTraveledPipe() {
    if (direction >= 0 && porcentage == 1.0)
      return true;
    else if (direction < 0 && porcentage == 0)
      return true;
    return false;
  }
}
