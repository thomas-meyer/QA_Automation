package AutomationFramework;

import java.util.Random;

//The contact class is designated to
//hold all relavent information about
//a potential reviewer
public class Contact {
	private String firstName;
	private String lastName;
	private String email;
	//True, they have experience
	//False.=, they don't have experience
	private boolean experience;
	//true has COIs, false has no COIs
	//by default a contact will not have COIs
	private boolean COIcond;
	//should always be size int[2][]
	//for second row,0: no ban, 1: partial ban, 2: total ban
	private int[][] COIs=null;
	
	//If the num used is a duplicate, the program will run into problems
	//Best practice is to generate and use a large random num (however
	//there will always be a small chance of a collision)
	public Contact(int num) {
		this.firstName="Automation";
		this.lastName="Bot"+num;
		this.email=this.firstName+"."+this.lastName+"@creativetests.com";
		//randomly generates if the bot has experience or not
		//this can be changed later
		Random rand=new Random();
		if(rand.nextInt(2)==1) {
			this.experience=true;
		}else {
			this.experience=false;
		}
		this.COIcond=false;
	}
	
	//If we want to manually name the bot
	public Contact(String fName, String lName) {
		this.firstName=fName;
		this.lastName=lName;
		this.email=this.firstName+"."+this.lastName+"@creativetests.com";
		//randomly generates if the bot has experience or not
		//this can be changed later
		Random rand=new Random();
		if(rand.nextInt(2)==1) {
			this.experience=true;
		}else {
			this.experience=false;
		}
		this.COIcond=false;
	}
	
	//returns the Contacts first name
	public String getFirstName() {
		return this.firstName;
	}
	
	//returns the Contacts last name
	public String getLastName() {
		return this.lastName;
	}
	
	//returns the Contacts email address
	public String getEmail() {
		return this.email;
	}
	
	//returns wether or not the 
	//contact has experience
	public boolean getExp() {
		return this.experience;
	}
	
	public boolean getCOIcond() {
		return this.COIcond;
	}
	
	public int[][] getCOIs() {
		return this.COIs;
	}
	
	//Manually set if the Contact has experience
	public void setExp(boolean newExp) {
		this.experience=newExp;
	}
	
	public void setCOI(boolean newCOI) {
		this.COIcond=newCOI;
	}
	
	public void setCOIs(int[][] newCOI) {
		this.COIs=newCOI;
		if(newCOI!=null) {
			this.COIcond=true;
		}else {
			this.COIcond=false;
		}
	}
	
	//Manually set the Contact's email address
	public void setEmail(String address) {
		this.email=address;
	}
}
