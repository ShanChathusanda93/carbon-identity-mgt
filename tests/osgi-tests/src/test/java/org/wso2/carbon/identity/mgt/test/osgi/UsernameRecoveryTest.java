/*
 *
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.mgt.test.osgi;

import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.ops4j.pax.exam.testng.listener.PaxExam;
import org.osgi.framework.BundleContext;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.wso2.carbon.container.CarbonContainerFactory;
import org.wso2.carbon.identity.mgt.RealmService;
import org.wso2.carbon.identity.mgt.User;
import org.wso2.carbon.identity.mgt.bean.UserBean;
import org.wso2.carbon.identity.mgt.claim.Claim;
import org.wso2.carbon.identity.mgt.exception.IdentityStoreException;
import org.wso2.carbon.identity.mgt.test.osgi.util.IdentityMgtOSGITestConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.username.NotificationUsernameRecoveryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Test class for NotificattionUsernameRecoveryManager class.
 */
@Listeners(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(CarbonContainerFactory.class)
public class UsernameRecoveryTest {
    private static List<User> users = new ArrayList<>();
    private static List<Claim> claims = new ArrayList<>();
    private static NotificationUsernameRecoveryManager instance = NotificationUsernameRecoveryManager.getInstance();

    @Inject
    private BundleContext bundleContext;

//    @Inject
//    private CarbonServerInfo carbonServerInfo;

    @Test(groups = "usernameRecovery")
    public void verifyUsernameWithZeroClaims() throws IdentityStoreException, IdentityRecoveryException {
        addUser("dinali123", "dinali", "dabarera", "dinali@wso2.com");
        claims = null;
        boolean result = instance.verifyUsername(claims);
        Assert.assertEquals(result, false, "No user should be recovered with no claims.");

    }

    @Test(groups = "usernameRecovery")
    public void verifyUsernameWithOneClaims() throws IdentityStoreException, IdentityRecoveryException {
        addUser("dinali123", "dinali", "dabarera", "dinali@wso2.com");
        Claim sampleClaim = new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                IdentityMgtOSGITestConstants.ClaimURIs.FIRST_NAME_CLAIM_URI, "dinali");
        List<Claim> claimList = new ArrayList<>();
        claimList.add(sampleClaim);
        boolean result = instance.verifyUsername(claimList);
        Assert.assertEquals(result, true, "There should be one user with given claim.");

    }

    @Test(groups = "usernameRecovery")
    public void verifyUsernameWithWrongClaims() throws IdentityStoreException, IdentityRecoveryException {
        addUser("dinali123", "dinali", "dabarera", "dinali@wso2.com");
        Claim sampleClaim = new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                IdentityMgtOSGITestConstants.ClaimURIs.FIRST_NAME_CLAIM_URI, "mala");
        claims.clear();
        claims.add(sampleClaim);
        boolean result = instance.verifyUsername(claims);
        Assert.assertEquals(result, false, "There should be no user with given claim.");

    }

    @Test(groups = "usernameRecovery")
    public void verifyUsernameWithmultipleUsers() throws IdentityStoreException, IdentityRecoveryException {
        addUser("dinali123", "dinali", "dabarera", "dinali@wso2.com");
        addUser("dinaliDuplicate", "dinali", "silva", "dinali@wso2.com");
        Claim sampleClaim = new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                IdentityMgtOSGITestConstants.ClaimURIs.EMAIL_CLAIM_URI, "dinali@wso2.com");
        List<Claim> claimList = new ArrayList<>();
        claimList.add(sampleClaim);
        boolean result = instance.verifyUsername(claimList);
        Assert.assertEquals(result, false, "There should be multiple users with given claim.");

    }

    private void addUser(String username, String firstName, String lastName, String email)
            throws IdentityStoreException {
        RealmService realmService = bundleContext.getService(bundleContext.getServiceReference(RealmService.class));
        Assert.assertNotNull(realmService, "Failed to get realm service instance");

        UserBean userBean = new UserBean();
        List<Claim> claims = Arrays
                .asList(new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                                IdentityMgtOSGITestConstants.ClaimURIs.USERNAME_CLAIM_URI, username),
                        new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                                IdentityMgtOSGITestConstants.ClaimURIs.FIRST_NAME_CLAIM_URI, firstName),
                        new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                                IdentityMgtOSGITestConstants.ClaimURIs.LAST_NAME_CLAIM_URI, lastName),
                        new Claim(IdentityMgtOSGITestConstants.ClaimURIs.WSO2_DIALECT_URI,
                                IdentityMgtOSGITestConstants.ClaimURIs.EMAIL_CLAIM_URI, email));
        userBean.setClaims(claims);
        User user = realmService.getIdentityStore().addUser(userBean);

        Assert.assertNotNull(user, "Failed to receive the user.");
        Assert.assertNotNull(user.getUniqueUserId(), "Invalid user unique id.");

        users.add(user);
    }

}
