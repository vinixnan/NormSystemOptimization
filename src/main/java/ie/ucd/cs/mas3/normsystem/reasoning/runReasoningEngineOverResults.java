package ie.ucd.cs.mas3.normsystem.reasoning;

import ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs;
import ie.ucd.cs.mas3.normsystem.problem.BiObjectiveJmetalOptimizationProblem;
import ie.ucd.cs.mas3.normsystem.problem.JmetalOptimizationProblem;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

/**
 *
 * @author vinicius
 */
public class runReasoningEngineOverResults {

    public static void main(String[] args) throws FileNotFoundException {

        String pfFile = args[0];
        Front pfFront = new ArrayFront(pfFile);
        int nAgents = Integer.parseInt(args[1]);
        int nObj = Integer.parseInt(args[2]);
        int i = Integer.parseInt(args[3]);
        
        List<PointSolution> pflist = FrontUtils.convertFrontToSolutionList(pfFront);
        NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
        for (PointSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
            nd.add(sol);
        }
        pflist = nd.getSolutionList();

        ReasoningGroup rg = new ReasoningGroup(nAgents, nObj);
        DoubleSolution sol = rg.getBestSolution(convertTotype(pflist, nObj));

        List<DoubleSolution> finalNewResult = new ArrayList<>();
        finalNewResult.add(sol);
        for (DoubleSolution s : finalNewResult) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(s, s.getNumberOfObjectives());
        }

        new SolutionListOutput(finalNewResult)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(pfFile + "_FUN_ALLMINSINGLE" + i))
                .setVarFileOutputContext(new DefaultFileOutputContext(pfFile + "_VAR_ALLMINSINGLE" + i))
                .print();
    }

    public static List<DoubleSolution> convertTotype(List<PointSolution> result, int m) {
        AbstractDoubleProblem p;
        if (m == 2) {
            p = new BiObjectiveJmetalOptimizationProblem(0, 0, 0, 0.0, 0, 0);
        } else {
            p = new JmetalOptimizationProblem(0, 0, 0, 0.0, 0, 0);
        }
        List<DoubleSolution> toReturn = new ArrayList<>();
        for (Solution s : result) {
            DoubleSolution ds = new DefaultDoubleSolution(p);
            for (int i = 0; i < m; i++) {
                ds.setObjective(i, s.getObjective(i));
            }
            toReturn.add(ds);
        }
        return toReturn;
    }

}
