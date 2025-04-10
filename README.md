# Rain World Classic
**A Celeste Classic (for PICO8) inspired port of Rain World for the SCC0504 OOP class**

## How to build and run the project

```bash
# If you have nix and don't have jdk nor maven
nix develop -c

# Builds the project
cd rainworld-classic
mvn package

# Runs the project
java -cp target/rainworld-classic-1.0-SNAPSHOT.jar com.locochoco.app.App
```
