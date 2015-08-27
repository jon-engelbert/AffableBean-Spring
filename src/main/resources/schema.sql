--SET MODE MySQL;

-- -----------------------------------------------------
-- Table `customer`
-- -----------------------------------------------------
drop table if exists payment_info ;

create table payment_info (
  id identity,
  name varchar(45) not null ,
  address varchar(100) not null ,
  city_region varchar(45) not null ,
  cc_number varchar(19) not null,
  `member_id` INT UNSIGNED NOT NULL ,
  foreign key (`member_id`) references member(`id`)
  );
--COMMENT = 'maintains customer payment info details';


-- -----------------------------------------------------
-- Table `customer_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS customer_order ;

CREATE  TABLE customer_order (
  id identity,
  `amount` DECIMAL(6,2) NOT NULL ,
  `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `confirmation_number` INT UNSIGNED NOT NULL ,
  `payment_info_id` INT UNSIGNED NOT NULL ,
  foreign key (payment_info_id) references payment_info(id)
);
--COMMENT = 'maintains customer order details';


-- -----------------------------------------------------
-- Table .`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS category ;

CREATE  TABLE category (
  id identity,
  name VARCHAR(45) NOT NULL
  );
--COMMENT = 'contains product categories, eg, dairy, meats, etc';


-- -----------------------------------------------------
-- Table `product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS product ;

CREATE  TABLE IF NOT EXISTS product (
  id identity,
  name VARCHAR(45) NOT NULL ,
  price DECIMAL(5,2) NOT NULL ,
  description VARCHAR(200) NULL ,
--  last_update TIMESTAMP NOT NULL ,
  category_id TINYINT UNSIGNED NOT NULL ,
  foreign key (category_id) references category(id)
  );
--COMMENT = 'contains product details';


-- -----------------------------------------------------
-- Table `ordered_product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS ordered_product ;

CREATE  TABLE ordered_product (
  `customer_order_id` INT UNSIGNED NOT NULL ,
  `product_id` INT UNSIGNED NOT NULL ,
  `quantity` SMALLINT UNSIGNED NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`customer_order_id`, `product_id`) ,
  foreign key (customer_order_id) references customer_order(id),
  foreign key (product_id) references product(id)
  );

---- -----------------------------------------------------
---- Table `role`
---- -----------------------------------------------------

DROP TABLE IF EXISTS role ;

CREATE  TABLE role (
  `id` identity,
  `name` VARCHAR(45) NOT NULL 
  );
-- COMMENT = 'maintains admin console member roles';


-- -----------------------------------------------------
-- Table `member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS member ;

CREATE  TABLE member (
  `id` identity,
  `name` VARCHAR(45),
  `username` VARCHAR(45),
  `password` VARCHAR(100),
  `enabled` boolean,
  email varchar(45) not null ,
  phone varchar(45),
  address varchar(100),
  city_region varchar(45),
  `role_id` INT UNSIGNED NOT NULL ,
  foreign key (`role_id`) references role(`id`),
  UNIQUE (`username`),
  UNIQUE(`email`)
  );
-- COMMENT = 'maintains admin console member details';


