NOTE: Naming Conventions are necessary to follow because the queries
	  written using the specified names of schemas 

1) LOADING THE TRANSACTIONS AND MASTERDATA TABLES' DATA
	1.1) Create a Database/Schema in MySql named "project".
	1.2) Execute the "Transaction_and_MasterData_Generator_UPDATED.sql" file 
		 in the "project" schema.
		 
2) BUILDING THE STAR SCHEMA (DDL) OF OUR DATA WAREHOUSE
	2.1) Execute the given "DDL.sql" script in the same server of MySql
		 where "project" schema is located.
	Our star shema is formed.
	
3) ETL PHASE AND IMPLEMENTING MESHJOIN
	3.1) Create a java project in eclipse 
	3.2) Include JDBC driver for MySql in your java project by following steps (skip if you know)
		3.2.1) Right click on your java project in "Package Explorer" tab
		3.2.2) Build Path > Configure Build Path > Add External JARs
		3.2.3) Select mysql-connector-java-version-jar file from where you have it on your pc
	3.3) Copy the given "MeshJoin.java", "Transactions.java", "Mdata.java" and "Fact.java" class files
		 in the project source
	3.4) Run the "Meshjoin.java" file. it will take some time to Extract Transform and Load
		 data into star schema
	After the execution is finished, Our Data Warehouse is now ready for analysis
	
4) Open the "Queries.sql" file in MySql (same server as that of StarSchema)
   and run the query for desired analysis