
import com.orchestra.api.model.core.BpmnElement;
import java.util.List;
import java.util.UUID;

public class BpmnDiagram {

    private UUID id;
    private String name;
    private String type; // BPMN or SEQUENCE
    private List<BpmnElement> steps;

    public BpmnDiagram() {}

    public BpmnDiagram(UUID id, String name, String type, List<BpmnElement> steps) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.steps = steps;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BpmnElement> getSteps() {
        return steps;
    }

    public void setSteps(List<BpmnElement> steps) {
        this.steps = steps;
    }
}