/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.meta.claim.mgt.service;

import org.wso2.carbon.identity.meta.claim.mgt.exception.ProfileMgtServiceException;
import org.wso2.carbon.identity.meta.claim.mgt.internal.profile.mapping.ClaimConfigEntry;
import org.wso2.carbon.identity.meta.claim.mgt.internal.profile.mapping.ProfileEntry;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ProfileMgtService {

    /**
     * Get the claims set of profiles.
     *
     * @return Map(ProfileName, List of claim entries of the profile) with the set of claims and their
     * properties.
     * @throws ProfileMgtServiceException : Error in getting the profiles.
     */
    Map<String, ProfileEntry> getProfiles() throws ProfileMgtServiceException;

    /**
     * Get the claims set of a profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return ProfileEntry with the set of claims and their properties.
     * @throws ProfileMgtServiceException : Error in getting the profile.
     */
    ProfileEntry getProfile(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the properties of a particular claim of a profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @param claim       : Root claim URI for the properties to be retrieved.
     * @return Map(Property Key : Property Value)
     * @throws ProfileMgtServiceException : Error in getting the properties of a claim.
     */
    ClaimConfigEntry getClaimProperties(String profileName, String claim) throws ProfileMgtServiceException;

    /**
     * Get the claims marked as required for a particular profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return List(Root claim URIs)
     * @throws ProfileMgtServiceException : Error in getting the claims with required property.
     */
    List<String> getRequiredProperties(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the claims marked as read-only for a particular profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return List(Root claim URIs)
     * @throws ProfileMgtServiceException : Error in getting the claims with read-only property.
     */
    List<String> getReadOnlyProperties(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the claims marked as unique for a particular profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return List(Root claim URIs)
     * @throws ProfileMgtServiceException : Error in getting the claims with unique property.
     */
    List<String> getUniqueProperties(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the claims marked as verify for a particular profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return List(Root claim URIs)
     * @throws ProfileMgtServiceException : Error in getting the claims with 'verify' property.
     */
    List<String> getVerifyingProperties(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the claims marked as verify with the verifying mechanism for a particular profile.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return Map(Claim : Verifying mechanism)
     * @throws ProfileMgtServiceException : Error in getting the claims with verifying mechanism.
     */
    Map<String, String> getValidating(String profileName) throws ProfileMgtServiceException;

    /**
     * Get the claims marked for regex validations.
     *
     * @param profileName : Uniquely identifying name of the profile.
     * @return Map(Claim : Regex)
     * @throws ProfileMgtServiceException : Error in getting the claims with regex validations.
     */
    Map<String, String> getRegexedProperties(String profileName) throws ProfileMgtServiceException;
}
