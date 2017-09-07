package Pages;

import java.io.IOException;
import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class reviewerPage extends Page{

	
	public By home=By.linkText("Home");
	public By appLauncher=By.linkText("App Launcher");
	public String homeTitle="Applicant";
	
	public By reviewerProfiles=By.linkText("Reviewer Profiles");
	public String reviewerProfilesTitle="Reviewer Profiles: Home ~ Applicant";
	
	public By scorecards=By.linkText("Scorecards");
	public String scorecardsTitle="Scorecards: Home ~ Applicant";
	
	public By portal=By.id("workWithPortalLabel");
	public By userLog=By.partialLinkText("Log in to Comm");
	public By newRevPro=By.xpath("//*[@title=\"Create New Reviewer Profile\"]");
	public By continueBut=By.xpath("//*[@title=\"Continue\"]");
	public By save=By.xpath("//*[@title=\"Save\"]");
	public By submit=By.xpath("//*[@title=\"Submit for Approval\"]");
	public By recordType=By.id("p3");
	public By formReviewProg=By.xpath("//*[@title=\"Review Program - Available\"]");
	//Form clickables
	public By add=By.xpath("//*[@title=\"Add\"]");
	public By fiscalYear=By.name("00Nt0000000SWhh");
	public By prevExp=By.name("00N35000000aO61");
	public By name=By.id("CF00Nt0000000SWKG");
	public By terms=By.name("00Nt0000000SgVo");
	public By app=By.partialLinkText("REV-");//This is sloppy
	public By review=By.partialLinkText("SC-");//extremely sloppy
	public By addRevCOI=By.name("new_applicant_list_identify_conflicts");
	public By addLedCOI=By.name("pending_application");
	public By correctCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id42");
	public By readCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id41");
	public By exitCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38");
	public By saveCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37");
	//Misc Buttons
	public By newBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	public By go=By.name("go");
	public By startRevi=By.name("new_view_scorecard");
	
		

	
	
	public reviewerPage(WebDriver driverBeingUsed, String sandboxURL, Contact reviewer){
		Page.driver=driverBeingUsed;
		Object[] loginInfo= {sandboxURL,reviewer};
		//refresh
		login(loginInfo);
	}
	
	@Override
	public void login(Object[] loginInfo) {
		if(loginInfo[0] instanceof String) {
			String sandBoxURL=(String) loginInfo[0];
			//Salesforce Admin Login
			if(!webPage(sandBoxURL,"Login "+'|'+" Salesforce")) {
					System.out.println("LOGIN ERROR: remember to logout, before logging back in");
				}else {
				try {
					String[] creditials=SystemCommands.getLoginCred();
					type(By.id("username"), creditials[0]);
					type(By.id("password"), creditials[1]);
				}catch(IOException f) {
					System.out.println("MISSING FILE: couldn't find \"loginInfo.txt\"");
				}
				click(By.id("Login"));
			}
		}
		if(loginInfo[1] instanceof Contact) {
			Contact searchFor=(Contact) loginInfo[1];
			SystemCommands.pause(2);
			click(By.xpath("//*[@title=\"Contacts Tab\"]"));
			SystemCommands.pause(2);
			click(By.xpath("//*[@title=\"Go!\"]"));
			SystemCommands.pause(2);
			String lookUp=searchFor.getLastName()+", "+searchFor.getFirstName();
			String letter=lookUp.charAt(0)+"";
			click(By.linkText(letter));
			SystemCommands.pause(2);
			boolean foundName=false;int infCount=0;
			//Potential Infinite Loop
			PrintStream original = System.out;
			System.setOut(new NullPrintStream());
			do {
				SystemCommands.pause(1);
				if(click(By.linkText(lookUp))) {
					foundName=true;
				}else {
					if(!click(By.partialLinkText("Next"))) {
						if(!"A".equals(letter)) {
							click(By.linkText("A"));
						}else {
							click(By.linkText("B"));
						}
						click(By.linkText(letter));
					}
				}
				infCount++;
			}while(!foundName & infCount<100);
			System.setOut(original);
			SystemCommands.pause(2);
			if(infCount!=100) {
				click(portal);
				click(userLog);
			}else {
				System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
			}
		}
	}
		
	@Override
	public void logout() {
		click(By.id("userNavLabel"));
		click(By.xpath("//*[@title=\"Logout\"]"));
	}

	//Reviewer Actions
	public void createRevProfile(Contact reviewer, int pauseTime) {
		SystemCommands.pause(pauseTime);
		click(reviewerProfiles);
		SystemCommands.pause(pauseTime);
		click(newRevPro);
		SystemCommands.pause(pauseTime);
		select(recordType, 2);
		click(continueBut);
		SystemCommands.pause(pauseTime);
		select(formReviewProg, 0);
		click(add);
		select(fiscalYear, 2);
		if(reviewer.getExp()) {
			select(prevExp,1);
		}else {
			select(prevExp,2);
		}
		type(name,reviewer.getLastName()+" "+reviewer.getFirstName());
		select(terms, 1);
		SystemCommands.pause(pauseTime);
		Boolean processed=false;int infCount=0;
		while(!processed & infCount!=300) {
			SystemCommands.pause(1);
			click(save);
			if(!getTitle().equals("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				processed=true;
			}
			infCount++;
		}
		if(infCount!=300) {
			SystemCommands.pause(pauseTime);
			click(submit);
			SystemCommands.pause(pauseTime);
			accept();
			SystemCommands.pause(pauseTime);
		}else {
			System.out.println("LOOPING ERROR: System couldn't find Reviewer's name.");
		}
	}
	
	public void reviewerAddCOI(Contact reviewer,int pauseTime) {
		SystemCommands.pause(pauseTime);
		click(reviewerProfiles);
		SystemCommands.pause(pauseTime);
		closeBar();
		click(app);
		SystemCommands.pause(pauseTime);
		click(addRevCOI);
		if(reviewer.getCOIcond()) {
			addCOI(reviewer, pauseTime,reviewer.getCOIs());
		}else {
			noCOI(reviewer, pauseTime);
		}
	}
	
	public void doAReview(int pauseTime) {
		click(home);
		SystemCommands.pause(pauseTime);
		click(review);
		SystemCommands.pause(pauseTime);
		
		
	}
	
	public String[] getCOIVal(int rowNum) {
		String[] result={"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id45"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id51"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id53"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id64"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id67"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id70"};
		return result;
	}
	
	public void noCOI(Contact reviewer,int pauseTime) {
		SystemCommands.pause(pauseTime);
		select(correctCOI, 2);
		select(readCOI, 1);
		SystemCommands.pause(pauseTime);
		click(saveCOI);
		SystemCommands.pause(pauseTime);
		click(exitCOI);
		SystemCommands.pause(pauseTime);
		click(submit);
		SystemCommands.pause(pauseTime);
		try {
			accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}
	//everyone actions
	public void addCOI(Contact reviewer,int pauseTime, int[][] COInums) {
		SystemCommands.pause(pauseTime);
		select(correctCOI, 1);
		select(readCOI, 1);
		//Fills out COI here
		for(int i=0;i<COInums[0].length;i++) {
			String[] COI=getCOIVal(COInums[0][i]);
			click(By.name(COI[0]));
			type(By.name(COI[1]), "Reason");
			//2: Partial Ban, 3: Total Ban, 4: No Conflict
			select(By.name(COI[2]), 2);
		}	
		SystemCommands.pause(pauseTime);
		click(saveCOI);
		SystemCommands.pause(pauseTime);
		click(exitCOI);
		SystemCommands.pause(pauseTime);
		click(submit);
		SystemCommands.pause(pauseTime);
		try {
			accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}
	
	public void closeBar() {
		try {
			click(newBut);
			click(sideBar);
			SystemCommands.pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, you are good to go!
		}
	}
	
	public void openBar() {
		try {
			click(newBut);
			click(newBut);
			//If it's already visible, you are good to go!
		}catch(ElementNotVisibleException e) {
			click(sideBar);
			SystemCommands.pause();
		}
	}

	public void fillOutScoreCard(int pauseTime) {
		click(scorecards);
		SystemCommands.pause(pauseTime);
		click(go);
		SystemCommands.pause(pauseTime);
		click(review);
		SystemCommands.pause(pauseTime);
		click(startRevi);
		//tab("ScoreCard Page");
		SystemCommands.pause(pauseTime);
		for(int i=1;i<58;i++) {
			answerQuestion(i,1);
		}
		click(By.id("j_id0:thecard:j_id30"));
		//click(By.id("j_id0:thecard:j_id32"));
	}
	
	public void answerQuestion(int questionNumber, int selectAnswer) {
		try {
			double hesitate=.04;
			SystemCommands.pause(hesitate);
			//BS=Business Strat
			By BS=By.id("BusinessStrategySection");
			By[] BSsub= {By.id("BusinessStrategyProductsServicesandInvestmentCriteriaSubsection"),
					By.id("BusinessStrategyProjectedBusinessActivitiesSubsection"),
					By.id("BusinessStrategyPriorPerformanceSubsection"),
					By.id("BusinessStrategyPriorPerformanceandProjectedBusinessActivitySubsection"),
					By.id("BusinessStrategyNotableRelationshipsSubsection")};
			int[] BSa= {1,7,13,15,17,18};
			for(int i=0;i<BSa.length-1;i++) {
				if(questionNumber>BSa[i]-1 & questionNumber<BSa[i+1]) {
					click(BS);
					SystemCommands.pause(hesitate);
					click(BSsub[i]);
					SystemCommands.pause(hesitate);
					answerField(hesitate, 0,i, questionNumber, selectAnswer);
					click(BS);
					SystemCommands.pause(hesitate);
				}
			}
			By PP=By.id("PriorityPointsSection");
			By[] PPsub= {By.id("PriorityPointsPriorityPointsExperienceSubsection"),
				By.id("PriorityPointsPriorityPointsPercentageSubsection")};
			int[] PPa= {18,19,20};
			for(int i=0;i<PPa.length-1;i++) {
				if(questionNumber>PPa[i]-2 & questionNumber<PPa[i+1]) {
					click(PP);
					SystemCommands.pause(hesitate);
					click(PPsub[i]);
					SystemCommands.pause(hesitate);
					answerField(hesitate, 1,i, questionNumber, selectAnswer);
					click(PP);
					SystemCommands.pause(hesitate);
				}
			}	
			By CO=By.id("CommunityOutcomesSection");
			By[] COsub= {By.id("CommunityOutcomesTargetingAreasofHigherDistressSubsection"),
				By.id("CommunityOutcomesJobRelatedCommunityOutcomesSubsection"),
				By.id("CommunityOutcomesGoodsServicesinLICsSubsection"),
				By.id("CommunityOutcomesFinancingMinorityBusinesses"+'/'+"HousingSubsection"),//button seems to fail
				By.id("CommunityOutcomesAdditionalCommunityDevelopmentOutcomesSubsection"),
				By.id("CommunityOutcomesTrackingCommunityOutcomesSubsection"),
				By.id("CommunityOutcomesCommunityAccountabilityandInvolvementSubsection"),
				By.id("CommunityOutcomesOtherCommunityBenefitsSubsection")};//button seems to fail
			int[] COa= {20,22,29,36,40,46,47,51,53};
			for(int i=0;i<COa.length-1;i++) {
				if(questionNumber>COa[i]-2 & questionNumber<COa[i+1]) {
					click(CO);
					SystemCommands.pause(hesitate);
					click(COsub[i]);
					SystemCommands.pause(hesitate);
					answerField(hesitate, 2,i, questionNumber, selectAnswer);
					click(CO);
					SystemCommands.pause(hesitate);
				}
			}
			By R=By.id("RecommendationSection");
			By[] Rsub= {By.id("RecommendationRecommendorgreceiveallocationSubsection")};
			int[] Ra= {53,54};
			for(int i=0;i<Ra.length-1;i++) {
				if(questionNumber>Ra[i]-2 & questionNumber<Ra[i+1]) {
					click(R);
					SystemCommands.pause(hesitate);
					click(Rsub[i]);
					SystemCommands.pause(hesitate);
					answerField(hesitate, 3,i, questionNumber, selectAnswer);
					click(R);
					SystemCommands.pause(hesitate);
				}
			}
			By PI=By.id("PanelIssuesSection");
			By[] PIsub= {By.id("PanelIssuesPanelIssues1Subsection"),
				By.id("PanelIssuesPanelIssues2Subsection"),
				By.id("PanelIssuesPanelIssues3Subsection"),
				By.id("PanelIssuesPanelIssues4Subsection")};
			int[] PIa= {54,55,56,57,58};
			for(int i=0;i<PIa.length-2;i++) {
				if(questionNumber>PIa[i]-2 & questionNumber<PIa[i+1]) {
					click(PI);
					SystemCommands.pause(hesitate);
					click(PIsub[i]);
					SystemCommands.pause(hesitate);
					answerField(hesitate, 4,i, questionNumber, selectAnswer);
					SystemCommands.pause(hesitate);
					click(PI);
					SystemCommands.pause(hesitate);
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			System.out.print("ACCESS ERROR: Trid to select a question that doesn't exist");
		}
	}
	
	public void answerField(double hesitate, int categoryNum, int subCategoryNum, int questionNumber, int selectAnswer) {
		By radio=By.id("j_id0:thecard:j_id63:"+categoryNum+":j_id65:"+subCategoryNum+":j_id67:"+(questionNumber-1)+":j_id80:"+(selectAnswer-1));
		click(radio);
		By info=By.name("j_id0:thecard:j_id63:"+categoryNum+":j_id65:"+subCategoryNum+":j_id67:"+(questionNumber-1)+":j_id99");
		type(info,"reason");
		SystemCommands.pause(hesitate);
	}
}
