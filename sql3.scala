import java.sql.{Connection,DriverManager}

var choice = 0


object bank{
	Class.forName("com.mysql.cj.jdbc.Driver")
	var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/personal", "root", "")
	val statement = connection.createStatement 
}



class bank{
def createaccquery(name:String, address:String) ={
	var create_query = "insert into personal values(NULL,'"+name+"','"+address+"')"
	bank.statement.executeUpdate(create_query)
}

def balance(Acno:Int):Int ={
	var rs_withdraws = bank.statement.executeQuery("select sum(amount) as Total from withdraw where accno ="+Acno)
	rs_withdraws.next
	var TotalWithdraws=rs_withdraws.getInt("Total")
	var rs_deposite = bank.statement.executeQuery("select sum(amount) as Total from deposit where accno ="+Acno)
	rs_deposite.next
	var Totaldeposits=rs_deposite.getInt("Total")
	var balance=Totaldeposits-TotalWithdraws
	return balance
}

def create_account= {
	println("Please enter your \nname: \naddress: ")
	createaccquery(readLine, readLine)
	println("your account has been created")
	val rs1 = bank.statement.executeQuery("SELECT max(accno) from personal")
		if (rs1.next) {
			val accno1 = rs1.getString("max(accno)")
			println("your account number is: "+accno1)
		}
}

def deposit = {
	println("\nPlease enter the account number: ")
	var acno=readInt
	if (checkuser(acno)){
			println("Your balance is : "+balance(acno))
			print("Enter the amount to Deposit: ")
			var amount=readInt
			bank.statement.executeUpdate(s"insert into deposit values(%d,%d,sysdate())".format(acno,amount))
			println("you have deposited amount: "+amount  )

	}
}

def checkuser(accno:Int):Boolean={
		var personalrecord=bank.statement.executeQuery("select * from personal where accno="+accno)
		if (personalrecord.next()){
			println("Account Holder's information:")
			println("Name: "+personalrecord.getString("name"))
			println("Address: "+personalrecord.getString("Address"))
			return true
		}else{
			println("Invalid Account number")
			return false
		}
}

def withdrawfunc = {
	println("\nPlease enter the account number: ")
	var acno=readInt
	if (checkuser(acno)){
			println("Your balance is : "+balance(acno))
			print("Enter the amount to WithDraw : ")
			var amount=readInt
			if(amount>balance(acno)){
				println(" cannot exceed balance ")
			}
			else{
				bank.statement.executeUpdate(s"insert into withdraw values(%d,%d,sysdate())".format(acno,amount))
				println("your new balance is : " +balance(acno) )
			}
	}
	}
	
}
		

var ref = new bank()
try{
println("please select an option")
println(" 1 - create account,\n 2- deposit money, \n 3 - withdraw money")
var choice = readInt
	if (choice == 1){
		ref.create_account
	}
	if (choice == 2){
	ref.deposit
	}
	if (choice == 3){
	ref.withdrawfunc
	}
}
catch {
	case e: Exception => println(e.toString)
}
