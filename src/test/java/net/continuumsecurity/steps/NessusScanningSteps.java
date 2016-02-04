package net.continuumsecurity.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.continuumsecurity.ClientFactory;
import net.continuumsecurity.Config;
import net.continuumsecurity.ReportClient;
import net.continuumsecurity.ScanClient;
import net.continuumsecurity.v5.model.Issue;
import org.apache.log4j.Logger;
import org.testng.internal.Utils;

import javax.security.auth.login.LoginException;
import java.net.MalformedURLException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class NessusScanningSteps {
    Logger log = Logger.getLogger(NessusScanningSteps.class);
    ScanClient scanClient;
    ReportClient reportClient;
    String policyName;
    List<String> hostNames = new ArrayList<String>();
    String scanUuid;
    String scanIdentifierForStatus;
    String username, password;
    Map<Integer, Issue> issues;
    String nessusUrl;
    int nessusVersion;
    boolean ignoreHostNamesInSSLCert = false;

    @Given("a nessus API client that accepts all hostnames in SSL certificates")
    public void ignoreHostNamesInSSLCert() {
        ignoreHostNamesInSSLCert = true;
    }

    @Given("a nessus version $version server at $nessusUrl")
    public void createNessusClient(int version, String url) {
        nessusUrl = url;
        nessusVersion = version;
        scanClient = ClientFactory.createScanClient(url, nessusVersion, ignoreHostNamesInSSLCert);
    }

    @Given("the nessus username $username and the password $password")
    public void setNessusCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Given("the scanning policy named $policyName")
    public void setScanningPolicy(String policyName) {
        this.policyName = policyName;
    }

    @Given("the target host names")
    public void setTargetHosts(Map<String, String> hostsTable) throws MalformedURLException {

        String hosts = hostsTable.get("hosts");
        String ports_open = hostsTable.get("posts_open");

        int size = hosts.split(";").length;

        assert size >= 0;

        Collections.addAll(hostNames, hosts.split(";"));


    }

    @When("the scanner is run with scan name $scanName")
    public void runScan(String scanName) throws LoginException {
        if (username == null) {
            username = Config.getInstance().getNessusUsername();
            password = Config.getInstance().getNessusPassword();
        }
        scanClient.login(username, password);
        scanUuid = scanClient.newScan(scanName, policyName, Utils.join(hostNames, ","));
        if (nessusVersion == 5) {
            scanIdentifierForStatus = scanName;
        } else {
            scanIdentifierForStatus = scanUuid;
        }
    }

    @When("the list of issues is stored")
    public void storeIssues() throws LoginException {
        waitForScanToComplete(scanIdentifierForStatus);
        reportClient = ClientFactory.createReportClient(nessusUrl, nessusVersion, ignoreHostNamesInSSLCert);
        reportClient.login(username, password);
        issues = reportClient.getAllIssuesSortedByPluginId(scanUuid);
    }

    @When("the following nessus false positive are removed: plugID (.*) hostname (.*)")
    public void removeFalsePositives(int plugID, String hostname) {
        Issue issue = issues.get(plugID);
        if (issue != null) {
            issue.getHostnames().remove(hostname);
            if (issue.getHostnames().size() == 0) {
                issues.remove(plugID);
            }
        }
    }

    @Then("no severity: $severity or higher issues should be present")
    public void verifyRiskOfIssues(int severity) {
        List<Issue> notable = new ArrayList<Issue>();
        for (Issue issue : issues.values()) {
            if (issue.getSeverity() >= severity) {
                notable.add(issue);
            }
        }
        assertThat(notable, empty());
    }

    private void waitForScanToComplete(String scanName) {
        while (scanClient.isScanRunning(scanName)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
