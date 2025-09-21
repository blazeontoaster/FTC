
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

* `MecanumDrive.java`: **The heart of the drivetrain.** You will edit this file **ONCE** during initial setup to configure hardware names and select the correct localizer. If any changes are made to the robot drivetrain, this file may need to be re-configured. Any green lines marked as TODO should be read and subsequent action should be taken as necessary.
* `tuning/` (package): Contains the OpModes used for tuning. **DO NOT EDIT** these files; simply run them from the Driver Hub.
* `messages/` (package): Used for advanced logging by Road Runner. **IGNORE THIS FOLDER.**

### Our Custom Code (Where We Work)

This is where we will build all of our robot's unique logic using FTCLib.

* `opmodes/` (package): Contains our final, runnable OpModes like `MainTeleop.java` and our various autonomous routines.
* `subsystems/` (package): Contains classes that represent physical parts of our robot, like `DrivetrainSubsystem.java` and `FlywheelSubsystem.java`.
* `commands/` (package): Contains the actions the robot can perform, like `TeleopDriveCommand.java` or `FlywheelCommand.java`.

---

## Initial Robot Setup Checklist

Follow these steps in order to get the robot from a fresh code clone to a fully tuned, driveable state. **All `// TODO` comments in the code must be completed.**
---

## How to Build the Code

To check for errors at any time without a robot, use the built-in Gradle task:

1.  In the top menu bar, go to **`Build` > `Make Project`**.
2.  Check the "Build" window at the bottom for a "BUILD SUCCESSFUL" message.
3. Ensure you are building to the correct device. Either connect to the **Controll Hub** (NOT the device with the screen, the device on the robot) with a physical cable or set it up over wifi using adb or the rev robotics application.
4. Wait for the device to reboot, choose your opmode, and start driving!
5. Pro tip: if your controller is not doing anything, try clicking `Option`, `Bottom Face Button`, and `Right Face Button` all at the same time.

## Help
Whenever you need help with your code or anything else, don't forget the coaches are here to help you. If you are stuck with anything, feel free to contact us or work with us at the meetings.


