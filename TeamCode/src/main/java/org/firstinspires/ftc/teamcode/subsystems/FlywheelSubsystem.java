package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlywheelSubsystem extends SubsystemBase {
    private final Motor flywheel;
    private Telemetry telemetry;

    public FlywheelSubsystem(HardwareMap hardwareMap) {
        flywheel = new Motor(hardwareMap, "flywheel");
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setPower(double triggerValue) {
        flywheel.set(triggerValue);
        if (telemetry != null) {
            telemetry.addData("Flywheel Power", "%.2f", triggerValue);
        }
    }

    @Override
    public void periodic() {
        if (telemetry != null) {
            telemetry.addData("Flywheel Velocity (ticks/s)", flywheel.getCorrectedVelocity());
            telemetry.update();
        }
    }
}
