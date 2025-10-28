package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import java.util.function.BooleanSupplier;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class TeleopIntakeCommand extends CommandBase {

    private final IntakeSubsystem intake;
    private final BooleanSupplier toggleIntake;
    private final BooleanSupplier reverseButton;

    public TeleopIntakeCommand(IntakeSubsystem intake,
                               BooleanSupplier toggleIntake,
                               BooleanSupplier reverseButton) {
        this.intake = intake;
        this.toggleIntake = toggleIntake;
        this.reverseButton = reverseButton;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        boolean reverse = reverseButton.getAsBoolean();
        boolean intakeActive = toggleIntake.getAsBoolean();

        if (reverse) {
            intake.runReverse();
        } else if (intakeActive) {
            intake.runForward();
        } else {
            intake.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false; // runs continuously while opMode is active
    }
}
