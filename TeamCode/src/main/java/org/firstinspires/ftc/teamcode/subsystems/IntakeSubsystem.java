package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem extends SubsystemBase {

    private final Motor intake;
    private Telemetry telemetry;
    private boolean running = false;
    private double lastPower = 0;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = new Motor(hardwareMap, "intakeMotor");
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    // ---- High-level control methods ----

    public void runForward() {
        setPower(1.0);
        running = true;
    }

    public void runReverse() {
        setPower(-1.0);
        running = true;
    }

    public void stop() {
        setPower(0.0);
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    // ---- Low-level power method ----
    public void setPower(double power) {
        intake.set(power);
        lastPower = power;
        if (telemetry != null) {
            telemetry.addData("Intake Power", "%.2f", power);
        }
    }

    @Override
    public void periodic() {
        if (telemetry != null) {
            telemetry.addData("Intake Velocity (ticks/s)", intake.getCorrectedVelocity());
            telemetry.addData("Running", running);
            telemetry.addData("Last Power", "%.2f", lastPower);
            telemetry.update();
        }
    }
}
