package ie.ucd.cs.mas3.normsystem.reasoning;

import java.util.ArrayList;
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

    protected final List<Agent> reasoningAgents;
    protected int bestValue;
    protected DoubleSolution bestSolution;
    protected String bestKey;

    public ReasoningGroup(int numVariableAgents, int numObjectiveAgents, int nObj, int numSegments) {
        this.reasoningAgents = new ArrayList<>();
        for (int j = 0; j < numObjectiveAgents; j++) {
            Agent ag = new ObjectiveAgent(nObj);
            this.reasoningAgents.add(ag);
        }
        int qtdAgentPerSegment = numVariableAgents / numSegments;
        for (int preferedSegment = 0; preferedSegment < numSegments; preferedSegment++) {
            for (int j = 0; j < qtdAgentPerSegment; j++) {
                Agent ag = new VariableAgent(preferedSegment);
                this.reasoningAgents.add(ag);
            }
        }
        for (Agent ag : this.reasoningAgents) {
            ag.init();
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
