package ie.ucd.cs.mas3.normsystem.problem;

import com.google.common.math.DoubleMath;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
@Data
public class Society {

    /**
     *
     */
    protected int num_segments;

    /**
     *
     */
    protected int num_agents;

    /**
     *
     */
    protected int num_evaders;

    /**
     *
     */
    protected double[] collecting_rates;

    /**
     *
     */
    protected double[] redistribution_rates;

    /**
     *
     */
    protected double fine_rate;

    /**
     *
     */
    protected double invest_rate;

    /**
     *
     */
    protected Double common_fund;

    /**
     *
     */
    protected List<Individual> agents;

    /**
     *
     */
    protected int time_step;
    
    protected double catche;
    
    protected List<Individual> evaders;

    /**
     *
     */
    public Society() {
    }

    /**
     * Constructor
     *
     * @param collecting_rates
     * @param redistribution_rates
     * @param num_agents
     * @param num_evaders
     * @param fine_rate
     * @param invest_rate
     * @param catche
     */
    public Society(double[] collecting_rates, double[] redistribution_rates, int num_agents, int num_evaders, double fine_rate, double invest_rate, double catche) {
        this.num_segments = collecting_rates.length;
        this.num_agents = num_agents;
        this.num_evaders = num_evaders;
        this.collecting_rates = collecting_rates; // collecting rates by group
        this.redistribution_rates = redistribution_rates;// redist. rates by group
        //this.catch = catch  # probability of catching an evader
        this.fine_rate = fine_rate; // fine to be imposed if an evader in caught
        this.invest_rate = invest_rate;  // interest return to the investment
        this.common_fund = 0.0;//.  # collected taxes for each transition
        this.catche = catche;

        this.agents = new ArrayList<>();
        this.evaders = new ArrayList<>();
        int aux_qtd = this.num_evaders;
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < this.num_agents; i++) {
            Individual ind = new Individual();
            ind.setCatche(catche);
            if (aux_qtd > 0 && rnd.nextBoolean()) {
                ind.is_evader = true;
                this.evaders.add(ind);
                aux_qtd--;
            }
            this.agents.add(ind);
        }
        this.time_step = 0;
        this.assign_agents_to_segments();
    }

    /**
     *
     */
    public void step() {
        this.common_fund = 0.0;
        //collect taxes from all agents
        this.agents.forEach(ind -> {
            this.common_fund = ind.step(common_fund, collecting_rates, fine_rate);
        });
        this.agents.forEach(ind -> {
            double payback = this.common_fund * (1 + this.invest_rate) * this.redistribution_rates[ind.segment] * this.num_segments / this.num_agents;
            ind.wealth += payback;
            ind.payback = payback;
        });
        this.assign_agents_to_segments();
        this.time_step += 1;
    }

    /**
     * Compute Gini Wealth
     *
     * @return
     */
    public double compute_gini_wealth() {
        List<Double> weaths = this.getAgentsWeath();
        List<Double> res=new ArrayList<>();
        weaths.forEach(x_i -> {
            weaths.forEach(x_j -> {
                res.add(Math.abs(x_i - x_j));
            });
        });
        double mean = weaths.stream().mapToDouble(a->a).average().getAsDouble();
        double num = res.stream().mapToDouble(a->a).sum();
        double gini_index = (num) / (2 * Math.pow(this.num_agents, 2) * mean);
        return gini_index;
    }

    /**
     *
     */
    public void assign_agents_to_segments() {
        List<Individual> agents = new ArrayList<>(this.agents);
        agents.sort(Comparator.comparing(individual -> individual.getWealth()));
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).position = this.num_agents - i;
        }
        int cut_index = (this.num_agents / this.num_segments);
        for (int n = 0; n < this.num_segments; n++) {
            int posini = n * cut_index;
            int posfim = (n + 1) * cut_index;
            for (int i = posini; i < posfim; i++) {
                agents.get(i).segment = n;
            }
            try {
                for (int i = posfim; i < agents.size(); i++) {
                    agents.get(i).segment = n;
                }
            } catch (Exception e) {
                System.out.println("Ue");
            }
        }
    }

    /**
     *
     * @return
     */
    public List<Double> getAgentsWeath() {
        List<Double> toReturn = new ArrayList<>();
        this.agents.forEach(ind -> {
            toReturn.add(ind.wealth);
        });
        return toReturn;
    }

    /**
     *
     * @return
     */
    public List<Individual> getEvadersAgents() {
        return this.evaders;
    }

    /**
     *
     * @param segment
     * @return
     */
    public List<Individual> getEvadersInSegment(int segment) {
        List<Individual> toReturn = new ArrayList<>();
        evaders.stream().filter(ind -> (ind.segment == segment)).forEachOrdered(ind -> {
            toReturn.add(ind);
        });
        return toReturn;
    }

    /**
     *
     * @param weaths
     */
    public void setWeath(double[] weaths) {
        int i = 0;
        for (Individual a : this.agents) {
            a.wealth = weaths[i++];
        }
    }

    /**
     *
     * @param weaths
     */
    public void setOldWeath(double[] weaths) {
        int i = 0;
        for (Individual a : this.agents) {
            a.old_wealth = weaths[i++];
        }
    }

    /**
     *
     * @param evaders
     */
    public void setEvaders(boolean[] evaders) {
        int i = 0;
        this.evaders = new ArrayList<>();
        for (Individual a : this.agents) {
            a.is_evader = evaders[i++];
            if(a.is_evader){
                this.evaders.add(a);
            }
        }
    }

    /**
     *
     * @param segments
     */
    public void setSegments(int[] segments) {
        int i = 0;
        for (Individual a : this.agents) {
            a.segment = segments[i++];
        }
    }

    /**
     * Create Object from JSON String
     *
     * @param json
     */
    public void createFromJSON(String json) {
        JSONObject obj = new JSONObject(json);

        this.num_segments = obj.getInt("num_segments");

        this.collecting_rates = new double[num_segments];
        JSONArray aux = obj.getJSONArray("collecting_rates");
        for (int i = 0; i < aux.length(); i++) {
            this.collecting_rates[i] = aux.getDouble(i);
        }

        this.redistribution_rates = new double[num_segments];
        aux = obj.getJSONArray("redistribution_rates");
        for (int i = 0; i < aux.length(); i++) {
            this.redistribution_rates[i] = aux.getDouble(i);
        }

        this.num_agents = obj.getInt("num_agents");
        this.num_evaders = obj.getInt("num_evaders");
        this.fine_rate = obj.getDouble("fine_rate");
        this.invest_rate = obj.getDouble("invest_rate");
        this.common_fund = obj.getDouble("common_fund");

        aux = obj.getJSONArray("agents");
        this.agents = new ArrayList<>();
        this.evaders = new ArrayList<>();
        for (int i = 0; i < this.num_agents; i++) {
            JSONObject obj2 = aux.getJSONObject(i);
            Individual ind = new Individual();
            ind.createFromJSON(obj2);
            this.agents.add(ind);
            if(ind.is_evader){
                this.evaders.add(ind);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.num_segments;
        hash = 97 * hash + this.num_agents;
        hash = 97 * hash + this.num_evaders;
        hash = 97 * hash + Arrays.hashCode(this.collecting_rates);
        hash = 97 * hash + Arrays.hashCode(this.redistribution_rates);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.fine_rate) ^ (Double.doubleToLongBits(this.fine_rate) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.invest_rate) ^ (Double.doubleToLongBits(this.invest_rate) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.common_fund);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Society other = (Society) obj;
        if (this.num_segments != other.num_segments) {
            return false;
        }
        if (this.num_agents != other.num_agents) {
            return false;
        }
        if (this.num_evaders != other.num_evaders) {
            return false;
        }
        if (Double.doubleToLongBits(this.fine_rate) != Double.doubleToLongBits(other.fine_rate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.invest_rate) != Double.doubleToLongBits(other.invest_rate)) {
            return false;
        }
        if (!Arrays.equals(this.collecting_rates, other.collecting_rates)) {
            return false;
        }
        if (!Arrays.equals(this.redistribution_rates, other.redistribution_rates)) {
            return false;
        }
        if (!DoubleMath.fuzzyEquals(this.common_fund, other.common_fund, 0.000001d)) {
            return false;
        }
        for (int i = 0; i < this.agents.size(); i++) {
            if (!this.agents.get(i).equals(other.getAgents().get(i))) {
                System.out.println(i + " is different");
                return false;
            }
        }
        return true;
    }

}
