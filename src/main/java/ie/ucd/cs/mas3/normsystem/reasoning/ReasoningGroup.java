package ie.ucd.cs.mas3.normsystem.reasoning;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author viniciusdecarvalho
 */
public class ReasoningGroup {

    private final Agent[] reasoningAgents;

    public ReasoningGroup(int numAgent, int nObj) {
        reasoningAgents = new Agent[numAgent];
        for (int j = 0; j < numAgent; j++) {
            reasoningAgents[j] = new Agent(nObj);
            reasoningAgents[j].generateWeights();
        }
    }

    public DoubleSolution getBestSolution(List<DoubleSolution> result) {
        Map<String, Integer> rankOfSolutions = new HashMap<>();
        Map<String, DoubleSolution> rankOfSolutionsSolutions = new HashMap<>();
        //Make all agents to use their own weights, find the best and sum this and add to Hash
        for (Agent reasoner : reasoningAgents) {
            reasoner.findBestInPopulation(result);
            int currentValueInTheRank = rankOfSolutions.getOrDefault(reasoner.getPersonalBestLabel(), 0);
            rankOfSolutions.put(reasoner.getPersonalBestLabel(), currentValueInTheRank + 1);
            rankOfSolutionsSolutions.put(reasoner.getPersonalBestLabel(), reasoner.getPersonalBest());
        }
        //Find the best
        int bestValue = 0;
        DoubleSolution bestSolution = null;
        for (String key : rankOfSolutions.keySet()) {
            if (rankOfSolutions.get(key) > bestValue) {
                bestValue = rankOfSolutions.get(key);
                bestSolution = rankOfSolutionsSolutions.get(key);
                System.out.println(key + " is the best now with " + bestValue + Arrays.toString(bestSolution.getObjectives()));
            }
        }
        return bestSolution;
    }
}
