package ie.ucd.cs.mas3.normsystem.problem;

import com.google.common.primitives.Doubles;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class JmetalOptimizationProblem extends AbstractDoubleProblem {

    protected int numAgents;
    protected int numEvaders;
    protected int numSegments;
    protected double investRate;
    protected int length;
    protected Functions fn;

    public JmetalOptimizationProblem(int numAgents, int numEvaders, int numSegments, double investRate, int length) {
        this.numAgents = numAgents;
        this.numEvaders = numEvaders;
        this.numSegments = numSegments;
        this.investRate = investRate;
        this.length = length;

        this.fn = new Functions(length);

        int numOfDecisionVariables = 2 + numSegments * 2;

        setNumberOfVariables(numOfDecisionVariables);
        setNumberOfObjectives(5);
        setNumberOfConstraints(0);
        setName("NormSystem");

        double[] lowerPrimitiveArray = new double[numOfDecisionVariables];
        Arrays.fill(lowerPrimitiveArray, 0);
        List<Double> lowerLimit = Doubles.asList(lowerPrimitiveArray);

        double[] upperPrimitiveArray = new double[numOfDecisionVariables];
        upperPrimitiveArray[0] = 0.5; //for catch
        Arrays.fill(upperPrimitiveArray, 1);
        List<Double> upperLimit = Doubles.asList(upperPrimitiveArray);

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    @Override
    public void evaluate(DoubleSolution s) {
        List<Double> decisionVariables = s.getVariables();
        double catche = decisionVariables.get(0);
        double fineRate = decisionVariables.get(1);
        double[] collectingRates = new double[this.numSegments];
        double[] redistributionRates = new double[this.numSegments];
        for (int i = 0; i < this.numSegments; i++) {
            collectingRates[i] = decisionVariables.get(i + 2);
            redistributionRates[i] = decisionVariables.get(i + this.numSegments);
        }
        fn.setLength(length);
        Society sc = new Society(collectingRates, redistributionRates, numAgents, numEvaders, fineRate, investRate, catche);
        s.setObjective(0, 1 - fn.equality_single_path(sc));
        fn.setLength(0);
        s.setObjective(1, 1 - fn.fairness_single_path(sc));
        s.setObjective(2, 1 - fn.newWealth_single_path(sc));
        s.setObjective(3, 1 - fn.gainedAmount_single_path(sc));
        s.setObjective(4, 1 - fn.collectAmount_single_path(sc));

    }

}
