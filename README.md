# DevOps MVP Anypoint Connector Jenkins 
This is a simple opensource Jenkins Connector for Anypoint Studio for orchestrating Jenkins Jobs using API-led connectivity in interaction with other systems such as Atlassian Jira, ServiceNow, ALM Octane, BMC Remedy, etc. This connector can also be used to orchestrate an automated release process involving multiple tools to interact with Jenkins.

![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/mvp-jenkins-connector.png)

## Getting started
This Anypoint Studio MVP (Minimum Viable Product) Connector for Jenkins has been built for the MuleSoft Community as a template to reuse and if required further extend. The connector supports 6 operations in this MVP release, which are:
- Get all Job
- Get all builds
- Get last build info
- Get last build console text
- Run job by name
- Run job by name using CURL

## Installation of the MVP Connector for Jenkins
This section describes the installation process for this mvp connector in order to use in Anypoint Studio. 

### Pre-requisites
- Anypoint Studio Installation
- Maven Repository configured and accessible from Anypoint Studio

### Step 1 - Download the MVP Jenkins Connector
- Download Repository as ZIP
- Unpack it to a preferred location, typically into your Anypoint Studio workspaces area

### Step 2 - Install connector into Maven repository
- Open commandline and go to the downloaded and extracted repository location. 
- Perform "mvn install" 
- Connector should be installed successfully

![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/cmd%20mvn%20install.PNG)

### Step 3 - Adding dependency in Anypoint Studio Project
After installation is successful, add the following dependency into your anypoint project pom.xml:

		<dependency>
			<classifier>mule-plugin</classifier>
			<groupId>embrace.devops.connectors</groupId>
			<artifactId>jenkins-connector</artifactId>
			<version>1.1.3</version>
		</dependency>

The current version of this connector is 1.1.3. Once added, save the pom.xml file and your Mule Palette gets updated and you should see the Jenkins connector.

![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/jenkins-mule-palette.PNG)

### Step 4 - Create Jenkins Configuration
Before you get started and consume the provided operations, make sure to configure the Jenkins Connection within Anypoint Studio. 
- Protocol - Select the protocol of your Jenkins installation (HTTP/HTTPS)
- Host - The name of the Jenkins Host
- Port - The port of the Jenkins Server
- User - Login / Connection user
- API-Token (not password) - Get the API Token for the specified user - more Information: [Create API Token in Jenkins](https://www.jenkins.io/blog/2018/07/02/new-api-token-system/)

![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/Jenkins-config.PNG)

Now you are all set to use the Jenkins Operations.

## Connector Operations - how to use
This section describes, how to use the provided operation for Jenkins Connector.

### Operation 1: Get all jobs
This operation get all configured jobs for the user entered in the configuration. The response is a json object containing **job name and job color**. 

Drag and drop the **Get all jobs** operation into the canvas and provide the configuration of Jenkins. 
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/getAllJobs-config.PNG)

Also make sure to change the MIME Type to application/json for the **Get all jobs** operation
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/getAllJobs-config-MIME-Type.PNG)

**Example response (containing 2 jobs)**:

  {
  
      "_class": "hudson.model.Hudson",
      "jobs": [
          {
              "_class": "hudson.model.FreeStyleProject",
              "name": "Anypoint-maven-demo",
              "color": "blue"
          },
          {
              "_class": "hudson.model.FreeStyleProject",
              "name": "Anypoint_MVP_Jenkins_Demo",
              "color": "blue"
          }
      ]
  }

### Operation 2: Get all builds
This operations **Get all builds** specified by a job. Job name need to be provided as an property on the operation. 

Drag and drop the **Get all builds** operation into the canvas and provide the configuration of Jenkins. Make sure to provide a valid Jobname in the property field.
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/getAllBuilds-config.PNG)

Similar to **Get all jobs** configure also the MIME Type to **application/json** for **Get All Builds**

**Example response (containing 3 builds of specified job)**:
  {
  
      "_class": "hudson.model.FreeStyleProject",
      "builds": [
          {
              "_class": "hudson.model.FreeStyleBuild",
              "id": "15",
              "number": 15,
              "result": "FAILURE",
              "timestamp": 1627902990372
          },
          {
              "_class": "hudson.model.FreeStyleBuild",
              "id": "14",
              "number": 14,
              "result": "SUCCESS",
              "timestamp": 1627902968664
          },
          {
              "_class": "hudson.model.FreeStyleBuild",
              "id": "13",
              "number": 13,
              "result": "SUCCESS",
              "timestamp": 1627902942285
          }
      ]
  }

### Operation 3: Get last build info
This operations **Get last build info** specified by a job. Job name need to be provided as an property on the operation.  This operation extracts all information regarding the last build specified by the job name.

