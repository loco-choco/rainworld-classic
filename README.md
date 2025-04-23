# Rain World Classic
**A Celeste Classic (for PICO8) inspired port of Rain World for the SCC0504 OOP class**

## How to build and run the project

```bash
# If you have nix and don't have neither jdk nor maven
nix develop -c

# Builds the project
cd rainworld-classic
mvn clean compile assembly:single
# Runs the project
java -jar target/rainworld-classic-1.0-SNAPSHOT-jar-with-dependencies.jar
# Convert the levels from yaml to json
(yj -yt < level0.yaml) > level0.json
```
## Current TO-DOs

- [ ] Add Game Configs and load them via json
    - [ ] Load Collision Matrix
    - [ ] Load Other Physics settings
- [x] Make Jump Reset when grounded
- [x] Get collision event
- [x] Physics Layer System
- [x] GameObject Hierarchy



## References

- UML Diagram template from https://inkscape.org/~mikapyon/%E2%98%85uml-class-diagram
