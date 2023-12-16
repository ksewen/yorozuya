DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`   bigint (20) NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL,
    PRIMARY KEY
        (`id`),
    UNIQUE KEY `UK8sewwnpamngi6b1dwaa88askk`
        (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;