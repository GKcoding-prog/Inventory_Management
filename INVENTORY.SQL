DROP DATABASE IF EXISTS inventory_system;

CREATE DATABASE IF NOT EXISTS inventory_system;
USE inventory_system;
# -----------------------------------------------------------------------------
#       TABLE : REPORT
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS REPORT
 (
   REPORT_ID BIGINT(4) NOT NULL  ,
   USER_ID BIGINT(4) NOT NULL  ,
   GENERATED_AT DATE NOT NULL  ,
   CONTENT VARCHAR(255) NOT NULL  
   , PRIMARY KEY (REPORT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE REPORT
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_REPORT_USERS
     ON REPORT (USER_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : PRODUCTS
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS PRODUCTS
 (
   PRODUCT_ID BIGINT(4) NOT NULL  ,
   CAT_ID BIGINT(4) NOT NULL  ,
   PRODUCT_NAME VARCHAR(255) NOT NULL  ,
   PRODUCT_PRICE DECIMAL(13,2) NOT NULL  ,
   PRODUCT_QUANTITY BIGINT(4) NOT NULL  
   , PRIMARY KEY (PRODUCT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE PRODUCTS
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_PRODUCTS_CATEGORY
     ON PRODUCTS (CAT_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : STOCKREQUEST
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS STOCKREQUEST
 (
   REQ_ID BIGINT(4) NOT NULL  ,
   SUPPLIER_ID BIGINT(4) NOT NULL  ,
   USER_ID BIGINT(4) NOT NULL  ,
   QUANTITY_REQ BIGINT(4) NOT NULL  ,
   PRODUCT_REQ VARCHAR(255) NOT NULL  ,
   DATE_REQ DATE NOT NULL  
   , PRIMARY KEY (REQ_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE STOCKREQUEST
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_STOCKREQUEST_SUPPLIER
     ON STOCKREQUEST (SUPPLIER_ID ASC);

CREATE  INDEX I_FK_STOCKREQUEST_USERS
     ON STOCKREQUEST (USER_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : CATEGORY
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS CATEGORY
 (
   CAT_ID BIGINT(4) NOT NULL  ,
   NAME_CAT VARCHAR(255) NOT NULL  
   , PRIMARY KEY (CAT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       TABLE : USERS
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS USERS
 (
   USER_ID BIGINT(4) NOT NULL  ,
   USER_ID_MANAGE BIGINT(4) NULL  ,
   USER_FNAME VARCHAR(255) NOT NULL  ,
   USER_PHONE VARCHAR(255) NOT NULL  ,
   USER_EMAIL VARCHAR(255) NOT NULL  ,
   USER_PWD VARCHAR(255) NOT NULL  ,
   USER_ROLE VARCHAR(255) NOT NULL  ,
   USER_STATUS VARCHAR(255) NOT NULL  
   , PRIMARY KEY (USER_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE USERS
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_USERS_USERS
     ON USERS (USER_ID_MANAGE ASC);

# -----------------------------------------------------------------------------
#       TABLE : SUPPLIER
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS SUPPLIER
 (
   SUPPLIER_ID BIGINT(4) NOT NULL  ,
   SUPPLIER_FNAME VARCHAR(255) NOT NULL  ,
   CONTACT_INFO VARCHAR(255) NOT NULL  
   , PRIMARY KEY (SUPPLIER_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       TABLE : APPEAR
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS APPEAR
 (
   PRODUCT_ID BIGINT(4) NOT NULL  ,
   REQ_ID BIGINT(4) NOT NULL  
   , PRIMARY KEY (PRODUCT_ID,REQ_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE APPEAR
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_APPEAR_PRODUCTS
     ON APPEAR (PRODUCT_ID ASC);

CREATE  INDEX I_FK_APPEAR_STOCKREQUEST
     ON APPEAR (REQ_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : DELIVER
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS DELIVER
 (
   PRODUCT_ID BIGINT(4) NOT NULL  ,
   SUPPLIER_ID BIGINT(4) NOT NULL  ,
   DELIVERY_DATE DATE NOT NULL  
   , PRIMARY KEY (PRODUCT_ID,SUPPLIER_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE DELIVER
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_DELIVER_PRODUCTS
     ON DELIVER (PRODUCT_ID ASC);

CREATE  INDEX I_FK_DELIVER_SUPPLIER
     ON DELIVER (SUPPLIER_ID ASC);


# -----------------------------------------------------------------------------
#       CREATION DES REFERENCES DE TABLE
# -----------------------------------------------------------------------------


ALTER TABLE REPORT 
  ADD FOREIGN KEY FK_REPORT_USERS (USER_ID)
      REFERENCES USERS (USER_ID) ;


ALTER TABLE PRODUCTS 
  ADD FOREIGN KEY FK_PRODUCTS_CATEGORY (CAT_ID)
      REFERENCES CATEGORY (CAT_ID) ;


ALTER TABLE STOCKREQUEST 
  ADD FOREIGN KEY FK_STOCKREQUEST_SUPPLIER (SUPPLIER_ID)
      REFERENCES SUPPLIER (SUPPLIER_ID) ;


ALTER TABLE STOCKREQUEST 
  ADD FOREIGN KEY FK_STOCKREQUEST_USERS (USER_ID)
      REFERENCES USERS (USER_ID) ;


ALTER TABLE USERS 
  ADD FOREIGN KEY FK_USERS_USERS (USER_ID_MANAGE)
      REFERENCES USERS (USER_ID) ;


ALTER TABLE APPEAR 
  ADD FOREIGN KEY FK_APPEAR_PRODUCTS (PRODUCT_ID)
      REFERENCES PRODUCTS (PRODUCT_ID) ;


ALTER TABLE APPEAR 
  ADD FOREIGN KEY FK_APPEAR_STOCKREQUEST (REQ_ID)
      REFERENCES STOCKREQUEST (REQ_ID) ;


ALTER TABLE DELIVER 
  ADD FOREIGN KEY FK_DELIVER_PRODUCTS (PRODUCT_ID)
      REFERENCES PRODUCTS (PRODUCT_ID) ;


ALTER TABLE DELIVER 
  ADD FOREIGN KEY FK_DELIVER_SUPPLIER (SUPPLIER_ID)
      REFERENCES SUPPLIER (SUPPLIER_ID) ;

