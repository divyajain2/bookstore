-- Sample data for Bookstore
-- This runs on application startup when spring.sql.init.mode=always

INSERT INTO books (isbn, title, author, description, price, stock_quantity, category, created_at, updated_at, version)
VALUES
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Best practices for Java platform development', 45.99, 100, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-1449373320', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 'The big ideas behind reliable, scalable, and maintainable systems', 54.99, 50, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-0596007126', 'Head First Design Patterns', 'Eric Freeman', 'A brain-friendly guide to design patterns', 39.99, 75, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-0132350884', 'Clean Code', 'Robert C. Martin', 'A handbook of agile software craftsmanship', 42.99, 80, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-0201633610', 'Design Patterns', 'Erich Gamma', 'Elements of reusable object-oriented software', 49.99, 40, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-1491950357', 'Building Microservices', 'Sam Newman', 'Designing fine-grained systems', 47.99, 60, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-0134494166', 'Core Java Volume I', 'Cay S. Horstmann', 'Fundamentals of Java programming', 59.99, 30, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-1617294945', 'Spring in Action', 'Craig Walls', 'Covers Spring 5 and Spring Boot 2', 44.99, 55, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-1484237236', 'Spring Boot 2 Recipes', 'Marten Deinum', 'A problem-solution approach', 39.99, 45, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('978-0135974445', 'Modern Java in Action', 'Raoul-Gabriel Urma', 'Lambdas, streams, functional and reactive programming', 52.99, 35, 'TECHNOLOGY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);