package ie.ucd.cs.mas3.normsystem.reasoning;

import static ie.ucd.cs.mas3.normsystem.reasoning.AgentKind.NUMBER_OF_DIFFERENT_AGENTS;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.stat.StatUtils;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author viniciusdecarvalho
 */
public class Agent {

    protected int nObj;
    protected int preferedObj;
    protected double[] weights;
    protected int kind;
    protected Solution personalBest;
    protected double personalBestFitness;

    public Agent(int nObj, int preferedObj, int kind) {
        this.nObj = nObj;
        this.preferedObj = preferedObj;
        this.kind = kind;
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
    }

    public Agent(int nObj, int kind) {
        Random rdn = new SecureRandom();
        this.nObj = nObj;
        this.preferedObj = rdn.nextInt(nObj);
        this.kind = kind;
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
    }

    public Agent(int nObj) {
        Random rdn = new SecureRandom();
        this.nObj = nObj;
        this.preferedObj = rdn.nextInt(nObj);
        this.kind = rdn.nextInt(NUMBER_OF_DIFFERENT_AGENTS);
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
    }

    public void generateWeights() {
        this.weights = AgentKind.generateWeights(nObj, preferedObj, kind);
    }

    public void findBestInPopulation(List<DoubleSolution> result) {
        for (DoubleSolution s : result) {
            findBestInPopulation(s);
        }
    }

    public void findBestInPopulation(Solution s) {
        double[] values = new double[s.getObjectives().length];
        for (int i = 0; i < s.getObjectives().length; i++) {
            values[i] = s.getObjectives()[i] * this.weights[i];
        }
        double currentFitness = StatUtils.sum(values);
        if (currentFitness < this.personalBestFitness) {
            this.personalBestFitness = currentFitness;
            this.personalBest = s;
        }
    }
}
