package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.commands.TeleopFlywheelCommand;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlywheelSubsystem;

@TeleOp(name = "Main Teleop With Flywheel")
public class MainTeleop extends CommandOpMode {

    private DrivetrainSubsystem drivetrain;
    private FlywheelSubsystem flywheel;
    private GamepadEx driverController;

    @Override
    public void initialize() {
        Pose2d initialPose = new Pose2d(0, 0, 0);

        drivetrain = new DrivetrainSubsystem(hardwareMap, initialPose);
        drivetrain.setTelemetry(telemetry); // Add telemetry for debugging
        driverController = new GamepadEx(gamepad1);

        flywheel = new FlywheelSubsystem(hardwareMap);
        flywheel.setTelemetry(telemetry);

        drivetrain.setDefaultCommand(
                new TeleopDriveCommand(
                        drivetrain,
                        () -> driverController.getLeftY(),
                        () -> driverController.getLeftX(),
                        () -> driverController.getRightX()
                )
        );

        flywheel.setDefaultCommand(
                new TeleopFlywheelCommand(
                        flywheel,
                        () -> driverController.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                )
        );
    }
}