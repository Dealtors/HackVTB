-- ============================================
-- Database Schema for Orchestra API
-- Based on Entity classes
-- ============================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- 1. Table: process_diagram
-- ============================================
CREATE TABLE IF NOT EXISTS process_diagram (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    type VARCHAR(20) NOT NULL, -- BPMN or SEQUENCE
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING / PARSED / READY / FAILED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. Table: process_step
-- ============================================
CREATE TABLE IF NOT EXISTS process_step (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    step_id TEXT NOT NULL,      -- ID from BPMN XML (e.g. Task_1)
    name TEXT,
    actor_from TEXT,
    actor_to TEXT,
    action TEXT,                -- e.g. "POST /orders"
    next_steps JSONB,           -- Serialized list of next step IDs
    CONSTRAINT unique_step_diagram UNIQUE (diagram_id, step_id)
);

CREATE INDEX IF NOT EXISTS idx_step_diagram ON process_step(diagram_id);
CREATE INDEX IF NOT EXISTS idx_step_step_id ON process_step(step_id);

-- ============================================
-- 3. Table: process_transition
-- ============================================
CREATE TABLE IF NOT EXISTS process_transition (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    from_step UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    to_step UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_transition_diagram ON process_transition(diagram_id);
CREATE INDEX IF NOT EXISTS idx_transition_from ON process_transition(from_step);
CREATE INDEX IF NOT EXISTS idx_transition_to ON process_transition(to_step);

-- ============================================
-- 4. Table: step_api_binding
-- ============================================
CREATE TABLE IF NOT EXISTS step_api_binding (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    step_id UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    http_method VARCHAR(10),
    api_path TEXT,
    confidence_score DOUBLE PRECISION
);

CREATE INDEX IF NOT EXISTS idx_api_binding_step ON step_api_binding(step_id);

-- ============================================
-- 5. Table: generated_test_case
-- ============================================
CREATE TABLE IF NOT EXISTS generated_test_case (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    diagram_id UUID NOT NULL REFERENCES process_diagram(id) ON DELETE CASCADE,
    name TEXT,
    steps_json JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_test_case_diagram ON generated_test_case(diagram_id);

-- ============================================
-- 6. Table: generated_test_data
-- ============================================
CREATE TABLE IF NOT EXISTS generated_test_data (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    test_case_id UUID NOT NULL REFERENCES generated_test_case(id) ON DELETE CASCADE,
    step_id UUID NOT NULL REFERENCES process_step(id) ON DELETE CASCADE,
    request_payload JSONB,
    response_expected JSONB
);

CREATE INDEX IF NOT EXISTS idx_testdata_case ON generated_test_data(test_case_id);
CREATE INDEX IF NOT EXISTS idx_testdata_step ON generated_test_data(step_id);

-- ============================================
-- Comments for documentation
-- ============================================
COMMENT ON TABLE process_diagram IS 'BPMN diagrams uploaded by users';
COMMENT ON TABLE process_step IS 'Individual steps/tasks from BPMN diagrams';
COMMENT ON TABLE process_transition IS 'Transitions between steps in BPMN diagrams';
COMMENT ON TABLE step_api_binding IS 'API endpoint bindings for process steps';
COMMENT ON TABLE generated_test_case IS 'Test cases generated from BPMN diagrams';
COMMENT ON TABLE generated_test_data IS 'Test data for individual steps in test cases';

