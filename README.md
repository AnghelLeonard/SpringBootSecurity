# Spring Boot Security Kickoff Application

  - register
      - password strength (client side, JS)
      - password confirmation (Hibernate validation)
      - activate account via e-mail in 24h (encrypted token, not stored in db, thymeleaf e-mail template)
      - email and password validation (Hibernate validation and Passay)
  - login (two roles)
  - logout
  - persistent "remember me"
  - forget(reset) password via 24h reset link (encrypted token, not stored in db, thymeleaf e-mail template) 
  - send a new link in case of expiration of current link
  - reCAPTCHA for registration form (https://developers.google.com/recaptcha/docs/start)
  - CSRF protection
  - two form login 
   - user login form with real database (ROLE_MEMBER)
   - admin login form with in-memory credentials; user: admin@kickoff.org, password: kickoff (ROLE_ADMIN)
  - OAuth (WIP)

General facts: 

  - Spring Boot 1.4.2.RELEASE
  - Hibernate 5
  - Hibernate bytecode enhancement plugin activated
  - embedded JMS activated
  - HikariCP activated
  - need a SMTP server (for testing Fake SMTP Server will do the job) - this must run in order to run the application
  - Bootstrap kickoff
  
![alt text][logo]
[logo]: https://github.com/AnghelLeonard/SpringBootSecurity/blob/master/register.png "Register form"
 
