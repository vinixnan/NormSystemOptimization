package ie.ucd.cs.mas3.normsystem.problem;

import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class BiObjectiveJmetalOptimizationProblem extends AbstractDoubleProblem {

    protected int numAgents;
    protected int numEvaders;
    protected int numSegments;
    protected double investRate;
    protected int length;
    protected Functions fn;
    protected int path;
    protected int individualPath;

    public BiObjectiveJmetalOptimizationProblem(int numAgents, int numEvaders, int numSegments, double investRate, int length, int path) {
        this.numAgents = numAgents;
        this.numEvaders = numEvaders;
        this.numSegments = numSegments;
        this.investRate = investRate;
        this.length = length;
        this.path = path;

        this.fn = new Functions(length);

        int numOfDecisionVariables = 2 + numSegments * 2;

        setNumberOfVariables(numOfDecisionVariables);
        setNumberOfObjectives(2);
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

    protected Society createSociety(DoubleSolution s) {
        List<Double> decisionVariables = s.getVariables();
        double catche = decisionVariables.get(0);
        double fineRate = decisionVariables.get(1);
        double[] collectingRates = new double[this.numSegments];
        double[] redistributionRates = new double[this.numSegments];
        for (int i = 0; i < this.numSegments; i++) {
            collectingRates[i] = decisionVariables.get(i + 2);
            redistributionRates[i] = decisionVariables.get(2  + i + this.numSegments);
        }
        Society sc = new Society(collectingRates, redistributionRates, numAgents, numEvaders, fineRate, investRate, catche);
        return sc;
    }

    @Override
    public void evaluate(DoubleSolution s) {
        Society sc = this.createSociety(s);
        fn.setLength(length);
        fn.walk(sc);
        fn.setLength(0);
        double obj1 = fn.equality_single_path(sc);
        double obj2 = fn.fairness_single_path(sc);
        s.setObjective(0, -1 * obj1);
        s.setObjective(1, -1 * obj2);
    }

    public void evaluateUsingMonteCarloSampling(DoubleSolution s) {
        List<Society> societies = new ArrayList<>();
        for (int i = 0; i < this.individualPath; i++) {
            societies.add(this.createSociety(s));
        }
        double[] obj0 = new double[societies.size()];
        double[] obj1 = new double[societies.size()];
        for (int i = 0; i < societies.size(); i++) {
            Society sc = societies.get(i);
            fn.setLength(length);
            fn.walk(sc);
            fn.setLength(0);
            obj0[i] = fn.equality_single_path(sc);
            obj1[i] = fn.fairness_single_path(sc);
        }
        double obj0mean = StatUtils.mean(obj0);
        double obj1mean = StatUtils.mean(obj1);
        s.setObjective(0, -1 * obj0mean);
        s.setObjective(1, -1 * obj1mean);
    }
    
    public void evaluateUsingMonteCarloSampling(List<DoubleSolution> population){
        this.individualPath = this.path / population.size();
        System.out.println("Individual Path"+this.individualPath);
        for (DoubleSolution s : population) {
            this.evaluateUsingMonteCarloSampling(s);
        }
    }

    public void revertToMaximization(DoubleSolution s) {
        for (int i = 0; i < this.getNumberOfObjectives(); i++) {
            s.setObjective(i,  -1 * s.getObjective(i));
        }
    }
    
    public void revertToMaximization(List<DoubleSolution> population) {
        for (DoubleSolution s : population) {
            this.revertToMaximization(s);
        }
    }
}
