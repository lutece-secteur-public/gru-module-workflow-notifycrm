--
-- Table structure for table tf_directory_cf
--
DROP TABLE IF EXISTS task_notify_crm_cf;
CREATE TABLE task_notify_crm_cf(
  id_task INT DEFAULT NULL,
  id_directory INT DEFAULT NULL,
  position_directory_entry_id_demand INT DEFAULT NULL,
  position_directory_entry_user_guid INT DEFAULT NULL,
  send_notification SMALLINT DEFAULT 1 NOT NULL,
  sender_name VARCHAR(255) DEFAULT NULL, 
  subject VARCHAR(255) DEFAULT NULL, 
  message long VARCHAR DEFAULT NULL,
  status_text VARCHAR(255) DEFAULT '' NOT NULL,
  position_directory_entry_crm_web_app_code VARCHAR(255) DEFAULT '' NOT NULL,
  PRIMARY KEY (id_task)
);
