# SpringBootSecurity

TotalRegistration1 Kickoff:
  - register
      - password strength (client side, JS)
      - password confirmation (Hibernate validation)
      - activate account via e-mail in 24h (encrypted token, not stored in db, thymeleaf e-mail template)
      - email and password validation (Hibernate validation and Passay)
  - login (two roles)
  - logout

TotalRegistration2 Kickoff:
  - added reCAPTCHA for registration form (https://developers.google.com/recaptcha/docs/start)
  
TotalRegistration3 (WIP):
  - adding "forgot password"
  - adding "reset password"

Bonus:
  - Hibernate bytecode enhancement plugin activated
  - embedded JMS activated
  - HikariCP activated
  
