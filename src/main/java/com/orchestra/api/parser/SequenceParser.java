package com.orchestra.api.parser;

import com.orchestra.api.dto.response.SequenceDiagramResponse;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SequenceParser {

    private static final Pattern PARTICIPANT = Pattern.compile(
            "(?i)^(participant|actor|entity|boundary|control)\\s+\"?([\\w\\- ]+)\"?.*$");
    private static final Pattern MESSAGE = Pattern.compile(
            "^(?<from>[\\w\\-]+)\\s*(->|->>|-->>|->x|--x)\\s*(?<to>[\\w\\-]+)\\s*:\\s*(?<text>.+)$");

    public ParsedSequence parse(String content) {
        List<String> lines = Arrays.stream(content.split("\\R"))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();

        Set<String> actors = new LinkedHashSet<>();
        List<Msg> msgs = new ArrayList<>();

        for (String line : lines) {
            Matcher p = PARTICIPANT.matcher(line);
            if (p.matches()) { actors.add(p.group(2).trim()); continue; }
            Matcher m = MESSAGE.matcher(line);
            if (m.matches()) {
                actors.add(m.group("from"));
                actors.add(m.group("to"));
                msgs.add(new Msg(m.group("from"), m.group("to"), m.group("text")));
            }
        }

        List<SequenceDiagramResponse.Step> steps = new ArrayList<>();
        Map<Integer, String> stepIdByIdx = new HashMap<>();
        for (int i = 0; i < msgs.size(); i++) {
            String stepId = "Step_" + (i + 1);
            stepIdByIdx.put(i, stepId);
            SequenceDiagramResponse.Step s = new SequenceDiagramResponse.Step();
            s.stepId = stepId;
            s.name = msgs.get(i).text;
            s.from = msgs.get(i).from;
            s.to = msgs.get(i).to;
            s.action = extractAction(msgs.get(i).text);
            s.next = (i + 1 < msgs.size()) ? List.of("Step_" + (i + 2)) : List.of();
            steps.add(s);
        }
        return new ParsedSequence(new ArrayList<>(actors), steps);
    }

    private String extractAction(String text) {
        if (text == null) return null;
        Matcher m = Pattern.compile("(GET|POST|PUT|DELETE|PATCH)\\s+\\S+").matcher(text);
        return m.find() ? m.group() : null;
    }

    public record ParsedSequence(List<String> actors, List<SequenceDiagramResponse.Step> steps) {}
    private record Msg(String from, String to, String text) {}
}
