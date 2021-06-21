package ie.ucd.cs.mas3.normsystem.main;

import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforAlgorithm;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.ParametersforHeuristics;
import br.usp.poli.pcs.lti.jmetalhhhelper.core.interfaces.LLHInterface;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.AlgorithmBuilder;
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
        int numAgents = 20;
        int numEvaders = 2;
        int numSegments = 5;
        double investRate = 0.05;
        int length = 10;
        int i = 0;
        JmetalOptimizationProblem problem = new JmetalOptimizationProblem(numAgents, numEvaders, numSegments, investRate, length);
        String algConf = "MOEADD.default";
        ParametersforAlgorithm params = new ParametersforAlgorithm(algConf);
        params.setPopulationSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setArchiveSize(Main.getPopulationSize(problem.getNumberOfObjectives()));
        params.setMaxIteractions(100);
        String opConf = "SBX.Poly.default";
        AlgorithmBuilder ab = new AlgorithmBuilder(problem);
        LLHInterface alg = ab.create(params, new ParametersforHeuristics(opConf, problem.getNumberOfVariables()));
        String dir = "result/" + params.getAlgorithmName() + "/" + problem.getName() + "_obj_" + problem.getNumberOfObjectives() + "/" + params.getMaxIteractions();
        new File(dir).mkdirs();
        String funFile = dir + "/FUN";
        System.out.println("Run " + alg.getClass().getSimpleName());
        alg.run();
        List<DoubleSolution> result = alg.getResult();
        NonDominatedSolutionListArchive arq = new NonDominatedSolutionListArchive();
        for (Solution s : result) {
            arq.add(s);
        }
        List<DoubleSolution> archive = arq.getSolutionList();

        new SolutionListOutput(archive)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(funFile + "_ALLMIN" + i))
                .print();

        System.out.println("The algorithm have found " + archive.size() + " solutions");
    }
}
