package ie.ucd.cs.mas3.normsystem.reasoning;

import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs;
import ie.ucd.cs.mas3.normsystem.problem.BiObjectiveJmetalOptimizationProblem;
import ie.ucd.cs.mas3.normsystem.problem.JmetalOptimizationProblem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author vinicius
 */
public class runReasoningEngineOverResults {

    public static void main(String[] args) throws FileNotFoundException {

        String varFile = args[0];
        String funFile = args[1];

        int numVariableAgents = Integer.parseInt(args[2]);
        int numObjectiveAgents = Integer.parseInt(args[3]);
        int nObj = Integer.parseInt(args[4]);
        int i = Integer.parseInt(args[5]);
        String algName = args[6];
        String problemName = args[7];

        int numSegments = 5;

        List<DoubleSolution> pflist = generatePopulation(varFile, funFile, nObj);

        NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
        for (DoubleSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
            nd.add(sol);
        }
        pflist = nd.getSolutionList();

        byte[] seed = Ints.toByteArray(0);
        Random rdn = new SecureRandom(seed);
        ReasoningGroup rg = new ReasoningGroup(numVariableAgents, numObjectiveAgents, nObj, numSegments, rdn);
        DoubleSolution bestSolution = rg.getBestSolution(pflist);
        int bestValue = rg.getBestValue();
        String bestKey = rg.getBestKey();
        double fitness = rg.avgFitness;
        BiObjectiveJmetalOptimizationProblem p = null;
        if (nObj == 2) {
            p = new BiObjectiveJmetalOptimizationProblem(0, 0, numSegments, 0.0, 0, 0);
        } else {
            p = new JmetalOptimizationProblem(0, 0, numSegments, 0.0, 0, 0);
        }
        p.revertToMaximization(bestSolution);
        //[0.9982308406082965, 0.8266800000000001]
        //System.out.println("The set of variables: " + bestKey + " is the best now with " + bestValue + " agent selections. The objective set is " + Arrays.toString(bestSolution.getObjectives()) + "\n");
        System.out.println(i + ";" + bestKey + ";" + bestValue + ";" + Arrays.toString(bestSolution.getObjectives()).replace("[", "").replace("]", ";").replace(", ", ";") + algName + ";" + problemName + ";" + fitness);

        List<DoubleSolution> finalNewResult = new ArrayList<>();
        finalNewResult.add(bestSolution);
        for (DoubleSolution s : finalNewResult) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(s, s.getNumberOfObjectives());
        }

        new SolutionListOutput(finalNewResult)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(funFile + "_ALLMINSINGLE" + i))
                .setVarFileOutputContext(new DefaultFileOutputContext(varFile + "_ALLMINSINGLE" + i))
                .print();
    }

    public static List<String> readFile(String fileName) {
        try {
            return Files.readLines(new File(fileName), Charset.forName("utf-8"));
        } catch (IOException ex) {
            Logger.getLogger(runReasoningEngineOverResults.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<DoubleSolution> generatePopulation(String variablesFile, String objectiveFile, int nObj) {
        BiObjectiveJmetalOptimizationProblem p = null;
        List<String> variablesList = readFile(variablesFile);
        List<String> objectivesList = readFile(objectiveFile);
        List<DoubleSolution> toReturn = new ArrayList<>();
        for (int i = 0; i < variablesList.size(); i++) {
            if (p == null) {
                //take the first element and discover how many decision variables
                int numberOfVariablesInFile = variablesList.get(i).split(" ").length;
                //oposite of int numOfDecisionVariables = 2 + numSegments * 2;
                int numSegments = (numberOfVariablesInFile - 2) / 2;
                if (nObj == 2) {
                    p = new BiObjectiveJmetalOptimizationProblem(200, 10, numSegments, 0.5, 10, 5000);
                } else {
                    p = new JmetalOptimizationProblem(200, 10, numSegments, 0.5, 10, 5000);
                }
            }
            DoubleSolution s = generateSolution(p, variablesList.get(i).split(" "), objectivesList.get(i).split(" "), nObj);
            toReturn.add(s);
        }
        return toReturn;
    }

    public static DoubleSolution generateSolution(BiObjectiveJmetalOptimizationProblem p, String[] variables, String[] objectives, int nObj) {
        NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
        for (int x = 0; x < 6; x++) {
            DoubleSolution ds = new DefaultDoubleSolution(p);
            for (int i = 0; i < variables.length; i++) {
                ds.setVariableValue(i, Double.valueOf(variables[i]));
            }
            p.evaluate(ds);
            nd.add(ds);
        }
        if (nd.size() == 1) {
            DoubleSolution ds = (DoubleSolution) nd.get(0);
            p.revertToMaximization(ds);
            return ds;
        }
        List<DoubleSolution> nonNegative = new ArrayList<>();
        for (Object s : nd.getSolutionList()) {
            DoubleSolution ds = (DoubleSolution) s;
            p.revertToMaximization(ds);
            boolean add = true;
            for (int i = 0; i < ds.getObjectives().length; i++) {
                if (ds.getObjectives()[i] < 0) {
                    add = false;
                }
            }
            if (add) {
                nonNegative.add(ds);
            }
        }
        Random rdn = new Random();
        if (!nonNegative.isEmpty()) {
            return nonNegative.get(rdn.nextInt(nonNegative.size()));
        }
        return (DoubleSolution) nd.getSolutionList().get(rdn.nextInt(nd.getSolutionList().size()));
    }
}
