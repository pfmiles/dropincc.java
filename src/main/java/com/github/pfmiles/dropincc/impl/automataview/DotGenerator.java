package com.github.pfmiles.dropincc.impl.automataview;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.pfmiles.dropincc.impl.util.Pair;
import com.github.pfmiles.dropincc.impl.util.Util;

/**
 * Utility class to convert a automata network to a dot file, the dot file could
 * further be processed by graphviz to generate images.
 * 
 * @author pf-miles
 * 
 */
public class DotGenerator {

    private Collection<GeneratingState> states = null;
    // [name, length, width, finals, transitions]
    private static MessageFormat graphTemp = new MessageFormat("digraph {0} '{'\n" + "rankdir=LR;\n" + "size=\"{1},{2}\"\n" + "node [shape = doublecircle]; {3};\n"
            + "node [shape = circle];\n" + "{4}\n" + "'}'\n");

    // [start, end, edge]
    private static MessageFormat transitionTemp = new MessageFormat("{0} -> {1} [ label = \"{2}\" ];");

    /**
     * Construct a dot generator, passed in all states contained in the graph.
     * 
     * @param states
     */
    public DotGenerator(Collection<GeneratingState> states) {
        this.states = states;
    }

    /**
     * Produce dot string according to the states.
     * 
     * @return
     */
    public String toDotString(String name, int length, int width) {
        List<String> finals = new ArrayList<String>();
        List<String[]> transitions = new ArrayList<String[]>();
        for (GeneratingState state : states) {
            if (state.isFinal())
                finals.add(state.getId());
            for (Pair<String, GeneratingState> trans : state.getTransitions()) {
                transitions.add(new String[] { state.getId(), trans.getRight().getId(), trans.getLeft() });
            }
        }
        String finalsStr = Util.join(" ", finals);
        String transitionsStr = Util.join("\n", renderTransitionStrs(transitions));
        return graphTemp.format(new String[] { name, String.valueOf(length), String.valueOf(width), finalsStr, transitionsStr });
    }

    private List<String> renderTransitionStrs(List<String[]> transitions) {
        List<String> ret = new ArrayList<String>();
        for (String[] trans : transitions) {
            trans[2] = trans[2].replaceAll("\\\"", "\\");
            ret.add(transitionTemp.format(trans));
        }
        return ret;
    }
}
