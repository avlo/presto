## <abbr title="Azure Active Directory Single-Sign-On">AzureAD SSO</abbr> sample application

&nbsp;&nbsp;&nbsp;&nbsp; _Activates AzureAD SSO (Active Directory Oauth2 + JWT) user authentication + spring-security authorization_

Substitute below <span style="color:red">(red)</span> attributes in _**sample-apps/azure/src/main/resources/application.yml**_
```yml
    spring:
      thymeleaf:
        cache: false
      cloud:
        azure:
          active-directory:
            enabled: true
            profile:
              tenant-id: <TENANT_ID>
    #          environment:
    #            active-directory-endpoint:
    #            microsoft-graph-endpoint:
            credential:
              client-id: <CLIENT_ID>
              client-secret: <CLIENT_SECRET_VALUE, not CLIENT-SECRET>
            authorization-clients:
              aad:
    #            authorization-grant-type: client_credentials
                scopes:
                  - openid
    #              - https://graph.microsoft.com/Analytics.Read
                  - email  

```
then:

	$ cd sample-apps/azure/
	/sample-apps/azure$ mvn spring-boot:run
  

<hr style="border:2px solid gray">  

## Application Use
    
Login & Authenticate AzureAD user:

    localhost:8080/login

Authenticated and Authorized endpoint displaying existing application users

    localhost:8080/users

<hr style="border:2px solid gray">  

## AzureAD Spring-Boot Starter Tutorial

[presto-azure-spring-boot-starter](TUTORIAL.md)  