package org.example.differentialEquation.operatorSplitting;

import java.util.function.Function;

public class MarchukStrangSplitting implements OperatorSplitting {

    final double linearTimeStep;
    final double nonlinearTimeStep;

    public MarchukStrangSplitting(double timeStep) {

        this.linearTimeStep = timeStep / 2.0;
        this.nonlinearTimeStep = timeStep;
    }

    @Override
    public void operatorSplit(Function<Double, Void> linearOperator, Function<Double, Void> nonLinearOperator) {

        linearOperator.apply(linearTimeStep);
        nonLinearOperator.apply(nonlinearTimeStep);
        linearOperator.apply(linearTimeStep);
    }
}
