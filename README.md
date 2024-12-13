```java                                                                                                                    
__/\\\\\\\\\\\\\______/\\\\\\\\\______/\\\\\\\\\\\\\\\_____/\\\\\\\\\\\____/\\\\\\\\\\\\\\\_______/\\\\\______        
 _\/\\\/////////\\\__/\\\///////\\\___\/\\\///////////____/\\\/////////\\\_\///////\\\/////______/\\\///\\\____       
  _\/\\\_______\/\\\_\/\\\_____\/\\\___\/\\\______________\//\\\______\///________\/\\\_________/\\\/__\///\\\__      
   _\/\\\\\\\\\\\\\/__\/\\\\\\\\\\\/____\/\\\\\\\\\\\_______\////\\\_______________\/\\\________/\\\______\//\\\_     
    _\/\\\/////////____\/\\\//////\\\____\/\\\///////___________\////\\\____________\/\\\_______\/\\\_______\/\\\_    
     _\/\\\_____________\/\\\____\//\\\___\/\\\_____________________\////\\\_________\/\\\_______\//\\\______/\\\__   
      _\/\\\_____________\/\\\_____\//\\\__\/\\\______________/\\\______\//\\\________\/\\\________\///\\\__/\\\____  
       _\/\\\_____________\/\\\______\//\\\_\/\\\\\\\\\\\\\\\_\///\\\\\\\\\\\/_________\/\\\__________\///\\\\\/_____ 
        _\///______________\///________\///__\///////////////____\///////////___________\///_____________\/////_______
```
# User Authentication, Authorization & Web/Application Security Framework

Java [spring-boot-starter](https://www.baeldung.com/spring-boot-starter) rapid development modules, built atop the Java [Spring-Security framework](https://docs.spring.io/spring-security/reference/index.html).

| Starter Module                                                                   | Description                                       |
|----------------------------------------------------------------------------------|---------------------------------------------------|
| [Nostr](https://nostr-nips.com/nip-07)                                           | _window.nostr_ capability for web browsers (NIP-07) |
| [Azure OAuth2](https://learn.microsoft.com/en-us/entra/architecture/auth-oauth2) | Microsoft Entra ID, OAuth2+JWT, Single Sign-On    |
| [LDAP](https://www.okta.com/identity-101/what-is-ldap/)                          | Lightweight Directory Access Protocol             |
| Standalone                                                                       | Custom/Locally provided                           |

#### Features:

- Each module's security implementation is automatically provided, requiring only deployment configuration.  
- Freed from security implementation details, application developers can focus exclusively on their application/business logic needs.  
- For security modules not listed above, Presto is easily extensible for developers to add new/additional starter implementations as needed _without compromising or breaking existing security functionality_.

<hr style="border:2px solid gray">

## Project Layout/Structure
    $ tree -L 2
    |-- autoconfigure (module)
    |   |-- azure
    |   |-- core
    |   |-- h2db
    |   |-- jpa
    |   |-- ldap
    |   |-- mysql
    |   |-- nostr
    |   |-- web
    |-- lib          (module)
    |   |-- azure
    |   |-- core
    |   |-- h2db
    |   |-- jpa
    |   |-- ldap
    |   |-- mysql
    |   |-- nostr
    |   |-- web
    |-- sample-apps  (module)
    |   |-- azure
    |   |-- jpa
    |   |-- ldap
    |   |-- nostr
    |-- starter      (module)
        |-- azure
        |-- core
        |-- h2db
        |-- jpa
        |-- ldap
        |-- mysql
        |-- nostr
        |-- web 

----  

### Module descriptions
- `autoconfigure`:  
  spring-boot3-related auto-configurations (used by Presto developers/maintainers)


- `lib`:  
  core libraries (used by Presto developers/maintainers)


- `sample-apps`:  
  clear & simple examples (for client-application developers/users)


- `starter`:  
  powerful spring-boot3 rapid-development packaging feature (for client-application developers/users)

<hr style="border:2px solid gray">

## Requirements

    $ java -version

>     openjdk version "21.0.2" 2024-01-16
>     OpenJDK Runtime Environment (build 21.0.2+13-58)
>     OpenJDK 64-Bit Server VM (build 21.0.2+13-58, mixed mode, sharing)

    $ mvn -version
>     Apache Maven 4.0.0-beta-3 (e92f645c2749eb2a4f5a8843cf01e7441e4b559f)
>     Maven home: ~/.sdkman/candidates/maven/current
>     Java version: 21.0.2, vendor: Oracle Corporation, runtime: /home/nick/.sdkman/candidates/java/21.0.2-open
>     Default locale: en_US, platform encoding: UTF-8
>     OS name: "linux", version: "5.15.0-126-generic", arch: "amd64", family: "unix"

<hr style="border:2px solid gray">  

## Build
From project root directory:

	$ mvn clean install

<hr style="border:2px solid gray">  

## Configure & Run Sample Applications

[Standalone](sample-apps/jpa/README.md)

[LDAP](sample-apps/ldap/README.md)

[AzureAD](sample-apps/azure/README.md)

<hr style="border:2px solid gray">  

## Presto Spring-Boot Starter Tutorials

[presto-azure-spring-boot-starter](sample-apps/azure/TUTORIAL.md)

<hr style="border:2px solid gray">  


## DB console: ##

    localhost:8080/h2-console/

*user: sa*  
*password: // blank*

Display all framework table contents:

    SELECT * FROM USERS;
    SELECT * FROM APPUSER_AUTHUSER;
    SELECT * FROM APPUSER;
    SELECT * FROM AUTHORITIES;


<hr style="border:2px solid gray">  

## Automated testing
### Build and run unit-tests

    $ mvn test

unit-test code-coverage reports can now be displayed by opening browser file:

    target/site/jacoco/index.html

_note: for complete code-coverage results, use **mvn verify** command below_

----

### Build and run both integration-tests and unit-tests

    $ mvn verify -P [jpa|ldap|adoauth2]

complete (integration-test and unit-test) code-coverage reports can now be displayed by opening browser file:

    target/site/jacoco/index.html


