package com.locochoco.gameengine;

/**
 * Representation of a Object in game
 */
public class Camera extends Component {

  public boolean main_camera; // For Serialization
  private boolean is_main_camera;

  public void OnCreated() {
    main_camera = true;
    is_main_camera = false;
  }

  public void OnEnabled() {
  }

  public void setIsMainCamera(boolean is_main_camera) {
    if (this.is_main_camera == is_main_camera)
      return;
    this.is_main_camera = is_main_camera;
    GameEngine.getGameEngine().getLevel().setMainCamera(this);
  }

  public boolean getIsMainCamera() {
    return is_main_camera;
  }

  public void OnDisabled() {
  }

  public void OnDestroyed() {
    if (is_main_camera)
      GameEngine.getGameEngine().getLevel().setMainCamera(null);
  }

  public void Start() {
    setIsMainCamera(main_camera);
  }

  public void PhysicsUpdate(double delta_time) {
  }

  public void GraphicsUpdate(double delta_time) {
  }

  public void Update(double delta_time) {
  }

  public void LateUpdate(double delta_time) {
  }

}
