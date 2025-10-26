package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Autonomous(name="Main Autonomous (Tag Tracking + Logging)", group="Auto")
public class MainAutonomous extends LinearOpMode {

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private FileWriter logWriter;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive;
        try {
            drive = new MecanumDrive(hardwareMap, startPose);
            telemetry.addLine("✅ MecanumDrive initialized successfully");
        } catch (Exception e) {
            telemetry.addLine("❌ MecanumDrive initialization failed: " + e.getMessage());
            telemetry.addLine("Check: Motor names, encoder connections, wheel dimensions");
            return; // Exit if drive system fails
        }

        aprilTag = new AprilTagProcessor.Builder().build();
        
        // Configure for Arducam (USB webcam)
        try {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
            telemetry.addLine("✅ Camera initialized successfully");
        } catch (Exception e) {
            telemetry.addLine("❌ Camera initialization failed: " + e.getMessage());
            telemetry.addLine("Check: USB connection, hardware map configuration");
        }

        // Try to open a log file
        try {
            logWriter = new FileWriter("/sdcard/FIRST/AprilTagLog.txt", false);
            logWriter.write("=== AprilTag Position Log ===\n");
            logWriter.write("Format: TagID, X(in), Y(in), Z(in), Yaw(deg)\n\n");
            logWriter.flush();
        } catch (IOException e) {
            telemetry.addLine("⚠️ Could not open log file!");
            telemetry.update();
        }

        telemetry.addLine("Camera initialized. Tracking AprilTags...");
        telemetry.addLine("Robot Status: Ready");
        telemetry.addData("Drive System", "MecanumDrive initialized");
        telemetry.addData("Camera", "Arducam configured as Webcam 1");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            List<AprilTagDetection> detections = aprilTag.getDetections();

            if (detections.size() > 0) {
                double avgX = 0, avgY = 0, avgYaw = 0;
                int validTags = 0;

                telemetry.addLine("=== Detected AprilTags ===");
                for (AprilTagDetection tag : detections) {
                    double x = tag.ftcPose.x;
                    double y = tag.ftcPose.y;
                    double z = tag.ftcPose.z;
                    double yawDeg = Math.toDegrees(tag.ftcPose.yaw);

                    telemetry.addData("Tag ID", tag.id);
                    telemetry.addData("→ X (in)", "%.2f", x);
                    telemetry.addData("→ Y (in)", "%.2f", y);
                    telemetry.addData("→ Z (in)", "%.2f", z);
                    telemetry.addData("→ Yaw (deg)", "%.2f", yawDeg);
                    telemetry.addLine();

                    // Log to file
                    try {
                        if (logWriter != null) {
                            logWriter.write(String.format(
                                "Tag %d: X=%.2f, Y=%.2f, Z=%.2f, Yaw=%.2f\n",
                                tag.id, x, y, z, yawDeg
                            ));
                            logWriter.flush();
                        }
                    } catch (IOException ignored) {}

                    avgX += x;
                    avgY += y;
                    avgYaw += tag.ftcPose.yaw;
                    validTags++;
                }

                avgX /= validTags;
                avgY /= validTags;
                avgYaw /= validTags;

                telemetry.addLine("=== Triangulated Data ===");
                telemetry.addData("Tags Detected", validTags);
                telemetry.addData("Average X (in)", "%.2f", avgX);
                telemetry.addData("Average Y (in)", "%.2f", avgY);
                telemetry.addData("Average Yaw (deg)", "%.2f", Math.toDegrees(avgYaw));

                Pose2d currentPose = drive.pose;
                Pose2d targetPose = new Pose2d(avgX, avgY, 0);

                double distance = Math.hypot(
                        targetPose.position.x - currentPose.position.x,
                        targetPose.position.y - currentPose.position.y
                );

                // Move if off target
                if (distance > 2.0) {
                    Action adjustCourse = drive.actionBuilder(currentPose)
                            .lineToX(avgX)
                            .lineToY(avgY)
                            .turnTo(avgYaw)
                            .build();
                    Actions.runBlocking(adjustCourse);
                }

            } else {
                telemetry.addLine("No tags detected...");
            }

            telemetry.update();
            sleep(200);
        }

        // Clean up
        visionPortal.close();
        try {
            if (logWriter != null) {
                logWriter.write("\n=== End of Log ===\n");
                logWriter.close();
            }
        } catch (IOException ignored) {}
    }
}
