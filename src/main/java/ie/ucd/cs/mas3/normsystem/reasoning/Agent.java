package ie.ucd.cs.mas3.normsystem.reasoning;

import static ie.ucd.cs.mas3.normsystem.reasoning.AgentKind.NUMBER_OF_DIFFERENT_AGENTS;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.Data;
import org.apache.commons.math3.stat.StatUtils;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author viniciusdecarvalho
 */
@Data
public class Agent {

    protected int nObj;
    protected int preferedObj;
    protected double[] weights;
    protected int kind;
    protected DoubleSolution personalBest;
    protected double personalBestFitness;
    protected String personalBestLabel;

    public Agent(int nObj, int preferedObj, int kind) {
        this.nObj = nObj;
        this.preferedObj = preferedObj;
        this.kind = kind;
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
        this.personalBestLabel = null;
    }

    public Agent(int nObj, int kind) {
        Random rdn = new SecureRandom();
        this.nObj = nObj;
        this.preferedObj = rdn.nextInt(nObj);
        this.kind = kind;
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
        this.personalBestLabel = null;
    }

    public Agent(int nObj) {
        Random rdn = new SecureRandom();
        this.nObj = nObj;
        this.preferedObj = rdn.nextInt(nObj);
        this.kind = rdn.nextInt(NUMBER_OF_DIFFERENT_AGENTS);
        this.weights = null;
        this.personalBestFitness = Double.MAX_VALUE;
        this.personalBest = null;
        this.personalBestLabel = null;
    }

    public void generateWeights() {
        this.weights = AgentKind.generateWeights(nObj, preferedObj, kind);
    }

    public void findBestInPopulation(List<DoubleSolution> result) {
        for (DoubleSolution s : result) {
            findBestInPopulation(s);
        }
    }

    public void findBestInPopulation(DoubleSolution s) {
        double[] values = new double[this.nObj];
        double[] objs=s.getObjectives();
        for (int i = 0; i < this.nObj; i++) {
            values[i] = objs[i] * this.weights[i];
        }
        double currentFitness = StatUtils.sum(values);
        if (currentFitness < this.personalBestFitness) {
            this.personalBestFitness = currentFitness;
            this.personalBest = s;
            this.personalBestLabel = Arrays.toString(s.getVariables().toArray());
        }
    }
}
