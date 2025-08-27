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
        driverController = new GamepadEx(gamepad1);

        // Tell the command scheduler to run our TeleopDriveCommand by default
        // for the drivetrain. This will run every loop.
        drivetrain.setDefaultCommand(
                new TeleopDriveCommand(
                        drivetrain,
                        () -> driverController.getLeftY(),
                        () -> driverController.getLeftX(),
                        () -> driverController.getRightX()
                )
        );
    }
}