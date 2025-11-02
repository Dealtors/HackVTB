-- Raw storage for sequence source
CREATE TABLE IF NOT EXISTS sequence_diagrams (
  id UUID PRIMARY KEY,
  name TEXT,
  format VARCHAR(20), -- PLANTUML | MERMAID | CMMN
  raw_content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);

-- Raw storage for OpenAPI source
CREATE TABLE IF NOT EXISTS openapi_specs (
  id UUID PRIMARY KEY,
  name TEXT,
  file_name TEXT,
  spec_json JSONB NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);
