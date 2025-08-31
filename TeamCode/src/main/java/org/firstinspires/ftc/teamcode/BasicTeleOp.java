package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;

@TeleOp(name = "Basic Mecanum TeleOp")
public class BasicTeleOp extends CommandOpMode {

    private DrivetrainSubsystem drivetrain;
    private GamepadEx driverController;

    @Override
    public void initialize() {
        Pose2d initialPose = new Pose2d(0, 0, 0);

        drivetrain = new DrivetrainSubsystem(hardwareMap, initialPose);
        drivetrain.setTelemetry(telemetry); // Add telemetry for debugging
        driverController = new GamepadEx(gamepad1);

        // Tell the command scheduler to run our TeleopDriveCommand by default
        // for the drivetrain. This will run every loop.
        
        // SWAPPED MAPPING to fix the control issue:
        // leftY parameter gets right stick X (which currently moves forward/back)
        // rightX parameter gets left stick Y (to make it turn instead)
        drivetrain.setDefaultCommand(
                new TeleopDriveCommand(
                        drivetrain,
                        () -> driverController.getRightX(),  // SWAPPED: right stick X for forward/back
                        () -> driverController.getLeftX(),   // Left stick X for strafe (unchanged)
                        () -> driverController.getLeftY()   // SWAPPED: left stick Y for turning
                )
        );
    }
}