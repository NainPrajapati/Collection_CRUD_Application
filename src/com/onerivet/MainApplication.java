package com.onerivet;

import java.util.List;
import java.util.Scanner;

import com.onerivet.model.Accounts;
import com.onerivet.model.Users;
import com.onerivet.repository.AccountDBRepositoryImplementation;
import com.onerivet.repository.AccountRepository;
import com.onerivet.repository.UserDBRepositoryImplementation;
import com.onerivet.repository.UserRepository;
import com.onerivet.service.AccountService;
import com.onerivet.service.AccountServiceImplementation;
import com.onerivet.service.UserService;
import com.onerivet.service.UserServiceImplementation;

public class MainApplication {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		UserRepository userRepo = new UserDBRepositoryImplementation();
		AccountRepository accountRepo = new AccountDBRepositoryImplementation();

		
		
//        UserRepository userRepo = new UserRepositoryImplementation();
//        AccountRepository accountRepo = new AccountRepositoryImplementation();

        UserService userService = new UserServiceImplementation(userRepo , accountRepo);
        AccountService accountService = new AccountServiceImplementation(accountRepo , userRepo);

        while (true) {

            System.out.println("\n==== BANK MENU ====");
            System.out.println("1. Create User");
            System.out.println("2. Create Account");
            System.out.println("3. Get User");
            System.out.println("4. Get Account");
            System.out.println("5. Update User");
            System.out.println("6. Update Account");
            System.out.println("7. Delete User");
            System.out.println("8. Delete Account");
            System.out.println("9. Get All Users");
            System.out.println("10.Get All Accounts");
            System.out.println("11. Get User by Email");
            System.out.println("12. Get User by User Name");
            System.out.println("13. Delete All Users");
            System.out.println("14. Delete All Accounts");
            System.out.println("15. Exit");

            System.out.print("Choose option: ");
            String input = scanner.nextLine();
            
            int choice;
            
            try {
            	choice = Integer.parseInt(input);
            }catch(NumberFormatException e) {
            	System.out.println("Invalid Choice!!.. Please Enter a valid Number.");
            	continue;
            }

            try {

                switch (choice) {

                    case 1:                 
                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        Users newUser = new Users(name, email);
                        userService.createUser(newUser);
                        System.out.println("User created successfully! Generated ID: " + newUser.getUserId());                        
                        break;

                    case 2:
                        System.out.print("User ID: ");
                        String uid = scanner.nextLine();

                        System.out.print("Balance: ");
                        Double bal = scanner.nextDouble();
                        scanner.nextLine();

                        Accounts newAccount = new Accounts(uid, bal);
                        accountService.createAccount(newAccount);
                        System.out.println("Account created successfully! Generated Account Number: " + newAccount.getAccountNumber());
                        break;

                    case 3:
                        System.out.print("Enter User ID: ");
                        String searchUserId = scanner.nextLine();

                        Users user = userService.getUserById(searchUserId);
                        System.out.println("User Found:");
                        System.out.println("ID: " + user.getUserId());
                        System.out.println("Name: " + user.getUserName());
                        System.out.println("Email: " + user.getUserEmail());
                        break;

                    case 4:
                        System.out.print("Enter Account Number: ");
                        String searchAcc = scanner.nextLine();

                        Accounts account = accountService.getAccount(searchAcc);
                        System.out.println("Account Found:");
                        System.out.println("Account Number: " + account.getAccountNumber());
                        System.out.println("User ID: " + account.getUserId());
                        System.out.println("Balance: " + account.getBalance());
                        break;

                    case 5:
                        System.out.print("User ID: ");
                        String uId = scanner.nextLine();

                        System.out.print("Registered Email: ");
                        String authEmail = scanner.nextLine();

                        System.out.print("New Name: ");
                        String newName = scanner.nextLine();

                        System.out.print("New Email: ");
                        String newEmail = scanner.nextLine();

                        userService.updateUser(uId, authEmail, newName, newEmail);
                        System.out.println("User updated successfully!");
                        break;

                    case 6:
                        System.out.print("User ID: ");
                        String userId = scanner.nextLine();

                        System.out.print("Account Number: ");
                        String accountNumber = scanner.nextLine();

                        System.out.print("New Balance: ");
                        Double newBal = scanner.nextDouble();
                        scanner.nextLine();

                        accountService.updateAccount(userId, accountNumber, newBal);
                        System.out.println("Account updated successfully!");
                        break;

                    case 7:
                        System.out.print("Enter User ID to delete: ");
                        String deleteUserId = scanner.nextLine();

                        userService.deleteUser(deleteUserId);
                        System.out.println("User deleted successfully!");
                        break;

                    case 8:
                        System.out.print("Enter Account Number to delete: ");
                        String deleteAcc = scanner.nextLine();

                        accountService.deleteAccount(deleteAcc);
                        System.out.println("Account deleted successfully!");
                        break;

                    case 9:
                    	List<Users> users = userService.getAllUsers();
                    	for (Users u : users) {
                            System.out.println("ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
                        }
                    	break;
                    
                    case 10:
                    	List<Accounts> accounts = accountService.getAllAccounts();
                    	for(Accounts a : accounts) {
                            System.out.println("Account: " + a.getAccountNumber() + ", User ID: " + a.getUserId() + ", Balance: " + a.getBalance());
                    	}
                        break;
                        
                    case 11: 
                        System.out.print("Enter Email to search: ");
                        String searchEmail = scanner.nextLine();
                        Users userByEmail = userService.getUserByEmail(searchEmail);
                        System.out.println("User Found: " + userByEmail.getUserName() + " (ID: " + userByEmail.getUserId() + ")");
                        break;

                    case 12: 
                    	System.out.print("Enter Name to search: ");
                        String searchName = scanner.nextLine();

                        List<Users> userList = userService.getUserByUserName(searchName);
                        System.out.println("\n--- Search Results ---");
                        for (Users u : userList) {
                            System.out.println("ID: " + u.getUserId() + 
                                               " | Name: " + u.getUserName() + 
                                               " | Email: " + u.getUserEmail());
                        }
                        break;
                        
                    case 13:
                        System.out.print("ARE YOU SURE? This will delete all users. (yes/no): ");
                        String confirmation = scanner.nextLine();
                        
                        if (confirmation.equalsIgnoreCase("yes")) {
                            userService.deleteAllUsers();
                            System.out.println("System reset successful.");
                        } else {
                            System.out.println("Operation cancelled.");
                        }
                        break;    
                    
                    case 14:
                        System.out.print("Are you sure you want to delete ALL accounts? (yes/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("yes")) {
                            accountService.deleteAllAccounts();
                            System.out.println("All accounts cleared.");
                        }
                        break;    
                    
                    case 15:
                        System.out.println("Exiting application...");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
	
}
