-- This script populates the database with initial data after the tables are created.
-- Default password for all users is 'password'.
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@wms.com', '$2a$10$E.q235sA6C/y8lY9A2fLd.3DEmWkP7/Ld9dZ9iP95SaaN2Y/iST0C', 'ADMIN'),
('worker', 'worker@wms.com', '$2a$10$E.q235sA6C/y8lY9A2fLd.3DEmWkP7/Ld9dZ9iP95SaaN2Y/iST0C', 'WORKER');

-- Sample inventory items.
INSERT INTO items (name, sku, quantity, location, category, threshold) VALUES
('Industrial Wrench', 'IW-001', 50, 'Aisle 1, Shelf A', 'Tools', 10),
('Safety Goggles', 'SG-205', 120, 'Aisle 2, Bin B', 'Safety Gear', 25),
('Heavy Duty Gloves', 'HDG-30', 8, 'Aisle 2, Bin C', 'Safety Gear', 10),
('Copper Wiring (25ft)', 'CW-45', 75, 'Aisle 4, Rack A', 'Materials', 20),
('LED Headlamp', 'LH-110', 5, 'Aisle 2, Bin D', 'Lighting', 5);
