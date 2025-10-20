package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem extends SubsystemBase {
    private final Motor intake;
    private Telemetry telemetry;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = new Motor(hardwareMap, "intakeMotor");
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setPower(double power) {
        intake.set(power);
        if (telemetry != null) {
            telemetry.addData("Intake Power", "%.2f", power);
        }
    }

    @Override
    public void periodic() {
        if (telemetry != null) {
            telemetry.addData("Intake Velocity (ticks/s)", intake.getCorrectedVelocity());
            telemetry.update();
        }
    }
}
