  /** * Database creation sql statement */ 
 CREATE TABLE ArthropodCapture (MANT INTEGER  , UNKI INTEGER  , THYS INTEGER  , SOLI INTEGER  , SCOR INTEGER  , PSEU INTEGER  , ORTH INTEGER  , LEPI INTEGER  , HYMB INTEGER  , HYMA INTEGER  , HETE INTEGER  , DIPT INTEGER  , DIEL INTEGER  , DERM INTEGER  , CRUS INTEGER  , COLE INTEGER  , CHIL INTEGER  , BLAT INTEGER  , AUCH INTEGER  , ARAN INTEGER  , CheckDates_CheckdateID TEXT , Trap TEXT , Predator TEXT , Notes TEXT , AnthropodCaptureID TEXT , Changed INTEGER ,   PRIMARY KEY('AnthropodCaptureID')); 

 CREATE TABLE Arthropods (ArthropodID TEXT , Class TEXT , OrderName TEXT , OrderCode TEXT , Description TEXT , Changed INTEGER , PRIMARY KEY('ArthropodID'));

 CREATE TABLE CheckDates (CheckdateID TEXT , Handler TEXT , Recorder TEXT , Date TEXT , Time TEXT , Closed TEXT , NoCapture TEXT , Comments TEXT , Location_LocationID TEXT , Changed INTEGER , PRIMARY KEY('CheckdateID')); 

 CREATE TABLE Herps (HerpID TEXT , Taxa TEXT , Genus TEXT , Species TEXT , Spp_Code TEXT , Changed INTEGER , PRIMARY KEY('HerpID'));  


 CREATE TABLE Locations (LocationID TEXT , SiteName TEXT , ArrayName TEXT , Latitude REAL , Longitude REAL , Changed INTEGER , PRIMARY KEY('LocationID')); 

 CREATE TABLE HerpCapture (HerpCaptureID TEXT , Trap TEXT , Recapture TEXT , SVL INTEGER , VTL INTEGER , Mass REAL , HDBody INTEGER , Sex TEXT , OTL INTEGER , Dead TEXT , Hatchling TEXT , IdNeeded TEXT , Notes TEXT , CheckDates_CheckdateID TEXT , Herps_HerpID TEXT , Identifiers TEXT , Changed INTEGER ,   PRIMARY KEY ('HerpCaptureID'));  