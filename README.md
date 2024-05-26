# LibraryJavaFX
# Requirement:
  A library offers its subscribers a list of books that can be borrowed.\
  Each book can exist in one or more copies, identified by unique codes (id). \
  The library has several terminals where subscribers(REGULAR users) can borrow books. \
  To be able to use a terminal, a subscriber must sign in. \
  After signing in, the user can see the list of books available at that moment and can borrow/return one or more.  (in order to borrow or return a book the user should make an appointment)\
  For the return of books, there is only one terminal. \
  After each loan/return, all the users see the updated list of available books.

# I divided the problem into 3 iterations 
1. Sign IN/UP/Delete Account+ JWT Authentication + Initialize Graphical Interface + Data Base connection + Show Books
UC- 1,2,3  #DONE
2. BorrowBooks + Cancel Appointments + confirm BorrowAppointment
UC- 4,5,6  #DONE
3. ReturnBooks + Cancel Appointments + confirm Return Appointment
UC- 4,7,8  #DONE

# Technologies Used 
1. PostgreSQL for the data base connection
2. SpringBoot framework
3. Hibernate
4. Spring security
5. JWT security for authentication
6. Lombok dependency to make it easier to manipulate the entities
7. JavaFX
8. SSE (Server-Sent Events) - in order to send updates to clients

# Server SpringBoot project repository link
[Link Client JavaFX App](https://github.com/DariusB12/LibrarySpringBoot/tree/main)
   
# Problems I faced:
  1. I had to add, in the dependency section of gradle.build, the lombok annotationProcessor because some lombok annotations were not handled during compilation time and this is the main reason why I could not compile my app.
  2. I had to disable the csrf for http requests, because when I wanted to make a request that changed the state (ex: POST), the application expected a csrf Token from this request, but i did not need this feature because I use JWT authentication.
  3. Exceptions thrown at the level of internal filter weren't catch in my CustomExceptionHandler class, so i had to use a handlerExceptionResolver and annotate my ControllerAdvice to be @Order(1) so that the exceptions were first handled by my CustomExceptionHandler.
   
# Images With the app functionalities
<img width="142" alt="SignInWindow" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/0f649cb9-4939-45ed-bdd5-fd481600c279">
<img width="283" alt="SignUp1" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/3f11b060-23c8-4d05-a95c-c101ecf56a57">
<img width="282" alt="SignUp2" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/2c3a52e3-bea6-430c-bb88-55fb512df798">
<img width="275" alt="SignUp3" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/7bfb50e2-36ec-45ca-9b34-d7593882fdd8">
<img width="824" alt="Regular1" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/3b797121-40c3-4c83-8a15-3834625af5e1">
<img width="481" alt="Regular2" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/63640ac6-9482-410d-8713-6246e30fb53c">
<img width="481" alt="Regular3" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/4d20e7f8-247e-4e7d-9988-53aa6a6f9384">
<img width="830" alt="Regular4" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/12d2354f-fde6-43ac-902c-8b7bd320a2c3">
<img width="398" alt="Regular5" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/9756e036-c416-4fcd-8e88-b1090e6dc861">
<img width="394" alt="Regular6" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/655ccf08-3f56-42f6-81ba-a87a2050adc0">
<img width="824" alt="Regular7" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/8d2c26a2-4978-413f-821a-0607f77ae8ad">
<img width="830" alt="Regular8" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/66250af3-5fc4-4c5b-9791-f418fa879943">
<img width="499" alt="Regular9" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/611355a9-271c-48bc-bba1-119a434aaddb">
<img width="391" alt="Regular10" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/4edbfcea-31ee-4092-b64c-d454cf6c4fbc">
<img width="388" alt="Regular11" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/47ecaa02-e195-4e34-aab4-6b7242a31891">
<img width="823" alt="Regular12" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/cee870cf-44f5-45f0-bfa4-8e57b3d180cd">
<img width="284" alt="Regular13" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/ea177c57-861a-4dbe-a280-1f7c6bc51e97">
<img width="276" alt="Regular14" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/3c08d02e-ea57-4e69-976f-f797330f5c04">
<img width="827" alt="Regular15" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/eae200f8-7d4c-46e5-a637-74fc06f3160d">
<img width="245" alt="Regular16" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/4cce646d-6e79-476d-b754-1a5af4789f08">
<img width="423" alt="Regular17" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/f393077f-37b3-4f26-a1aa-3bbdb35c7aad">
<img width="818" alt="Regular18" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/61da34a2-aeb9-4575-914a-7dfa02b082c6">
<img width="278" alt="Regular19" src="https://github.com/DariusB12/LibraryJavaFX/assets/131203165/5a08437e-3094-4824-8278-e353c26214f9">




