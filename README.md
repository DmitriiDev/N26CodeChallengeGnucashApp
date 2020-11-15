# N26CodeChallengeGnucashApp

### The main purpose of the application:
Accounting of finance. Personal accounting. The application allows breaking up accounts by type and using common accounting standards and practices. 

### Who is the target audience? 
First of all, these are people who have an economy and complex distribution of costs and income. The application allows you to track the total value of accounts - capital. The application is also good for business accounting. 

### Highlights of the app
I can highlight two main things in the application - the Account and transactions. 
I want to divide the essence of the account into two types - Balance and Income / Expense. Also, the account may be of different types. 

### Test strategy description: 
I want to create a test model of the application usage, where I will display the standard process of interaction between the user and the application. My model will not work with the account type equity. Because the test model will become very complicated. 

I want to test how different accounts interact with each other in debit and credit relations. I also want to do different types of transactions that change the balance.  

### Test model description:
The user has assets (savings account, cash, bank card), there is a job that brings him money in the form of salary and bonuses. And there are liabilities and expenses in the form of payment for the apartment, telephone and food.

I will create 4 parent accounts, each account will have sub-accounts. At the end I will do 7 transactions from different accounts and check that the final balance and the sum of transactions are correct.  

### Test framework: 
I will use espresso, the native test framework for Android, for tests. It will allow me to interact effectively with both the UI application and its code database and the database that stores the data. 

I will use the screen object pattern to organize the code. 

I want to test a complex test scenario on this I will divide the test into 4 tests. Each test will prepare a database for the next one. Normally, this is bad practice, but if we do small units (4-5 tests per script), we can test complex models and not create a complex unreliable system. 

### File Structure
You can find my tests here:
gnucash-android-master/app/src/androidTest/java/org/gnucash/android/test/ui/testFrameworkVokuevDmitriiN26Challenge

