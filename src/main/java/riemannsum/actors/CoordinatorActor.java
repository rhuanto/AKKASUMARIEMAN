package riemannsum.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import riemannsum.calculator.TrapezoidCalculation;

import java.util.function.Function;

public class CoordinatorActor extends AbstractActor {
    double suma=0;
    int contador=1;
    private final Function<Double, Double> function;
    private final double a;
    private final double b;
    private final int numTrapezoids;
    private final int numActors;

    public CoordinatorActor(Function<Double, Double> function, double a, double b, int numTrapezoids, int numActors) {
        this.function = function;
        this.a = a;
        this.b = b;
        this.numTrapezoids = numTrapezoids;
        this.numActors = numActors;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("start", message -> startCalculation())
                .match(Double.class, result -> handleResult(result))
                .build();
    }

    private void startCalculation() {
        double interval = (b - a) / numActors;
        ActorRef[] trapezoidActors = new ActorRef[numActors];

        for (int i = 0; i < numActors; i++) {
            double start = a + i * interval;
            double end = a + (i + 1) * interval;

            // Crea el actor de trapezoide y envía el trabajo
            trapezoidActors[i] = getContext().actorOf(Props.create(TrapezoidCalculation.class, function, start, end, numTrapezoids));
            trapezoidActors[i].tell("calculate", getSelf());
        }
    }

    private void handleResult(double result) {
        // Maneja el resultado (puede sumar todos los resultados parciales)
        suma=suma+result;       
        System.out.println("Resultado de la suma del actor"+contador+" :"+result);      
        if(contador!=10){
            contador++;
        }else{
        System.out.println("RESULTADO DE LA SUMA TOTAL :" + suma);
        getContext().getSystem().terminate();
        }
        }
}
