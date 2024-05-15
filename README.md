# LibrarySpringBoot
# Requirement:
  A library offers its subscribers a list of books that can be borrowed.\
  Each book can exist in one or more copies, identified by unique codes(id). \
  The library has several terminals where subscribers(REGULAR uses) can borrow books. \
  To be able to use a terminal, a subscriber must log in. \
  After logging in, he see the list of books available at that moment and can borrow one or more.  (in order to borrow or return a book the user makes an appointment and if not pressent at the appointment (without canceling it) he gets a penalization)\
  Upon reaching a certain number of penalty points, the user's account becomes DEACTIVATED, so they must physically go to the library to reactivate their account.\
  For the return of books, there is only one terminal, served by a librarian(EMPLOYEE users). \
  After each loan/return, all the users see the updated list of available books.

# I divided the problem into 3 iterations 
1. Sign IN/UP/Delete Account+ JWT Authentication + Initialize Graphical Interface + Show Books + Send Correctly HTTP request\
UC - 1,2,3 #DONE
3. BorrowBooks + Penalizations + Cancel Appointment + Reactivate Account + confirm Borrow Appointment\
  UC- 4,5,7
4. ReturnBooks + Penalizations + Cancel Appointment + Confirm Return Appointment + Confirm Other Appointments\
  UC- 4,6,8

# The project stage
For the time being I implemented the iteration 1.
# Technologies Used (for the JavaFX project)
1. Lombok dependency to make it easier to manipulate the entities
2. JavaFX 

# Model UML Diagram
![ModelDigram](https://github.com/DariusB12/LibrarySpringBoot/assets/131203165/ea8cc665-3980-4491-ad78-610a5e343bd7)


# UseCases UML Diagram
![UseCasesDiagram](https://github.com/DariusB12/LibrarySpringBoot/assets/131203165/be306242-ef9b-45f4-b38d-0a49b57db777)


# UseCases Scenarios with Alternative flows and Exceptions:
[Use Cases 2.pdf](https://github.com/DariusB12/LibrarySpringBoot/files/14855176/Use.Cases.2.pdf)
