package com.qa.jobhunt.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.jobhunt.base.BaseTest;
import com.qa.jobhunt.pages.HomePage;


public class LoginPageTest extends BaseTest {

	@Test
	
	public void loginPageTitleTest() {
    homepage=	loginpage.doLogin("dattaharshithreddynagieddy@gmail.com", "Harshithdhr@1968");

	}
      
	
	
}

