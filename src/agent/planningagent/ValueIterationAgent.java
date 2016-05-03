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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**
     *
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;
		//*** VOTRE CODE

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
		//delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon 
        this.delta = 0.0;
		//*** VOTRE CODE

		// mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
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

        return null;
    }

    @Override
    public double getValeur(Etat _e) {
		//*** VOTRE CODE

        return 0.0;
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
        for (Action a : l) {
            try {
                Map<Etat, Double> d = mdp.getEtatTransitionProba(_e, a);
                System.out.println(d);
            } catch (Exception ex) {
                Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;

    }

    @Override
    public void reset() {
        super.reset();
		//*** VOTRE CODE

        /*-----------------*/
        this.notifyObs();

    }

    @Override
    public void setGamma(double arg0) {
        this.gamma = arg0;
    }

}
