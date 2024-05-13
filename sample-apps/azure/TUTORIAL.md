## Example Project Structure, with minimal required classes & property file  

```
  $ tree
  |-- pom.xml
  `-- src
      `-- main
          |-- java
          |   `-- com
          |       `-- prosilion
          |           `-- presto
          |               |-- PrestoAzureUserDemo.java                   <--- 1
          |               |-- config
          |               |   `-- ExampleAzureUserConfig.java            <--- 2
          |               |-- model
          |               |   `-- entity
          |               |       `-- ExampleAzureUser.java              <--- 3
          |               |-- repository
          |               |   `-- ExampleAzureUserRepository.java        <--- 4
          `-- resources
              |-- application.yml                                        <--- 5
```
<hr style="border:2px solid gray">  

## Per 5 components shown above, each requiring provision:
----
a custom user class ([example above, ExampleAzureUser.java](../src/main/java/com/prosilion.presto/model/entity/ExampleAzureUser.java)), containing:
  - `extends AppUser`  
  - implements required methods:  
    `T getInstantiatedCustomAppUserType();`  
    `T createNewCustomAppUserInstance();`   
  - annotations  
    `@Embeddable`  
    `@Entity`  
  - any/all fields which you'd like your user object to have  

----

a jpa/hibernate JpaRepository ([example above, ExampleAzureUserRepository.java](../src/main/java/com/prosilion/presto/repository/ExampleAzureUserRepository.java)) interface for your custom user class, containing:
  - annotated with  
    `@Repository`  
  - `extends JpaRepository<<YOUR_CUSTOM_USER_CLASS>, Long>`  

----

an `application.yml` file, with values populated for <span style="color:red"><TENANT_ID>, <CLIENT_ID></span> and <span style="color:red"><CLIENT_SECRET_VALUE></span>
  ```java
spring:  
    cloud:
      azure:
        active-directory:
          enabled: true
          profile:
            tenant-id: <TENANT_ID>                                      <----
#          environment:
#            active-directory-endpoint:
#            microsoft-graph-endpoint:
          credential:
            client-id: <CLIENT_ID>                                      <----
            client-secret: <CLIENT_SECRET_VALUE, not CLIENT-SECRET>     <----
          authorization-clients:
            aad:
#            authorization-grant-type: client_credentials
              scopes:
                - openid
#              - https://graph.microsoft.com/Analytics.Read
                - email
```  

----

a spring boot `@Configuration` annotated file ([example above, ExampleAzureUserConfig.java](../src/main/java/com/prosilion/presto/config/ExampleAzureUserConfig.java)), containing:
  - annotations:  
```java
    @Configuration  
    @EnableJpaRepositories(basePackageClasses = {<YOUR_CUSTOM_USER_CLASS>Repository.class})  
    @EntityScan(basePackageClasses = {<YOUR_CUSTOM_USER_CLASS>.class})  
    @PropertySource("classpath:application.yml")  
    @ComponentScan(basePackages = "com.prosilion.presto.*")
```
  - `@Bean` definition:  
```
    public CustomizableAppUserService customizableAppUserService()  
```

----  
  
after implementing the above, all remaining/application logic can be implemented per developer/application needs.  

<hr style="border:2px solid gray">  

coming soon:demo extra's:

  CustomController  
  CustomService  
  CustomDTO  
