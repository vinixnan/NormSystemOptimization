package ie.ucd.cs.mas3.normsystem.problem;

import java.security.SecureRandom;
import lombok.Data;
import org.json.JSONObject;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
@Data
public class Individual {

    /**
     *
     */
    protected double wealth;

    /**
     *
     */
    protected int segment;

    /**
     *
     */
    protected int position;

    /**
     *
     */
    protected boolean is_evader;

    /**
     *
     */
    protected double old_wealth;

    /**
     *
     */
    protected double collected_amount;

    /**
     *
     */
    protected double redistributed_amount;

    /**
     *
     */
    protected double payment;

    /**
     *
     */
    protected double payback;

    /**
     *
     */
    protected double catche;

    /**
     * Constructor
     */
    public Individual() {
        SecureRandom rdn = new SecureRandom();
        this.wealth = rdn.nextInt(100);
        this.segment = 0;
        this.position = 0;
        this.is_evader = false;
        //added by Maha
        this.old_wealth = -1;
        this.collected_amount = 0;
        //there is something called payment
        this.redistributed_amount = 0;
        this.catche = Double.MAX_VALUE;
        //there is something called payback
    }

    /**
     * Individual contributes to common fund according to his/her tax rate.
     *
     * @param common_fund
     * @param collecting_rates
     * @param fine_rate
     * @return
     */
    public double step(double common_fund, double[] collecting_rates, double fine_rate) {
        SecureRandom rdn = new SecureRandom();
        double payment = 0;
        this.old_wealth = this.wealth;//added by Maha
        //law-abiding agents
        if (!this.is_evader) {
            payment = this.wealth * collecting_rates[this.segment];
            this.wealth -= payment;
            common_fund += payment;
        } else {
            if (rdn.nextDouble() < this.catche) {
                payment = this.wealth * collecting_rates[this.segment] * (1 + fine_rate);
                if (payment >= this.wealth) {
                    payment = this.wealth;
                    common_fund += payment;
                    this.wealth = 0;
                } else {
                    this.wealth -= payment;
                    common_fund += payment;
                }
            }
        }
        this.payment = payment;
        return common_fund;
    }

    /**
     * Create object from JSON String
     *
     * @param json
     */
    public void createFromJSON(String json) {
        JSONObject obj = new JSONObject(json);
        this.createFromJSON(obj);
    }

    /**
     * Create object from json object
     *
     * @param obj
     */
    public void createFromJSON(JSONObject obj) {
        this.wealth = obj.getDouble("wealth");
        this.segment = obj.getInt("segment");
        this.position = obj.getInt("position");
        this.is_evader = obj.getBoolean("is_evader");
        //added by Maha
        this.old_wealth = obj.getDouble("old_wealth");
        if (obj.has("collected_amount")) {
            this.collected_amount = obj.getDouble("collected_amount");
        }
        if (obj.has("redistributed_amount")) {
            this.collected_amount = obj.getDouble("redistributed_amount");
        }
        if (obj.has("payment")) {
            this.payment = obj.getDouble("payment");
        }
        if (obj.has("payback")) {
            this.payback = obj.getDouble("payback");
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.wealth) ^ (Double.doubleToLongBits(this.wealth) >>> 32));
        hash = 59 * hash + this.segment;
        hash = 59 * hash + this.position;
        hash = 59 * hash + (this.is_evader ? 1 : 0);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.old_wealth) ^ (Double.doubleToLongBits(this.old_wealth) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.collected_amount) ^ (Double.doubleToLongBits(this.collected_amount) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.redistributed_amount) ^ (Double.doubleToLongBits(this.redistributed_amount) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.payment) ^ (Double.doubleToLongBits(this.payment) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.payback) ^ (Double.doubleToLongBits(this.payback) >>> 32));
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
        final Individual other = (Individual) obj;
        if (Double.doubleToLongBits(this.wealth) != Double.doubleToLongBits(other.wealth)) {
            return false;
        }
        if (this.segment != other.segment) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if (this.is_evader != other.is_evader) {
            return false;
        }
        if (Double.doubleToLongBits(this.old_wealth) != Double.doubleToLongBits(other.old_wealth)) {
            return false;
        }
        if (Double.doubleToLongBits(this.collected_amount) != Double.doubleToLongBits(other.collected_amount)) {
            return false;
        }
        if (Double.doubleToLongBits(this.redistributed_amount) != Double.doubleToLongBits(other.redistributed_amount)) {
            return false;
        }
        if (Double.doubleToLongBits(this.payment) != Double.doubleToLongBits(other.payment)) {
            return false;
        }
        if (Double.doubleToLongBits(this.payback) != Double.doubleToLongBits(other.payback)) {
            return false;
        }
        return true;
    }

}
