package ie.ucd.cs.mas3.normsystem.main;

import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforAlgorithm;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforHeuristics;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.interfaces.LLHInterface;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.AlgorithmBuilder;
import ie.ucd.cs.mas3.normsystem.problem.BiObjectiveJmetalOptimizationProblem;
import ie.ucd.cs.mas3.normsystem.problem.JmetalOptimizationProblem;
import ie.ucd.cs.mas3.normsystem.reasoning.ReasoningGroup;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.management.JMException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.uma.jmetal.solution.DoubleSolution;
import java.util.List;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author viniciusdecarvalho
 */
public class Main {

    public static int getPopulationSize(int qtdObj) {
        switch (qtdObj) {
            case 2:
                return 100;
            case 3:
                return 91;
            case 5:
                return 210;
            case 8:
                return 156;
            //9
            case 10:
                return 275;
            case 15:
                return 135;
        }
        return 100;
    }

    public static void main(String[] args) throws ConfigurationException, JMException, FileNotFoundException {
        /*Get parameters externally or set default*/
        String algConf = "NSGAII";
        int maxIterations = 500;
        int numberOfReasoningAgents = 50;
        int numAgents = 200;
        int numEvaders = 10;
        int numSegments = 5;
        double investRate = 0.05;
        int length = 10;
        int path = 5000;
        int i = 14;
        BiObjectiveJmetalOptimizationProblem problem;
        int qtdObj = 5;

        String opConf = "SBX.Poly.default";

        if (args.length == 9) {
            algConf = args[0];
            numAgents = Integer.parseInt(args[1]);
            numEvaders = Integer.parseInt(args[2]);
            numSegments = Integer.parseInt(args[3]);
            investRate = Double.parseDouble(args[4]);
            length = Integer.parseInt(args[5]);
            path = Integer.parseInt(args[6]);
            qtdObj = Integer.parseInt(args[7]);
            i = Integer.parseInt(args[8]);
        }
        if (qtdObj == 2) {
            problem = new BiObjectiveJmetalOptimizationProblem(numAgents, numEvaders, numSegments, investRate, length, path);
        } else {
            problem = new JmetalOptimizationProblem(numAgents, numEvaders, numSegments, investRate, length, path);
        }

        algConf += ".default";
        System.out.println(algConf + " numAgents:" + numAgents + " numEvaders:" + numEvaders + " numSegments:" + numSegments + " investRate:" + investRate + " length:" + length + " path:" + path + " idExecution:" + i + " qtdObj:" + qtdObj);

        String confApendixName = "conf_" + numAgents + "_" + numEvaders + "_" + numSegments + "_" + investRate + "_" + length + "_" + path;
        /*--END  Get parameters externally or set default*/

        /*Create Algorithm Instance */
        ParametersforAlgorithm params = new ParametersforAlgorithm(algConf);
        params.setPopulationSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setArchiveSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setMaxIteractions(maxIterations);
        if (params.getAlgorithmName().startsWith("Mombi")) {
            params.setWeightsPath("mombi2-weights/weight/W" + qtdObj + "D_" + params.getPopulationSize() + ".dat");
        }
        AlgorithmBuilder ab = new AlgorithmBuilder(problem);
        LLHInterface alg = ab.create(params, new ParametersforHeuristics(opConf, problem.getNumberOfVariables()));
        /*--ENDCreate Algorithm Instance */

        /*Create result directory*/
        String dir = "result/" + params.getAlgorithmName() + "/" + problem.getName() + "_obj_" + problem.getNumberOfObjectives() + "/" + confApendixName + "/" + params.getMaxIteractions();
        new File(dir).mkdirs();
        String funFile = dir + "/FUN";
        String varFile = dir + "/VAR";
        /*--ENDCreate result directory*/

        /*Run generation by generation*/
        System.out.println("Run " + alg.getClass().getSimpleName());
        long init = System.currentTimeMillis();
        alg.initMetaheuristic();
        for (int it = 0; it < maxIterations; it++) {
            alg.generateNewPopulation();
            System.err.println(it + " for " + i);
        }
        /*--ENDRun generation by generation*/

        /*Get results, Submit it to Monte Carlo Sampling and get Non-Dominated Solutions*/
        List<DoubleSolution> result = alg.getResult();
        System.out.println("Size: " + result.size());
        //result = Main.removeRepeated(result);
        System.out.println("Remove Non Dominated");
        NonDominatedSolutionListArchive arq = new NonDominatedSolutionListArchive();
        for (Solution s : result) {
            arq.add(s);
        }
        List<DoubleSolution> archive = arq.getSolutionList();
        result = archive;
        System.out.println("Size: " + result.size());
        System.out.println("Monte Carlo Sampling");
        problem.evaluateUsingMonteCarloSampling(result);

        /*--ENDGet results, Submit it to Monte Carlo Sampling and get Non-Dominated Solutions*/
        
        /* Reasoning Engine */
        ReasoningGroup rg = new ReasoningGroup(numberOfReasoningAgents, problem.getNumberOfObjectives());
        DoubleSolution sol = rg.getBestSolution(archive);
        problem.revertToMaximization((DoubleSolution) sol);
        System.out.println(Arrays.toString(sol.getVariables().toArray()));
        List<DoubleSolution> finalNewResult = new ArrayList<>();
        finalNewResult.add(sol);

        /*Revert minimization problem into its normal form (maximization) and save the result in text files*/
        System.out.println("Reverting to maximization");
        problem.revertToMaximization(archive);
        new SolutionListOutput(archive)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(funFile + "_ALLMIN" + i))
                .setVarFileOutputContext(new DefaultFileOutputContext(varFile + "_ALLMIN" + i))
                .print();
        /*--ENDRevert minimization problem into its normal form (maximization) and save the result in text files*/

        //Save the best solution into a file
        new SolutionListOutput(finalNewResult)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(funFile + "_ALLMINSINGLE" + i))
                .setVarFileOutputContext(new DefaultFileOutputContext(varFile + "_ALLMINSINGLE" + i))
                .print();
        
        long end = System.currentTimeMillis();
        double total = (((double) (end - init)) / 1000.0) / 60.0;
        System.out.println("The algorithm have found " + archive.size() + " solutions in " + total + " minutes");
    }

    public static List<DoubleSolution> removeRepeated(List<DoubleSolution> pop) {
        HashMap<String, Boolean> isInside = new HashMap<>();
        List<DoubleSolution> toReturn = new ArrayList<>();
        for (DoubleSolution s : pop) {
            String str = Arrays.toString(s.getVariables().toArray());
            if (!isInside.getOrDefault(str, false)) {
                isInside.put(str, true);
                toReturn.add(s);
            }
        }
        return toReturn;
    }
}
