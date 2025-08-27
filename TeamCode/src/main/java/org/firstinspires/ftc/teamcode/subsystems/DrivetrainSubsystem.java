package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class DrivetrainSubsystem extends SubsystemBase {
    private final MecanumDrive drive;

    public DrivetrainSubsystem(HardwareMap hardwareMap, Pose2d initialPose) {
        drive = new MecanumDrive(hardwareMap, initialPose);
    }

    @Override
    public void periodic() {
        drive.updatePoseEstimate();
    }

    public void drive(double leftY, double leftX, double rightX) {
        double heading = drive.localizer.getPose().heading.toDouble();

        // Standard 2D vector rotation formula:
        // x' = x * cos(theta) - y * sin(theta)
        // y' = x * sin(theta) + y * cos(theta)
        // Here, x is leftX, y is -leftY, and theta is -heading.

        double rotX = leftX * Math.cos(-heading) - (-leftY) * Math.sin(-heading);
        double rotY = leftX * Math.sin(-heading) + (-leftY) * Math.cos(-heading);

        // Road Runner's PoseVelocity2d constructor takes (forward, strafe, turn)
        // Our rotY is the new forward component, and rotX is the new strafe component.
        drive.setDrivePowers(
                new PoseVelocity2d(new Vector2d(rotY, rotX), // Forward (Y), Strafe (X)
                        -rightX // Turning
                )
        );
    }

    public void stop() {
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0));
    }
}