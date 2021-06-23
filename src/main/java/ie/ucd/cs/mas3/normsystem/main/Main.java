package ie.ucd.cs.mas3.normsystem.main;

import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforAlgorithm;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforHeuristics;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.interfaces.LLHInterface;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.AlgorithmBuilder;
import ie.ucd.cs.mas3.normsystem.problem.BiObjectiveJmetalOptimizationProblem;
import ie.ucd.cs.mas3.normsystem.problem.JmetalOptimizationProblem;
import java.io.File;
import java.io.FileNotFoundException;
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
            case 2 -> {
                return 100;
            }
            case 3 -> {
                return 91;
            }
            //4
            case 5 -> {
                return 210;
            }
            //6
            case 8 -> {
                return 156;
            }
            //9
            case 10 -> {
                return 275;
            }
            case 15 -> {
                return 135;
            }
        }
        return 100;
    }

    public static void main(String[] args) throws ConfigurationException, JMException, FileNotFoundException {

        String algConf = "NSGAII";
        int numAgents = 200;
        int numEvaders = 10;
        int numSegments = 5;
        double investRate = 0.05;
        int length = 10;
        int path = 5000;
        int i = 0;
        BiObjectiveJmetalOptimizationProblem problem;
        int qtdObj = 2;

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
        ParametersforAlgorithm params = new ParametersforAlgorithm(algConf);
        params.setPopulationSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setArchiveSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setMaxIteractions(500);

        AlgorithmBuilder ab = new AlgorithmBuilder(problem);
        LLHInterface alg = ab.create(params, new ParametersforHeuristics(opConf, problem.getNumberOfVariables()));
        String dir = "result/" + params.getAlgorithmName() + "/" + problem.getName() + "_obj_" + problem.getNumberOfObjectives() + "/" + confApendixName + "/" + params.getMaxIteractions();
        new File(dir).mkdirs();
        String funFile = dir + "/FUN";
        String varFile = dir + "/VAR";
        System.out.println("Run " + alg.getClass().getSimpleName());
        long init = System.currentTimeMillis();
        alg.run();
        List<DoubleSolution> result = alg.getResult();
        System.out.println("Monte Carlo Sampling");
        problem.evaluateUsingMonteCarloSampling(result);
        System.out.println("Remove Non Dominated");
        NonDominatedSolutionListArchive arq = new NonDominatedSolutionListArchive();
        for (Solution s : result) {
            arq.add(s);
        }
        List<DoubleSolution> archive = arq.getSolutionList();

        new SolutionListOutput(archive)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(funFile + "_ALLMIN" + i))
                .setVarFileOutputContext(new DefaultFileOutputContext(varFile + "_ALLMIN" + i))
                .print();

        long end = System.currentTimeMillis();
        double total = (((double) (end - init)) / 1000.0) / 60.0;
        System.out.println("The algorithm have found " + archive.size() + " solutions in " + total + " minutes");
    }
}
