package ie.ucd.cs.mas3.normsystem.problem;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class JmetalOptimizationProblem extends BiObjectiveJmetalOptimizationProblem {

    public JmetalOptimizationProblem(int numAgents, int numEvaders, int numSegments, double investRate, int length, int path) {
        super(numAgents, numEvaders, numSegments, investRate, length, path);
        setNumberOfObjectives(5);
        maxValuesToInvert.add(10.0);
        maxValuesToInvert.add(10.0);
        maxValuesToInvert.add(10.0);
    }

    @Override
    public void evaluate(DoubleSolution s) {
        fn.setLength(length);
        Society sc = this.createSociety(s);
        s.setObjective(0, maxValuesToInvert.get(0) - fn.equality_single_path(sc));
        fn.setLength(0);
        s.setObjective(1, maxValuesToInvert.get(1) - fn.fairness_single_path(sc));
        s.setObjective(2, maxValuesToInvert.get(2) - fn.newWealth_single_path(sc));
        s.setObjective(3, maxValuesToInvert.get(3) - fn.gainedAmount_single_path(sc));
        s.setObjective(4, maxValuesToInvert.get(4) - fn.collectAmount_single_path(sc));

    }

    @Override
    public void evaluateUsingMonteCarloSampling(DoubleSolution s) {
        List<Society> societies = new ArrayList<>();
        for (int i = 0; i < this.path; i++) {
            societies.add(this.createSociety(s));
        }
        double[] obj0 = new double[societies.size()];
        double[] obj1 = new double[societies.size()];
        double[] obj2 = new double[societies.size()];
        double[] obj3 = new double[societies.size()];
        double[] obj4 = new double[societies.size()];
        for (int i = 0; i < societies.size(); i++) {
            Society sc = societies.get(i);
            fn.setLength(length);
            obj0[i] = fn.equality_single_path(sc);
            fn.setLength(0);
            obj1[i] = fn.fairness_single_path(sc);
            obj2[i] = fn.newWealth_single_path(sc);
            obj3[i] = fn.gainedAmount_single_path(sc);
            obj4[i] = fn.collectAmount_single_path(sc);
        }
        double obj0mean = StatUtils.mean(obj0);
        double obj1mean = StatUtils.mean(obj1);
        double obj2mean = StatUtils.mean(obj2);
        double obj3mean = StatUtils.mean(obj3);
        double obj4mean = StatUtils.mean(obj4);

        s.setObjective(0, maxValuesToInvert.get(0) - obj0mean);
        s.setObjective(1, maxValuesToInvert.get(1) - obj1mean);
        s.setObjective(2, maxValuesToInvert.get(2) - obj2mean);
        s.setObjective(3, maxValuesToInvert.get(3) - obj3mean);
        s.setObjective(4, maxValuesToInvert.get(4) - obj4mean);
    }

}
