package com.orchestra.api.parser;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.Process;
import com.orchestra.api.model.project.BpmnDiagram;
import com.orchestra.api.model.core.BpmnElement;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class BpmnParser {

    public BpmnDiagram parse(InputStream inputStream) {
        BpmnModelInstance model = Bpmn.readModelFromStream(inputStream);

        String diagramName = extractDiagramName(model);
        List<BpmnElement> steps = extractSteps(model);
        extractTransitions(model, steps);

        return createBpmnDiagram(steps, diagramName);
    }

    private String extractDiagramName(BpmnModelInstance model) {
        Collection<Process> processes = model.getModelElementsByType(Process.class);
        if (processes.isEmpty()) {
            return "Unnamed Process";
        }
        
        Process process = processes.iterator().next();

        // Если name отсутствует → используем id
        if (process.getName() != null && !process.getName().isEmpty()) {
            return process.getName();
        }
        return process.getId();
    }

    public List<BpmnElement> extractSteps(BpmnModelInstance model) {
        List<BpmnElement> steps = new ArrayList<>();
        Collection<FlowNode> nodes = model.getModelElementsByType(FlowNode.class);

        for (FlowNode node : nodes) {
            BpmnElement element = new BpmnElement();
            element.setStepId(node.getId());
            element.setName(node.getName());
            element.setNextSteps(new ArrayList<>()); 

            
            element.setActorFrom(null);
            element.setActorTo(null);
            String action = extractAction(node.getName());
            element.setAction(action);

            steps.add(element);
        }
        return steps;
    }

    private String extractAction(String name) {
        if (name == null) return null;
    
        String regex = "(GET|POST|PUT|DELETE|PATCH)\\s+[^\\s]+";
        var matcher = java.util.regex.Pattern.compile(regex).matcher(name);
    
        if (matcher.find()) {
            return matcher.group(); // например: "POST /auth/bank-token"
        }
        return null; // нет HTTP-метода → это не API-операция
    }
    

    public void extractTransitions(BpmnModelInstance model, List<BpmnElement> steps) {
        Collection<SequenceFlow> flows = model.getModelElementsByType(SequenceFlow.class);

        for (SequenceFlow flow : flows) {
            String sourceId = flow.getSource().getId();
            String targetId = flow.getTarget().getId();

            steps.stream()
                    .filter(e -> e.getStepId().equals(sourceId))
                    .findFirst()
                    .ifPresent(e -> e.getNextSteps().add(targetId));
        }
    }

    public BpmnDiagram createBpmnDiagram(List<BpmnElement> steps, String name) {
        BpmnDiagram diagram = new BpmnDiagram();
        diagram.setSteps(steps);
        diagram.setName(name);
        diagram.setType("BPMN");
        return diagram;
    }
}
