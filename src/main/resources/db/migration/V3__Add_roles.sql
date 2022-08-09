ALTER TABLE employee ALTER COLUMN first_name DROP NOT NULL;
ALTER TABLE employee ALTER COLUMN last_name DROP NOT NULL;
ALTER TABLE employee ADD firebase_uid VARCHAR(255);
ALTER TABLE employee ADD CONSTRAINT firebase_uid_unique UNIQUE(firebase_uid);
ALTER TABLE employee ADD role VARCHAR(255) NOT NULL;

