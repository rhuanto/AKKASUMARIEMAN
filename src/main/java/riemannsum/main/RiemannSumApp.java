package riemannsum.main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import riemannsum.actors.CoordinatorActor;
import riemannsum.calculator.TrapezoidCalculation;

import java.util.function.Function;

public class RiemannSumApp {

    public static void main(String[] args) {
        // Crea el sistema de actores
        ActorSystem system = ActorSystem.create("RiemannSumSystem");

        // Define la función a integrar
        Function<Double, Double> function = x -> Math.pow(x, 3) + 2 * x + Math.exp(x + 2);

        // Define los parámetros del intervalo
        double a = 1.0;
        double b = 4.0;
        int numTrapezoids = 1000;

        // Crea el actor coordinador
        ActorRef coordinator = system.actorOf(Props.create(CoordinatorActor.class, function, a, b, numTrapezoids, 10), "coordinator");

        // Inicia el cálculo
        coordinator.tell("start", ActorRef.noSender());
    }
}
