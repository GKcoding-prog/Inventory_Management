DROP DATABASE IF EXISTS Inventory_sys;

CREATE DATABASE IF NOT EXISTS Inventory_sys;
USE Inventory_sys;
# -----------------------------------------------------------------------------
#       TABLE : REPORT
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS REPORT
 (
   REPORT_ID BIGINT(4) NOT NULL  ,
   BOSS_ID BIGINT(4) NOT NULL  ,
   GENERATED_AT VARCHAR(255) NOT NULL  ,
   CONTENT VARCHAR(255) NOT NULL  
   , PRIMARY KEY (REPORT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE REPORT
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_REPORT_BOSS
     ON REPORT (BOSS_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : BOSS
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS BOSS
 (
   BOSS_ID BIGINT(4) NOT NULL  ,
   NAME VARCHAR(255) NOT NULL  ,
   EMAIL VARCHAR(255) NOT NULL  ,
   PASSWORD VARCHAR(255) NOT NULL  
   , PRIMARY KEY (BOSS_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       TABLE : STOCKREQUEST
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS STOCKREQUEST
 (
   REQUEST_ID BIGINT(4) NOT NULL  ,
   EMP_ID BIGINT(4) NOT NULL  ,
   QUANTITY_REQ BIGINT(4) NOT NULL  ,
   DATE_REQ DATE NOT NULL  
   , PRIMARY KEY (REQUEST_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE STOCKREQUEST
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_STOCKREQUEST_EMPLOYEE
     ON STOCKREQUEST (EMP_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : PRODUCTS
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS PRODUCTS
 (
   PRODUCT_ID BIGINT(4) NOT NULL  ,
   CATEGORY_ID BIGINT(4) NOT NULL  ,
   NAME VARCHAR(255) NOT NULL  ,
   PRICE DECIMAL(13,2) NOT NULL  ,
   QUANTITY BIGINT(4) NOT NULL  
   , PRIMARY KEY (PRODUCT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE PRODUCTS
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_PRODUCTS_CATEGORY
     ON PRODUCTS (CATEGORY_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : EMPLOYEE
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS EMPLOYEE
 (
   EMP_ID BIGINT(4) NOT NULL  ,
   BOSS_ID BIGINT(4) NOT NULL  ,
   NAME VARCHAR(255) NOT NULL  ,
   EMAIL VARCHAR(255) NOT NULL  ,
   PASSWORD VARCHAR(255) NOT NULL  
   , PRIMARY KEY (EMP_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE EMPLOYEE
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_EMPLOYEE_BOSS
     ON EMPLOYEE (BOSS_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : CATEGORY
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS CATEGORY
 (
   CATEGORY_ID BIGINT(4) NOT NULL  ,
   NAME_CAT VARCHAR(255) NOT NULL  
   , PRIMARY KEY (CATEGORY_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       TABLE : SUPPLIER
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS SUPPLIER
 (
   SUPPLIER_ID BIGINT(4) NOT NULL  ,
   NAME VARCHAR(255) NOT NULL  ,
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
   REQUEST_ID BIGINT(4) NOT NULL  
   , PRIMARY KEY (PRODUCT_ID,REQUEST_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE APPEAR
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_APPEAR_PRODUCTS
     ON APPEAR (PRODUCT_ID ASC);

CREATE  INDEX I_FK_APPEAR_STOCKREQUEST
     ON APPEAR (REQUEST_ID ASC);

# -----------------------------------------------------------------------------
#       TABLE : DELIVER
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS DELIVER
 (
   SUPPLIER_ID BIGINT(4) NOT NULL  ,
   PRODUCT_ID BIGINT(4) NOT NULL  ,
   DATE_DEL DATE NULL  
   , PRIMARY KEY (SUPPLIER_ID,PRODUCT_ID) 
 ) 
 comment = "";

# -----------------------------------------------------------------------------
#       INDEX DE LA TABLE DELIVER
# -----------------------------------------------------------------------------


CREATE  INDEX I_FK_DELIVER_SUPPLIER
     ON DELIVER (SUPPLIER_ID ASC);

CREATE  INDEX I_FK_DELIVER_PRODUCTS
     ON DELIVER (PRODUCT_ID ASC);


# -----------------------------------------------------------------------------
#       CREATION DES REFERENCES DE TABLE
# -----------------------------------------------------------------------------


ALTER TABLE REPORT 
  ADD FOREIGN KEY FK_REPORT_BOSS (BOSS_ID)
      REFERENCES BOSS (BOSS_ID) ;


ALTER TABLE STOCKREQUEST 
  ADD FOREIGN KEY FK_STOCKREQUEST_EMPLOYEE (EMP_ID)
      REFERENCES EMPLOYEE (EMP_ID) ;


ALTER TABLE PRODUCTS 
  ADD FOREIGN KEY FK_PRODUCTS_CATEGORY (CATEGORY_ID)
      REFERENCES CATEGORY (CATEGORY_ID) ;


ALTER TABLE EMPLOYEE 
  ADD FOREIGN KEY FK_EMPLOYEE_BOSS (BOSS_ID)
      REFERENCES BOSS (BOSS_ID) ;


ALTER TABLE APPEAR 
  ADD FOREIGN KEY FK_APPEAR_PRODUCTS (PRODUCT_ID)
      REFERENCES PRODUCTS (PRODUCT_ID) ;


ALTER TABLE APPEAR 
  ADD FOREIGN KEY FK_APPEAR_STOCKREQUEST (REQUEST_ID)
      REFERENCES STOCKREQUEST (REQUEST_ID) ;


ALTER TABLE DELIVER 
  ADD FOREIGN KEY FK_DELIVER_SUPPLIER (SUPPLIER_ID)
      REFERENCES SUPPLIER (SUPPLIER_ID) ;


ALTER TABLE DELIVER 
  ADD FOREIGN KEY FK_DELIVER_PRODUCTS (PRODUCT_ID)
      REFERENCES PRODUCTS (PRODUCT_ID) ;

