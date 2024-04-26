package org.example.differential_equation.operatorSplitting;

import java.util.function.Function;

public class SequentialSplitting implements OperatorSplitting {

    final double timeStep;

    public SequentialSplitting(double timeStep) {

        this.timeStep = timeStep;
    }

    @Override
    public void operatorSplit(Function<Double, Void> linearOperator, Function<Double, Void> nonLinearOperator) {

        linearOperator.apply(timeStep);
        nonLinearOperator.apply(timeStep);
    }
}
