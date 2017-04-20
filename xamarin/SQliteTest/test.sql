CREATE TABLE Mammal(ID TEXT , 
   Trap TEXT , 
   Recapture BOOLEAN,  
   Mass REAL , 
   HDBody INTEGER , 
   Sex TEXT , 
   Dead BOOLEAN , 
   Notes TEXT , 
   Identifiers TEXT , 
   Changed INTEGER ,   
   PRIMARY KEY ('ID'));  

CREATE TABLE Lizard(
   ID INT PRIMARY KEY     NOT NULL,
   
   SpeciesCode           TEXT,
   Recapture BOOLEAN,
   Mass            INT     NOT NULL,
   Sex	INT,
   Dead         BOOLEAN,
   SVL INT,
   VTL INT,
   RegenTail BOOLEAN

);

CREATE TABLE Snake(
   ID INT PRIMARY KEY     NOT NULL,
   SpeciesCode          TEXT,
   Trap TEXT , 
   
   SVL INTEGER , 
   VTL INTEGER , 
   Mass REAL ,  
   Sex TEXT , 
   OTL INTEGER , 
   Hatchling BOOLEAN ,
   
   Recapture BOOLEAN, 
   Dead         BOOLEAN,
   RegenTail BOOLEAN
);

CREATE TABLE Amphibian(
   ID INT PRIMARY KEY     NOT NULL,
   SpeciesCode          TEXT,
   Trap TEXT , 
   
   HDBody INTEGER ,
   Mass REAL ,  
   Sex TEXT , 
  
   Dead         BOOLEAN,
   FOREIGN KEY (Sex) REFERENCES Genders(sex));

create table Genders(ID INT PRIMARY KEY     NOT NULL,
   sex  TEXT Not null UNIQUE );


