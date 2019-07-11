# Knucklebones
Knucklebones is a Java Object-Relational Mapping (ORM) library that attempts to simplify communication between java applications and a database.
Knucklebones simplifies communication to the database by allowing scripts to be run and then filling java structures.

## What is Knucklebones?

The heart of Knucklebones is the Persistence class. A Persistence can be created from the PersistenceFactory, which must be configured to communicate with the database.
When a persistence has been created, simple commands to interact with the database will be available. Database objects can be represented as simple structs in java (classes with public fields)
Knucklebones uses only the fields and annotations for the fields to represent the data and fill the objects. Knucklebones does not require objects to be in the Bean form like some other ORMs. This means that
getters, setters, and other methods can be defined on the data objects without affecting Knucklebones. Knucklebones does not keep track of objects once they are loaded. The struct is
a simple struct and once it has been filled by Knucklebones, it no longer has any connection to Knucklebones. There is no concept of "detaching" fetched data.

## Installation
Knucklebones is available on the Maven Central Repository. Importing from Maven will depend on the build too for the project, guidelines can be found on [Maven Central](https://search.maven.org/artifact/com.jubalrife/knucklebones/)

Knucklebones can also be downloaded directly from Github as Source, compiled, and included as a library.

## Usage

### Setting Up Persistence

The first step for using Knucklebones is to determine a javax.sql.DataSource that will connect to the database.
For the purpose of this example, the [H2 Datasource](http://www.h2database.com/html/features.html) will be used (to allow for a local file or in-memory database)
Once a datasource has been determined, create an instance of the Knucklebones PersistenceFactory passing in the datasource
```
DataSource ds = JdbcConnectionPool.create("jdbc:h2:databaseFileName", "aUsername", "aSecurePassword");
PersistenceFactory factory = new PersistenceFactory(ds);
```
Now that a persistence factory has been created, a Persistence can be created from the factory
```
try(Persistence persistence = factory.create()){
  //do work with persistence here
}
```
With an instance of the persistence pointed at the database, it is now possible to perform database operations.

_Please note that Persistence is an AutoCloseable and should be closed to prevent leaking connections._

### Structuring Data Objects
Knucklebones uses field access to fill structs and write data from structs into the database.

Suppose there is a table t that looks like t(_id_) a struct for this table would look as follows
```
@Table(name="t")
public static class RowOfT{
  @Id
  public Integer id;
}
```
If id was a generated value,
```
@Table(name="t")
public static class RowOfT{
  @Id
  @GeneratedValue
  public Integer id;
}
```
If another name is preferable in the java code
```
@Table(name="t")
public static class RowOfT{
  @Id
  @GeneratedValue
  @Column(name="id")
  public Integer anotherName;
}
```
This is the basic structure, from here more complex objects can be built.
to represent a table that looks like email(_emailId_,emaile,verified) the structure may look like
```
@Table(name="email")
public static class Email{
  @Id
  public Integer emailId;
  public String email;
  public Boolean verified;
}
```
Compound ids can be represented by putting the Id annotation on multiple fields
```
@Table(name="UserEmail")
public static class UserEmail{
  @Id
  public Integer userId;
  @Id
  public Integer emailId;
}
```

Knucklebones is method agnostic in the data objects, meaning that formatting or other logic an be stored in the struct directly. This is useful for situations such as enums
```
public enum MyEnum{
  VALUE1,
  VALUE2
}

@Table(name="TableWithEnum")
public static class SavedAsEnum{
  @Id
  public String value;

  public MyEnum getValue(){
    return MyEnum.valueOf(value);
  }
}
```
### Performing operations
Operations are performed using the methods exposed by the persistence

### Find
Using the email example
```
@Table(name="email")
public static class Email{
  @Id
  public Integer emailId;
  public String email;
  public Boolean verified;
}
```
a find may look like the following
```
try(Persistence p = factory.createPersistence()){
  Email e = new Email();
  e.id = 1;//a known id
  Email found = p.find(e);//E is filled as a parameter, but the result can be assigned also if needed
  System.out.println(e.email);//will print the same value as found.email
  System.out.println(found.email);
}
```




