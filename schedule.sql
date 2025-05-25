CREATE TABLE `Author` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `name` VARCHAR(50) NOT NULL,
                          `email` VARCHAR(100) NOT NULL,
                          `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
                          `updated_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `Schedule` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `author_id` BIGINT NOT NULL,
                            `task` VARCHAR(200) NOT NULL,
                            `password` VARCHAR(30) NOT NULL,
                            `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
                            `updated_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
);
