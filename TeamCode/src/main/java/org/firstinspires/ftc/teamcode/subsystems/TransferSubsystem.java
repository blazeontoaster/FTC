package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TransferSubsystem extends SubsystemBase {
    private final Motor transfer;
    private Telemetry telemetry;

    public TransferSubsystem(HardwareMap hardwareMap) {
        transfer = new Motor(hardwareMap, "transferMotor");
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setPower(double power) {
        transfer.set(power);
        if (telemetry != null) {
            telemetry.addData("Transfer Power", "%.2f", power);
        }
    }

    @Override
    public void periodic() {
        if (telemetry != null) {
            telemetry.addData("Transfer Velocity (ticks/s)", transfer.getCorrectedVelocity());
            telemetry.update();
        }
    }
}
