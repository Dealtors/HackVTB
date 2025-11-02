-- ============================================
-- 1. Table: process_diagram
-- ============================================
CREATE TABLE process_diagram (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    type VARCHAR(20) NOT NULL, -- BPMN or SEQUENCE
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ============================================
-- 2. Table: process_step
-- ============================================
CREATE TABLE process_step (
    id UUID PRIMARY KEY,
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    step_id TEXT NOT NULL,      -- ID from XML (e.g. Task_1)
    name TEXT,
    actor_from TEXT,
    actor_to TEXT,
    action TEXT,                -- e.g. "POST /orders"
    order_index INT,
    UNIQUE (diagram_id, step_id)
);

CREATE INDEX idx_step_diagram ON process_step(diagram_id);

-- ============================================
-- 3. Table: process_transition
-- ============================================
CREATE TABLE process_transition (
    id UUID PRIMARY KEY,
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    from_step UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    to_step UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE
);

CREATE INDEX idx_transition_diagram ON process_transition(diagram_id);

-- ============================================
-- 4. Table: step_api_binding
-- ============================================
CREATE TABLE step_api_binding (
    id UUID PRIMARY KEY,
    step_id UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    http_method VARCHAR(10),
    api_path TEXT,
    confidence_score FLOAT
);

CREATE INDEX idx_api_binding_step ON step_api_binding(step_id);

-- ============================================
-- 5. Table: generated_test_case
-- ============================================
CREATE TABLE generated_test_case (
    id UUID PRIMARY KEY,
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    name TEXT,
    steps_json JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ============================================
-- 6. Table: generated_test_data
-- ============================================
CREATE TABLE generated_test_data (
    id UUID PRIMARY KEY,
    test_case_id UUID NOT NULL REFERENCES generated_test_case(id) ON DELETE CASCADE,
    step_id UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    request_payload JSONB,
    response_expected JSONB
);

CREATE INDEX idx_testdata_case ON generated_test_data(test_case_id);
