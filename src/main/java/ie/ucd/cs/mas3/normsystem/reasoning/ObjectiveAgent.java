package ie.ucd.cs.mas3.normsystem.reasoning;

import java.security.SecureRandom;
import java.util.Random;
import lombok.Data;

import org.apache.commons.math3.stat.StatUtils;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author viniciusdecarvalho
 */
@Data
public class ObjectiveAgent extends Agent {

    protected int nObj;
    protected int preferedObj;
    protected double[] weights;

    public ObjectiveAgent(int nObj) {
        this.nObj = nObj;
    }

    @Override
    public void init() {
        Random rdn = new SecureRandom();
        this.preferedObj = rdn.nextInt(nObj);
        this.weights = AgentKind.generateWeights(nObj, preferedObj, kind);
    }

    @Override
    public boolean whichIsBest(double currentFitness, double currentBestFitness) {
        return currentFitness < currentBestFitness;
    }

    @Override
    protected double calculateFitness(DoubleSolution s) {
        double[] values = new double[this.nObj];
        double[] objs = s.getObjectives();
        for (int i = 0; i < this.nObj; i++) {
            values[i] = objs[i] * this.weights[i];
        }
        return StatUtils.sum(values);
    }
}
