## Nostr Sample Application
&nbsp;&nbsp;&nbsp;&nbsp;_activates Nostr authentication + spring-security authorization_  

Substitute password for work-account (in this case, TU01088) in _**sample-apps/ldap/src/main/resources/application.properties**_

    spring.ldap.username=CN=TU01088,OU=Normal,OU=WorkAccounts,DC=mfad,DC=mfroot,DC=org
    spring.ldap.password=<TU01088_PASSWORD>
then:

	$ cd sample-apps/nostr/
	/sample-apps/nostr  mvn spring-boot:run  

<hr style="border:2px solid gray">  

## Application Use

Login & Authenticate Nostr user:

    localhost:8080/login

Authenticated and Authorized endpoint displaying existing application users

    localhost:8080/users