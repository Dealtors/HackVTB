-- Таблицы BPMN и API
CREATE TABLE bpmn_diagrams (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    name VARCHAR(255),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    diagram_data TEXT,
    parsed_elements JSONB,
    upload_date TIMESTAMP DEFAULT NOW(),
    version INTEGER
);

CREATE TABLE openapi_specs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    name VARCHAR(255),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    spec_data JSONB,
    upload_date TIMESTAMP DEFAULT NOW(),
    version VARCHAR(50)
);

CREATE TABLE endpoints (
    id BIGSERIAL PRIMARY KEY,
    openapi_spec_id BIGINT REFERENCES openapi_specs(id) ON DELETE CASCADE,
    path VARCHAR(500),
    method VARCHAR(10),
    operation_id VARCHAR(255),
    summary TEXT,
    description TEXT,
    parameters JSONB,
    request_body JSONB,
    responses JSONB
);

--Таблицы для BPMN элементов
CREATE TABLE bpmn_elements (
    id BIGSERIAL PRIMARY KEY,
    bpmn_diagram_id BIGINT REFERENCES bpmn_diagrams(id) ON DELETE CASCADE,
    element_id VARCHAR(255),
    element_type VARCHAR(50),
    element_name VARCHAR(255),
    business_meaning TEXT,
    properties JSONB
);

--Таблицы для связи BPMN элементов и API
CREATE TABLE process_endpoint_mapping (
    id BIGSERIAL PRIMARY KEY,
    bpmn_element_id BIGINT REFERENCES bpmn_elements(id) ON DELETE CASCADE,
    endpoint_id BIGINT REFERENCES endpoints(id) ON DELETE CASCADE,
    mapping_type VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    created_by BIGINT
);

--Таблицы для тестирования
CREATE TABLE test_scenarios (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    name VARCHAR(255),
    description TEXT,
    scenario_type VARCHAR(50),
    scenario_steps JSONB,
    is_ai_generated BOOLEAN,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE test_data_sets (
    id BIGSERIAL PRIMARY KEY,
    scenario_id BIGINT REFERENCES test_scenarios(id) ON DELETE CASCADE,
    name VARCHAR(255),
    data_type VARCHAR(50),
    test_data JSONB,
    is_ai_generated BOOLEAN,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE test_executions (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id),
    scenario_id BIGINT REFERENCES test_scenarios(id),
    name VARCHAR(255),
    status VARCHAR(20),
    started_by BIGINT,
    started_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP,
    environment VARCHAR(100)
);

--Таблицы для выполнения сценариев
CREATE TABLE execution_steps (
    id BIGSERIAL PRIMARY KEY,
    execution_id BIGINT REFERENCES test_executions(id),
    step_number INTEGER,
    bpmn_element_id BIGINT REFERENCES bpmn_elements(id),
    endpoint_id BIGINT REFERENCES endpoints(id),
    step_name VARCHAR(255),
    status VARCHAR(20),
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE step_execution_details (
    id BIGSERIAL PRIMARY KEY,
    step_id BIGINT REFERENCES execution_steps(id),
    request_url TEXT,
    request_method VARCHAR(10),
    request_headers JSONB,
    request_payload TEXT,
    response_status INTEGER,
    response_headers JSONB,
    response_payload TEXT,
    response_time_ms INTEGER,
    error_message TEXT,
    stack_trace TEXT,
    execution_log TEXT
);

--Остальные таблицы
CREATE TABLE execution_errors (
    id BIGSERIAL PRIMARY KEY,
    execution_id BIGINT REFERENCES test_executions(id) ON DELETE CASCADE,
    step_id BIGINT REFERENCES execution_steps(id),
    error_type VARCHAR(100),
    error_code VARCHAR(50),
    error_message TEXT,
    error_details JSONB,
    occurred_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE ai_generated_content (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    content_type VARCHAR(50),
    prompt_used TEXT,
    generated_content JSONB,
    ai_model VARCHAR(100),
    tokens_used INTEGER,
    generated_at TIMESTAMP DEFAULT NOW()
);
