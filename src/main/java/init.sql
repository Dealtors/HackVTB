CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description TEXT,
                       permissions JSONB,
                       created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW(),
                       last_login TIMESTAMP,
                       is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_roles (
                            user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
                            assigned_at TIMESTAMP DEFAULT NOW(),
                            assigned_by BIGINT,
                            PRIMARY KEY (user_id, role_id)
);

CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          owner_id BIGINT REFERENCES users(id),
                          created_at TIMESTAMP DEFAULT NOW(),
                          updated_at TIMESTAMP DEFAULT NOW(),
                          status VARCHAR(20)
);

CREATE TABLE user_sessions (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                               session_token VARCHAR(500) NOT NULL,
                               ip_address VARCHAR(45),
                               user_agent TEXT,
                               expires_at TIMESTAMP,
                               created_at TIMESTAMP DEFAULT NOW(),
                               last_activity TIMESTAMP
);


--DROP TABLE IF EXISTS user_roles, user_sessions, users, roles, projects CASCADE;
