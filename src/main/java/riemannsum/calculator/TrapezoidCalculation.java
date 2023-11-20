package riemannsum.calculator;

import akka.actor.AbstractActor;

import java.util.function.Function;

public class TrapezoidCalculation extends AbstractActor {

    private final Function<Double, Double> function;
    private final double start;
    private final double end;
    private final int numTrapezoids;

    public TrapezoidCalculation(Function<Double, Double> function, double start, double end, int numTrapezoids) {
        this.function = function;
        this.start = start;
        this.end = end;
        this.numTrapezoids = numTrapezoids;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("calculate", message -> calculate())
                .build();
    }

    private void calculate() {
        double h = (end - start) / numTrapezoids;
        double sum = 0.5 * (function.apply(start) + function.apply(end));

        for (int i = 1; i < numTrapezoids; i++) {
            double x = start + i * h;
            sum += function.apply(x);
        }

        double result = h * sum;

        // Envía el resultado al coordinador
        getSender().tell(result, getSelf());
    }
}
