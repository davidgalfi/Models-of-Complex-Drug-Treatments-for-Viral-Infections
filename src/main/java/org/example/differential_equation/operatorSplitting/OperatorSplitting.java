package org.example.differential_equation.operatorSplitting;

import java.util.function.Function;

public interface OperatorSplitting {
    public void operatorSplit(Function<Double, Void> linearOperator, Function<Double, Void> nonLinearOperator);
}