Drag and drop the **Get last build info** operation into the canvas and provide the configuration of Jenkins. Make sure to provide a valid Jobname in the property field.
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/getLastBuildInfo.PNG)

Similar to **Get all jobs** configure also the MIME Type to **application/json** for **Get last build info**

**Example response (for specified job)**:
  {
  
      "_class": "hudson.model.FreeStyleBuild",
      "actions": [
          {
              "_class": "hudson.model.CauseAction",
              "causes": [
                  {
                      "_class": "hudson.model.Cause$UserIdCause",
                      "shortDescription": "Gestartet durch Benutzer API",
                      "userId": "api_activist",
                      "userName": "API"
                  }
              ]
          },
          {},
          {
              "_class": "org.jenkinsci.plugins.displayurlapi.actions.RunDisplayAction"
          }
      ],
      "artifacts": [],
      "building": false,
      "description": null,
      "displayName": "#15",
      "duration": 2462,
      "estimatedDuration": 1321,
      "executor": null,
      "fullDisplayName": "Anypoint_MVP_Jenkins_Demo #15",
      "id": "15",
      "keepLog": false,
      "number": 15,
      "queueId": 16,
      "result": "FAILURE",
      "timestamp": 1627902990372,
      "url": "http://localhost:8089/job/Anypoint_MVP_Jenkins_Demo/15/",
      "builtOn": "",
      "changeSet": {
          "_class": "hudson.scm.EmptyChangeLogSet",
          "items": [],
          "kind": null
      },
      "culprits": []
  }

### Operation 4: Get last build console text
This operations **Get last build console text** specified by a job. Job name need to be provided as an property on the operation. This operation extracts the last build console text of the specified job. 

Drag and drop the **Get last build console text** operation into the canvas and provide the configuration of Jenkins. Make sure to provide a valid Jobname in the property field.
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/getLastBuildConsoleText.PNG)

Different than with the previous operations, this one requires a MIME Type set to **text/plain**.

**Example response**:

  Gestartet durch Benutzer API
  Running as SYSTEM
  Building in workspace C:\WINDOWS\system32\config\systemprofile\AppData\Local\Jenkins\.jenkins\workspace\Anypoint_MVP_Jenkins_Demo
  [Anypoint_MVP_Jenkins_Demo] $ cmd /c call C:\WINDOWS\TEMP\jenkins2065184340071688622.bat

  C:\WINDOWS\system32\config\systemprofile\AppData\Local\Jenkins\.jenkins\workspace\Anypoint_MVP_Jenkins_Demo>echo "This is a test" 
  "This is a test"

  C:\WINDOWS\system32\config\systemprofile\AppData\Local\Jenkins\.jenkins\workspace\Anypoint_MVP_Jenkins_Demo>exit 0 
  Finished: SUCCESS

### Operation 5: Run job with REST client
This operations **Run job with REST client** specified by a job. Job name need to be provided as an property on the operation. This operation executes a jenkins job remotely using the provided Job name.

Drag and drop the **Run job with REST client** operation into the canvas and provide the configuration of Jenkins. Make sure to provide a valid Jobname in the property field.
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/runJob.PNG)

Similar to **Get all jobs** configure also the MIME Type to **application/json** for **Run job with REST client**

**Example response**: 

  { 
  
    "message": 201 
  }
  
### Operation 6: Run job with CURL
This operations **Run job with CURL** specified by a job. Job name need to be provided as an property on the operation. This operation executes a jenkins job remotely using the provided Job name.

Works similarly to Operation 5. 

### Example Jenkins and Jira
![Image of Jenkins MuleSoft Connector](https://github.com/API-Activist/devops-mvp-anypoint-connector-jenkins/blob/master/example-jira-jenkins.PNG)

## Video Tutorial
Link to the video tutorial: https://youtu.be/LjErAoSrf7Y

## Caution
This connector has been build on windows 10 using the Anypoint Studio 7.10 IDE. It has only been tested with Jenkins 2.289.3 on HTTP. HTTPS has not  been tested, but expected to work without further technical issues. This is a contribution to the MuleSoft community as part of the devops-mvp-connectors initiatives by [Amir Khan](https://www.linkedin.com/in/amir-khan-ak). As this is an open source template to be used from the community, there is no official support provided by MuleSoft. Also if operations are missing, please use the Jenkins Remote API references to implement using the examples provided within this template.

### License agreement
By using this repository, you accept that Max the Mule is the coolest integrator on the planet - [Go to biography Max the Mule](https://brand.salesforce.com/content/characters-overview__3?tab=BogXMx2m)
