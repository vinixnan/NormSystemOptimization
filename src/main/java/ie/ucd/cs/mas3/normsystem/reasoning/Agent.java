package ie.ucd.cs.mas3.normsystem.reasoning;

import lombok.Data;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ie.ucd.cs.mas3.normsystem.reasoning.AgentKind.NUMBER_OF_DIFFERENT_AGENTS;

@Data
public abstract class Agent {

    protected int kind;
    protected DoubleSolution personalBest;
    protected double personalBestFitness;
    protected String personalBestLabel;
    protected Random rdn;

    public Agent(Random rdn) {
        this.rdn = rdn;
        this.kind = this.rdn.nextInt(NUMBER_OF_DIFFERENT_AGENTS);
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
        this.personalBestLabel = null;
    }

    public void findBestInPopulation(List<DoubleSolution> result) {
        for (DoubleSolution s : result) {
            findBestInPopulation(s);
        }
    }

    public void findBestInPopulation(DoubleSolution s) {
        double currentFitness = calculateFitness(s);
        if (whichIsBest(currentFitness, this.personalBestFitness)) {
            this.personalBestFitness = currentFitness;
            this.personalBest = s;
            this.personalBestLabel = Arrays.toString(s.getVariables().toArray());
        }
    }

    public abstract void init();

    protected abstract boolean whichIsBest(double currentFitness, double currentBestFitness);

    protected abstract double calculateFitness(DoubleSolution s);
}
