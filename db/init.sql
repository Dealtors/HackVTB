-- Create tables actually used by JPA entities
-- Note: UUID primary keys, JSONB columns, and FKs matching entity mappings

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Core diagrams
CREATE TABLE IF NOT EXISTS process_diagram (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255),
    type        VARCHAR(50),
    status      VARCHAR(50),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS process_step (
    id          UUID PRIMARY KEY,
    diagram_id  UUID REFERENCES process_diagram(id) ON DELETE CASCADE,
    step_id     VARCHAR(255),
    name        VARCHAR(255),
    actor_from  VARCHAR(255),
    actor_to    VARCHAR(255),
    action      VARCHAR(255),
    next_steps  JSONB
);

CREATE TABLE IF NOT EXISTS process_transition (
    id          UUID PRIMARY KEY,
    diagram_id  UUID REFERENCES process_diagram(id) ON DELETE CASCADE,
    from_step   UUID REFERENCES process_step(id) ON DELETE CASCADE,
    to_step     UUID REFERENCES process_step(id) ON DELETE CASCADE
);

-- API bindings per step
CREATE TABLE IF NOT EXISTS step_api_binding (
    id               UUID PRIMARY KEY,
    step_id          UUID REFERENCES process_step(id) ON DELETE CASCADE,
    http_method      VARCHAR(16),
    api_path         VARCHAR(1024),
    confidence_score DOUBLE PRECISION
);

-- Test generation artifacts
CREATE TABLE IF NOT EXISTS generated_test_case (
    id          UUID PRIMARY KEY,
    diagram_id  UUID REFERENCES process_diagram(id) ON DELETE CASCADE,
    name        VARCHAR(255),
    steps_json  JSONB,
    created_at  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS generated_test_data (
    id                 UUID PRIMARY KEY,
    test_case_id       UUID REFERENCES generated_test_case(id) ON DELETE CASCADE,
    step_id            UUID REFERENCES process_step(id) ON DELETE CASCADE,
    request_payload    JSONB,
    response_expected  JSONB
);

-- Raw sequence diagrams storage
CREATE TABLE IF NOT EXISTS sequence_diagrams (
    id           UUID PRIMARY KEY,
    name         VARCHAR(255),
    format       VARCHAR(50),
    raw_content  TEXT NOT NULL,
    created_at   TIMESTAMP
);

-- OpenAPI specs storage
CREATE TABLE IF NOT EXISTS openapi_specs (
    id         UUID PRIMARY KEY,
    name       VARCHAR(255),
    file_name  VARCHAR(255),
    spec_json  JSONB NOT NULL,
    created_at TIMESTAMP
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_process_step_diagram ON process_step(diagram_id);
CREATE INDEX IF NOT EXISTS idx_process_transition_diagram ON process_transition(diagram_id);
CREATE INDEX IF NOT EXISTS idx_step_api_binding_step ON step_api_binding(step_id);
CREATE INDEX IF NOT EXISTS idx_gen_test_case_diagram ON generated_test_case(diagram_id);
CREATE INDEX IF NOT EXISTS idx_gen_test_data_case ON generated_test_data(test_case_id);


