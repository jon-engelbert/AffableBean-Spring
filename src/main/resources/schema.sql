SET MODE MySQL;
DROP SCHEMA IF EXISTS `affablebean` ;
CREATE SCHEMA IF NOT EXISTS `affablebean`  ;
USE `affablebean` ;

-- -----------------------------------------------------
-- Table `affablebean`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affablebean`.`customer` ;

CREATE  TABLE IF NOT EXISTS `affablebean`.`customer` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(45) NOT NULL ,
  `phone` VARCHAR(45) NOT NULL ,
  `address` VARCHAR(45) NOT NULL ,
  `city_region` VARCHAR(2) NOT NULL ,
  `cc_number` VARCHAR(19) NOT NULL ,
  PRIMARY KEY (`id`) )
COMMENT = 'maintains customer details';


-- -----------------------------------------------------
-- Table `affablebean`.`customer_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affablebean`.`customer_order` ;

CREATE  TABLE IF NOT EXISTS `affablebean`.`customer_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `amount` DECIMAL(6,2) NOT NULL ,
  `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `confirmation_number` INT UNSIGNED NOT NULL ,
  `customer_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_customer_order_customer` (`customer_id` ASC) ,
  CONSTRAINT `fk_customer_order_customer`
    FOREIGN KEY (`customer_id` )
    REFERENCES `affablebean`.`customer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'maintains customer order details';


-- -----------------------------------------------------
-- Table `affablebean`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affablebean`.`category` ;

CREATE  TABLE IF NOT EXISTS `affablebean`.`category` (
  `id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) )
COMMENT = 'contains product categories, eg, dairy, meats, etc';


-- -----------------------------------------------------
-- Table `affablebean`.`product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affablebean`.`product` ;

CREATE  TABLE IF NOT EXISTS `affablebean`.`product` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `price` DECIMAL(5,2) NOT NULL ,
  `description` TINYTEXT NULL ,
  `last_update` TIMESTAMP NOT NULL ,
  `category_id` TINYINT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_product_category` (`category_id` ASC) ,
  CONSTRAINT `fk_product_category`
    FOREIGN KEY (`category_id` )
    REFERENCES `affablebean`.`category` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'contains product details';


-- -----------------------------------------------------
-- Table `affablebean`.`ordered_product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affablebean`.`ordered_product` ;

CREATE  TABLE IF NOT EXISTS `affablebean`.`ordered_product` (
  `customer_order_id` INT UNSIGNED NOT NULL ,
  `product_id` INT UNSIGNED NOT NULL ,
  `quantity` SMALLINT UNSIGNED NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`customer_order_id`, `product_id`) ,
  INDEX `fk_ordered_product_customer_order` (`customer_order_id` ASC) ,
  INDEX `fk_ordered_product_product` (`product_id` ASC) ,
  CONSTRAINT `fk_ordered_product_customer_order`
    FOREIGN KEY (`customer_order_id` )
    REFERENCES `affablebean`.`customer_order` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ordered_product_product`
    FOREIGN KEY (`product_id` )
    REFERENCES `affablebean`.`product` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
