import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.HashMap;
import java.util.Map;

public class BasicTraversalPolicy extends FocusTraversalPolicy {

    private final Map<Component, Integer> componentToIndexMap;
    private final Component[] components;
    private final int numComponents;

    public BasicTraversalPolicy(Component ... components) {
        this.components = components;
        numComponents = components.length;

        componentToIndexMap = new HashMap<>();
        
        for (int i = 0; i < numComponents; i++) {
            componentToIndexMap.put(components[i], i);
        }
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        Integer indexWrapped = componentToIndexMap.get(aComponent);
        if (indexWrapped == null) {
            return null;
        }
        int index = indexWrapped;
        int tries = 0;
        do {
            index = (index + 1) % numComponents;
            tries++;
        } while (!components[index].isEnabled() && tries < numComponents);
        return components[index];
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        int index = componentToIndexMap.get(aComponent);
        int tries = 0;
        do {
            index = (index + numComponents - 1) % numComponents;
            tries++;
        } while (!components[index].isEnabled() && tries < numComponents);
        return components[index];
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return components[0];
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return components[numComponents - 1];
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return components[0];
    }
    
}
