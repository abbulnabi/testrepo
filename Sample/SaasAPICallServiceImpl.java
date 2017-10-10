package com.primesoft.saas.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class SaasAPICallServiceImpl
{
  public String _CAM_DEPLOYMENTID;
  public String _CAM_PASSWORD;
  public String _CAM_TENANTID;
  public String _CAM_URI;
  public String _CAM_USERNAME;
  public String _CAM_SUBCRIPTION_ID;
  public String _AUTHENTICATION_TOKEN;
  public String _CAM_VERIFYCERT;
  public boolean _CAM_VERIFYCERT_FLAG = true;
  
  public static final Logger logger = Logger.getLogger(SaasAPICallServiceImpl.class.getName());
  
  public SaasAPICallServiceImpl() {
    initializeProperties();
  }
  
  private void initializeProperties() {
    logger.info("initializing environment variables");
    _CAM_DEPLOYMENTID = System.getenv("CAM_DEPLOYMENTID");
    _CAM_PASSWORD = System.getenv("CAM_PASSWORD");
    _CAM_TENANTID = System.getenv("CAM_TENANTID");
    _CAM_URI = System.getenv("CAM_URI");
    _CAM_USERNAME = System.getenv("CAM_USERNAME");
    _CAM_VERIFYCERT = System.getenv("CAM_VERIFYCERT");
    
    if ("FALSE".equalsIgnoreCase(_CAM_VERIFYCERT)) {
      _CAM_VERIFYCERT_FLAG = false;
    } else {
      _CAM_VERIFYCERT_FLAG = true;
    }
    
    File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(credFile));
      Enumeration enuKeys = properties.keys();
      while (enuKeys.hasMoreElements()) {
        String key = (String)enuKeys.nextElement();
        if ("subscription".equals(key)) {
          _CAM_SUBCRIPTION_ID = properties.getProperty(key);
          break;
        }
      }
      logger.info("initialized environment variables");
    } catch (Exception e) {
      logger.error("Exception at loading environment variables " + e);
    }
  }
  
  public String getAuthenticationToken()
  {
    try {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.1(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.2(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      
      Client restClient = Client.create();
      if (_CAM_VERIFYCERT_FLAG) {
        WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/auth/signin/");
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("username", _CAM_USERNAME);
        params.add("password", _CAM_PASSWORD);
        params.add("tenantId", _CAM_TENANTID);
        
        ClientResponse resp = 
          (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).post(ClientResponse.class, params);
        logger.info(" SAAS Response " + resp);
        if (resp.getStatus() == 200) {
          String jsonStr = (String)resp.getEntity(String.class);
          JSONObject jsonObj = new JSONObject(jsonStr);
          JSONObject data = (JSONObject)jsonObj.get("data");
          _AUTHENTICATION_TOKEN = data.getString("token");
        } else if (resp.getStatus() == 400) {
          String jsonStr = (String)resp.getEntity(String.class);
          JSONObject jsonObj = new JSONObject(jsonStr);
          JSONObject data = (JSONObject)jsonObj.get("data");
          _AUTHENTICATION_TOKEN = data.getString("missingFields");
        } else if (resp.getStatus() == 401) {
          String jsonStr = (String)resp.getEntity(String.class);
          JSONObject jsonObj = new JSONObject(jsonStr);
          _AUTHENTICATION_TOKEN = jsonObj.getString("message");
        } else {
          _AUTHENTICATION_TOKEN = "";
        }
      } else {
        logger.error("CAM SaaS service certification verification failed ");
        _AUTHENTICATION_TOKEN = "";
      }
      return _AUTHENTICATION_TOKEN;
    } catch (Exception e) {
      logger.error("Exception at getting authentication token " + e); }
    return "";
  }
  
  public String verifyAuthenticationToken(String authenticationToken)
    throws Exception
  {
    TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.3(this) };
    








    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    

    HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.4(this);
    





    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    
    Client restClient = Client.create();
    if (_CAM_VERIFYCERT_FLAG) {
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/auth/verify/");
      ClientResponse resp = 
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", authenticationToken)).post(ClientResponse.class);
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 200) {
        return authenticationToken;
      }
      return getAuthenticationToken();
    }
    
    logger.error("CAM SaaS service certification verification failed ");
    return "";
  }
  

  public Map<String, SaasMachineResponse> getSAASMachineDetails(String resourceGroup)
  {
    Map<String, SaasMachineResponse> saasMachineInfoList = new HashMap();
    
    try
    {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.5(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.6(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      
      Client restClient = Client.create();
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines" + "?deploymentId=" + _CAM_DEPLOYMENTID + "&active=true");
      ClientResponse resp = 
      
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).get(ClientResponse.class);
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 200) {
        String jsonStr = (String)resp.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray data = (JSONArray)jsonObj.get("data");
        for (Object object : data) {
          SaasMachineResponse saasMachineInfo = new SaasMachineResponse();
          JSONObject machineObject = (JSONObject)object;
          saasMachineInfo.setActive(machineObject.get("active").toString());
          saasMachineInfo.setMachineName(machineObject.get("machineName").toString());
          saasMachineInfo.setMachineId(machineObject.get("machineId").toString());
          saasMachineInfo.setSubscriptionId(machineObject.get("subscriptionId").toString());
          saasMachineInfo.setDeploymentId(machineObject.get("deploymentId").toString());
          saasMachineInfo.setResourceGroup(machineObject.get("resourceGroup").toString());
          saasMachineInfo.setCreatedBy(machineObject.get("createdBy").toString());
          saasMachineInfo.setCreatedOn(machineObject.get("createdOn").toString());
          saasMachineInfo.setUpdatedOn(machineObject.get("updatedOn").toString());
          saasMachineInfo.setPowerState(machineObject.get("powerState").toString());
          saasMachineInfo.setLocation(machineObject.get("location").toString());
          saasMachineInfo.setVmSize(machineObject.get("vmSize").toString());
          JSONObject provisioningStatusObject = (JSONObject)machineObject.get("provisioningStatus");
          ProvisioningStatus provisioningStatus = new ProvisioningStatus();
          provisioningStatus.setProvisioningstate(provisioningStatusObject.get("state").toString());
          provisioningStatus.setProvisioningMessage(provisioningStatusObject.get("message").toString());
          saasMachineInfo.setProvisioningStatus(provisioningStatus);
          JSONObject osInfoObject = (JSONObject)machineObject.get("osInfo");
          OsInfo osInfo = new OsInfo();
          osInfo.setOffer(osInfoObject.get("offer").toString());
          osInfo.setPublisher(osInfoObject.get("publisher").toString());
          osInfo.setSku(osInfoObject.get("sku").toString());
          osInfo.setVersion(osInfoObject.get("version").toString());
          saasMachineInfo.setOsInfo(osInfo);
          saasMachineInfoList.put(machineObject.get("machineName").toString().trim().toLowerCase(), saasMachineInfo);
        }
      } else {
        SaasMachineResponse saasMachineResponse = new SaasMachineResponse();
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
        saasMachineResponse.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        saasMachineResponse.setReason((String)responseData.get("reason"));
        saasMachineInfoList.put("status", saasMachineResponse);
      }
      return saasMachineInfoList;
    } catch (Exception e) {
      logger.error("Exception at getting authentication token " + e); }
    return null;
  }
  
























































































  public SaasMachineResponse startStopAzureMachineUsingSAASApi(String machineId, String requestType)
    throws Exception
  {
    TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.7(this) };
    








    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    

    HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.8(this);
    




    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    SaasMachineResponse saasMachineResponse = new SaasMachineResponse();
    Client restClient = Client.create();
    String machineManagement_URL = _CAM_URI + "/api/v1/machines" + "/" + machineId + "/" + requestType + "?deploymentId=" + _CAM_DEPLOYMENTID;
    WebResource webResource = restClient.resource(machineManagement_URL);
    ClientResponse resp = 
      (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).post(ClientResponse.class);
    logger.info(" SAAS Response " + resp);
    if (resp.getStatus() == 200) {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      saasMachineResponse.setReason("");
    } else {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      JSONObject responseData = (JSONObject)jsonObj.get("data");
      saasMachineResponse.setReason((String)responseData.get("reason"));
    }
    return saasMachineResponse;
  }
  
  public SaasMachineResponse registerMachineInSaas(String resourceGroup, String machineName)
    throws Exception
  {
    TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.9(this) };
    








    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    

    HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.10(this);
    





    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    
    SaasMachineResponse saasMachineResponse = new SaasMachineResponse();
    Client restClient = Client.create();
    WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines");
    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
    params.add("deploymentId", _CAM_DEPLOYMENTID);
    params.add("resourceGroup", resourceGroup);
    params.add("machineName", machineName);
    params.add("subscriptionId", _CAM_SUBCRIPTION_ID);
    
    ClientResponse resp = 
    
      (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).post(ClientResponse.class, params);
    logger.info(" SAAS Response " + resp);
    if (resp.getStatus() == 201) {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      saasMachineResponse.setReason("");
      JSONObject machineObject = (JSONObject)jsonObj.get("data");
      saasMachineResponse.setActive(machineObject.get("active").toString());
      saasMachineResponse.setMachineName(machineObject.get("machineName").toString());
      saasMachineResponse.setMachineId(machineObject.get("machineId").toString());
      saasMachineResponse.setSubscriptionId(machineObject.get("subscriptionId").toString());
      saasMachineResponse.setDeploymentId(machineObject.get("deploymentId").toString());
      saasMachineResponse.setResourceGroup(machineObject.get("resourceGroup").toString());
      saasMachineResponse.setCreatedBy(machineObject.get("createdBy").toString());
      saasMachineResponse.setCreatedOn(machineObject.get("createdOn").toString());
      saasMachineResponse.setUpdatedOn(machineObject.get("updatedOn").toString());
      saasMachineResponse.setPowerState(machineObject.get("powerState").toString());
    } else {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      JSONObject responseData = (JSONObject)jsonObj.get("data");
      saasMachineResponse.setReason((String)responseData.get("reason"));
    }
    return saasMachineResponse;
  }
  


  public SaasMachineResponse deRegisterMachineInSaas(String resourceGroup, String machineId)
    throws Exception
  {
    TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.11(this) };
    








    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    

    HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.12(this);
    





    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    
    SaasMachineResponse saasMachineResponse = new SaasMachineResponse();
    Client restClient = Client.create();
    WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines" + "/" + machineId + "?deploymentId=" + _CAM_DEPLOYMENTID);
    ClientResponse resp = 
    
      (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).delete(ClientResponse.class);
    logger.info(" SAAS Response " + resp);
    if (resp.getStatus() == 200) {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      saasMachineResponse.setReason("");
    } else {
      JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
      saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
      saasMachineResponse.setStatus((String)jsonObj.get("status"));
      JSONObject responseData = (JSONObject)jsonObj.get("data");
      saasMachineResponse.setReason((String)responseData.get("reason"));
    }
    return saasMachineResponse;
  }
  
  public SaasMachineResponse getSAASSingleMachineDetails(String machineId) {
    SaasMachineResponse saasMachineInfo = new SaasMachineResponse();
    
    try
    {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.13(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.14(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      
      Client restClient = Client.create();
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines" + "/" + machineId + "?deploymentId=" + _CAM_DEPLOYMENTID);
      ClientResponse resp = 
      
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).get(ClientResponse.class);
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 200) {
        String jsonStr = (String)resp.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONObject machineObject = (JSONObject)jsonObj.get("data");
        saasMachineInfo.setActive(machineObject.get("active").toString());
        saasMachineInfo.setMachineName(machineObject.get("machineName").toString());
        saasMachineInfo.setMachineId(machineObject.get("machineId").toString());
        saasMachineInfo.setSubscriptionId(machineObject.get("subscriptionId").toString());
        saasMachineInfo.setDeploymentId(machineObject.get("deploymentId").toString());
        saasMachineInfo.setResourceGroup(machineObject.get("resourceGroup").toString());
        saasMachineInfo.setCreatedBy(machineObject.get("createdBy").toString());
        saasMachineInfo.setCreatedOn(machineObject.get("createdOn").toString());
        saasMachineInfo.setUpdatedOn(machineObject.get("updatedOn").toString());
        saasMachineInfo.setPowerState(machineObject.get("powerState").toString());
        saasMachineInfo.setLocation(machineObject.get("location").toString());
        saasMachineInfo.setVmSize(machineObject.get("vmSize").toString());
        JSONObject provisioningStatusObject = (JSONObject)machineObject.get("provisioningStatus");
        ProvisioningStatus provisioningStatus = new ProvisioningStatus();
        provisioningStatus.setProvisioningstate(provisioningStatusObject.get("state").toString());
        provisioningStatus.setProvisioningMessage(provisioningStatusObject.get("message").toString());
        saasMachineInfo.setProvisioningStatus(provisioningStatus);
        JSONObject osInfoObject = (JSONObject)machineObject.get("osInfo");
        OsInfo osInfo = new OsInfo();
        osInfo.setOffer(osInfoObject.get("offer").toString());
        osInfo.setPublisher(osInfoObject.get("publisher").toString());
        osInfo.setSku(osInfoObject.get("sku").toString());
        osInfo.setVersion(osInfoObject.get("version").toString());
        saasMachineInfo.setOsInfo(osInfo);
      } else {
        SaasMachineResponse saasMachineResponse = new SaasMachineResponse();
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        saasMachineResponse.setCode(((Integer)jsonObj.get("code")).intValue());
        saasMachineResponse.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        saasMachineResponse.setReason((String)responseData.get("reason"));
      }
      return saasMachineInfo;
    } catch (Exception e) {
      logger.error("Exception at getting SAAS machine details token " + e); }
    return null;
  }
  

  public UserEntitlements addSAASUserMachineEntitlements(String userGuid, String machineId)
  {
    UserEntitlements userEntitlements = new UserEntitlements();
    
    try
    {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.15(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.16(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      

      Client restClient = Client.create();
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines/entitlements");
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add("machineId", machineId);
      params.add("CAM_DEPLOYMENTID", _CAM_DEPLOYMENTID);
      params.add("userGuid", userGuid);
      

      ClientResponse resp = 
      
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).post(ClientResponse.class, params);
      
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 201) {
        String jsonStr = (String)resp.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(jsonStr);
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus(jsonObj.get("status").toString());
        JSONObject machineObject = (JSONObject)jsonObj.get("data");
        userEntitlements.setMachineId(machineObject.get("machineId").toString());
        userEntitlements.setEntitlementId(machineObject.get("entitlementId").toString());
        userEntitlements.setDeploymentId(machineObject.get("deploymentId").toString());
        userEntitlements.setCreatedBy(machineObject.get("createdBy").toString());
        userEntitlements.setCreatedOn(machineObject.get("createdOn").toString());
      } else if (resp.getStatus() == 401) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        userEntitlements.setReason((String)jsonObj.get("message"));
      } else if (resp.getStatus() == 403) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        userEntitlements.setReason((String)responseData.get("reason"));
      }
      else if (resp.getStatus() == 404) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        userEntitlements.setReason((String)jsonObj.get("message"));
      } else {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        userEntitlements.setReason((String)responseData.get("message"));
      }
      return userEntitlements;
    } catch (Exception e) {
      logger.error("Exception at getting SAAS Entitlement details " + e); }
    return null;
  }
  

  public Map<String, UserEntitlements> getSAASUserEntitlementsDetails(String userGuid)
  {
    Map<String, UserEntitlements> userEntitlementsList = new HashMap();
    
    try
    {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.17(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.18(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      
      Client restClient = Client.create();
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines/entitlements" + "?userGuid=" + userGuid);
      ClientResponse resp = 
      
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).get(ClientResponse.class);
      UserEntitlements userEntitlements = new UserEntitlements();
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 200) {
        String jsonStr = (String)resp.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(jsonStr);
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus(jsonObj.get("status").toString());
        userEntitlements.setStatus(jsonObj.get("limit").toString());
        userEntitlements.setStatus(jsonObj.get("offset").toString());
        
        JSONArray data = (JSONArray)jsonObj.get("data");
        for (Object object : data) {
          JSONObject entitlementObject = (JSONObject)object;
          userEntitlements.setMachineId(entitlementObject.get("machineId").toString());
          userEntitlements.setEntitlementId(entitlementObject.get("entitlementId").toString());
          userEntitlements.setDeploymentId(entitlementObject.get("deploymentId").toString());
          userEntitlements.setCreatedBy(entitlementObject.get("createdBy").toString());
          userEntitlements.setCreatedOn(entitlementObject.get("createdOn").toString());
          userEntitlementsList.put(entitlementObject.get("machineId").toString(), userEntitlements);
        }
      } else if (resp.getStatus() == 401) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        userEntitlements.setReason((String)jsonObj.get("message"));
        userEntitlementsList.put(jsonObj.get("code").toString(), userEntitlements);
      } else if (resp.getStatus() == 403) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        userEntitlements.setReason((String)responseData.get("reason"));
        userEntitlementsList.put(jsonObj.get("code").toString(), userEntitlements);
      } else {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        userEntitlements.setCode(((Integer)jsonObj.get("code")).intValue());
        userEntitlements.setStatus((String)jsonObj.get("status"));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        userEntitlements.setReason((String)responseData.get("message"));
        userEntitlementsList.put(jsonObj.get("code").toString(), userEntitlements);
      }
      return userEntitlementsList;
    } catch (Exception e) {
      logger.error("Exception at getting SAAS Entitlement details " + e); }
    return null;
  }
  


  public Set<String> getSAASUserEntitlementsMachineNames(String userGuid)
  {
    Set<String> machineNames = new java.util.HashSet();
    
    try
    {
      TrustManager[] trustAllCerts = { new SaasAPICallServiceImpl.19(this) };
      








      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      

      HostnameVerifier allHostsValid = new SaasAPICallServiceImpl.20(this);
      





      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      
      Client restClient = Client.create();
      WebResource webResource = restClient.resource(_CAM_URI + "/api/v1/machines/entitlements" + "?userGuid=" + userGuid);
      ClientResponse resp = 
      
        (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/x-www-form-urlencoded" }).header("Authorization", verifyAuthenticationToken(_AUTHENTICATION_TOKEN))).get(ClientResponse.class);
      logger.info(" SAAS Response " + resp);
      if (resp.getStatus() == 200) {
        String jsonStr = (String)resp.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray data = (JSONArray)jsonObj.get("data");
        for (Object object : data) {
          JSONObject entitlementObject = (JSONObject)object;
          machineNames.add(getSAASSingleMachineDetails(entitlementObject.get("machineId").toString()).getMachineName());
        }
      } else if (resp.getStatus() == 401) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        machineNames.add((String)jsonObj.get("message"));
      } else if (resp.getStatus() == 403) {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        machineNames.add((String)jsonObj.get("reason"));
      } else {
        JSONObject jsonObj = new JSONObject((String)resp.getEntity(String.class));
        JSONObject responseData = (JSONObject)jsonObj.get("data");
        machineNames.add((String)responseData.get("message"));
      }
      return machineNames;
    } catch (Exception e) {
      logger.error("Exception at getting SAAS Entitlement details " + e); }
    return null;
  }
  



  public static void main(String[] args)
  {
    SaasAPICallServiceImpl saasAPICallServiceImpl = new SaasAPICallServiceImpl();
    saasAPICallServiceImpl.addSAASUserMachineEntitlements("ca7a6fb9-ea2e-420d-964f-505beeee8290", "59d45e2bedcac50010bdee86");
  }
}