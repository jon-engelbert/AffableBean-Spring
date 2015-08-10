--SET MODE MySQL;

-- -----------------------------------------------------
-- Table `customer`
-- -----------------------------------------------------
drop table if exists customer ;

create table customer (
  id identity,
  name varchar(45) not null ,
  email varchar(45) not null ,
  phone varchar(45) not null ,
  address varchar(45) not null ,
  city_region varchar(2) not null ,
  cc_number varchar(19) not null 
  );
--COMMENT = 'maintains customer details';


-- -----------------------------------------------------
-- Table `customer_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS customer_order ;

CREATE  TABLE customer_order (
  id identity,
  `amount` DECIMAL(6,2) NOT NULL ,
  `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `confirmation_number` INT UNSIGNED NOT NULL ,
  `customer_id` INT UNSIGNED NOT NULL ,
  foreign key (customer_id) references customer(id)
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
--DROP TABLE IF EXISTS role ;
--
--CREATE  TABLE role (
--  id identity,
--  name VARCHAR(45) NOT NULL 
--  );
---- COMMENT = 'maintains admin console member roles';
--
--
---- -----------------------------------------------------
---- Table `member`
---- -----------------------------------------------------
--DROP TABLE IF EXISTS member ;
--
--CREATE  TABLE member (
--  id identity,
--  name VARCHAR(45) NOT NULL ,
--  username VARCHAR(45) NOT NULL ,
--  password VARCHAR(100) NOT NULL , -- this should be the hashed(password+salt)
--  --salt VARCHAR(100) NOT NULL ,
--  `status` INT UNSIGNED NOT NULL ,
--  `role_id` INT UNSIGNED NOT NULL ,
--  foreign key (role_id) references role(id),
--  UNIQUE (`username`) 
--  );
---- COMMENT = 'maintains admin console member details';

DROP TABLE IF EXISTS users;
create table users (
    username varchar(50) not null,
    password varchar(100) not null,
    enabled boolean not null,
    PRIMARY KEY (`username`)
);

DROP TABLE IF EXISTS authorities;
create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username),
    unique index authorities_idx_1 (username, authority)
);

