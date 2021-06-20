package ie.ucd.cs.mas3.normsystem.problem;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class BaseProblem {

    public static void main(String[] args) {
        int num_agents = 20;
        int num_evaders = 10;
        double invest_rate = 0.05;
        double[] collecting_rate = {0.5488135039273248, 0.7151893663724195, 0.6027633760716439, 0.5448831829968969, 0.4236547993389047};
        double[] redistribution_rates = {0.19440830746079796, 0.1317098072068841, 0.2684156368884406, 0.29005380671842657, 0.11541244172545075};
        double catche = 0.3958625190413323;
        double fine_rate = 0.5288949197529045;
        /*{'num_agents': 200, 'num_evaders': 10, 'invest_rate': 0.05, 
        'collecting_rates': [0.6478898663769349, 0.7134226815101801, 0.08904775878693694, 0.04600230062452726, 0.6034071555626516], 
        'redistribution_rates': [0.015188582298102793, 0.2716671931032027, 0.03478122550201137, 0.3916746189756817, 0.2866883801210014], 'catch': 0.4058077132698487, 'fine_rate': 0.36355946153811125}gens 0.24268958843571323*/
        Society sc = new Society(collecting_rate, redistribution_rates, num_agents, num_evaders, fine_rate, invest_rate);

        double[] weaths = {97.94942013856087, 59.90309989217499, 175.865232866729, 68.27244005916933, 38.103959945470095, 41.765680292647176, 38.103866090141, 53.881320134746815, 60.69500372398621, 27.602230331988256, 75.67270575740619, 38.113644432406296, 141.75386133110004, 126.66201458806702, 27.785677425616512, 59.91467906733314, 123.27862010169103, 47.093639555456946, 18.128667139086467, 68.7160208293054};
        sc.setWeath(weaths);
        System.out.println(Functions.equality_single_path(sc));
    }
}
