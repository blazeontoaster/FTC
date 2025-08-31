package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.messages.PinpointInputsMessage;

@Config
public final class PinpointLocalizer implements Localizer {
    public static class Params {
        // Pod offsets in millimeters - adjust these for your robot
        // X pod offset: how far sideways from tracking point (left = positive, right = negative)
        // Y pod offset: how far forwards from tracking point (forward = positive, backward = negative)
        public double xPodOffsetMM = -84.0;  // Default from sample
        public double yPodOffsetMM = -168.0; // Default from sample
        
        // Pod type - set to your actual pod type
        public GoBildaPinpointDriver.GoBildaOdometryPods podType = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        
        // Encoder directions - adjust if your encoders count backwards
        public GoBildaPinpointDriver.EncoderDirection xDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        public GoBildaPinpointDriver.EncoderDirection yDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        
        // Custom encoder resolution (ticks per mm) - only used if podType is set to null
        public double customEncoderResolution = 13.26291192; // ticks per mm
    }

    public static Params PARAMS = new Params();

    private final GoBildaPinpointDriver pinpoint;
    private Pose2d pose;
    private boolean initialized = false;
    
    // For velocity calculation
    private Pose2d lastPose;
    private long lastUpdateTime;

    public PinpointLocalizer(HardwareMap hardwareMap, Pose2d initialPose) {
        // Get the Pinpoint device from hardware map
        // Make sure your robot configuration has a device named "pinpoint"
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        
        // Configure the Pinpoint device
        configurePinpoint();
        
        // Set initial position
        pose = initialPose;
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 
            initialPose.position.x, 
            initialPose.position.y, 
            AngleUnit.RADIANS, 
            initialPose.heading.toDouble()));

        FlightRecorder.write("PINPOINT_PARAMS", PARAMS);
    }

    private void configurePinpoint() {
        // Set pod offsets
        pinpoint.setOffsets(PARAMS.xPodOffsetMM, PARAMS.yPodOffsetMM, DistanceUnit.MM);
        
        // Set encoder resolution (pod type or custom)
        if (PARAMS.podType != null) {
            pinpoint.setEncoderResolution(PARAMS.podType);
        } else {
            pinpoint.setEncoderResolution(PARAMS.customEncoderResolution, DistanceUnit.MM);
        }
        
        // Set encoder directions
        pinpoint.setEncoderDirections(PARAMS.xDirection, PARAMS.yDirection);
        
        // Reset position and IMU - this calibrates the IMU
        pinpoint.resetPosAndIMU();
    }

    @Override
    public void setPose(Pose2d pose) {
        this.pose = pose;
        // Update the Pinpoint's position to match
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH,
            pose.position.x,
            pose.position.y,
            AngleUnit.RADIANS,
            pose.heading.toDouble()));
    }

    @Override
    public Pose2d getPose() {
        return pose;
    }

    @Override
    public PoseVelocity2d update() {
        // Update the Pinpoint sensor
        pinpoint.update();
        
        // Get current position from Pinpoint
        Pose2D currentPosition = pinpoint.getPosition();
        long currentTime = System.nanoTime();
        
        // Convert from Pinpoint's Pose2D to RoadRunner's Pose2d
        // Pinpoint uses inches and radians, which matches RoadRunner's default units
        pose = new Pose2d(
            currentPosition.getX(DistanceUnit.INCH),
            currentPosition.getY(DistanceUnit.INCH),
            currentPosition.getHeading(AngleUnit.RADIANS)
        );
        
        PoseVelocity2d velocity;
        
        if (!initialized) {
            initialized = true;
            lastPose = pose;
            lastUpdateTime = currentTime;
            velocity = new PoseVelocity2d(new Vector2d(0.0, 0.0), 0.0);
        } else {
            // Calculate velocity from position change
            double dt = (currentTime - lastUpdateTime) / 1e9; // Convert to seconds
            
            if (dt > 0) {
                double vx = (pose.position.x - lastPose.position.x) / dt;
                double vy = (pose.position.y - lastPose.position.y) / dt;
                double vHeading = (pose.heading.toDouble() - lastPose.heading.toDouble()) / dt;
                
                // Handle angle wrap-around for heading velocity
                if (vHeading > Math.PI) {
                    vHeading -= 2 * Math.PI;
                } else if (vHeading < -Math.PI) {
                    vHeading += 2 * Math.PI;
                }
                
                velocity = new PoseVelocity2d(new Vector2d(vx, vy), vHeading);
            } else {
                velocity = new PoseVelocity2d(new Vector2d(0.0, 0.0), 0.0);
            }
            
            lastPose = pose;
            lastUpdateTime = currentTime;
        }
        
        // Log data for debugging - create a dummy velocity Pose2D for logging
        Pose2D velocityForLogging = new Pose2D(DistanceUnit.INCH, 
            velocity.linearVel.x, 
            velocity.linearVel.y, 
            AngleUnit.RADIANS, 
            velocity.angVel);
        FlightRecorder.write("PINPOINT_INPUTS", new PinpointInputsMessage(currentPosition, velocityForLogging));
        
        return velocity;
    }
    
    /**
     * Reset the Pinpoint's position and recalibrate the IMU.
     * Call this when the robot is stationary for best results.
     */
    public void resetPosAndIMU() {
        pinpoint.resetPosAndIMU();
    }
    
    /**
     * Get the raw Pinpoint device for advanced usage.
     * @return the GoBildaPinpointDriver instance
     */
    public GoBildaPinpointDriver getPinpointDevice() {
        return pinpoint;
    }
}
