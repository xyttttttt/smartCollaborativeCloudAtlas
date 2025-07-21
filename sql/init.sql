CREATE TABLE ai_conversation_log
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id          VARCHAR(255) NOT NULL,
    session_id       VARCHAR(255) NOT NULL,
    date             DATE         NOT NULL,
    model            VARCHAR(255) NOT NULL,
    thought_content  TEXT,
    user_input       TEXT         NOT NULL,
    response_content TEXT         NOT NULL,
    input_tokens     INT          NOT NULL,
    output_tokens    INT          NOT NULL,
    timestamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (user_id),
    INDEX (session_id),
    INDEX (date),
    INDEX (model)
);