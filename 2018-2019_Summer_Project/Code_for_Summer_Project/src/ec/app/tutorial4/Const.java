/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package ec.app.tutorial4;

import ec.*;
import ec.gp.*;
import ec.util.*;

public class Const extends GPNode {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private double label;

	public String toString() {
		return String.format("%2f", label);
	}

	/*
	 * public void checkConstraints(final EvolutionState state, final int tree,
	 * final GPIndividual typicalIndividual, final Parameter individualBase) {
	 * super.checkConstraints(state,tree,typicalIndividual,individualBase); if
	 * (children.length!=0)
	 * state.output.error("Incorrect number of children for node " +
	 * toStringForError() + " at " + individualBase); }
	 */
	public int expectedChildren() {
		return 0;
	}

	public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual,
			final Parameter individualBase) {
		super.checkConstraints(state, tree, typicalIndividual, individualBase);

		if (children.length != 0)
			state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		DoubleData rd = ((DoubleData) (input));
		rd.x = state.random[thread].nextDouble();//((MultiValuedRegression) problem).constVal;

		// System.out.println(rd.x);
		label = rd.x;
	}
}
