package ie.ucd.cs.mas3.normsystem.problem;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
@Data
public class Functions {

    /**
     *
     */
    protected int length = 0; // length of the paths for evaluation of alignment

    /**
     *
     */
    protected int paths = 500;  // number of paths for evaluation of fitness

    public Functions(int length) {
        this.length = length;
    }
    
    
    

    /**
     * Updates the model considering the length
     *
     * @param model
     */
    public void walk(Society model) {
        for (int i = 0; i < this.length; i++) {
            model.step();
        }
    }

    /**
     * Evaluate a model by its alignment with respect to equality after one
     * path.
     *
     * @param model
     * @return equality
     */
    public double equality_single_path(Society model) {
        this.walk(model);
        double gini_index = model.compute_gini_wealth();
        return (1 - 2 * gini_index);
    }

    /**
     * Evaluate a model by its alignment with respect to fairness after one
     * path.
     *
     * @param model
     * @return fairness
     */
    public double fairness_single_path(Society model) {
        this.walk(model);
        List<Individual> evaders_segment = model.getEvadersInSegment(0);
        double proportion = ((double) evaders_segment.size()) / ((double) model.num_evaders);
        return (2 * proportion - 1);
    }

    /**
     * Evaluate a model by its alignment with the new wealth of segment (5)
     *
     * @param model
     * @return newWeath
     */
    public double newWealth_single_path(Society model) {
        //print("Currently we evaluate a model by its alignment with the new wealth of segment (5) \n")
        double totalWealth = 0;
        double newWealth = 0;
        this.walk(model);
        //agent_wealths = [agent.wealth for agent in model.agents if agent.segment==5]
        List<Individual> agents = model.getAgents();
        for (Individual agent : agents) {
            totalWealth += agent.wealth;
            if (agent.segment == 4) {
                newWealth += agent.wealth;
            }
        }
        //print("The alignment function finished successfully \n")
        return newWealth / totalWealth;
    }

    /**
     * Evaluate a model by its alignment with gained amount of segment (4)
     *
     * @param model
     * @return gainedAmount
     */
    public double gainedAmount_single_path(Society model) {
        double gainedAmount = 0;
        this.walk(model);
        List<Individual> agents = model.getAgents();
        gainedAmount = agents.stream().filter(agent -> (agent.segment == 3)).map(agent -> agent.wealth - agent.old_wealth).reduce(gainedAmount, (accumulator, _item) -> accumulator + _item);
        return gainedAmount / model.common_fund;
    }

    /**
     * Evaluate a model by its alignment with redistribution amount of segment
     * (2)
     *
     * @param model
     * @return redistributed amount
     */
    public double redistributeAmount_single_path(Society model) {
        this.walk(model);
        return model.redistribution_rates[1];
    }

    /**
     * Evaluate a model by its alignment with collected amount of segment (1)
     *
     * @param model
     * @return colected amount
     */
    public double collectAmount_single_path(Society model) {
        this.walk(model);
        for (Individual agent : model.getAgents()) {
            if (agent.segment == 0) {
                double collect_rate = model.collecting_rates[0];
                return 1 - collect_rate;
            }
        }
        return 0;
    }

    /**
     *
     * @param model
     * @return agregation between fairness and equality
     */
    public double aggregation_single_path(Society model) {
        this.walk(model);
        double gini_index = model.compute_gini_wealth();
        double equality = (1 - 2 * gini_index);
        List<Individual> evaders_segment = model.getEvadersInSegment(0);
        double proportion = ((double) evaders_segment.size()) / ((double) model.num_evaders);
        double fairness = (2 * proportion - 1);
        if (fairness < 0 && equality < 0) {
            return -fairness * equality;
        }
        return fairness * equality;
    }
}
