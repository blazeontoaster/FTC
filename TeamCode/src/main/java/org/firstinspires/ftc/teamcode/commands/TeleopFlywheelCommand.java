package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.FlywheelSubsystem;

import java.util.function.DoubleSupplier;

public class TeleopFlywheelCommand extends CommandBase {
    private final FlywheelSubsystem flywheel;
    private final DoubleSupplier triggerValue;

    public TeleopFlywheelCommand(FlywheelSubsystem flywheel, DoubleSupplier triggerValue) {
        this.flywheel = flywheel;
        this.triggerValue = triggerValue;

        addRequirements(flywheel);
    }

    @Override
    public void execute() {
        flywheel.setPower(triggerValue.getAsDouble());
    }
}
