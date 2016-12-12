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
  - reCAPTCHA for registration form (https://developers.google.com/recaptcha/docs/start)
  - OAuth (WIP)

General facts: 

  - Spring Boot 1.4.2.RELEASE
  - Hibernate 5
  - Hibernate bytecode enhancement plugin activated
  - embedded JMS activated
  - HikariCP activated
  - need a SMTP server (for testing Fake SMTP Server will do the job)
  - Bootstrap starting point
  
