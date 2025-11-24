package com.qa.jobhunt.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.jobhunt.base.BaseTest;

public class HomePageTest extends BaseTest {

   @BeforeClass
	public void homePageSetup() {
	homepage=	loginpage.doLogin("dattaharshithreddynagireddy@gmail.com", "Harshithdhr@1968");
	}
   
   

   
   
	@Test
	public void homePageTitleTest() {
		homepage.doSearchBarClick();
		homepage.dropDownHandlingToSearch("automation test ", "4", "bangalore");
		
	}

}
