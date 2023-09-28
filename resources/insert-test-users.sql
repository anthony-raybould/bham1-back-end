INSERT INTO `User` (email, password, roleID)
VALUES
    -- admin@test.com:test, employee1@test.com:test, employee2@test.com:test
    ('admin@test.com', '$2a$10$2ZIWyQ.XypgCWtAwZuvQ8eFrB22axyQ5AViXFohbQkx4OSNwAo1Pe', 1),
    ('employee1@test.com', '$2a$10$kYDeuiP/Dj.FQoLC.frLD.01cDP97oXrddu9XsCkV1raROA7J4FjO', 2),
    ('employee2@test.com', '$2a$10$NcnW9mZZFPjvHeyhFvRRX.qphy6e5GLhiifSUFJcWFcxQJl9/bfNK', 2)
