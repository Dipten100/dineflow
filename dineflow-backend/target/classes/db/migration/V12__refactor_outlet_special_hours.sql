-- Drop foreign key constraint first
ALTER TABLE outlet_special_hours
    DROP FOREIGN KEY fk_special_hours_outlet;

-- Drop the unique constraint
ALTER TABLE outlet_special_hours
    DROP INDEX uk_special_hours_date;

-- Add the new column
ALTER TABLE outlet_special_hours
    ADD COLUMN period_number INT NOT NULL DEFAULT 1;

-- Add the new unique constraint
ALTER TABLE outlet_special_hours
    ADD CONSTRAINT uk_special_hours_period
    UNIQUE (
        outlet_id,
        special_date,
        period_number
    );

-- Re-add the foreign key constraint
ALTER TABLE outlet_special_hours
    ADD CONSTRAINT fk_special_hours_outlet
        FOREIGN KEY (outlet_id)
        REFERENCES outlets(id)
        ON DELETE CASCADE;