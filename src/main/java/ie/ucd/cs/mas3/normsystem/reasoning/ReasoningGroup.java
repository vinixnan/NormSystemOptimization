package ie.ucd.cs.mas3.normsystem.reasoning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author viniciusdecarvalho
 */
@Data
public class ReasoningGroup {

    private final Agent[] reasoningAgents;
    protected int bestValue;
    protected DoubleSolution bestSolution;
    protected String bestKey;

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
        bestValue = 0;
        bestSolution = null;
        for (String key : rankOfSolutions.keySet()) {
            if (rankOfSolutions.get(key) > bestValue) {
                bestValue = rankOfSolutions.get(key);
                bestSolution = rankOfSolutionsSolutions.get(key);
                bestKey = key;
            }
        }
        return bestSolution;
    }
}
