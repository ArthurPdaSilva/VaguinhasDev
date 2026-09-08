CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    external_id VARCHAR(255) NOT NULL,
    source VARCHAR(50) NOT NULL,
    source_url VARCHAR(2048) NOT NULL,
    company VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    seniority VARCHAR(50) NOT NULL,
    work_model VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE,
    discovered_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_seen_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,
    fingerprint VARCHAR(64) NOT NULL,
    CONSTRAINT uk_jobs_source_external_id UNIQUE (source, external_id)
);

CREATE INDEX idx_jobs_status_published_at ON jobs (status, published_at);
CREATE INDEX idx_jobs_fingerprint ON jobs (fingerprint);
