package ie.ucd.cs.mas3.normsystem.reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    protected double avgFitness;
    protected int qtdSelected;

    public ReasoningGroup(int numVariableAgents, int numObjectiveAgents, int nObj, int numSegments) {
        this(numVariableAgents, numObjectiveAgents, nObj, numSegments, null);
    }

    public ReasoningGroup(int numVariableAgents, int numObjectiveAgents, int nObj, int numSegments, Random rdn) {
        this.reasoningAgents = new ArrayList<>();
        for (int j = 0; j < numObjectiveAgents; j++) {
            Agent ag = new ObjectiveAgent(nObj, rdn);
            this.reasoningAgents.add(ag);
        }
        int qtdAgentPerSegment = numVariableAgents / numSegments;
        for (int preferedSegment = 0; preferedSegment < numSegments; preferedSegment++) {
            for (int j = 0; j < qtdAgentPerSegment; j++) {
                Agent ag = new VariableAgent(preferedSegment, rdn);
                this.reasoningAgents.add(ag);
            }
        }
        for (Agent ag : this.reasoningAgents) {
            ag.init();
        }
        avgFitness = 0;
    }

    public DoubleSolution getBestSolution(List<DoubleSolution> result) {
        Map<String, Integer> rankOfSolutions = new HashMap<>();
        Map<String, Double> sumOfFitness = new HashMap<>();
        Map<String, DoubleSolution> rankOfSolutionsSolutions = new HashMap<>();
        //Make all agents to use their own weights, find the best and sum this and add to Hash
        for (Agent reasoner : reasoningAgents) {
            reasoner.findBestInPopulation(result);
            int currentValueInTheRank = rankOfSolutions.getOrDefault(reasoner.getPersonalBestLabel(), 0);
            rankOfSolutions.put(reasoner.getPersonalBestLabel(), currentValueInTheRank + 1);
            double currentValue = sumOfFitness.getOrDefault(reasoner.getPersonalBestLabel(), 0.0);
            sumOfFitness.put(reasoner.getPersonalBestLabel(), currentValue + reasoner.personalBestFitness);
            rankOfSolutionsSolutions.put(reasoner.getPersonalBestLabel(), reasoner.getPersonalBest());
        }
        bestValue = 0;
        bestSolution = null;
        for (String key : rankOfSolutions.keySet()) {
            if (rankOfSolutions.get(key) > bestValue) {
                bestValue = rankOfSolutions.get(key);
                bestSolution = rankOfSolutionsSolutions.get(key);
                bestKey = key;
            }
        }
        avgFitness = (sumOfFitness.get(bestKey) / bestValue) * -1;
        return bestSolution;
    }
}
