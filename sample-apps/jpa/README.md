## <abbr title="Self-contained user authentication and authorization using JPA/Hibernate">Standalone</abbr> mode
&nbsp;&nbsp;&nbsp;&nbsp; _Activates canonical spring-security user mode + spring-security authentication **and** authorization_   

Works by default.  No configuration necessary.  

	$ cd sample-apps/jpa/
	/sample-apps/jpa$  mvn spring-boot:run


<hr style="border:2px solid gray">  

## Application Use

Register new application user:

    localhost:8080/register
    
Login & Authenticate user:

    localhost:8080/login

Authenticated and Authorized endpoint displaying existing application users

    localhost:8080/users
  
  