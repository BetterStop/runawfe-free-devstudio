package ru.runa.gpd.lang.model.bpmn;

import java.util.List;
import java.util.stream.Collectors;
import ru.runa.gpd.lang.model.Node;
import ru.runa.gpd.lang.model.Transition;

public class DataStore extends Node implements ConnectableViaDottedTransition {
    @Override
    protected boolean allowLeavingTransition(List<Transition> transitions) {
        return false;
    }

    @Override
    protected boolean allowArrivingTransition(Node source, List<Transition> transitions) {
        return false;
    }

    @Override
    public void addLeavingDottedTransition(DottedTransition transition) {
        addChild(transition);
    }

    @Override
    public List<DottedTransition> getLeavingDottedTransitions() {
        return getChildren(DottedTransition.class);
    }

    @Override
    public List<DottedTransition> getArrivingDottedTransitions() {
        return getProcessDefinition().getNodesRecursive().stream().filter(node -> node instanceof ConnectableViaDottedTransition)
                .flatMap(node -> ((ConnectableViaDottedTransition) node).getLeavingDottedTransitions().stream())
                .filter(dottedTransition -> dottedTransition.getTarget().equals(this)).collect(Collectors.toList());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean canAddArrivingDottedTransition(ConnectableViaDottedTransition source) {
        return !DataStore.class.equals(source.getClass())
                && getArrivingDottedTransitions().stream().noneMatch(transition -> transition.getSource().equals(source));
    }

    @Override
    public boolean canAddLeavingDottedTransition() {
        return true;
    }

    @Override
    public void removeLeavingDottedTransition(DottedTransition transition) {
        removeChild(transition);
    }

    @Override
    public void addArrivingDottedTransition(DottedTransition transition) {
        transition.setTarget(this);
    }

}
