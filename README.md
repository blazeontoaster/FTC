Welcome to the official repository for our 2025-2026 FTC robot. This project is built on a powerful combination of **Road Runner 1.0** for advanced motion planning and **FTCLib** for a clean, command-based robot architecture.

This document serves as the primary guide for setting up, tuning, and developing the robot's software.

---

## Core Technologies & Documentation

Before diving into the code, familiarize yourself with the core libraries we use. All development should follow the best practices outlined in their documentation.

* **Road Runner 1.0:** The high-accuracy trajectory generation and following library.
    * **Official Tuning Guide:** [learnroadrunner.com](https://rr.brott.dev/docs/v1-0/tuning/) (This is the most important link)
* **FTCLib:** The command-based framework that organizes our entire robot.
    * **Official Documentation:** [docs.ftclib.org](https://docs.ftclib.org/ftclib/)

---

## Project Structure: What to Edit and What to Ignore

Our code is organized to separate the complex underlying libraries from our custom robot logic.

###  Untouchable Core Road Runner Files (Configure Once, Then Leave Alone)

These files are part of the Road Runner template. After the initial hardware setup, you should rarely need to edit them.

* `MecanumDrive.java`: **The heart of the drivetrain.** You will edit this file **ONCE** during initial setup to configure hardware names and select the correct localizer. If any changes are made to the robot drivetrain, this file may need to be re-configured.
* `TwoDeadWheelLocalizer.java`: **The brain of our position tracking.** You will edit this file **ONCE** during initial setup to input physical robot measurements and encoder names. If any changes are made to the robot drivetrain, this file may need to be re-configured.
* `tuning/` (package): Contains the OpModes used for tuning. **DO NOT EDIT** these files; simply run them from the Driver Hub.
* `messages/` (package): Used for advanced logging by Road Runner. **IGNORE THIS FOLDER.**

### Our Custom Code (Where We Work)

This is where we will build all of our robot's unique logic using FTCLib.

* `opmodes/` (package): Contains our final, runnable OpModes like `MainTeleOp.java` and our various autonomous routines.
* `subsystems/` (package): Contains classes that represent physical parts of our robot, like `DrivetrainSubsystem.java` and `IntakeSubsystem.java`.
* `commands/` (package): Contains the actions the robot can perform, like `TeleopDriveCommand.java` or `ScorePixelCommand.java`.

---

## Initial Robot Setup Checklist

Follow these steps in order to get the robot from a fresh code clone to a fully tuned, driveable state. **All `// TODO` comments in the code must be completed.**

### Step 1: Configure Hardware in `TwoDeadWheelLocalizer.java`

1.  **Open `TwoDeadWheelLocalizer.java`**.
2.  **Set Encoder Names:** In the constructor, change the hardware map strings (`"par"`, `"perp"`) to match the names of your odometry pod encoders as configured on the Driver Hub.
3.  **Measure and Set Pod Positions:**
    * Measure the physical X and Y distances (in inches) of your odometry pods. We will do this step together to ensure accuracy.
    * You will enter these values later during the tuning process, which will give you the `inPerTick` value needed to convert your measurements.

### Step 2: Configure Hardware in `MecanumDrive.java`

1.  **Open `MecanumDrive.java`**.
2.  **Set Motor and IMU Names:** In the constructor, change the hardware map strings (`"leftFront"`, `"imu"`, etc.) to match your robot's configuration.
3.  **Set IMU Orientation:** In the `Params` class, correctly set the `logoFacingDirection` and `usbFacingDirection` to match how your Control Hub is physically mounted on the robot.

### Step 3: Complete the Road Runner Tuning Process

Your code is now configured. The next step is to find the "magic numbers" that make it accurate.

1.  **Go to the [Roadrunner Tuning Guide](https://rr.brott.dev/docs/v1-0/tuning/)**.
2.  Follow **every step** of the tuning guide.
3.  As you complete each tuning OpMode, you will get values (`inPerTick`, `trackWidthTicks`, `kV`, etc.). Update these values in the `Params` classes inside `MecanumDrive.java` and `TwoDeadWheelLocalizer.java`.

---

## How to Build the Code

To check for errors at any time without a robot, use the built-in Gradle task:

1.  In the top menu bar, go to **`Build` > `Make Project`**.
2.  Check the "Build" window at the bottom for a "BUILD SUCCESSFUL" message.

