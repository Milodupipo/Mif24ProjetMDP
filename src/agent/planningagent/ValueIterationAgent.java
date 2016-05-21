package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    protected double gamma;
    Map<Etat, Double> map = new HashMap<Etat, Double>();
    private double valMax = 1;

    /**
     *
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;
        this.delta = 1;
        //*** VOTRE CODE
        List<Etat> le = this.mdp.getEtatsAccessibles();
        for (Etat e : le) {
            map.put(e, 0.0);
        }
    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);
    }

    /**
     *
     * Mise a jour de V: effectue UNE iteration de value iteration
     */
    @Override
    public void updateV() {
        Vector<Double> vDiff = new Vector<>();
        //Calcul des valeurs pour chaque case et des différences (pour delta)
        List<Etat> le = this.mdp.getEtatsAccessibles();
        for (Etat e : le) {
            Vector<Double> valeurs = calculerValeurs(e);
            if (!valeurs.isEmpty()) {
                double max = 0;
                for (int i = 0; i < valeurs.size(); i++) {
                    if (valeurs.get(i) > max) {
                        max = valeurs.get(i);
                    }
                }
                double ancienneVal = map.get(e);
                map.replace(e, max);
                vDiff.add(Math.abs(map.get(e) - ancienneVal));
            }
        }
        //Recherche du maximum et minimum
        double max = 0;
        double min = map.get(le.get(0));
        for (Entry e2 : map.entrySet()) {
            if ((Double) e2.getValue() < min) {
                min = (Double) e2.getValue();
            } else if ((Double) e2.getValue() > max) {
                max = (Double) e2.getValue();
            }
        }
        //Récupération du maximum des différences entre l'ancien et le nouveau état
        double maxDiff = 0;
        for(Double d : vDiff){
            if(d > maxDiff){
                maxDiff = d;
            }
        }
        valMax = vmax;
        vmax = max;
        vmin = min;
        this.delta = maxDiff;
        // ...
        //******************* a laisser a la fin de la methode
        this.notifyObs();
    }

    /**
     * renvoi l'action executee par l'agent dans l'etat e Si aucune actions
     * possibles, renvoi NONE ou null.
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> la = getPolitique(e);
        //Choix d'une action au hasard si il y a deux actions ou plus possible
        //Sinon on retourne l'action
        if (la.size() > 1) {
            int num = ThreadLocalRandom.current().nextInt(0, la.size() + 1);
            return la.get(num);
        } else if (la.size() == 1) {
            return la.get(0);
        } else {
            return null;
        }
    }

    public Vector<Double> calculerValeurs(List<Action> la, Etat e) {
        Map<Etat, Double> ma = new HashMap<Etat, Double>();
        Vector<Double> resultats = new Vector<Double>();
        try {
            for (Action action : la) {
                double res = 0;
                //renvoie une map de de proba de transition, pas un float
                ma = this.mdp.getEtatTransitionProba(e, action);
                for (Entry e2 : ma.entrySet()) {
                    double recompense = this.mdp.getRecompense(e, action, (Etat) e2.getKey());
                    Etat etmp = (Etat) e2.getKey();
                    //***************************************************************************************
                    res += (double) e2.getValue() * (recompense + this.gamma * map.get(e2.getKey()));
                }
                resultats.add(res);
            }
        } catch (Exception ex) {
            Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultats;
    }

    public Vector<Double> calculerValeurs(Etat e) {
        List<Action> la = getPolitique(e);
        return calculerValeurs(la, e);
    }

    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE
        if (map.get(_e) == null) {
            System.err.println(map.get(_e));
        }
        return map.get(_e);
    }

    /**
     * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si
     * aucune action n'est possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        List<Action> l = new ArrayList<Action>();
        //*** VOTRE CODE
        l = this.mdp.getActionsPossibles(_e);

        //Calcul des valeurs pour chaque case
        Vector<Double> vd = calculerValeurs(l, _e);

        int indiceMin = 0;

        //Supprime de la liste d'actions les actions qui comportent les plus petites valeurs
        //Garde les égalités si celles-ci correspondent à la plus grande valeur
        if (!vd.isEmpty()) {
            double min = vd.get(0);
            for (int i = 1; i < vd.size(); i++) {
                if (vd.get(i) > min) {
                    l.remove(indiceMin);
                    vd.remove(indiceMin);
                    min = vd.get(0);
                    indiceMin = 0;
                    i = 0;
                } else if (vd.get(i) < min) {
                    min = vd.get(i);
                    indiceMin = i;
                    i = -1;
                }
            }
        }
        return l;
    }

    @Override
    public void reset() {
        super.reset();
        //*** VOTRE CODE
        for (Entry e : map.entrySet()) {
            map.put((Etat) e.getKey(), 0.0);
        }
        /*-----------------*/
        this.notifyObs();

    }

    @Override
    public void setGamma(double arg0
    ) {
        this.gamma = arg0;
    }
}
