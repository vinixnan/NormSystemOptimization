package ie.ucd.cs.mas3.normsystem.reasoning;

import lombok.Data;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;
import java.util.Random;

/**
 *
 * @author viniciusdecarvalho
 */
@Data
public class VariableAgent extends Agent {

    protected int preferedSegment;
    protected int preferedDecision;

    public VariableAgent(int preferedSegment, Random rdn) {
        super(rdn);
        this.preferedSegment = preferedSegment; //segments have to be equally distributed
    }

    @Override
    public void init() {
        //4 possible decision cathe, finerate, collecting, redistribution. 
        //but we are taking just collecting - 2 and redistribution - 3
        this.preferedDecision = rdn.nextInt(2, 4);
        if (this.preferedDecision == 3) {
            //collecting is a maximization, so start the search with 0
            this.personalBestFitness = 0;
        }
    }

    @Override
    public boolean whichIsBest(double currentFitness, double currentBestFitness) {
        if (this.preferedDecision == 2) {
            return currentFitness < currentBestFitness;
        } else {
            return currentFitness > currentBestFitness;
        }
    }

    @Override
    protected double calculateFitness(DoubleSolution s) {
        List<Double> variables = s.getVariables();
        int collectingOrRedistribution = 0;
        if (this.preferedDecision == 3) {
            //collecting is the first element while redistribution is the second
            collectingOrRedistribution = 5;
        }
        //ignore the 2 first, take the prefered segment, take either collecting or redistribution
        int chosen = 2 + this.preferedSegment + collectingOrRedistribution;
        return variables.get(chosen);
    }
}
