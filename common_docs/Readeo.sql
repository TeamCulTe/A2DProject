#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Country
#------------------------------------------------------------

CREATE TABLE Country(
        id_country Int  Auto_increment  NOT NULL ,
        deleted    Bool NOT NULL DEFAULT FALSE ,
        name       Varchar (50) NOT NULL
	,CONSTRAINT Country_AK UNIQUE (name)
	,CONSTRAINT Country_PK PRIMARY KEY (id_country)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: City
#------------------------------------------------------------

CREATE TABLE City(
        id_city Int  Auto_increment  NOT NULL ,
        name    Varchar (50) NOT NULL ,
        deleted Bool NOT NULL DEFAULT FALSE
	,CONSTRAINT City_PK PRIMARY KEY (id_city)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Author
#------------------------------------------------------------

CREATE TABLE Author(
        id_author Int  Auto_increment  NOT NULL ,
        name      Varchar (50) NOT NULL ,
        deleted   Bool NOT NULL DEFAULT FALSE
	,CONSTRAINT Author_PK PRIMARY KEY (id_author)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Category
#------------------------------------------------------------

CREATE TABLE Category(
        id_category Int  Auto_increment  NOT NULL ,
        deleted     Bool NOT NULL DEFAULT FALSE,
        name        Varchar (50) NOT NULL
	,CONSTRAINT Category_AK UNIQUE (name)
	,CONSTRAINT Category_PK PRIMARY KEY (id_category)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Book
#------------------------------------------------------------

CREATE TABLE Book(
        id_book        Int  Auto_increment  NOT NULL ,
        cover          Varchar (50) NOT NULL ,
        summary        Text NOT NULL ,
        deleted        Bool NOT NULL DEFAULT FALSE,
        title          Varchar (50) NOT NULL ,
        date_published Int NOT NULL ,
        id_category    Int NOT NULL ,
        id_author      Int NOT NULL
	,CONSTRAINT Book_Idx INDEX (title,date_published)
	,CONSTRAINT Book_PK PRIMARY KEY (id_book)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: User
#------------------------------------------------------------

CREATE TABLE User(
        id_user    Int  Auto_increment  NOT NULL ,
        password   Varchar (50) NOT NULL ,
        email      Varchar (50) NOT NULL ,
        deleted    Bool NOT NULL DEFAULT FALSE,
        pseudo     Varchar (50) NOT NULL ,
        id_profile Int NOT NULL ,
        id_country Int NOT NULL ,
        id_city    Int NOT NULL
	,CONSTRAINT User_Idx INDEX (pseudo)
	,CONSTRAINT User_PK PRIMARY KEY (id_user)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Profile
#------------------------------------------------------------

CREATE TABLE Profile(
        id_profile  Int  Auto_increment  NOT NULL ,
        description Varchar (50) NOT NULL ,
        avatar      Varchar (50) NOT NULL ,
        deleted     Bool NOT NULL DEFAULT FALSE,
        id_user     Int NOT NULL
	,CONSTRAINT Profile_PK PRIMARY KEY (id_profile)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Quote
#------------------------------------------------------------

CREATE TABLE Quote(
        id_quote Int  Auto_increment  NOT NULL ,
        quote    Text NOT NULL ,
        deleted  Bool NOT NULL DEFAULT FALSE,
        id_user  Int NOT NULL ,
        id_book  Int NOT NULL
	,CONSTRAINT Quote_PK PRIMARY KEY (id_quote)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Review
#------------------------------------------------------------

CREATE TABLE Review(
        id_book Int NOT NULL ,
        id_user Int NOT NULL ,
        review  Text NOT NULL ,
        shared  Bool NOT NULL ,
        deleted Bool NOT NULL DEFAULT FALSE
	,CONSTRAINT Review_PK PRIMARY KEY (id_book,id_user)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: BookList
#------------------------------------------------------------

CREATE TABLE BookList(
        id_book Int NOT NULL ,
        id_user Int NOT NULL ,
        type    Varchar (50) NOT NULL ,
        deleted Bool NOT NULL DEFAULT FALSE
	,CONSTRAINT BookList_PK PRIMARY KEY (id_book,id_user)
)ENGINE=InnoDB;




ALTER TABLE Book
	ADD CONSTRAINT Book_Category0_FK
	FOREIGN KEY (id_category)
	REFERENCES Category(id_category);

ALTER TABLE Book
	ADD CONSTRAINT Book_Author1_FK
	FOREIGN KEY (id_author)
	REFERENCES Author(id_author);

ALTER TABLE User
	ADD CONSTRAINT User_Profile0_FK
	FOREIGN KEY (id_profile)
	REFERENCES Profile(id_profile);

ALTER TABLE User
	ADD CONSTRAINT User_Country1_FK
	FOREIGN KEY (id_country)
	REFERENCES Country(id_country);

ALTER TABLE User
	ADD CONSTRAINT User_City2_FK
	FOREIGN KEY (id_city)
	REFERENCES City(id_city);

ALTER TABLE User 
	ADD CONSTRAINT User_Profile0_AK 
	UNIQUE (id_profile);

ALTER TABLE Profile
	ADD CONSTRAINT Profile_User0_FK
	FOREIGN KEY (id_user)
	REFERENCES User(id_user);

ALTER TABLE Profile 
	ADD CONSTRAINT Profile_User0_AK 
	UNIQUE (id_user);

ALTER TABLE Quote
	ADD CONSTRAINT Quote_User0_FK
	FOREIGN KEY (id_user)
	REFERENCES User(id_user);

ALTER TABLE Quote
	ADD CONSTRAINT Quote_Book1_FK
	FOREIGN KEY (id_book)
	REFERENCES Book(id_book);

ALTER TABLE Review
	ADD CONSTRAINT Review_Book0_FK
	FOREIGN KEY (id_book)
	REFERENCES Book(id_book);

ALTER TABLE Review
	ADD CONSTRAINT Review_User1_FK
	FOREIGN KEY (id_user)
	REFERENCES User(id_user);

ALTER TABLE BookList
	ADD CONSTRAINT BookList_Book0_FK
	FOREIGN KEY (id_book)
	REFERENCES Book(id_book);

ALTER TABLE BookList
	ADD CONSTRAINT BookList_User1_FK
	FOREIGN KEY (id_user)
	REFERENCES User(id_user);
