CREATE TABLE outbox_events (
                               id BIGSERIAL PRIMARY KEY,
                               event_type VARCHAR(255) NOT NULL,
                               aggregate_id BIGINT NOT NULL,
                               payload JSONB NOT NULL,
                               sent BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               sent_at TIMESTAMP
);

CREATE INDEX idx_outbox_unsent ON outbox_events(sent) WHERE sent = false;