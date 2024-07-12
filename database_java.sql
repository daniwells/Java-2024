use javaproject;

create table if not exists user(
	id_user int not null auto_increment,
    username_user varchar(45),
    email_user varchar(100) unique,
    password_user varchar(100),
	PRIMARY KEY (id_user)
);

create table if not exists category(
	id_category int not null auto_increment,
    name_category varchar(45),
    PRIMARY KEY (id_category)
);

create table if not exists product (
	id_product int not null auto_increment,
	name_product varchar(45),
    value_product float,
    mark_product varchar(45),
    description_product text,
    id_category int,
    PRIMARY KEY (id_product),
    foreign key (id_category) references category(id_category)
);

insert into category (name_category) values 
("Thecnology"),
("Leisure"),
("self-propelled");

use javaproject;
select * from product;
select * from custumer;
select * from product_has_sales;
select * from sales;

delete from sales;
delete from product_has_sales;
delete from custumer where idcustumer = 9;
delete from product where id_category=1;

ALTER TABLE sales CHANGE COLUMN id_sales id_sales INT NOT NULL AUTO_INCREMENT;

select * from cashier;

SET FOREIGN_KEY_CHECKS = 0;


